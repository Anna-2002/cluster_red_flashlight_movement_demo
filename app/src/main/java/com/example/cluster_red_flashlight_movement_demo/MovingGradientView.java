package com.example.cluster_red_flashlight_movement_demo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MovingGradientView extends View {
    private Paint paint;
    private float animatedValue = 0f;
    private int width, height;

    public MovingGradientView(Context context) {
        super(context);
        init();
    }

    public MovingGradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(3000);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(animation -> {
            animatedValue = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float startX = width * animatedValue;
        float endX = startX + width / 3f;

        LinearGradient gradient = new LinearGradient(
                startX, height,
                endX, height,
                new int[]{0x00FF0000, 0xFFFF0000, 0x00FF0000},
                new float[]{0f, 0.5f, 1f},
                Shader.TileMode.CLAMP
        );

        paint.setShader(gradient);
        canvas.drawRect(0, 0, width, height, paint);
    }
}