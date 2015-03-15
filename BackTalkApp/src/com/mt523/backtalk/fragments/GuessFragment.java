package com.mt523.backtalk.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
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
    private GuessInterface guessInterface;
    InputMethodManager imm;

    public GuessFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_guess, container,
                false);
        imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        btnGuess = (Button) rootView.findViewById(R.id.btnSubmitGuess);
        input = (EditText) rootView.findViewById(R.id.input);
        btnGuess.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                guessInterface.guess(input.getText().toString());
                getFragmentManager().popBackStack();
            }
        });
        // input.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        // imm.showSoftInput(input, 0);
        return rootView;
    }

    public void setGuessInterface(GuessInterface guessInterface) {
        this.guessInterface = guessInterface;
    }

    public interface GuessInterface {
        public void guess(String guess);
    }

}