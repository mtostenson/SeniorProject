package com.mt523.backtalk.fragments;

import com.mt523.backtalk.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GuessFragment extends Fragment {

	public GuessFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_guess, container, false);
	}
	
}
