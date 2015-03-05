/**
 * @Title WavRecorder.java 
 * @author Michael Tostenson
 */

package com.mt523.backtalk.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioRecord.OnRecordPositionUpdateListener;
import android.media.MediaRecorder.AudioSource;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class WavRecorder implements OnRecordPositionUpdateListener {

    // Number of samples per second (44.1 kHz)
    private final int SAMPLE_RATE = 44100;

    // The time interval in milliseconds between outputting samples
    private final int TIMER_INTERVAL = 120;

    // Sample size in bits
    private final short SAMPLES = 16;

    // Number of channels, monophonic audio is 1
    private final int CHANNELS = 1;

    // Tells android hardware to record from the built-in mic
    private final int AUDIO_SOURCE = AudioSource.MIC;

    private final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private final int FRAME_PERIOD = SAMPLE_RATE * TIMER_INTERVAL / 1000;

    private final int BUFFER_SIZE = FRAME_PERIOD * 2 * SAMPLES / 8;

    private State state;

    private int amplitude;

    private int payloadSize;

    private long dataBegin;

    private final Context context;

    private AudioRecord audioRecord;

    private RandomAccessFile writerForward;

    private RandomAccessFile writerReverse;

    private File fileForward;

    private File fileReverse;

    private byte[] buffer;

    private ReverseProgressUpdater reverseProgressUpdater;

    private Reverser reverser;

    public enum State {
        INITIALIZING, READY, RECORDING, ERROR, STOPPED
    };

    // Constructor -------------------------------------------------------------
    public WavRecorder(Context context) {
        this.context = context;
        try {
            audioRecord = new AudioRecord(AUDIO_SOURCE, SAMPLE_RATE, CHANNELS,
                    AUDIO_FORMAT, BUFFER_SIZE);
            if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
                throw new Exception("AudioRecord initialization failed.");
            }
            audioRecord.setRecordPositionUpdateListener(this);
            audioRecord.setPositionNotificationPeriod(FRAME_PERIOD);
            state = State.INITIALIZING;
        } catch (Exception e) {
            Log.e(WavRecorder.class.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    // Prepares the recording for recording and writes WAV header --------------
    public void prepare() {
        try {
            if (state == State.INITIALIZING) {
                if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {

                    // External storage directory
                    File folder = new File(
                            Environment.getExternalStorageDirectory()
                                    + "/Backtalk/");
                    folder.mkdir();

                    // fileForward = new File(context.getFilesDir(),
                    // "forward.wav");
                    fileForward = new File(folder, "EXT_TEST_FORWARD.wav");

                    // fileReverse = new File(context.getFilesDir(),
                    // "reverse.wav");
                    fileReverse = new File(folder, "EXT_TEST_REVERSE.wav");

                    writerForward = new RandomAccessFile(fileForward, "rw");
                    writerReverse = new RandomAccessFile(fileReverse, "rw");
                    createWavHeader(writerForward);
                    createWavHeader(writerReverse);
                    dataBegin = writerForward.getFilePointer();
                    buffer = new byte[FRAME_PERIOD * SAMPLES / 8 * CHANNELS];
                    state = State.READY;
                } else {
                    Log.e(WavRecorder.class.getName(),
                            "prepare() called on unitialized recorder");
                    state = State.ERROR;
                }
            } else {
                Log.e(WavRecorder.class.getName(),
                        "prepare() called on unitialized recorder.");
                state = State.ERROR;
            }
        } catch (Exception e) {
            Log.e(WavRecorder.class.getName(), e.getMessage());
            state = State.ERROR;
        }
    }

    // start() called after prepare() and sets state to RECORDING --------------
    public void start() {
        if (state == State.READY) {
            payloadSize = 0;
            audioRecord.startRecording();
            audioRecord.read(buffer, 0, buffer.length);
            state = State.RECORDING;
        } else {
            Log.e(WavRecorder.class.getName(),
                    "start() called on illegal state");
            state = State.ERROR;
        }
    }

    // Recording method --------------------------------------------------------
    @Override
    public void onPeriodicNotification(AudioRecord recorder) {
        audioRecord.read(buffer, 0, buffer.length);
        try {
            writerForward.write(buffer);
            payloadSize += buffer.length;
            for (int i = 0; i < buffer.length / 2; i++) {
                short curSample = getShort(buffer[i * 2], buffer[i * 2 + 1]);
                if (curSample > amplitude) {
                    amplitude = curSample;
                }
            }
        } catch (IOException e) {
            Log.e(WavRecorder.class.getName(), e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onMarkerReached(AudioRecord recorder) {
        // Unused
    }

    // Converts a byte[2] to a short in little-ending format -------------------
    private short getShort(byte b1, byte b2) {
        return (short) (b1 | (b2 << 8));
    }

    // Creates WAV file header per audio recorder specs ------------------------
    public void createWavHeader(RandomAccessFile writer) throws IOException {
        writer.setLength(0);
        writer.writeBytes("RIFF");
        writer.writeInt(0);
        writer.writeBytes("WAVE");
        writer.writeBytes("fmt ");
        writer.writeInt(Integer.reverseBytes(16));
        writer.writeShort(Short.reverseBytes((short) 1));
        writer.writeShort(Short.reverseBytes((short) CHANNELS));
        writer.writeInt(Integer.reverseBytes(SAMPLE_RATE));
        writer.writeInt(Integer.reverseBytes(SAMPLE_RATE * SAMPLES * CHANNELS
                / 8));
        writer.writeShort(Short.reverseBytes((short) (CHANNELS * SAMPLES / 8)));
        writer.writeShort(Short.reverseBytes(SAMPLES));
        writer.writeBytes("data");
        writer.writeInt(0);
    }

    // Completes WAV header with information determined by the recording -------
    public void completeWavHeader(RandomAccessFile writer) throws IOException {
        writer.seek(4);
        writer.writeInt(Integer.reverseBytes(36 + payloadSize));
        writer.seek(40);
        writer.writeInt(Integer.reverseBytes(payloadSize));
    }

    // Reverses samples from original recording and writes to file -------------
    public void reverse() throws IOException {
        reverser = new Reverser();
        reverser.execute();
    }

    // Stops recording and sets state to STOPPED -------------------------------
    public void stop() {
        if (state == State.RECORDING) {
            audioRecord.stop();
            try {
                completeWavHeader(writerForward);
                completeWavHeader(writerReverse);
            } catch (IOException e) {
                Log.e(WavRecorder.class.getName(),
                        "I/O exception occured while closing the output file.");
                state = State.ERROR;
            }
            state = State.STOPPED;
        } else {
            Log.e(WavRecorder.class.getName(), "stop() called on illegal state");
            state = State.ERROR;
        }
    }

    // Releases the resources and deletes unneccessary files -------------------
    public void release() {
        if (state == State.RECORDING) {
            stop();
        } else if (state == State.READY) {
            try {
                writerForward.close();
                writerReverse.close();
            } catch (IOException e) {
                Log.e(WavRecorder.class.getName(),
                        "I/O exception occured while closing the output file.");
            }
        }
    }

    // Reverser class to process audio async -----------------------------------
    private class Reverser extends AsyncTask<Void, Integer, Void> {

        int SAMPLE_SIZE;
        long dataLength;
        long locater;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                SAMPLE_SIZE = CHANNELS * SAMPLES / 8;
                dataLength = writerForward.length() - SAMPLE_SIZE;
                locater = dataLength;
                writerReverse.seek(dataBegin);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                while (locater >= dataBegin) {
                    byte[] currentSample = new byte[SAMPLE_SIZE];
                    writerForward.seek(locater);
                    writerForward.read(currentSample, 0, SAMPLE_SIZE);
                    writerReverse.write(currentSample);
                    locater -= SAMPLE_SIZE;
//                    double completion = (dataLength - locater) / dataLength;
//                    reverser.onProgressUpdate((int) (completion * 100));
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            reverseProgressUpdater.onReverseUpdate(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                writerForward.close();
                writerReverse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            reverseProgressUpdater.onReverseCompleted();
        }
    }

    public interface ReverseProgressUpdater {
        public void onReverseUpdate(int percentage);

        public void onReverseCompleted();
    }

    public void SetReverseProgressUpdater(ReverseProgressUpdater updater) {
        this.reverseProgressUpdater = updater;
    }

}