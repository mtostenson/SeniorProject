package com.mt523.backtalk.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mt523.backtalk.R;
import com.mt523.backtalk.packets.client.Card;
import com.mt523.backtalk.util.BtAnimations;
import com.mt523.backtalk.util.FontUtil;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class CardFragment extends Fragment {
    private static final String TAG = CardFragment.class.getName();

    private CardInterface activity;
    private Card card;
    private TextView display, resultMessage, category, cardId;

    public static CardFragment newCard(Card card) {
        CardFragment cardFragment = new CardFragment();
        cardFragment.card = card;
        return cardFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.card_layout, container, false);
        // activity = (CardInterface) getActivity();
        display = (TextView) rootView.findViewById(R.id.display);
        resultMessage = (TextView) rootView
                .findViewById(R.id.result_message_view);
        category = (TextView) rootView.findViewById(R.id.category);
        cardId = (TextView) rootView.findViewById(R.id.card_id);
        display.setTypeface(FontUtil.instance(
                getActivity().getApplicationContext()).getFont());
        resultMessage.setTypeface(FontUtil.instance(
                getActivity().getApplicationContext()).getFont());
        display.setText("\"" + card.getQuestion() + "\"");
        category.setText(card.getCategory());
        cardId.setText(Integer.toString(card.getId() % 100 + 1));
        display.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToCard(card.getId() + 1);
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (CardInterface) activity;
    }

    @Override
    public void onResume() {
        super.onDetach();
        activity.showControls(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        Crouton.cancelAllCroutons();
        activity.showControls(false);
    }

    public Card getCard() {
        return this.card;
    }

    public void showMessage(String message) {
        resultMessage.setText(message);
        resultMessage.setVisibility(View.VISIBLE);
    }

    public void shake() {
        BtAnimations.instance.shake(display);
    }

    public interface CardInterface {

        public void goToCard(int index);

        public int getIndex();

        public void setCardSolved(Card card);

        public void showControls(boolean show);

    }
}
