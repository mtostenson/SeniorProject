package com.mt523.backtalk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.mt523.backtalk.R;

public class RecorderControlFragment extends Fragment {

    public RecorderControlFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.controller, container, false);
        final RecordControlInterface activity = (RecordControlInterface) getActivity();
        OnTouchListener onTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent m) {
                switch (v.getId()) {
                case R.id.btnRecord: {
                    if (m.getAction() == MotionEvent.ACTION_DOWN) {
                        activity.onRecord();
                    } else if (m.getAction() == MotionEvent.ACTION_UP) {
                        activity.onStopRecord();
                    }
                    return true;
                }
                case R.id.btnGuess: {
                    if (m.getAction() == MotionEvent.ACTION_DOWN) {
                        activity.onGuess();
                    }
                    return true;
                }
                case R.id.btnPlay: {
                    if (m.getAction() == MotionEvent.ACTION_DOWN) {
                        activity.onPlay();
                    }
                    return true;
                }
                }
                return false;
            }
        };
        ((Button) rootView.findViewById(R.id.btnRecord))
                .setOnTouchListener(onTouchListener);
        ((Button) rootView.findViewById(R.id.btnGuess))
                .setOnTouchListener(onTouchListener);
        ((Button) rootView.findViewById(R.id.btnPlay))
                .setOnTouchListener(onTouchListener);
        return rootView;
    }

    public interface RecordControlInterface {
        public void onGuess();

        public void onRecord();

        public void onStopRecord();

        public void onPlay();
    }
}
