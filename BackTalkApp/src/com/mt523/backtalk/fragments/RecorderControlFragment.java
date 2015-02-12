package com.mt523.backtalk.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mt523.backtalk.R;

public class RecorderControlFragment extends Fragment {

	private Button btnGuess;
	private RecorderControlFragment recorderControlFragment = this;

	public RecorderControlFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.controller, container, false);

		btnGuess = (Button) rootView.findViewById(R.id.btnGuess);
		btnGuess.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(RecorderControlFragment.class.getName(), "CLICK!");
				getFragmentManager()
						.beginTransaction()
						.addToBackStack(null)
						.replace(container.getId(), new GuessFragment())
						.setTransition(
								FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
						.commit();
			}
		});

		return rootView;
	}

}
