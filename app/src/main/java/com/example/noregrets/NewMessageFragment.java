package com.example.noregrets;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import java.util.ArrayList;

/*
 * @author: Samantha Mathis, Jacob Hurley
 * @class: CSC 317
 * @description: This is the new message fragment which allows the user
 * to send a text to one of his contacts if they aren't displayed as a previous conversation
 */
public class NewMessageFragment extends Fragment {

    public Activity containerActivity = null;
    private ListView contactsListView;
    private ArrayAdapter<String> contactsAdapter = null;
    private ArrayList<String> contacts = new ArrayList<String>();

    public NewMessageFragment() {
        // Required empty public constructor
    }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_message, container, false);
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
     * to be displayed for the user to select a new person to text
     */
    @Override
    public void onResume() {
        super.onResume();
        setupContactsAdapter();
    }

    /**
     * PURPOSE: This method adds all the contacts from the contact app into an
     * array to later be displayed
     */
    public void getContacts() {
        int limit = 1000;
        Cursor cursor = containerActivity.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
        while (cursor.moveToNext() && limit > 0) {
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String given = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String contact = given + " :: " + contactId;
            contacts.add(contact);
            limit--;
        }
        cursor.close();
    }

    /**
     * PURPOSE: This method sets up the contact adapter
     * to display the contacts and make each clickable to
     * they start up a message fragment
     */
    private void setupContactsAdapter() {
        contactsListView =
                (ListView)containerActivity.findViewById(R.id.contact_list_view);
        contactsAdapter = new
                ArrayAdapter<String>(containerActivity, R.layout.contact_row,
                R.id.contact_row_text_view, contacts);
        contactsListView.setAdapter(contactsAdapter);
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick (AdapterView < ? > adapter, View view,int position, long arg){
                                                        String phone ="";
                                                        String name = "";
                                                        int idNum = position +1;
                                                        Cursor numbers =  containerActivity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + String.valueOf(idNum), null, null);

                                                        if (numbers.moveToNext()) {
                                                            phone = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                                            name = numbers.getString(numbers.getColumnIndex(
                                                                    ContactsContract.Contacts.DISPLAY_NAME));
                                                        }
                                                        String phoneNo = "";
                                                        String [] pieces = phone.split("");
                                                        for (String p:pieces){
                                                            if (p.length() != 0){
                                                                if (Character.isDigit(p.charAt(0))) {
                                                                    phoneNo += p;
                                                                }
                                                            }
                                                        }
                                                        ((MainActivity)getActivity()).createMessageFrag(name, phoneNo);

                                                    }
                                                }
        );
    }

}