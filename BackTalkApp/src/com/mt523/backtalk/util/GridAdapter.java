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
    public ArrayList<TextView> buttons;

    public GridAdapter(Context context, ArrayList<Card> deck,
            DeckInterface activity) {
        super(context, R.layout.grid_item, deck);
        this.context = context;
        this.activity = activity;
        this.cards = deck;
        buttons = new ArrayList<TextView>();
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

        if (cards.get(position).locked) {
            button.setEnabled(false);
        }
        buttons.add((TextView) button);
        return button;
    }

    public void enableButton(int index) {
        buttons.get(index).setEnabled(true);
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