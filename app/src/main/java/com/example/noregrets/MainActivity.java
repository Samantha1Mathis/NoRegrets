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
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.gsm.SmsManager;
import android.util.DisplayMetrics;
import android.view.View;
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
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    MessageFragment messageFragment = null;
    String phone="";
    String pref = TASKS_THEME;
    double xInches = 0.0;
    double yInches = 0.0;
    DisplayMetrics metrics = new DisplayMetrics();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        setContentView(R.layout.activity_main);
        createMainFrag();
        setTheme();
        permission();
    }

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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++){
            if (requestCode == 1 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {


            }
        }

    }
    /**
     * @return true if the screen is larger than 5 by 5 inches, fale otherwise
     */
    private boolean isBigScreen() {
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.xInches = (((float)this.metrics.widthPixels) / metrics.xdpi);
        this.yInches = (((float)this.metrics.widthPixels) / metrics.xdpi);
        return xInches > 5.0 && yInches > 5.0;
    }

    public void settingClick(View v){
        createSettingFrag();
    }
    public void helpClick(View v){
        createHelpFrag();
    }
    public void resultsClick(View v){
        createResultsFrag();
    }
    public void beginClick(View v){
        createQuestFrag();
    }

    public void nextClick(View v){
        createQuestFrag();
    }

    public void submitClick(View v){
        createTextFrag();
    }

    public void newClick(View v){
        createNewMessageFrag();
    }

    public void createMainFrag(){
        MainFragment displayFragment = new MainFragment();
        Bundle args = new Bundle();

        displayFragment.setArguments(args);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        displayFragment.setContainerActivity(this);
        transaction.replace(R.id.outer,
                displayFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void createSettingFrag(){
        SettingFragment settingFragment = new SettingFragment();
        Bundle args = new Bundle();

        settingFragment.setArguments(args);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        settingFragment.setContainerActivity(this);
        transaction.replace(R.id.outer,
                settingFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void createHelpFrag(){
        HelpFragment helpFragment = new HelpFragment();
        Bundle args = new Bundle();

        helpFragment.setArguments(args);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        helpFragment.setContainerActivity(this);
        transaction.replace(R.id.outer,
                helpFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void createResultsFrag(){
        ResultsFragment resultsFragment = new ResultsFragment();
        Bundle args = new Bundle();

        resultsFragment.setArguments(args);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        resultsFragment.setContainerActivity(this);
        transaction.replace(R.id.outer,
                resultsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void createQuestFrag(){
        System.out.println("!!!" + pref);
        QuestionsFragment questFragment = new QuestionsFragment(pref);
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

    public void createMessageFrag(String name, String phone){
        this.phone = phone;
        this.messageFragment = new MessageFragment(name, phone);
        Bundle args = new Bundle();
        //args.putString("display_text", displayText);
        this.messageFragment.setArguments(args);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        this.messageFragment.setContainerActivity(this);
        if (isBigScreen()){
            //setContentView(R.layout.activity_main_big);
            transaction.replace(R.id.outer_big,
                    this.messageFragment);
        }else {
            transaction.replace(R.id.outer,
                    this.messageFragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private static final String TASKS_THEME = "THEME";
    private static final String THEME_DARK = "DARK";
    private static final String THEME_LIGHT = "LIGHT";

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

    public void onRadioButtonClicked(View view) {
        //Determines which answer was picked
        boolean checked = ((RadioButton) view).isChecked();
        if (view.getId() == R.id.sober) {
            if (checked) {
                //do something
            }
        }
        if (view.getId() == R.id.drunk) {
            if (checked) {
                //do something
            }
        }

    }

    private static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView mostPicture = null;
    private String currentPhotoPath = "";

    public void takePhoto (View view){
        //gets which picture was clicked in order to know where to place the new picture
        //mostPicture = (ImageView)findViewById(R.id.image);
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

    public File createImageFile () throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        this.currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

            //Re-figures the image to be a square
            int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
            Bitmap resized = Bitmap.createBitmap(bitmap, 0, 0, size, size);
            //mostPicture.setImageBitmap(resized);
            addImage(resized);
            File file = new File(currentPhotoPath);
            System.out.println(file);
            sendImageIntent(file);
        }
    }

    public void addImage(Bitmap bitmap){
        ImageView iv = new ImageView(this);
        LinearLayout ll = findViewById(R.id.image);
        iv.setImageBitmap(bitmap);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 75);
        lp.setMargins(20, 20, 20, 20);

        iv.setLayoutParams(lp);

        ll.addView(iv);

    }

    public void sendImageIntent(File file){
        System.out.println(file);

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
    public void draw(View v){

    }
    /**
     * PURPOSE: This method get initialized by the clear button then it
     * calls the clear drawing method from the fragment
     */
    public void clearDrawing(View v) {

        messageFragment.clearDrawing(v);
    }

    String address;
    public String getConversationInfo(View v) {
        String text = ((TextView)v).getText().toString();
        String thread_id = text.substring(text.indexOf(" :: ") + 4);
        this.address = text.substring(text.indexOf(" || ") + 4, text.indexOf(" :: "));
        String displayText = "";
        //displayText += "Conversation with " + address + "\n\n\n";

        Uri uriSMSURI = Uri.parse("content://sms/");
        Cursor cur = getContentResolver().query(uriSMSURI, null, "thread_id=" + thread_id, null, null);
        while (cur.moveToNext()){
            //Available columns: [_id, thread_id, address, person, date, date_sent, protocol, read, status,
            // type, reply_path_present, subject, body, service_center, locked, sub_id, error_code, creator, seen]
            String type = cur.getString(cur.getColumnIndexOrThrow("type"));
            /*System.out.println(cur.getString(cur.getColumnIndexOrThrow("creator")) + " : " +
                    cur.getString(cur.getColumnIndexOrThrow("person")) + " : " +
                    cur.getString(cur.getColumnIndexOrThrow("thread_id")) + " : " +
                    cur.getString(cur.getColumnIndexOrThrow("protocol")) + " : " +
                    cur.getString(cur.getColumnIndexOrThrow("sub_id")) + " : " +
                    cur.getString(cur.getColumnIndexOrThrow("reply_path_present")) + " : " +
                    cur.getString(cur.getColumnIndexOrThrow("type")) + " : " +
                    cur.getString(cur.getColumnIndexOrThrow("status")));*/
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

    public void sendSMSMessage(String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(this.phone, null, message, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

}