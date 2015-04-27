package com.mt523.backtalk.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mt523.backtalk.R;
import com.mt523.backtalk.util.BtAnimations;
import com.mt523.backtalk.util.FontUtil;
import com.mt523.backtalk.util.WavRecorder.ReverseProgressUpdater;

public class ProgressFragment extends Fragment implements
        ReverseProgressUpdater {

    ProgressBar progressBar;

    public ProgressFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.progress, container, false);
        TextView textView = (TextView) rootView
                .findViewById(R.id.progress_text);
        textView.setTypeface(FontUtil.instance(
                getActivity().getApplicationContext()).getFont());
        textView.startAnimation(BtAnimations.pulse(1000));
        return rootView;
    }

    @Override
    public void onReverseUpdate(int percentage) {
        // progressBar.setProgress(percentage);
    }

    @Override
    public void onReverseCompleted() {
        getFragmentManager().popBackStack();
    }

}
