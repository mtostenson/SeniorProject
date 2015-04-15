package com.mt523.backtalk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jirbo.adcolony.AdColony;
import com.jirbo.adcolony.AdColonyAd;
import com.jirbo.adcolony.AdColonyAdAvailabilityListener;
import com.jirbo.adcolony.AdColonyAdListener;
import com.jirbo.adcolony.AdColonyV4VCListener;
import com.jirbo.adcolony.AdColonyV4VCReward;
import com.mt523.backtalk.fragments.CardFragment;
import com.mt523.backtalk.fragments.DeckFragment;
import com.mt523.backtalk.fragments.GuessFragment;
import com.mt523.backtalk.fragments.NavigationDrawerFragment;
import com.mt523.backtalk.fragments.ProgressFragment;
import com.mt523.backtalk.fragments.RecorderControlFragment;
import com.mt523.backtalk.packets.client.Card;
import com.mt523.backtalk.util.BackTalkDbHelper;
import com.mt523.backtalk.util.WavRecorder;

public class DrawerActivity extends ActionBarActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        RecorderControlFragment.RecordControlInterface,
        GuessFragment.GuessInterface, DeckFragment.DeckInterface,
        CardFragment.CardInterface, MediaPlayer.OnCompletionListener,
        AdColonyAdListener, AdColonyV4VCListener,
        AdColonyAdAvailabilityListener {

    private static final String TAG = DrawerActivity.class.getName();

    // My fragments ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private RecorderControlFragment controlFragment;
    private DeckFragment deckFragment;
    private CardFragment cardFragment;
    private GuessFragment guessFragment;

    private WavRecorder recorder;
    private MediaPlayer player;
    private File folder;
    private BackTalkDbHelper dbHelper;
    private SQLiteDatabase database;
    private ArrayList<Card> deck;

    // Ads and stuff ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Properties properties;
    int credits;
    String vc_name = "credits";

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

        // Ad things
        AdColony.configure(this, "version:1.0,store:google",
                getString(R.string.APP_ID), getString(R.string.ZONE_ID));
        AdColony.addV4VCListener(this);
        AdColony.addAdAvailabilityListener(this);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        loadProperties();

        // Populate the deck
        // getDeck(prefs.getString("category", "places"));

        // Set up recording controls
        controlFragment = new RecorderControlFragment();

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
    protected void onPause() {
        super.onPause();
        AdColony.pause();
        if (database != null) {
            database.close();
        }
        database = null;
        dbHelper = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdColony.resume(this);
    }

    public void onSectionAttached(int number) {
        mTitle = deck.get(0).getCategory();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        // actionBar.setTitle(mTitle);
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
        } else if (id == R.id.action_hint) {
            String toastBody = "Not enough credits";
            if (credits > 0) {
                toastBody = cardFragment.getCard().getHint();
                updateCredits(--credits);
            }
            Toast.makeText(DrawerActivity.this, toastBody, Toast.LENGTH_SHORT)
                    .show();
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
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((DrawerActivity) activity).onSectionAttached(getArguments()
                    .getInt(ARG_SECTION_NUMBER));
        }
    }

    @Override
    public void onGuess() {
        guessFragment = new GuessFragment();
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

    @Override
    public void setCardSolved(Card card) {
        checkDb();
        card.solved = true;
        ContentValues values = new ContentValues();
        values.put("solved", 1);
        String where = "_id='" + card.getId() + "'";
        database.update(BackTalkDbHelper.TABLE_CARDS, values, where, null);
        Log.d(TAG, "Set card solved with id " + card.getId());
    }

    public void getDeck(String category) {
        ArrayList<Card> newDeck = new ArrayList<Card>();
        String query = "SELECT * FROM cards WHERE "
                + BackTalkDbHelper.COLUMN_CATEGORY + "='" + category + "';";
        checkDb();
        Cursor cursor = database.rawQuery(query, new String[] {});
        while (cursor.moveToNext()) {
            newDeck.add(new Card(
                    cursor.getInt(cursor
                            .getColumnIndex(BackTalkDbHelper.COLUMN_ID)),
                    cursor.getString(cursor
                            .getColumnIndex(BackTalkDbHelper.COLUMN_QUESTION)),
                    cursor.getString(cursor
                            .getColumnIndex(BackTalkDbHelper.COLUMN_ANSWER)),
                    cursor.getString(cursor
                            .getColumnIndex(BackTalkDbHelper.COLUMN_HINT)),
                    cursor.getString(cursor
                            .getColumnIndex(BackTalkDbHelper.COLUMN_CATEGORY)),
                    cursor.getInt(cursor
                            .getColumnIndex(BackTalkDbHelper.COLUMN_LOCKED)) == 1,
                    cursor.getInt(cursor
                            .getColumnIndex(BackTalkDbHelper.COLUMN_SOLVED)) == 1));
        }
        deck = newDeck;

        /*
         * // Set up card fragment cardFragment =
         * CardFragment.newCard(deck.firstElement()); // cardFragment =
         * CardFragment.newCard(new // Card(2345,"test","test","test"));
         * getSupportFragmentManager().beginTransaction() .replace(R.id.center,
         * cardFragment).commit();
         */
    }

    private void checkDb() {
        if (database == null || !database.isOpen()) {
            dbHelper = new BackTalkDbHelper(DrawerActivity.this);
            database = dbHelper.getWritableDatabase();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        controlFragment.bPlay.setEnabled(true);
    }

    @Override
    public void onCategorySelected(String category) {
        getDeck(category);
        deckFragment = DeckFragment.instance(deck);
        getSupportFragmentManager().popBackStack(null,
                getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.center, deckFragment).commit();
        getSupportActionBar().setTitle(deck.get(0).getCategory());

    }

    @Override
    public void onAdColonyAdAttemptFinished(AdColonyAd arg0) {
    }

    @Override
    public void onAdColonyAdStarted(AdColonyAd arg0) {
    }

    @Override
    public void onAdColonyAdAvailabilityChange(boolean available, String zone_id) {
        if (available)
            Log.d("AdColony", "Video Ad available");
    }

    @Override
    public void onAdColonyV4VCReward(AdColonyV4VCReward reward) {
        if (reward.success()) {
            Log.d("AdColony", "Earned " + reward.amount() + " credits.");
            vc_name = reward.name();
            credits += reward.amount();
            updateCredits(credits);
        }
    }

    private void updateCredits(int amount) {
        TextView t = (TextView) mNavigationDrawerFragment.getView()
                .findViewById(R.id.stats_label);
        t.setText("Credits: " + amount);
        properties.setProperty("vc_name", vc_name);
        properties.setProperty("total_amount", "" + credits);
        try {
            OutputStream outfile = openFileOutput("vc_info.properties", 0);
            properties.store(outfile, "vc info");
            outfile.close();
        } catch (Exception err) {
        }
    }

    void loadProperties() {
        properties = new Properties();
        try {
            properties.load(openFileInput("vc_info.properties"));
            vc_name = properties.getProperty("vc_name", "credits");
            credits = Integer.parseInt(properties.getProperty("total_amount",
                    "0"));
        } catch (Exception err) {
            vc_name = "credits";
            credits = 0;
        }
        updateCredits(credits);
    }

    @Override
    public boolean guess(String guess) {
        if (normalize(guess).equals(
                normalize(cardFragment.getCard().getAnswer()))) {
            try {
                setCardSolved(cardFragment.getCard());
            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), e.getMessage());
                // e.printStackTrace();
            }
            unlockCard(cardFragment.getCard().getId() + 1);
            cardFragment.showMessage("Correct!");
            return true;
        } else {
            // cardFragment.showMessage("Nope :(");
            cardFragment.shake();
            return false;
        }

    }

    private void unlockCard(int id) {
        Log.d(TAG, "Unlocked card id: " + id);
        try {
            checkDb();
            Card card = deck.get(id % 100);
            card.locked = false;
            ContentValues values = new ContentValues();
            values.put("locked", 0);
            String where = "_id='" + id + "'";
            database.update(BackTalkDbHelper.TABLE_CARDS, values, where, null);
        } catch (Exception e) {
            Log.d(TAG, "AS;LDFJASLPDFJA;SLDKFJA;SLKDJF");
            Log.d(TAG, e.getMessage());
        }
    }

    private String normalize(String s) {
        return s.replaceAll("\\W", "").toUpperCase(Locale.ENGLISH);
    }

    @Override
    public void onCardSelected(int id) {
        cardFragment = CardFragment.newCard(deck.get(id));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.center, cardFragment)
                .replace(R.id.bottom, controlFragment).addToBackStack(null)
                .commit();
    }

    @Override
    public void showControls(boolean show) {
        ((FrameLayout) findViewById(R.id.bottom))
                .setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
