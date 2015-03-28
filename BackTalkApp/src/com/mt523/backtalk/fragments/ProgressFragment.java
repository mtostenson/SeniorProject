package com.mt523.backtalk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mt523.backtalk.R;
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
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
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
