package com.example.karyc.testapp;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.example.karyc.testapp.databinding.ViewClockBinding;

public class ClockView extends ConstraintLayout {
    final String MINUTE = "minute";
    final String HOUR = "hour";
    ViewClockBinding binding;
    DragEventListener dragListen = new DragEventListener();
    ClockViewListener listener;

    public ClockView(Context context) {
        super(context);
        init();
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(ClockViewListener listener) {
        this.listener = listener;
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = ViewClockBinding.inflate(inflater, this, true);

        binding.handMinuteView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        ClipData.Item item = new ClipData.Item(MINUTE);

                        ClipData dragData = new ClipData(
                                MINUTE,
                                new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                                item);
                        View.DragShadowBuilder myShadow = new MyDragShadowBuilder(binding.handMinuteView);
                        v.startDrag(dragData,
                                myShadow,
                                null,
                                0
                        );
                        return true;
                }

                return false;
            }
        });

        binding.handHourView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        ClipData.Item item = new ClipData.Item(HOUR);

                        ClipData dragData = new ClipData(
                                HOUR,
                                new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                                item);
                        DragShadowBuilder myShadow = new MyDragShadowBuilder(binding.handHourView);
                        v.startDrag(dragData,
                                myShadow,
                                null,
                                0
                        );
                        return true;
                }

                return false;
            }
        });

        this.setOnDragListener(dragListen);
    }

    private static class MyDragShadowBuilder extends View.DragShadowBuilder {
        private static Drawable shadow;

        MyDragShadowBuilder(View v) {
            super(v);
            shadow = new ColorDrawable(Color.TRANSPARENT);
        }

        @Override
        public void onProvideShadowMetrics(Point size, Point touch) {
            int width, height;
            width = 0;
            height = 0;
            shadow.setBounds(0, 0, width, height);
            size.set(width, height);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            shadow.draw(canvas);
        }
    }

    protected class DragEventListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:
                    if (event.getClipDescription().getLabel().toString().equals(HOUR)) {
                        float rotationDeg = getRotationDegree(v, event);
                        binding.handHourView.setRotation(rotationDeg + 90);
                        float angleDegree = (rotationDeg + 90) / 30.f;
                        int angleHour = Math.round(angleDegree);
                        if (angleHour == 12) {
                            angleHour = 0;
                        }
                        if (listener != null) {
                            listener.onHourChanged(angleHour);
                        }
                        return true;
                    } else {
                        float rotationDegM = getRotationDegree(v, event);
                        binding.handMinuteView.setRotation(rotationDegM + 90);
                        float angleDegreeM = (rotationDegM + 90) / 6.f;
                        int angleMinute = Math.round(angleDegreeM);
                        if (angleMinute == 60) {
                            angleMinute = 0;
                        }
                        if (listener != null) {
                            listener.onMinuteChanged(angleMinute);
                        }
                    }

                case DragEvent.ACTION_DRAG_EXITED:
                    return true;

                case DragEvent.ACTION_DROP:
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    return true;

                default:
                    Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                    break;
            }

            return false;
        }
    }

    private float getRotationDegree(View v, DragEvent event) {
        int width = v.getWidth() / 2;
        int height = v.getHeight() / 2;
        int coordX = (int) event.getX();
        int coordY = (int) event.getY();
        float cos = (float) (coordX - width);
        float expression = (float) (coordY - height) / (float) (coordX - width);
        float rotationDeg;
        if (cos < 0) {
            rotationDeg = (float) Math.atan(expression) * 180.f / (float) Math.PI + 180;
        } else {
            rotationDeg = (float) Math.atan(expression) * 180.f / (float) Math.PI;
        }
        return rotationDeg ;
    }
}
