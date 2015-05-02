package com.mt523.backtalk.util;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mt523.backtalk.R;
import com.mt523.backtalk.fragments.DeckFragment.DeckInterface;
import com.mt523.backtalk.packets.client.Card;

public class GridAdapter extends ArrayAdapter<Card> {

    private static final String TAG = GridAdapter.class.getName();

    private Context context;
    private DeckInterface activity;
    private ArrayList<Card> cards;

    private Typeface font;
    private LayoutInflater inflater;

    public GridAdapter(Context context, ArrayList<Card> deck,
            DeckInterface activity) {
        super(context, R.layout.grid_item, deck);
        this.context = context;
        this.activity = activity;
        this.cards = deck;
        font = FontUtil.instance(context).getFont();
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View button;
        if (convertView == null) {
            button = inflater.inflate(R.layout.grid_item, parent, false);
            ((TextView) button).setTypeface(font);
        } else {
            button = (TextView) convertView;
        }
        if (cards.get(position).locked) {
            button.setEnabled(false);
        }
        ((TextView) button).setText(Integer.toString(position + 1));
        return button;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}