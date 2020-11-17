package com.example.noregrets;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class TextFragment extends Fragment {

    public Activity containerActivity = null;

    public TextFragment() {
        // Required empty public constructor
    }

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*String[] from = {"place", "author"};
        int[] to = {R.id.website, R.id.authors};
        View v = inflater.inflate(R.layout.fragment_display, container, false);
        SimpleAdapter simple = new SimpleAdapter(containerActivity, adapterList, R.layout.news_list, from, to);
        newsView = v.findViewById(R.id.news_item_list);
        newsView.setAdapter(simple);
        newsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick (AdapterView < ? > adapter, View view,int position, long arg){
                                                ((MainActivity)getActivity()).createMessageFragment(position, info);
                                            }
                                        }
        );
        return v;*/
        return inflater.inflate(R.layout.fragment_text, container, false);


    }
}