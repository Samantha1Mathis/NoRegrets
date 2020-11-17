package com.example.noregrets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class MessageFragment extends Fragment {
    public Activity containerActivity = null;
    public String name = "";
    public String phone = "";
    private DrawingView dv = null;
    private int paintColor = Color.RED;
    private float strokeSize = 15.0f;

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

        dv = new DrawingView(containerActivity, null);
        dv.setupDrawing();

        LinearLayout ll = v.findViewById(R.id.drawing);
        ll.addView(dv);
        return v;
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
     * @param color, Color is what color the user wants to change the paintColor to
     */
    public void setColor(String color){
        if (color.equals("RED")){
            this.paintColor = Color.RED;
        }else if (color.equals("BLUE")){
            this.paintColor = Color.BLUE;
        }else if (color.equals("GREEN")){
            this.paintColor = Color.GREEN;
        }else if (color.equals("YELLOW")){
            this.paintColor = Color.YELLOW;
        }else if (color.equals("MAG")){
            this.paintColor = Color.MAGENTA;
        }

        dv.changeColor(paintColor);
    }

    /**
     * PURPOSE: This method uses the string that was made from clicking one of the size
     * buttons (small, medium or large) and then uses that to set strokeWidth to the respective
     * size that was clicked on
     *
     * @param size, size is what size the stroke width should be
     */
    public void setSize(String size){
        if (size.equals("SMALL")){
            this.strokeSize = 5.0f;
        }else if (size.equals("MEDIUM")){
            this.strokeSize = 15.0f;
        }else if (size.equals("LARGE")) {
            this.strokeSize = 25.0f;
        }
        dv.changeSize(strokeSize);
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