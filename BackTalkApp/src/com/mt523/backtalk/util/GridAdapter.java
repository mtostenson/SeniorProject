package com.mt523.backtalk.util;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mt523.backtalk.R;
import com.mt523.backtalk.packets.client.Card;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Card> cards;

    public GridAdapter(Context context, ArrayList<Card> cards) {
        this.context = context;
        this.cards = cards;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            Typeface font = FontUtil.instance(context).getFont();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_item, parent, false);
            ((TextView) v).setTypeface(font);
        } else {
            v = (TextView) convertView;
        }
        ((TextView) v).setText(Integer.toString(position + 1));
        return v;
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