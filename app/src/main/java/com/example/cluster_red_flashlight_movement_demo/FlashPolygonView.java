package com.example.cluster_red_flashlight_movement_demo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class FlashPolygonView extends View {
    private Paint paint;
    private Path hexagonPath;
    private int width, height;
    private float rotationAngle = 0f;

    public FlashPolygonView(Context context) {
        super(context);
        init();
    }

    public FlashPolygonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hexagonPath = new Path();

        // Animator to rotate the beam
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 360f);
        animator.setDuration(4000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(null);
        animator.addUpdateListener(animation -> {
            rotationAngle = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        buildHexagonPath();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void buildHexagonPath() {
        hexagonPath.reset();

        float centerX = width / 2f;
        float centerY = height / 2f;
        float radius = Math.min(centerX, centerY) * 0.9f;

        for (int i = 0; i < 6; i++) {
            double angleRad = Math.toRadians(60 * i - 30);
            float x = centerX + (float) (radius * Math.cos(angleRad));
            float y = centerY + (float) (radius * Math.sin(angleRad));
            if (i == 0) {
                hexagonPath.moveTo(x, y);
            } else {
                hexagonPath.lineTo(x, y);
            }
        }
        hexagonPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.clipPath(hexagonPath); // Draw inside hexagon only

        float centerX = width / 2f;
        float centerY = height / 2f;

        // Create sweep gradient with red beam and trailing effect
        SweepGradient sweepGradient = new SweepGradient(
                centerX, centerY,
                new int[]{
                        0x00FF0000, // Transparent
                        0x66FF0000, // Light Red
                        0xFFFF0000, // Bright Red (beam)
                        0x66FF0000, // Light Red
                        0x00FF0000  // Transparent
                },
                new float[]{
                        0f,
                        0.15f,
                        0.2f,
                        0.25f,
                        1f
                }
        );

        // Rotate gradient
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, centerX, centerY);
        sweepGradient.setLocalMatrix(matrix);

        paint.setShader(sweepGradient);

        // Draw inside circle (will be clipped to hexagon)
        canvas.drawCircle(centerX, centerY, Math.min(centerX, centerY), paint);

        canvas.restore();
    }
}

