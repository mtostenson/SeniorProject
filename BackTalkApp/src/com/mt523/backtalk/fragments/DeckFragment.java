package com.mt523.backtalk.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.mt523.backtalk.R;
import com.mt523.backtalk.packets.client.Card;
import com.mt523.backtalk.util.GridAdapter;

public class DeckFragment extends Fragment {

    private static final String TAG = DeckFragment.class.getName();

    private DeckInterface activity;
    private ArrayList<Card> deck;
    private GridAdapter adapter;

    public static final DeckFragment instance(ArrayList<Card> deck) {
        DeckFragment deckFragment = new DeckFragment();
        deckFragment.deck = deck;
        return deckFragment;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deck, container,
                false);
        GridView grid = (GridView) rootView.findViewById(R.id.deck_grid);
        adapter = new GridAdapter(getActivity().getBaseContext(), activity,
                deck);
        grid.setAdapter(adapter);
        // grid.setOnItemClickListener(new OnItemClickListener() {
        // @Override
        // public void onItemClick(AdapterView<?> parent, View v,
        // int position, long id) {
        // activity.onCardSelected(position);
        // }
        // });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (DeckInterface) getActivity();
    }

    public interface DeckInterface {

        public void onCardSelected(int id);

    }

}
