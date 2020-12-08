package com.example.noregrets;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.gsm.SmsManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * @author: Samantha Mathis, Jacob Hurley
 * @class: CSC 317
 * @description: This is the main activity which implements each of
 * the fragments to be initialized.
 */
public class MainActivity extends AppCompatActivity {
    private MessageFragment messageFragment = null;
    private String phone="";
    private String pref = TASKS_THEME;
    public DisplayMetrics metrics = new DisplayMetrics();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        createMainFrag();
        setTheme();
        permission();
        System.out.println("called");
        getDifficulty();
    }

    /**
     * PURPOSE: This method checks to make sure
     * the user has enabled all necessary permission
     * needed for the app -- Needs to be after API 23
     */
    public void permission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(android.Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(android.Manifest.permission.RECEIVE_MMS) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_SMS,
                        Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.RECEIVE_MMS, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET,
                        Manifest.permission.CAMERA}, 1);
            }
        }
    }

    /**
     * PURPOSE: This method is started if not all the permissions are enabled. It
     * creates a pop up window for the user to easily enable the permissions that
     * were denied
     * @param requestCode, The request code to allow the pop up to activate
     * @param permissions, The list of permissions needed to be allowed
     * @param grantResults, The list of granted results to see if the permission
     *                      was allowed
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++){
            if (requestCode == 1 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {


            }
        }

    }

    /**
     * PURPOSE: To call the method to create the settings fragment
     * @param v, the view where the button was clicked
     */
    public void settingClick(View v){
        createSettingFrag();
    }
    /**
     * PURPOSE: To call the method to create the help fragment
     * @param v, the view where the button was clicked
     */
    public void helpClick(View v){
        createHelpFrag();
    }
    /**
     * PURPOSE: To call the method to create the results fragment
     * @param v, the view where the button was clicked
     */
    public void resultsClick(View v){
        createResultsFrag();
    }

    /**
     * PURPOSE: To call the method to create the questions fragment,
     * to start the questions
     * @param v, the view where the button was clicked
     */
    public void beginClick(View v){
        createQuestFrag();
    }
    /**
     * PURPOSE: To call the method to create another instance of
     * the questions fragment
     * @param v, the view where the button was clicked
     */
    public void nextClick(View v){
        createQuestFrag();
    }

    /**
     * Needs to be deleted
     * */
    public void submitClick(View v){
        createTextFrag();
    }

    /**
     * PURPOSE: To call the method to create the new message fragment
     * @param v, the view where the button was clicked
     */
    public void newClick(View v){
        createNewMessageFrag();
    }

    /**
     * PURPOSE: To create the main display of the app
     * when it is first launched
     */
    public void createMainFrag(){
        MainFragment displayFragment = new MainFragment();
        Bundle args = new Bundle();

        displayFragment.setArguments(args);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.outer,
                displayFragment);
        transaction.addToBackStack("main");
        transaction.commit();
    }

    /**
     * PURPOSE: To create the settings fragment
     * to display the layout of the settings page
     */
    public void createSettingFrag(){
        SettingFragment settingFragment = new SettingFragment();
        Bundle args = new Bundle();

        settingFragment.setArguments(args);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.outer,
                settingFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * PURPOSE: To create the help fragment
     * to display the layout of the help page
     */
    public void createHelpFrag(){
        HelpFragment helpFragment = new HelpFragment();
        Bundle args = new Bundle();

        helpFragment.setArguments(args);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.outer,
                helpFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * PURPOSE: To create the results fragment
     * to display the layout of the results page
     */
    public void createResultsFrag(){
        ResultsFragment resultsFragment = new ResultsFragment();
        Bundle args = new Bundle();

        resultsFragment.setArguments(args);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.outer,
                resultsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * PURPOSE: To create the questions fragment
     * to display the layout of the questions page
     */
    public void createQuestFrag(){

        QuestionsFragment questFragment = new QuestionsFragment(pref, difficulty, this);
        Bundle args = new Bundle();

        questFragment.setArguments(args);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        questFragment.setContainerActivity(this);
        transaction.replace(R.id.outer,
                questFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * PURPOSE: To create the text fragment
     * to display the layout of the conversations
     * that the user can choose to text
     */
    public void createTextFrag(){
        TextFragment textFragment = new TextFragment();
        Bundle args = new Bundle();

        textFragment.setArguments(args);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        textFragment.setContainerActivity(this);
        transaction.replace(R.id.outer,
                textFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * PURPOSE: To create the new message fragment
     * to display the layout of when the user
     * wants to create a new message
     */
    public void createNewMessageFrag(){
        NewMessageFragment newMessageFragment = new NewMessageFragment();
        Bundle args = new Bundle();

        newMessageFragment.setArguments(args);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        newMessageFragment.setContainerActivity(this);
        transaction.replace(R.id.outer,
                newMessageFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * PURPOSE: To create the message fragment
     * to display the layout of the messaging portion
     * of the app
     * @param name, the name of the recipient
     * @param phone, the phone number of the recipient
     */
    public void createMessageFrag(String name, String phone){
        this.phone = phone;
        this.messageFragment = new MessageFragment(name, phone);
        Bundle args = new Bundle();
        this.messageFragment.setArguments(args);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        this.messageFragment.setContainerActivity(this);
        transaction.replace(R.id.outer,
                this.messageFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //Portion to enable different themes
    private static final String TASKS_THEME = "THEME";
    private static final String THEME_DARK = "DARK";
    private static final String THEME_LIGHT = "LIGHT";

    /**
     * PURPOSE: To have the users preference of light or dark
     * mode saved, in order to keep it the same theme
     * every time they go back to the app
     */
    public void setTheme() {
        SharedPreferences sharedPrefs = this.getPreferences(Context.MODE_PRIVATE);
        if (sharedPrefs.getString(TASKS_THEME, THEME_LIGHT).equals(THEME_LIGHT)) {
            setTheme(R.style.AppThemeLight);
            pref = THEME_LIGHT;
        } else {
            setTheme(R.style.AppThemeDark);
            pref = THEME_DARK;
        }
    }

    /**
     * PURPOSE: To update the theme based on when the
     * user clicks the button to switch between the two themes
     * @param v, the view where the button was clicked
     */
    public void toggleTheme(View v) {
        SharedPreferences sharedPrefs = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        String theme  = sharedPrefs.getString(TASKS_THEME, THEME_LIGHT);
        if (theme.equals(THEME_LIGHT)) {
            editor.putString(TASKS_THEME, THEME_DARK);
        } else {
            editor.putString(TASKS_THEME, THEME_LIGHT);
        }
        editor.commit();
        recreate();
    }

    /**
     * PURPOSE: TO change the type of questions depending
     * on which radio button was selected
     * @param view, the view where the radio was selected
     */
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (view.getId() == R.id.sober) {
            if (checked) {
                difficulty = 1;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("Difficulty", 1);
                editor.commit();
            }
        }
        if (view.getId() == R.id.drunk) {
            if (checked) {
                difficulty = 2;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("Difficulty", 2);
                editor.commit();
            }
        }

    }

    // Portion to enable images
    private static final int REQUEST_TAKE_PHOTO = 1;
    private String currentPhotoPath = "";

    /**
     * PURPOSE: This method creates the intent to open
     * the camera, then saves the image to a file inorder be send
     * and added to the layout
     * @param view, the view of the button was clicked
     */
    public void takePhoto (View view){
        //gets which picture was clicked in order to know where to place the new picture
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                //saves the image into a file
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //Checks to make sure the photoFile has been created
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.example.noregrets.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * PURPOSE: This method creates a file for an image
     * @return the file for the image
     * @throws IOException
     */
    public File createImageFile () throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        this.currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * PURPOSE: This method is called by the takePicture method in order to
     * start the intent to open the camera. It creates a bitmap sends
     * that file to the sendImageIntent method
     * @param requestCode, code to match it allow the the camera to open
     * @param data, data which is the intent (in this case the takeImage intent
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
            //Re-figures the image to be a square
            int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
            Bitmap resized = Bitmap.createBitmap(bitmap, 0, 0, size, size);
            addImage(resized);
            File file = new File(currentPhotoPath);
            sendImageIntent(file);
        }
    }

    /**
     * PURPOSE: This method adds the image that was taken, selected
     * or drawn to a layout to be displayed in the message fragment.
     * @param bitmap, the bitmap of the image being added
     */
    public void addImage(Bitmap bitmap){
        ImageView iv = new ImageView(this);
        LinearLayout ll = findViewById(R.id.image);
        iv.setImageBitmap(bitmap);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300, 100);
        lp.setMargins(20, 20, 20, 20);
        iv.setLayoutParams(lp);
        ll.addView(iv);

    }


    /**
     * PURPOSE: This method sends the image file as an intent
     * to be sent through message app (MMS doesn't send through
     * emulators)
     * @param file, the file that contains the image being sent
     */
    public void sendImageIntent(File file){
        Uri uri = FileProvider.getUriForFile(this,
                "com.example.noregrets.fileprovider", file);
        if (!this.address.equals("")){
            this.phone = this.address;
        }

        Intent smsIntent = new Intent(Intent.ACTION_SEND);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.setData(Uri.parse("sms:" + this.phone));

        smsIntent.putExtra(Intent.EXTRA_STREAM, uri);
        smsIntent.setType("image/jpeg");
        startActivity(smsIntent);
    }

    /**
     * PURPOSE: This method get initialized by the clear button then it
     * calls the clear drawing method from the fragment
     */
    public void clearDrawing(View v) {

        messageFragment.clearDrawing(v);
    }
    // Contains the phone number of the recipient
    private String address;

    /**
     * PURPOSE: This method gets previous messages send back and forth
     * from the contact that the user wants to send messages to.
     * It separates messages between which person sent what to be easily
     * distinguished
     * @param v, the view of which button was clicked
     * @return, a string that contains all the message data
     */
    public String getConversationInfo(View v) {
        String text = ((TextView)v).getText().toString();
        String thread_id = text.substring(text.indexOf(" :: ") + 4);
        this.address = text.substring(text.indexOf(" || ") + 4, text.indexOf(" :: "));
        String displayText = "";

        Uri uriSMSURI = Uri.parse("content://sms/");
        Cursor cur = getContentResolver().query(uriSMSURI, null, "thread_id=" + thread_id, null, null);
        while (cur.moveToNext()){
            String type = cur.getString(cur.getColumnIndexOrThrow("type"));
            this.phone = cur.getString(cur.getColumnIndexOrThrow("address"));
            String name ="";
            if (Integer.parseInt(type) == 1){
                name = getContactName(phone, this);
            } else{
                name = "me";
            }

            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            displayText += name + " : " + body + "\n---\n";
        }
        cur.close();
        return displayText;
    }

    /**
     * PURPOSE: To grab the contact name to be displayed when given the phone number
     * @param phoneNumber, the phone number we are trying to match with a name from the contacts
     * @param context, the main activity
     * @return, a string with the contact's name
     */
    public String getContactName(final String phoneNumber, Context context)
    {
        Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
        String contactName="";
        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                contactName=cursor.getString(0);
            }
            cursor.close();
        }
        if (contactName == null || contactName.length() == 0){
            contactName = phoneNumber;
        }
        return contactName;
    }

    /**
     * PURPOSE: THis method allows users to send texts through NoRegrets
     * and have them show up on android's original messaging app. Creates
     * an SMS MANAGER to send the texts
     * @param message, the message the user wants to send.
     */
    public void sendSMSMessage(String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(this.phone, null, message, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * PURPOSE: This method grabs the info from the other methods,
     * to grab the conversations from the specified recipient, and
     * creates the message fragment with the address the user wants to message
     * @param v
     */
    public void onInfoClick(View v) {
        String name= "Number: ";
        String displayText = "";
        displayText = getConversationInfo(v);
        this.messageFragment = new MessageFragment(name, this.address);
        Bundle args = new Bundle();
        args.putString("display_text", displayText);
        this.messageFragment.setArguments(args);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        this.messageFragment.setContainerActivity(this);
        transaction.replace(R.id.outer,
                this.messageFragment);
        transaction.addToBackStack(null);
        transaction.commit();


    }
    public int NumberAnswered = 0;
    public int getNumberAnswered(){
        return NumberAnswered;
    }

    public int NumberCorrect = 0;
    public int getNumberCorrect(){
        return NumberCorrect;
    }

    public int difficulty;
    public void getDifficulty(){
        SharedPreferences preferences =   PreferenceManager.getDefaultSharedPreferences(this);
        this.difficulty = preferences.getInt("Difficulty", 1);
    }

    public ArrayList<ArrayList<String>> allAnswers = new ArrayList<>();
    public void addAnswer(ArrayList<String> singleQuestion){  // single question is in the format QUESTION, ANSWER, USERANSWER, CORRECT(right or wrong)
        if (singleQuestion.size() == 4){
            allAnswers.add(singleQuestion);
        }
    }

    public void createSingleAttemptResultPage(int i){
        SingleAttemptResult displayFragment = new SingleAttemptResult(i);
        Bundle args = new Bundle();

        displayFragment.setArguments(args);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.outer,
                displayFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}