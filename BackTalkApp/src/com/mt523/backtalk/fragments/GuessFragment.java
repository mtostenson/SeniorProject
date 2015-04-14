package com.mt523.backtalk.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.mt523.backtalk.R;
import com.mt523.backtalk.util.FontUtil;
import com.mt523.backtalk.views.GuessInput;

public class GuessFragment extends Fragment {

    private static final String TAG = GuessFragment.class.getName();

    private Button btnGuess;
    private GuessInput input;
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
        btnGuess.setTypeface(FontUtil.instance(getActivity().getBaseContext())
                .getFont());
        input = (GuessInput) rootView.findViewById(R.id.input);
        input.setGuessFragment(this);
        btnGuess.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (guessInterface.guess(input.getText().toString())) {
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    getFragmentManager().popBackStack();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        guessInterface = (GuessInterface) activity;
    }

    public interface GuessInterface {
        public boolean guess(String guess);
    }

    @Override
    public void onResume() {
        super.onResume();
        input.requestFocus();
        imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
    }

    public void onUserCloseKeyboard() {
        Log.d(TAG, "User is backing out of keyboard");
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        getFragmentManager().popBackStack();
    }

}