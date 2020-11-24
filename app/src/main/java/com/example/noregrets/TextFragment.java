package com.example.noregrets;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;

import android.widget.ListView;


import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class TextFragment extends Fragment {

    private Activity containerActivity = null;
    private View v = null;

    private ListView messagesListView;
    ArrayAdapter<String> messagesAdapter = null;
    private ArrayList<String> messages = new ArrayList<String>();

    public TextFragment() { }

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_text, container, false);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        getContacts();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupContactsAdapter();
    }

    public void getContacts() {

        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri uri = Uri.parse("content://mms-sms/conversations/");
        Cursor query = contentResolver.query(uri,
                new String[]{"_id", "ct_t", "thread_id", "address"},
                null, null, null);
        while (query.moveToNext()) {
            String thread_id = query.getString(query.getColumnIndex("thread_id"));
            String address = query.getString((query.getColumnIndex("address")));
            String id = query.getString(query.getColumnIndex("_id"));
            String name = ((MainActivity)getActivity()).getContactName(address, containerActivity);
            String row = "Convo || " + name + " :: " +thread_id;
            messages.add(row);
        }
        query.close();
    }


    private void setupContactsAdapter() {
        messagesListView = containerActivity.findViewById(R.id.contact_list_view);
        messagesAdapter = new
                ArrayAdapter<String>(containerActivity, R.layout.text_row,
                R.id.text_row_text_view, messages);
        messagesListView.setAdapter(messagesAdapter);
    }

}