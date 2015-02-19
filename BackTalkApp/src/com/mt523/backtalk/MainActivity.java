package com.mt523.backtalk;

import java.io.File;

import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.mt523.backtalk.fragments.CardFragment;
import com.mt523.backtalk.fragments.GuessFragment;
import com.mt523.backtalk.fragments.RecorderControlFragment;
import com.mt523.backtalk.packets.client.CardPacket;
import com.mt523.backtalk.packets.client.ClientPacket.IBackTalkClient;
import com.mt523.backtalk.packets.server.CardRequest;
import com.mt523.backtalk.util.BtConnection;
import com.mt523.backtalk.util.WavRecorder;

public class MainActivity extends ActionBarActivity implements
        RecorderControlFragment.RecordControlInterface, IBackTalkClient {

    private WavRecorder recorder;
    private File folder;
    private CardPacket card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getFragmentManager().beginTransaction()
                .add(R.id.container2, new RecorderControlFragment()).commit();

        folder = new File(Environment.getExternalStorageDirectory()
                + "/BackTalk/");
        new CardGetter(1).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGuess() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container2, new GuessFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null).commit();
    }

    @Override
    public void onRecord() {
        new CardGetter(card.getId() - 1).execute();
    }

    @Override
    public void onPlay() {
        new CardGetter(card.getId() + 1).execute();
    }

    private class CardGetter extends AsyncTask<Void, Void, CardPacket> {
        private int id;
        private BtConnection connection;

        public CardGetter(int id) {
            this.id = id;
        }

        @Override
        protected CardPacket doInBackground(Void... arg0) {
            try {
                connection = new BtConnection();
                connection.getOutput().writeObject(new CardRequest(id));
                return (CardPacket) connection.getInput().readObject();
            } catch (Exception e) {
                Log.e(MainActivity.class.getName(),
                        "Packet read error:" + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(CardPacket result) {
            super.onPostExecute(result);
            result.setClient(MainActivity.this);
            result.handlePacket();
            if (connection != null) {
                connection.close();
            }
        }

    }

    @Override
    public void setCard(CardPacket cardPacket) {
        // TODO Auto-generated method stub
        this.card = cardPacket;
        getFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.container1, new CardFragment(card)).commit();

    }

}
