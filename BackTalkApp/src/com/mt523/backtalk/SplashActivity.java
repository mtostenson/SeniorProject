package com.mt523.backtalk;

import java.util.Vector;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.mt523.backtalk.packets.client.Card;
import com.mt523.backtalk.packets.client.ClientPacket;
import com.mt523.backtalk.packets.server.CardRequest;
import com.mt523.backtalk.packets.server.CardRequest.CardTier;
import com.mt523.backtalk.packets.server.ServerPacket;
import com.mt523.backtalk.util.BackTalkDbHelper;
import com.mt523.backtalk.util.BtConnection;

public class SplashActivity extends Activity implements
        ClientPacket.IBackTalkClient {
    
    BackTalkDbHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbHelper = new BackTalkDbHelper(SplashActivity.this);
        database = dbHelper.getWritableDatabase();
        database = dbHelper.getReadableDatabase();
        String query = "SELECT DISTINCT category FROM cards;";
        if(database.rawQuery(query, new String[]{}).getCount() <= 0) {
            new ServerTransaction(new CardRequest(CardTier.DEFAULT)).execute();            
        } else {
            finish();
        }
    }

    private class ServerTransaction extends AsyncTask<Void, Void, ClientPacket> {

        private ServerPacket outPacket;
        private BtConnection connection;
        
        public ServerTransaction(ServerPacket outPacket) {
            this.outPacket = outPacket;
        }

        @Override
        protected ClientPacket doInBackground(Void... arg0) {
            try {
                connection = new BtConnection();
                connection.getOutput().writeObject(outPacket);
                return (ClientPacket) connection.getInput().readObject();
            } catch (Exception e) {
                Log.e(MainActivity.class.getName(),
                        "Packet read error:" + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(ClientPacket result) {
            super.onPostExecute(result);
            result.setClient(SplashActivity.this);
            result.handlePacket();
            if (connection != null) {
                connection.close();
            }
        }
    }
    
    @Override
    public void finish() {
        database.close();
        startActivity(new Intent(SplashActivity.this, DrawerActivity.class));
        super.finish();
    }

    @Override
    public void setDeck(Vector<Card> deck) {
        for (Card card : deck) {
            ContentValues values = new ContentValues();
            values.put(BackTalkDbHelper.COLUMN_ID, card.getId());
            values.put(BackTalkDbHelper.COLUMN_QUESTION, card.getQuestion());
            values.put(BackTalkDbHelper.COLUMN_ANSWER, card.getAnswer());
            values.put(BackTalkDbHelper.COLUMN_CATEGORY, card.getCategory());
            database.insert(BackTalkDbHelper.TABLE_CARDS, null, values);
        }
        finish();
    }
}
