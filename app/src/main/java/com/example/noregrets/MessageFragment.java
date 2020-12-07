package com.example.noregrets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;

import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;

import android.view.LayoutInflater;
import android.view.MotionEvent;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import yuku.ambilwarna.AmbilWarnaDialog;


/*
 * @author: Samantha Mathis, Jacob Hurley
 * @class: CSC 317
 * @description: This is the message fragment which has all the
 * necessary elements to perform like a regular messaging app. It allows
 * the user to scroll through old messages, send texts, images (by opening up
 * the camera or gallery) or even drawing a picture for the recipient with a color
 * wheel to choose which color the paint is
 */
public class MessageFragment extends Fragment {
    private static final int RESULT_OK = -1;
    public Activity containerActivity = null;
    public String name = "";
    public String phone = "";
    private DrawingView dv = null;
    private int paintColor = Color.RED;
    private View v = null;
    private Button send;
    private Button draw;
    private Button photo;
    private EditText txtMessage;
    private Button colorPicker;
    private int seekBarValue =0;
    private SeekBar seekBar;

    /**
     * PURPOSE: Initializes the fragment with the name and phone number of
     * the recipient.
     * @param name, Name of the recipient
     * @param phone, Phone number attached with the name
     */
    public MessageFragment(String name, String phone) {
        this.name = name;
        this.phone = phone;

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

    private static final int RESULT_LOAD_IMAGE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.v =  inflater.inflate(R.layout.fragment_message, container, false);
        String displayText = getArguments().getString("display_text");

        //Allows message to be scrollable
        TextView tv2 = v.findViewById(R.id.messages);
        tv2.setText(displayText);
        tv2.setMovementMethod(new ScrollingMovementMethod());

        //Grabs the name of the user
        TextView tv = v.findViewById(R.id.name);
        String name = ((MainActivity)getActivity()).getContactName(this.phone, containerActivity);
        tv.setText(name);

        // Creates objects inorder to set up onClick methods
        send = (Button) v.findViewById(R.id.send);
        draw = (Button) v.findViewById(R.id.draw);
        photo = (Button) v.findViewById(R.id.photo);
        colorPicker = (Button) v.findViewById(R.id.color);
        txtMessage = (EditText) v.findViewById(R.id.current);

        //Sets up message length based on width of device
        int width = (((MainActivity)getActivity()).metrics.widthPixels)/70;
        txtMessage.setEms((int) width);

        // When the send button is clicked it activates the sendSMS method from
        // the main activity
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = txtMessage.getText().toString();

                ((MainActivity)getActivity()).sendSMSMessage(message);

            }
        });

        // When the draw button is clicked it activates the screenshot method
        draw.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    screenShot();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //When the photo button is clicked it activates an intent to open
        // the gallery to scroll through pictures
        photo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
         });

        //When the color button is clicked it calls the setColor method
        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor();
            }
        });

        seekBar = (SeekBar)v.findViewById(R.id.stroke);
        seekBar.getProgress();

        // When the seekbar is moved it calls the changeSize method to change the stroke width
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               seekBarValue = progress;
               dv.changeSize(seekBarValue);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Creates a DrawingView object from the main class
        dv = new DrawingView(containerActivity, null);
        dv.setupDrawing();

        //adds it to the view on the layout
        LinearLayout ll = v.findViewById(R.id.drawing);
        ll.addView(dv);
        return v;
    }

    /**
     * PURPOSE: This method takes a screenshot of the drawing layout
     * inorder to send it to the recipient
     * @throws IOException
     */
    public void screenShot() throws IOException {
        View view = this.v.findViewById(R.id.drawing);

        //Creates a bitmap (Screenshot of canvas)
        Bitmap bitmap = Bitmap.createBitmap(
                view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        // Saves it to a file
        File file = ((MainActivity)getActivity()).createImageFile();
        FileOutputStream outputStream;
        try {
            outputStream = ((MainActivity)getActivity()).openFileOutput(String.valueOf(file), containerActivity.MODE_PRIVATE);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, os);
        os.close();
        ((MainActivity)getActivity()).sendImageIntent(file);
        BitmapFactory.Options bmOptions2 = new BitmapFactory.Options();
        Bitmap bitmap2 = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions2);
        ((MainActivity)getActivity()).addImage(bitmap2);
    }

    /**
     * PURPOSE: This method is called when the photo intent is created in
     * order to open up the gallery to be search through and create
     * file of the image selected to then be sent to the user.
     * @param requestCode, code to match it allow the the gallery to open
     * @param data, data which is the intent (in this case the load Image intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = containerActivity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();


            Bitmap galleryBitmap = BitmapFactory.decodeFile(filePath);
            ((MainActivity)getActivity()).addImage(galleryBitmap);
            File file =null;
            try {
                file = ((MainActivity)getActivity()).createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ((MainActivity)getActivity()).sendImageIntent(file);
        }

    }
    /**
     * PURPOSE: This method allows the user to start over and create a new drawing
     */
    public void clearDrawing(View v){
        dv.startNew();
    }

    /**
     * PURPOSE: This method uses the string that was made from clicking one of the color
     * buttons and then uses that to set paintColor to the respective color that was clicked on
     */
    public void setColor(){
        AmbilWarnaDialog colorPickerDialog = new AmbilWarnaDialog(containerActivity, paintColor,
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                paintColor = color;
                dv.changeColor(paintColor);
            }
        });
        colorPickerDialog.show();
    }

    public class DrawingView extends View {

        //drawing path
        private Path drawPath;
        //drawing and canvas paint
        private Paint drawPaint;
        //initial color
        private int paintColor = Color.RED;
        //initial size
        private float strokeWidth = 1;
        //canvas
        private Canvas drawCanvas;
        //canvas bitmap
        private Bitmap canvasBitmap;

        /**
         * PURPOSE: This initializes the DrawingView object by calling the setupDrawing
         * method which sets all the values
         */
        public DrawingView(Context context, AttributeSet attrs){
            super(context, attrs);
            setupDrawing();
            this.setBackgroundColor(0xFFFFFFFF);
        }

        /**
         * PURPOSE: This method changes the color of the paint by setting it to the
         * color int that was passed in from the fragment, it overrides the drawPaint and drawPath
         * and recreates them to reset up the values
         * @param color, the new color of the paint
         */
        public void changeColor(int color){
            this.paintColor = color;
            drawPaint = new Paint();
            drawPath = new Path();
            drawPaint.setColor(paintColor);
            drawPaint.setStyle(Paint.Style.STROKE);
            drawPaint.setStrokeWidth(strokeWidth);
            drawPaint.setAntiAlias(true);

        }

        /**
         * PURPOSE: This method changes the size of the strokeWidth by setting it to the
         * size float that was passed in from the fragment, it overrides the drawPaint and drawPath
         * and recreates them to reset up the values
         * @param size, the new size of the strokeWidth
         */
        public void changeSize(float size){
            this.strokeWidth = size;
            drawPaint = new Paint();
            drawPath = new Path();
            drawPaint.setColor(paintColor);
            drawPaint.setStyle(Paint.Style.STROKE);
            drawPaint.setStrokeWidth(strokeWidth);
            drawPaint.setAntiAlias(true);
        }

        /**
         * PURPOSE: This method creates all the values and sets them to their default values
         * when the DrawingView object is first created
         */
        private void setupDrawing(){
            //prepare for drawing and setup paint stroke properties
            drawPath = new Path();
            drawPaint = new Paint();
            drawPaint.setColor(this.paintColor);
            drawPaint.setStyle(Paint.Style.STROKE);
            drawPaint.setStrokeWidth(strokeWidth);
            drawPaint.setAntiAlias(true);
        }

        //size assigned to view
        /**
         * PURPOSE: This method sets new canvas based on the size assigned to the view
         */
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            canvasBitmap = Bitmap.createBitmap(w+100, h+100, Bitmap.Config.ARGB_8888);
            drawCanvas = new Canvas(canvasBitmap);
        }

        /**
         * PURPOSE: This method will draw the view - will be called after touch event
         */
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(canvasBitmap, 0, 0, drawPaint);
            canvas.drawPath(drawPath, drawPaint);
        }

        /**
         * PURPOSE: This method register user touches as drawing action
         */
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            //respond to down, move and up events
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drawPath.moveTo(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawPath.lineTo(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    drawPath.lineTo(x, y);
                    drawCanvas.drawPath(drawPath, drawPaint);
                    drawPath.reset();
                    break;
                default:
                    return false;
            }
            //redraw
            invalidate();
            return true;

        }

        /**
         * PURPOSE: This method clears the canvas to start a new drawing
         */
        public void startNew(){
            drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            invalidate();
        }
    }


}