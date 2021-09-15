package com.zpj.fragmentation.dialog.animator;

import android.animation.Animator;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;

import com.zpj.fragmentation.dialog.AbstractDialogFragment;
import com.zpj.fragmentation.dialog.base.BaseDialogFragment;
import com.zpj.fragmentation.dialog.enums.DialogAnimation;

public abstract class AbsDialogAnimator<S, T> implements DialogAnimator {

    protected View targetView;

    protected long mShowDuration = 360;
    protected long mDismissDuration = 360;

    private S mShowAnimator;
    private T mDismissAnimator;

    public DialogAnimation dialogAnimation;

    public AbsDialogAnimator(View target){
        this(target, null);
    }

    public AbsDialogAnimator(View target, DialogAnimation dialogAnimation){
        this.targetView = target;
        this.dialogAnimation = dialogAnimation;
    }

    @Override
    public void setShowDuration(long showAnimDuration) {
        this.mShowDuration = showAnimDuration;
    }

    @Override
    public void setDismissDuration(long dismissAnimDuration) {
        this.mDismissDuration = dismissAnimDuration;
    }

    @Override
    public long getShowDuration() {
        return mShowDuration;
    }

    @Override
    public long getDismissDuration() {
        return mDismissDuration;
    }


    @Override
    public void animateToShow(BaseDialogFragment<?> fragment) {
        mShowAnimator = onCreateShowAnimator(fragment);
        startAnimator(mShowAnimator, fragment.getShowAnimDuration(), true);
    }

    @Override
    public void animateToDismiss(BaseDialogFragment<?> fragment) {
        mDismissAnimator = onCreateDismissAnimator(fragment);
        startAnimator(mDismissAnimator, fragment.getDismissAnimDuration(), false);
    }

    protected void startAnimator(Object animator, long duration, boolean isShow) {
        Log.d("startAnimator", "duration=" + duration + " animator=" + animator);
        if (animator instanceof Animator) {
//            if (mListener != null) {
//                ((Animator) animator).addListener(createAnimatorListener(isShow));
//            }
            ((Animator) animator).setDuration(duration);
            ((Animator) animator).start();
        } else if (animator instanceof ViewPropertyAnimator) {
//            if (mListener != null) {
//                ((ViewPropertyAnimator) animator).setListener(createAnimatorListener(isShow));
//            }
            ((ViewPropertyAnimator) animator).setDuration(duration);
            ((ViewPropertyAnimator) animator).start();
        }
    }

//    private Animator.AnimatorListener createAnimatorListener(boolean isShow) {
//        return new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                if (isShow) {
//                    mListener.onShowAnimationStart();
//                } else {
//                    mListener.onDismissAnimationStart();
//                }
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                if (isShow) {
//                    mListener.onShowAnimationEnd();
//                } else {
//                    mListener.onDismissAnimationEnd();
//                }
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        };
//    }

    public abstract S onCreateShowAnimator(BaseDialogFragment<?> fragment);
    public abstract T onCreateDismissAnimator(BaseDialogFragment<?> fragment);

}
