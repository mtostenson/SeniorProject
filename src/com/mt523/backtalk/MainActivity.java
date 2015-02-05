package com.mt523.backtalk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.mt523.backtalk.util.WavRecorder;

public class MainActivity extends ActionBarActivity {

   private WavRecorder recorder;
   private Button btnRecord;
   private Button btnForward;
   private Button btnReverse;
   private File folder;

   @Override
   protected void onCreate(Bundle savedInstanceState) {      
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      folder = new File(Environment.getExternalStorageDirectory()
            + "/BackTalk/");
      btnRecord = (Button) findViewById(R.id.btnRecord);
      btnForward = (Button) findViewById(R.id.btnForward);
      btnReverse = (Button) findViewById(R.id.btnReverse);
      btnRecord.setOnTouchListener(new OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent e) {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
               btnForward.setEnabled(false);
               btnReverse.setEnabled(false);
               recorder = new WavRecorder(MainActivity.this);
               recorder.prepare();
               recorder.start();
            } else if (e.getAction() == MotionEvent.ACTION_UP) {
               try {
                  btnRecord.setEnabled(false);
                  recorder.stop();
                  recorder.reverse();
                  recorder.release();
                  recorder = null;
                  btnForward.setEnabled(true);
                  btnReverse.setEnabled(true);
                  btnRecord.setEnabled(true);
                  return true;
               } catch (IOException e1) {
                  e1.printStackTrace();
               }
            }
            return false;
         }
      });
      btnForward.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            MediaPlayer player = new MediaPlayer();
            try {               
               FileInputStream fis = new FileInputStream(folder
                     .getAbsolutePath() + "/EXT_TEST_FORWARD.wav");
               player.setDataSource(fis.getFD());
               player.prepare();
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
      });
      btnReverse.setOnClickListener(new OnClickListener() {         
         @Override
         public void onClick(View arg0) {
            MediaPlayer player = new MediaPlayer();
            try {
               FileInputStream fis = new FileInputStream(folder.getAbsolutePath() + "/EXT_TEST_REVERSE.wav");
               player.setDataSource(fis.getFD());
               player.prepare();
               player.start();
            } catch (FileNotFoundException e) {
               e.printStackTrace();
            } catch (IllegalArgumentException e) {
               e.printStackTrace();
            } catch (IllegalStateException e) {
               e.printStackTrace();
            } catch (IOException e) {
               e.printStackTrace();
            } 
         }
      });
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
}
