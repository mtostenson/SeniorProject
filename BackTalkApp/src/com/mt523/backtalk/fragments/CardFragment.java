package com.mt523.backtalk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mt523.backtalk.MainActivity;
import com.mt523.backtalk.R;
import com.mt523.backtalk.packets.client.Card;
import com.mt523.backtalk.util.FontUtil;

public class CardFragment extends Fragment {

    private CardInterface activity;
    private Card card;
    private TextView tv;
    private Button btnPrev;
    private Button btnNext;

    public CardFragment(Card card) {
        super();
        this.card = card;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.card_layout, container, false);
        activity = (CardInterface) getActivity();
        tv = (TextView) rootView.findViewById(R.id.display);
        tv.setTypeface(FontUtil.instance(getActivity().getApplicationContext())
                .getFont());
        tv.setText(card.getQuestion());
        btnPrev = (Button) rootView.findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToCard(activity.getIndex() - 1);
            }
        });
        btnNext = (Button) rootView.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToCard(activity.getIndex() + 1);
            }
        });
        return rootView;
    }

    public interface CardInterface {
       
        public void goToCard(int index);
        
        public int getIndex();
    }
}
