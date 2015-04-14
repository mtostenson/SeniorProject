package com.mt523.backtalk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import com.mt523.backtalk.fragments.GuessFragment;

public class GuessInput extends EditText {

    private final String TAG = GuessInput.class.getName();
    private GuessFragment guessFragment;

    public GuessInput(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GuessInput(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public void setGuessFragment(GuessFragment guessFragment) {
        this.guessFragment = guessFragment;
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            guessFragment.onUserCloseKeyboard();
            return true;
        } else {
            return super.dispatchKeyEventPreIme(event);
        }
    }
}
