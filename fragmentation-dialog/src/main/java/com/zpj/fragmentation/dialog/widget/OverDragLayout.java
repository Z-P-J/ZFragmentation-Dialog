package com.zpj.fragmentation.dialog.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import com.zpj.fragmentation.dialog.animator.ShadowMaskAnimator;
import com.zpj.fragmentation.dialog.enums.LayoutStatus;
import com.zpj.utils.ViewUtils;

public class OverDragLayout extends FrameLayout implements NestedScrollingParent {

    private static final String TAG = "SmartDragLayout";

    private final ShadowMaskAnimator bgAnimator = new ShadowMaskAnimator(null);

    private final int mTouchSlop;

    private View child;
    private View contentView;
    OverScroller scroller;
    VelocityTracker tracker;

    boolean enableDrag = true;//是否启用手势
    boolean dismissOnTouchOutside = true;
    private boolean handleTouchOutsideEvent = true;
    boolean hasShadowBg = true;
    boolean isUserClose = false;
    LayoutStatus status = LayoutStatus.Close;

    protected long showDuration = 360;
    protected long dismissDuration = 360;

    private int mMaxOverScrollOffset;
    private boolean mIsNestedScrollUp;

    public void setShowDuration(long showDuration) {
        this.showDuration = showDuration;
        bgAnimator.setShowDuration(showDuration);
    }

    public void setDismissDuration(long dismissDuration) {
        this.dismissDuration = dismissDuration;
        bgAnimator.setDismissDuration(dismissDuration);
    }

    public OverDragLayout(Context context) {
        this(context, null);
    }

