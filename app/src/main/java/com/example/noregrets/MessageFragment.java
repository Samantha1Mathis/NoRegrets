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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class MessageFragment extends Fragment {
    private static final int RESULT_OK = -1;
    public Activity containerActivity = null;
    public String name = "";
    public String phone = "";
    private DrawingView dv = null;
    private int paintColor = Color.RED;
    String picturePath ="";
    View v = null;
    Button send;
    Button draw;
    Button photo;
    EditText txtMessage;
    Button colorPicker;
    int seekBarValue =0;
    SeekBar seekBar;
    public MessageFragment(String name, String phone) {
        this.name = name;
        this.phone = phone;

    }

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    private static final int RESULT_LOAD_IMAGE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.v =  inflater.inflate(R.layout.fragment_message, container, false);
        String displayText = getArguments().getString("display_text");
        TextView tv2 = v.findViewById(R.id.messages);
        tv2.setText(displayText);
        send = (Button) v.findViewById(R.id.send);
        draw = (Button) v.findViewById(R.id.draw);
        photo = (Button) v.findViewById(R.id.photo);
        colorPicker = (Button) v.findViewById(R.id.color);
        txtMessage = (EditText) v.findViewById(R.id.current);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = txtMessage.getText().toString();
                ((MainActivity)getActivity()).sendSMSMessage(message);

            }
        });
        draw.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    screenShot();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        photo.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
         });
        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor();
            }
        });
        seekBar=(SeekBar)v.findViewById(R.id.stroke); // initiate the Seek bar

        seekBar.getProgress(); // get progress value from the Seek bar
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
        //TextView tv = (TextView)v.findViewById(R.id.name);
        //tv.setText(this.name + " : " + this.phone);

        dv = new DrawingView(containerActivity, null);
        dv.setupDrawing();

        LinearLayout ll = v.findViewById(R.id.drawing);
        ll.addView(dv);
        return v;
    }
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
        //((MainActivity)getActivity()).shareImage(file);
        ((MainActivity)getActivity()).sendImageIntent(file);
        ImageView iv2 = this.v.findViewById(R.id.image);
        BitmapFactory.Options bmOptions2 = new BitmapFactory.Options();
        Bitmap bitmap2 = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions2);
        iv2.setImageBitmap(bitmap2);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = containerActivity.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            this.picturePath = cursor.getString(columnIndex);
            ImageView iv = this.v.findViewById(R.id.image);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(this.picturePath, bmOptions);

            iv.setImageBitmap(bitmap);
            File file = new File(this.picturePath);
            ((MainActivity)getActivity()).sendImageIntent(file);
            cursor.close();
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
     *
     *
     */
    public void setColor(){
        AmbilWarnaDialog colorPickerDialog = new AmbilWarnaDialog(containerActivity, paintColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                System.out.println("here");
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
        private float strokeWidth = 15.0f;
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