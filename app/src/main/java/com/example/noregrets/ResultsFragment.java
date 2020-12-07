package com.example.noregrets;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * @author: Samantha Mathis, Jacob Hurley
 * @class: CSC 317
 * @description: This is the results fragment which displays the
 * users statistics on their past questions that they have answered
 */
public class ResultsFragment extends Fragment {

    public ResultsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_results, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        ArrayList<String> attempts = new ArrayList<>();
        final int numberOfAttempts = preferences.getInt("AttemptNumber",0);
        for(int i = numberOfAttempts; i > 0; i--){
            attempts.add("Attempt Number " + i);
        }
        ListView results = (ListView) v.findViewById(R.id.result_list);
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getContext(),R.layout.results_layout,R.id.results_row_item , attempts);
        results.setAdapter(itemsAdapter);
        results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity) getActivity()).createSingleAttemptResultPage(numberOfAttempts-i);
            }
        });
        return v;

    }
}