    public OverDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        if (enableDrag) {
            scroller = new OverScroller(context, new OvershootInterpolator());
        }
    }

    @Override
    public void onViewAdded(View c) {
        super.onViewAdded(c);
        child = c;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (contentView == null) {
            return;
        }
        mMaxOverScrollOffset = child.getMeasuredHeight() - contentView.getMeasuredHeight();
        int l = getMeasuredWidth() / 2 - child.getMeasuredWidth() / 2;
        if (enableDrag) {
            // horizontal center
            child.layout(l, getMeasuredHeight(), l + child.getMeasuredWidth(), getMeasuredHeight() + child.getMeasuredHeight());
            if (status == LayoutStatus.Open) {
                //通过scroll上移
                scrollTo(getScrollX(), contentView.getMeasuredHeight());
            }
        } else {
            // like bottom gravity
            child.layout(l, getMeasuredHeight() - contentView.getMeasuredHeight(), l + contentView.getMeasuredWidth(), getMeasuredHeight());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        isUserClose = true;
        return super.dispatchTouchEvent(ev);
    }

    float touchX, touchY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!handleTouchOutsideEvent) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) {
                    scroller.forceFinished(true);
                }
                if (enableDrag)
                    tracker = VelocityTracker.obtain();
                touchX = event.getX();
                touchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (enableDrag) {
                    tracker.addMovement(event);
                    tracker.computeCurrentVelocity(1000);
                    int dy = (int) (event.getY() - touchY);

                    if (dy < 0) {
                        if (dy < -mMaxOverScrollOffset * 5) {
                            dy = -mMaxOverScrollOffset;
                        } else {
                            dy /= 5;
                        }
                    } else {
                        if (getScrollY() > contentView.getMeasuredHeight()) {
                            dy -= (getScrollY() - contentView.getMeasuredHeight());
                        }
                    }

                    scrollTo(getScrollX(), contentView.getMeasuredHeight() - dy);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // click in child rect
                if (contentView == null) {
                    contentView = child;
                }
                Rect rect = new Rect();
                contentView.getGlobalVisibleRect(rect);
                if (!ViewUtils.isInRect(event.getRawX(), event.getRawY(), rect) && dismissOnTouchOutside) {
                    float distance = (float) Math.sqrt(Math.pow(event.getX() - touchX, 2) + Math.pow(event.getY() - touchY, 2));
                    if (distance < mTouchSlop) {
                        performClick();
                        break;
                    }
                }
                if (enableDrag) {
                    float yVelocity = tracker.getYVelocity();
                    if (getScrollY() <= getMeasuredHeight() && yVelocity > 1500) {
                        close();
                    } else {
                        finishScroll();
                    }

                    tracker.clear();
                    tracker.recycle();
                }

                break;
        }
        return true;
    }

    private void finishScroll() {
        if (enableDrag) {
            int contentHeight = contentView.getMeasuredHeight();
            int dy;
            int duration;
            if (getScrollY() > contentHeight) {
                dy = contentHeight - getScrollY();
                duration = 250;
            } else {
                int threshold = isScrollUp ? contentHeight / 3 : contentHeight * 2 / 3;
                dy = (getScrollY() > threshold ? contentHeight : 0) - getScrollY();
                duration = (int) (dy > 0 ? showDuration : dismissDuration);
            }
            scroller.startScroll(getScrollX(), getScrollY(), 0, dy, duration);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    boolean isScrollUp;

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) y = 0;
        float fraction = y * 1f / contentView.getMeasuredHeight();
        isScrollUp = y > getScrollY();
        if (hasShadowBg) {
            setBackgroundColor(bgAnimator.calculateBgColor(fraction));
        }
        if (y <= contentView.getMeasuredHeight() && listener != null) {
            if (isUserClose && fraction == 0f && status != LayoutStatus.Close) {
                status = LayoutStatus.Close;
                listener.onClose();
            } else if (fraction == 1f && status != LayoutStatus.Open) {
                status = LayoutStatus.Open;
                listener.onOpen();
            }
        }
        super.scrollTo(x, y);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isScrollUp = false;
        isUserClose = false;
        setTranslationY(0);
    }

    public void open() {
        status = LayoutStatus.Opening;
        post(new Runnable() {
            @Override
            public void run() {
                ValueAnimator animator = ValueAnimator.ofInt(0, contentView.getMeasuredHeight() + mMaxOverScrollOffset / 2);
                animator.setDuration(showDuration);
                animator.setInterpolator(new FastOutSlowInInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int scrollY = (int) animation.getAnimatedValue();
                        scrollTo(getScrollX(), scrollY);
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finishScroll();
                    }
                });
                animator.start();
            }
        });
    }

    public void close() {
        isUserClose = true;
        status = LayoutStatus.Closing;
        post(new Runnable() {
            @Override
            public void run() {
                ValueAnimator animator = ValueAnimator.ofInt(getScrollY(), 0);
                animator.setDuration(dismissDuration);
                animator.setInterpolator(new FastOutSlowInInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int scrollY = (int) animation.getAnimatedValue();
                        scrollTo(getScrollX(), scrollY);
                    }
                });
                animator.start();
            }
        });
    }

    public void smoothScroll(final int dy, final long duration) {
        post(new Runnable() {
            @Override
            public void run() {
                scroller.startScroll(getScrollX(), getScrollY(), 0, dy, (int) duration);
                ViewCompat.postInvalidateOnAnimation(OverDragLayout.this);
            }
        });
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL && enableDrag;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        //必须要取消，否则会导致滑动初次延迟
        scroller.abortAnimation();
    }

    @Override
    public void onStopNestedScroll(View target) {
        finishScroll();
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyUnconsumed != 0) {
            if (mIsNestedScrollUp) {
                dyUnconsumed /= 5;
            }
            int newY = getScrollY() + dyUnconsumed;
            scrollTo(getScrollX(), Math.min(newY, child.getMeasuredHeight()));
        }
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        mIsNestedScrollUp = dy > 0;
//        if (dy > 0) {
//            //scroll up
//            int newY = getScrollY() + dy;
//            if (newY < maxY) {
//                consumed[1] = dy; // dy不一定能消费完
//            }
//            scrollTo(getScrollX(), newY);
//        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        boolean isDragging = getScrollY() > 0 && getScrollY() < contentView.getMeasuredHeight();
        if (isDragging && velocityY < -1500) {
            close();
        }
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    public void bindContentView(View contentView) {
        this.contentView = contentView;
    }

    public void enableDrag(boolean enableDrag) {
        this.enableDrag = enableDrag;
    }

    public void dismissOnTouchOutside(boolean dismissOnTouchOutside) {
        this.dismissOnTouchOutside = dismissOnTouchOutside;
    }

    public void handleTouchOutsideEvent(boolean handleTouchOutsideEvent) {
        this.handleTouchOutsideEvent = handleTouchOutsideEvent;
    }

    public void hasShadowBg(boolean hasShadowBg) {
        this.hasShadowBg = hasShadowBg;
    }

    private OnCloseListener listener;

    public void setOnCloseListener(OnCloseListener listener) {
        this.listener = listener;
    }

    public interface OnCloseListener {
        void onClose();

        void onOpen();
    }
}
