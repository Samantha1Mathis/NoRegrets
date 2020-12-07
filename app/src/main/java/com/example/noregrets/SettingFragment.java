package com.example.noregrets;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {
    public Activity containerActivity = null;

    public SettingFragment() {
        // Required empty public constructor
    }
    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
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