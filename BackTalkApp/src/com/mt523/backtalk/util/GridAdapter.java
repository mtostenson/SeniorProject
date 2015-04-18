package com.mt523.backtalk.util;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mt523.backtalk.R;
import com.mt523.backtalk.fragments.DeckFragment.DeckInterface;
import com.mt523.backtalk.packets.client.Card;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private DeckInterface activity;
    private ArrayList<Card> cards;

    public GridAdapter(Context context, DeckInterface activity,
            ArrayList<Card> cards) {
        this.context = context;
        this.activity = activity;
        this.cards = cards;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View button;
        if (convertView == null) {
            Typeface font = FontUtil.instance(context).getFont();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            button = inflater.inflate(R.layout.grid_item, parent, false);
            ((TextView) button).setTypeface(font);
        } else {
            button = (TextView) convertView;
        }
        ((TextView) button).setText(Integer.toString(position + 1));
//        button.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                activity.onCardSelected(position);
//            }
//        });
        if (cards.get(position).locked) {
            button.setEnabled(false);
        }
        return button;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}