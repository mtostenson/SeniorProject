package com.mt523.backtalk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.mt523.backtalk.fragments.CardFragment;
import com.mt523.backtalk.fragments.GuessFragment;
import com.mt523.backtalk.fragments.NavigationDrawerFragment;
import com.mt523.backtalk.fragments.ProgressFragment;
import com.mt523.backtalk.fragments.RecorderControlFragment;
import com.mt523.backtalk.packets.client.Card;
import com.mt523.backtalk.packets.client.ClientPacket;
import com.mt523.backtalk.packets.server.CardRequest;
import com.mt523.backtalk.packets.server.CardRequest.CardTier;
import com.mt523.backtalk.packets.server.ServerPacket;
import com.mt523.backtalk.util.BackTalkDbHelper;
import com.mt523.backtalk.util.BtConnection;
import com.mt523.backtalk.util.WavRecorder;

public class DrawerActivity extends ActionBarActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        RecorderControlFragment.RecordControlInterface,
        CardFragment.CardInterface, MediaPlayer.OnCompletionListener {

    // My fragments ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private RecorderControlFragment controlFragment;
    private CardFragment cardFragment;
    private GuessFragment guessFragment;

    private WavRecorder recorder;
    private MediaPlayer player;
    private File folder;
    private BackTalkDbHelper dbHelper;
    private SQLiteDatabase database;
    private Vector<Card> deck;

    /**
     * Fragment managing the behaviors, interactions and presentation of the
     * navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in
     * {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Initialize database
        dbHelper = new BackTalkDbHelper(DrawerActivity.this);
        database = dbHelper.getWritableDatabase();

        // Populate the deck
        deck = getDeck();
        /*
         * if (deck.size() == 0) { Log.d(this.getClass().getSimpleName(),
         * "Pulling content from SERVER database."); new ServerTransaction(new
         * CardRequest(CardTier.DEFAULT)).execute(); } else {
         * Log.d(this.getClass().getSimpleName(),
         * "Pulling content from CLIENT database."); deck = getDeck(); }
         */

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Set up card fragment
        cardFragment = CardFragment.newCard(deck.firstElement());
        // cardFragment = CardFragment.newCard(new
        // Card(2345,"test","test","test"));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.center, cardFragment).commit();

        // Set up recording controls
        controlFragment = new RecorderControlFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.bottom, controlFragment).commit();

        // Initialize player
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        folder = new File(Environment.getExternalStorageDirectory()
                + "/BackTalk/");
        if (!folder.exists()) {
            folder.mkdir();
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        // fragmentManager
        // .beginTransaction()
        // .replace(R.id.container,
        // PlaceholderFragment.newInstance(position + 1)).commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
        case 1:
            mTitle = getString(R.string.title_section1);
            break;
        case 2:
            mTitle = getString(R.string.title_section2);
            break;
        case 3:
            mTitle = getString(R.string.title_section3);
            break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.drawer, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_drawer,
                    container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((DrawerActivity) activity).onSectionAttached(getArguments()
                    .getInt(ARG_SECTION_NUMBER));
        }
    }

    @Override
    public void onGuess() {
        guessFragment = new GuessFragment();
        guessFragment.setGuessInterface(cardFragment);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.bottom, guessFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null).commit();
    }

    @Override
    public void onRecord() {
        recorder = new WavRecorder(DrawerActivity.this);
        recorder.prepare();
        recorder.start();
    }

    @Override
    public void onStopRecord() {
        recorder.stop();
        ProgressFragment progressFragment = new ProgressFragment();
        recorder.SetReverseProgressUpdater(progressFragment);
        getFragmentManager().beginTransaction().addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.bottom, progressFragment).commit();
        try {
            recorder.reverse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPlay() {
        if (!player.isPlaying()) {
            try {
                player.reset();
                FileInputStream fis = new FileInputStream(
                        folder.getAbsolutePath() + "/EXT_TEST_REVERSE.wav");
                player.setDataSource(fis.getFD());
                player.prepare();
                controlFragment.bPlay.setEnabled(false);
                player.start();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void goToCard(int index) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getIndex() {
        // TODO Auto-generated method stub
        return 0;
    }

    public Vector<Card> getDeck() {
        Vector<Card> newDeck = new Vector<Card>();
        String query = "SELECT * FROM cards;";
        Cursor cursor = database.rawQuery(query, new String[] {});
        while (cursor.moveToNext()) {
            newDeck.add(new Card(cursor.getInt(cursor
                    .getColumnIndex(BackTalkDbHelper.COLUMN_ID)), cursor
                    .getString(cursor
                            .getColumnIndex(BackTalkDbHelper.COLUMN_QUESTION)),
                    cursor.getString(cursor
                            .getColumnIndex(BackTalkDbHelper.COLUMN_ANSWER)),
                    cursor.getString(cursor
                            .getColumnIndex(BackTalkDbHelper.COLUMN_CATEGORY))));
        }
        return newDeck;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        controlFragment.bPlay.setEnabled(true);
    }

}
