package com.example.tsquared;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import java.lang.reflect.Field;


public class ExpandableTextView extends androidx.appcompat.widget.AppCompatTextView {
    private ExpandableTextView textView;
    private String TAG = "TAG";
    private Boolean shouldTextExpand;

    // copy off TextView.LINES
    private static final int MAXMODE_LINES = 1;

    private OnExpandListener onExpandListener;
    private TimeInterpolator expandInterpolator;
    private TimeInterpolator collapseInterpolator;

    private final int maxLines;
    private long animationDuration;
    private boolean animating;
    private boolean expanded;
    private int originalHeight;

    public ExpandableTextView(final Context context)
    {
        this(context, null);
    }

    public ExpandableTextView(final Context context, final AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ExpandableTextView(final Context context, final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);

        // read attributes
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView, defStyle, 0);
        this.animationDuration = attributes.getInt(R.styleable.ExpandableTextView_animation_duration, 750);
        attributes.recycle();

        // keep the original value of maxLines
        this.maxLines = this.getMaxLines();

        // create default interpolator
        this.expandInterpolator = new AccelerateDecelerateInterpolator();
        this.collapseInterpolator = new AccelerateDecelerateInterpolator();
    }

    @Override
    public int getMaxLines()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            return super.getMaxLines();
        }

        try
        {
            final Field mMaxMode = TextView.class.getField("mMaxMode");
            mMaxMode.setAccessible(true);
            final Field mMaximum = TextView.class.getField("mMaximum");
            mMaximum.setAccessible(true);

            final int mMaxModeValue = (int) mMaxMode.get(this);
            final int mMaximumValue = (int) mMaximum.get(this);

            return mMaxModeValue == MAXMODE_LINES ? mMaximumValue : -1;
        }
        catch (final Exception e)
        {
            return -1;
        }
    }

    /**
     * Toggle the expanded state of this {@link ExpandableTextView}.
     * @return true if toggled, false otherwise.
     */
    public boolean toggle()
    {
        if (this.expanded)
        {
            return this.collapse();
        }

        return this.expand();
    }

    /**
     * Expand this {@link ExpandableTextView}.
     * @return true if expanded, false otherwise.
     */
    public boolean expand()
    {
        if (!this.expanded && !this.animating && this.maxLines >= 0)
        {
            this.animating = true;

            // notify listener
            if (this.onExpandListener != null)
            {
                this.onExpandListener.onExpand(this);
            }

            // get original height
            this.measure
                    (
                            MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                    );

            this.originalHeight = this.getMeasuredHeight();

            // set maxLines to MAX Integer
            this.setMaxLines(Integer.MAX_VALUE);

            // get new height
            this.measure
                    (
                            MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                    );

            final int fullHeight = this.getMeasuredHeight();

            final ValueAnimator valueAnimator = ValueAnimator.ofInt(this.originalHeight, fullHeight);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(final ValueAnimator animation)
                {
                    final ViewGroup.LayoutParams layoutParams = ExpandableTextView.this.getLayoutParams();
                    layoutParams.height = (int) animation.getAnimatedValue();
                    ExpandableTextView.this.setLayoutParams(layoutParams);
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(final Animator animation)
                {
                    ExpandableTextView.this.expanded = true;
                    ExpandableTextView.this.animating = false;
                }
            });

            // set interpolator
            valueAnimator.setInterpolator(this.expandInterpolator);

            // start the animation
            valueAnimator
                    .setDuration(this.animationDuration)
                    .start();

            return true;
        }

        return false;
    }

    /**
     * Collapse this {@link TextView}.
     * @return true if collapsed, false otherwise.
     */
    public boolean collapse()
    {
        if (this.expanded && !this.animating && this.maxLines >= 0)
        {
            this.animating = true;

            // notify listener
            if (this.onExpandListener != null)
            {
                this.onExpandListener.onCollapse(this);
            }

            // get new height
            final int fullHeight = this.getMeasuredHeight();

            final ValueAnimator valueAnimator = ValueAnimator.ofInt(fullHeight, this.originalHeight);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(final ValueAnimator animation)
                {
                    final ViewGroup.LayoutParams layoutParams = ExpandableTextView.this.getLayoutParams();
                    layoutParams.height = (int) animation.getAnimatedValue();
                    ExpandableTextView.this.setLayoutParams(layoutParams);
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(final Animator animation)
                {
                    // set maxLines to original value
                    ExpandableTextView.this.setMaxLines(ExpandableTextView.this.maxLines);

                    ExpandableTextView.this.expanded = false;
                    ExpandableTextView.this.animating = false;
                }
            });

            // set interpolator
            valueAnimator.setInterpolator(this.collapseInterpolator);

            // start the animation
            valueAnimator
                    .setDuration(this.animationDuration)
                    .start();

            return true;
        }

        return false;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (onExpandListener != null) {
            onExpandListener.onLayouted(this);
        }
    }

    /**
     * Sets the duration of the expand / collapse animation.
     * @param animationDuration duration in milliseconds.
     */
    public void setAnimationDuration(final long animationDuration)
    {
        this.animationDuration = animationDuration;
    }

    /**
     * Sets a listener which receives updates about this {@link ExpandableTextView}.
     * @param onExpandListener the listener.
     */
    public void setOnExpandListener(final OnExpandListener onExpandListener)
    {
        this.onExpandListener = onExpandListener;
    }

    /**
     * Returns the {@link OnExpandListener}.
     * @return the listener.
     */
    public OnExpandListener getOnExpandListener()
    {
        return onExpandListener;
    }

    /**
     * Sets a {@link TimeInterpolator} for expanding and collapsing.
     * @param interpolator the interpolator
     */
    public void setInterpolator(final TimeInterpolator interpolator)
    {
        this.expandInterpolator = interpolator;
        this.collapseInterpolator = interpolator;
    }

    /**
     * Sets a {@link TimeInterpolator} for expanding.
     * @param expandInterpolator the interpolator
     */
    public void setExpandInterpolator(final TimeInterpolator expandInterpolator)
    {
        this.expandInterpolator = expandInterpolator;
    }

    /**
     * Returns the current {@link TimeInterpolator} for expanding.
     * @return the current interpolator, null by default.
     */
    public TimeInterpolator getExpandInterpolator()
    {
        return this.expandInterpolator;
    }

    /**
     * Sets a {@link TimeInterpolator} for collpasing.
     * @param collapseInterpolator the interpolator
     */
    public void setCollapseInterpolator(final TimeInterpolator collapseInterpolator)
    {
        this.collapseInterpolator = collapseInterpolator;
    }

    /**
     * Returns the current {@link TimeInterpolator} for collapsing.
     * @return the current interpolator, null by default.
     */
    public TimeInterpolator getCollapseInterpolator()
    {
        return this.collapseInterpolator;
    }

    /**
     * Is this {@link ExpandableTextView} expanded or not?
     * @return true if expanded, false if collapsed.
     */
    public boolean isExpanded()
    {
        return this.expanded;
    }

    public interface OnExpandListener
    {
        void onExpand(ExpandableTextView view);
        void onCollapse(ExpandableTextView view);
        void onLayouted(ExpandableTextView view);
    }
}