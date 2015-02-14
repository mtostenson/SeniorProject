package com.mt523.backtalk;

import java.io.File;
import java.util.Vector;

import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mt523.backtalk.fragments.CardFragment;
import com.mt523.backtalk.fragments.GuessFragment;
import com.mt523.backtalk.fragments.RecorderControlFragment;
import com.mt523.backtalk.packets.BasePacket;
import com.mt523.backtalk.util.BtConnection;
import com.mt523.backtalk.util.WavRecorder;

public class MainActivity extends ActionBarActivity implements
		RecorderControlFragment.RecordControlInterface {

	private WavRecorder recorder;
	private File folder;
	private Vector<BasePacket> cards;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getFragmentManager().beginTransaction()
				.add(R.id.container2, new RecorderControlFragment()).commit();

		folder = new File(Environment.getExternalStorageDirectory()
				+ "/BackTalk/");
		new CardGetter().execute();
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlay() {
		// TODO Auto-generated method stub

	}

	private class CardGetter extends AsyncTask<Void, Void, BasePacket> {

		private BtConnection connection;

		@Override
		protected BasePacket doInBackground(Void... arg0) {
			try {
				connection = BtConnection.connect();
				return (BasePacket) connection.getInput().readObject();
			} catch (Exception e) {
				Log.e(MainActivity.class.getName(), "Packet read error:" + e.getMessage());
				return null;
			}
		}

		@Override
		protected void onPostExecute(BasePacket result) {
			super.onPostExecute(result);
			if(connection != null) {
				connection.close();
			}
			getFragmentManager().beginTransaction()
					.add(R.id.container1, new CardFragment(result)).commit();
		}

	}

}
