package com.mt523.backtalk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mt523.backtalk.R;
import com.mt523.backtalk.packets.BasePacket;

public class CardFragment extends Fragment {

    BasePacket card;

    public CardFragment(BasePacket basePacket) {
        super();
        card = basePacket;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.card_layout, container, false);
        ((TextView) rootView.findViewById(R.id.display)).setText(card.getQ());
        return rootView;
    }
}
