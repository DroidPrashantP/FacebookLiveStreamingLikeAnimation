package app.prashant.com.facebooklivestreamlikeanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    DrawPath drawPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawPath = new DrawPath(this);
        drawPath.setBackgroundColor(Color.DKGRAY);
        setContentView(drawPath);
        createPlayBtn();

    }

    /**
     * created play button and set listener to play animation
     */
    private void createPlayBtn() {
        Button playBtn = new Button(this);
        playBtn.setText("Play Animation");
        playBtn.setTextColor(Color.WHITE);
        playBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        playBtn.setPadding(10, 10, 10, 10);
        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rl.addRule(RelativeLayout.ALIGN_BOTTOM);
        playBtn.setLayoutParams(rl);
        drawPath.addView(playBtn);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMultpileView();
            }
        });
    }

    /**
     * add multiple views
     */
    private void addMultpileView() {
        for (int i = 0; i < 10; i++) {
            createImageView(drawPath, i);
        }
    }

    /**
     * create ImageView and set Value animator to play animation on each image
     * @param drawPath
     * @param i
     */
    private void createImageView(final DrawPath drawPath, int i) {
        final ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getRandomNumer(), getRandomNumer());
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(i > 5 ? R.drawable.heart : R.drawable.thumbs_up);
        drawPath.addView(imageView);

        ValueAnimator pathAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        pathAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            float[] point = new float[2];

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                long duration = animation.getDuration();
                float val = animation.getAnimatedFraction();
                PathMeasure pathMeasure = new PathMeasure(drawPath.getPath(), true);
                pathMeasure.getPosTan(pathMeasure.getLength() * val, point, null);
                imageView.setX(point[0]);
                imageView.setY(point[1]);

                if (val > 0.6) {  // > 0.6 - to stop animation back to reverse state
                    if (drawPath != null) {
                        drawPath.removeView(imageView);
                    }
                }
            }
        });
        pathAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (drawPath != null) {
                    drawPath.removeView(imageView);
                }
            }
        });
        pathAnimator.setDuration(2000 + getRandomDuration());
        pathAnimator.start();
    }

    public class DrawPath extends RelativeLayout {

        public Path mPath;
        public Paint mPaint;

        public DrawPath(Context context) {
            super(context);
            intialisePaint();
        }

        private void intialisePaint() {
            mPaint = new Paint();
            mPaint.setColor(Color.TRANSPARENT);
        }

        /**
         * return Path
         *
         * @return
         */
        private Path getPath() {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            float width = metrics.widthPixels;
            float height = metrics.heightPixels;

            float cp25 = width * 25 / 100;  // 25 percent of width
            float cp50 = width * 50 / 100;  // 50 percent of width
            float cp75 = width * 75 / 100;  // 75 percent of width

            mPath = new Path();
            mPath.moveTo(0.0f, cp50);
            float X3 = width;
            float Y3 = cp50;

            float randomYShift = cp50 + getRandomYValue() * cp75;
            float X1 = cp25;
            float Y1 = cp25 - randomYShift;
            float X2 = cp50;
            float Y2 = cp75 + randomYShift;

            mPath.cubicTo(X1, Y1, X2, Y2, X3, Y3);
            return mPath;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawPath(getPath(), mPaint);
        }
    }

    /**
     * get Random Number for image width and height
     * @return
     */
    public int getRandomNumer() {
        Random random = new Random();
        int randomNumber = random.nextInt(90 - 60) + 60;
        return randomNumber;
    }

    /**
     * get random duration between 2000 to 100
     * @return
     */
    public int getRandomDuration() {
        Random random = new Random();
        int randomNumber = random.nextInt(2000 - 100) + 100;
        return randomNumber;
    }

    /**
     * get random value between 1.0 to 0.1
     * @return
     */
    public float getRandomYValue() {
        Random random = new Random();
        float randomNumber = random.nextFloat() * (1.0f - 0.1f) + 0.1f;
        Log.d(TAG, "getRandomYValue: " + randomNumber);
        return randomNumber;
    }
}
