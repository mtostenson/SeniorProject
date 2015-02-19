package com.mt523.backtalk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mt523.backtalk.R;
import com.mt523.backtalk.packets.client.CardPacket;
import com.mt523.backtalk.util.FontUtil;

public class CardFragment extends Fragment {

    private CardInterface activity;
    private CardPacket card;
    private TextView tv;
    private Button btnPrev;
    private Button btnNext;

    public CardFragment(CardPacket basePacket) {
        super();
        card = basePacket;
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
                activity.prevCard();
            }
        });
        btnNext = (Button) rootView.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.nextCard();                
            }            
        });
        return rootView;
    }

    public interface CardInterface {
        public void nextCard();

        public void prevCard();
    }
}
