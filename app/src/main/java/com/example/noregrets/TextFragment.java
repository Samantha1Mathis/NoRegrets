package com.example.noregrets;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

/*
 * @author: Samantha Mathis, Jacob Hurley
 * @class: CSC 317
 * @description: This is the text fragment which displays the previous conversations
 * the user has stored in their phone, each are clickable to continue messaging the specified contact
 * or to create a new conversation with another contact
 */
public class TextFragment extends Fragment {
    private Activity containerActivity = null;
    private View v = null;
    private ListView messagesListView;
    private ArrayAdapter<String> messagesAdapter = null;
    private ArrayList<String> messages = new ArrayList<String>();

    public TextFragment() { }

    /**
     * PURPOSE: This method connects this fragment
     * to the activity that created it
     *
     * @param containerActivity, which would be MainActivity
     */
    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_text, container, false);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    ((AppCompatActivity)getContext()).getSupportFragmentManager().popBackStack("main", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ((MainActivity)getActivity()).createMainFrag();
                    return true;
                }
                return false;
            }
        } );

        return v;
    }

    /**
     * PURPOSE: This method calls the getContact method
     * @param savedInstance
     */
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        getContacts();
    }

    /**
     * PURPOSE: This method adds all the contacts to an array adapter
     * to be displayed for the user to select a previous conversation
     */
    @Override
    public void onResume() {
        super.onResume();
        setupContactsAdapter();
    }

    /**
     * PURPOSE: This method adds all the previous conversations the user has stored,
     * along with the contact name or phone number
     */
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

    /**
     * PURPOSE: This method sets up the message adapter
     * to display the conversations the user has.
     */
    private void setupContactsAdapter() {
        messagesListView = containerActivity.findViewById(R.id.contact_list_view);
        messagesAdapter = new
                ArrayAdapter<String>(containerActivity, R.layout.text_row,
                R.id.text_row_text_view, messages);
        messagesListView.setAdapter(messagesAdapter);
    }

}