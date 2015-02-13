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

	public RecorderControlFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.controller, container, false);
		final RecordControlInterface activity = (RecordControlInterface) getActivity();
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btnRecord: {
					activity.onRecord();
					break;
				}
				case R.id.btnGuess: {
					Log.d(activity.getClass().getName(), "CLICKCLICKCLICK");
					activity.onGuess();
					break;
				}
				case R.id.btnPlay: {
					activity.onPlay();
					break;
				}
				}
			}
		};
		((Button) rootView.findViewById(R.id.btnRecord))
				.setOnClickListener(onClickListener);
		((Button) rootView.findViewById(R.id.btnGuess))
				.setOnClickListener(onClickListener);
		((Button) rootView.findViewById(R.id.btnPlay))
				.setOnClickListener(onClickListener);
		return rootView;
	}

	public interface RecordControlInterface {
		public void onGuess();

		public void onRecord();

		public void onPlay();
	}
}
