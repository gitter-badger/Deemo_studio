package com.sevenre.trackre.driver.views.lib;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.internal.text.AllCapsTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.sevenre.trackre.driver.R;

public class DialogActionButton extends TextView {

    private boolean mStacked = false;
    private GravityEnum mStackedGravity;

    private int mStackedEndPadding;
    private Drawable mStackedBackground;
    private Drawable mDefaultBackground;

    public DialogActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public DialogActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DialogActionButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mStackedEndPadding = context.getResources()
                .getDimensionPixelSize(R.dimen.dialog_frame_margin);
        mStackedGravity = GravityEnum.END;
    }

    /**
     * Set if the button should be displayed in stacked mode.
     * This should only be called from MDRootLayout's onMeasure, and we must be measured
     * after calling this.
     */
    /* package */ void setStacked(boolean stacked, boolean force) {
        if (mStacked != stacked || force) {

            setGravity(stacked ? (Gravity.CENTER_VERTICAL | mStackedGravity.getGravityInt()) : Gravity.CENTER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                //noinspection ResourceType
                setTextAlignment(stacked ? mStackedGravity.getTextAlignment() : TEXT_ALIGNMENT_CENTER);
            }

            setBackgroundCompat(this, stacked ? mStackedBackground : mDefaultBackground);

            if (stacked) {
                setPadding(mStackedEndPadding, getPaddingTop(), mStackedEndPadding, getPaddingBottom());
            } /* Else the padding was properly reset by the drawable */

            mStacked = stacked;
        }
    }

    public void setStackedGravity(GravityEnum gravity) {
        mStackedGravity = gravity;
    }

    public void setStackedSelector(Drawable d) {
        mStackedBackground = d;
        if (mStacked)
            setStacked(true, true);
    }

    public void setDefaultSelector(Drawable d) {
        mDefaultBackground = d;
        if (!mStacked)
            setStacked(false, true);
    }

    public void setAllCapsCompat(boolean allCaps) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setAllCaps(allCaps);
        } else {
            if (allCaps)
                setTransformationMethod(new AllCapsTransformationMethod(getContext()));
            else
                setTransformationMethod(null);
        }
    }

    void setBackgroundCompat(View view, Drawable d) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            //noinspection deprecation
            view.setBackgroundDrawable(d);
        } else {
            view.setBackground(d);
        }
    }
}
