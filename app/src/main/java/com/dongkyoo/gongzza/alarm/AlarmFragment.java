package com.dongkyoo.gongzza.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dongkyoo.gongzza.R;

public class AlarmFragment extends Fragment {

    public AlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getFragmentManager().beginTransaction()
                .replace(R.id.container, new AlarmPreferenceFragment())
                .addToBackStack(null)
                .commit();

        return inflater.inflate(R.layout.fragment_alarm, container, false);
    }
}