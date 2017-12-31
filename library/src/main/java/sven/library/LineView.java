package sven.library;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sven on 17/1/5.
 */
public class LineView extends View {

    private float p1XLength = 400, p1YLength = 20, speedP1 = 0.15f;
    private float p2XLength = 20, p2YLength = 400, speedP2 = 0.05f;
    private double angleP1 = 0, angleP2 = 0;
    private int viewWidth, viewHeight;
    private Paint paint;
    private ValueAnimator valueAnimator;
    private int animDuration = 12 * 1000;

    private DrawingListener drawingListener;

    private List<DrawModel> pointList = new ArrayList<DrawModel>();

    public LineView(Context context) {
        super(context);
        init();
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setBackgroundColor(Color.BLACK);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);

        valueAnimator = ValueAnimator.ofFloat(0, 1f);
        valueAnimator.addUpdateListener(updateListener);
        valueAnimator.addListener(listenerAdapter);
        valueAnimator.setDuration(animDuration);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paint == null) {
            return;
        }
        for (DrawModel point : pointList) {
            canvas.drawLine(point.p1X, point.p1Y, point.p2X, point.p2Y, paint);
        }
    }

    private ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            calculate();
            invalidate();
        }
    };

    private void calculate() {
        angleP1 = angleP1 + speedP1;
        angleP2 = angleP2 + speedP2;

        float nowp1X = (float) (p1XLength * Math.cos(angleP1) + viewWidth / 2f);
        float nowP1Y = (float) (p1YLength * Math.sin(angleP1) + viewHeight / 2f);
        float nowP2X = (float) (p2XLength * Math.cos(angleP2) + viewWidth / 2f);
        float nowP2Y = (float) (p2YLength * Math.sin(angleP2) + viewHeight / 2f);

        DrawModel drawModel = new DrawModel(nowp1X, nowP1Y, nowP2X, nowP2Y);
        pointList.add(drawModel);
    }

    private AnimatorListenerAdapter listenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            if (drawingListener != null) {
                drawingListener.drawOver();
            }
            super.onAnimationEnd(animation);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            if (drawingListener != null) {
                drawingListener.drawStart();
            }
            pointList.clear();
            super.onAnimationStart(animation);
        }
    };

    private class DrawModel {
        float p1X, p1Y, p2X, p2Y;

        public DrawModel(float p1X, float p1Y, float p2X, float p2Y) {
            this.p1X = p1X;
            this.p1Y = p1Y;
            this.p2X = p2X;
            this.p2Y = p2Y;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            viewHeight = getHeight();
            viewWidth = getWidth();
        }
    }

    public void startAnimation() {
        pointList.clear();
        valueAnimator.start();
    }

    public void setDrawingListener(DrawingListener listener) {
        drawingListener = listener;
    }

    public interface DrawingListener {
        void drawStart();

        void drawOver();
    }

    /**
     * 设置参数
     */
    public void setParam(float p1XLength, float p1YLength, float p2XLength, float p2YLength, float speedP1, float speedP2) {
        this.p1XLength = p1XLength;
        this.p1YLength = p1YLength;
        this.p2XLength = p2XLength;
        this.p2YLength = p2YLength;
        this.speedP1 = speedP1;
        this.speedP2 = speedP2;
    }

}
