package com.example.noregrets;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class MessageFragment extends Fragment {
    public Activity containerActivity = null;
    public String name = "";
    public String phone = "";

    public MessageFragment(String name, String phone) {
        this.name = name;
        this.phone = phone;

    }

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_message, container, false);
        TextView tv = (TextView)v.findViewById(R.id.name);
        tv.setText(this.name + " : " + this.phone);

        return v;
    }


}