package com.example.noregrets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;

/*
 * @author: Samantha Mathis, Jacob Hurley
 * @class: CSC 317
 * @description: This is the settings fragment which allows the user to click to
 * see their statistics, change the theme of the app and set the difficulty of the questions
 */
public class SettingFragment extends Fragment {

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        if (((MainActivity) getActivity()).difficulty == 1){
            RadioButton button = (RadioButton) v.findViewById(R.id.sober);
            button.setChecked(true);
        }
        else{
            RadioButton button = (RadioButton) v.findViewById(R.id.drunk);
            button.setChecked(true);
        }
        return v;
    }
}