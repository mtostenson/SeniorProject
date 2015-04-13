package com.mt523.backtalk.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.mt523.backtalk.R;
import com.mt523.backtalk.util.FontUtil;

public class RecorderControlFragment extends Fragment {

    private RecordControlInterface recordControlInterface;

    // GUI ---------------------------------------------------------------------
    public Button bRecord;
    public Button bGuess;
    public Button bPlay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.controller, container, false);
        OnTouchListener onTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent m) {
                if (m.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                } else if (m.getAction() == MotionEvent.ACTION_UP) {
                    v.setPressed(false);
                    v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                }
                switch (v.getId()) {
                case R.id.btnRecord: {
                    if (m.getAction() == MotionEvent.ACTION_DOWN) {
                        recordControlInterface.onRecord();
                    } else if (m.getAction() == MotionEvent.ACTION_UP) {
                        recordControlInterface.onStopRecord();
                    }
                    return true;
                }
                case R.id.btnGuess: {
                    if (m.getAction() == MotionEvent.ACTION_DOWN) {
                        recordControlInterface.onGuess();
                    }
                    return true;
                }
                case R.id.btnPlay: {
                    if (m.getAction() == MotionEvent.ACTION_DOWN) {
                        recordControlInterface.onPlay();
                    }
                    return true;
                }
                }
                return false;
            }
        };
        bRecord = (Button) rootView.findViewById(R.id.btnRecord);
        bRecord.setOnTouchListener(onTouchListener);
        bRecord.setTypeface(FontUtil.instance(getActivity().getBaseContext())
                .getFont());
        bGuess = (Button) rootView.findViewById(R.id.btnGuess);
        bGuess.setOnTouchListener(onTouchListener);
        bGuess.setTypeface(FontUtil.instance(getActivity().getBaseContext())
                .getFont());
        bPlay = (Button) rootView.findViewById(R.id.btnPlay);
        bPlay.setOnTouchListener(onTouchListener);
        bPlay.setTypeface(FontUtil.instance(getActivity().getBaseContext())
                .getFont());
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        recordControlInterface = (RecordControlInterface) activity;
    }

    public interface RecordControlInterface {
        public void onGuess();

        public void onRecord();

        public void onStopRecord();

        public void onPlay();
    }
}