package rimp.rild.com.android.android_blocks;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class HeightAnimation extends Animation {

    int targetHeight;
    int startHeight;

    View view;

    // The gesture threshold expressed in dip
    private static final float GESTURE_THRESHOLD_DIP = 16.0f;

    private int concertDipsToPixels(int dips, Context context) {
        // Convert the dips to pixels
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dips * scale);
    }

    public HeightAnimation(View view, int startHeight, int targetHeight) {
        this.view = view;
        this.targetHeight = concertDipsToPixels(targetHeight, view.getContext());
        this.startHeight = concertDipsToPixels(startHeight, view.getContext());
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight = (int) (startHeight + (targetHeight - startHeight) * interpolatedTime);
        view.getLayoutParams().height = newHeight;
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, ((View) view.getParent()).getWidth(), parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}