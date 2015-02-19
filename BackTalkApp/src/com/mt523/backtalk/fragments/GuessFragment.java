package com.mt523.backtalk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.mt523.backtalk.R;

public class GuessFragment extends Fragment {

    private Button btnGuess;
    private EditText input;

    public GuessFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_guess, container,
                false);
        btnGuess = (Button) rootView.findViewById(R.id.btnSubmitGuess);
        input = (EditText) rootView.findViewById(R.id.input);
        btnGuess.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        input.requestFocus();        
        return rootView;
    }

}
