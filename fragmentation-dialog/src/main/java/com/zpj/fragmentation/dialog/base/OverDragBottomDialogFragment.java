package com.zpj.fragmentation.dialog.base;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zpj.fragmentation.dialog.R;
import com.zpj.fragmentation.dialog.animator.DialogAnimator;
import com.zpj.fragmentation.dialog.utils.DialogThemeUtils;
import com.zpj.fragmentation.dialog.widget.OverDragLayout;
import com.zpj.utils.ScreenUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public abstract class OverDragBottomDialogFragment<T extends OverDragBottomDialogFragment<T>> extends BaseDialogFragment<T> {

    protected ViewGroup contentView;

    private float cornerRadius;

    public OverDragBottomDialogFragment() {
        setMaxWidth(MATCH_PARENT);
        cornerRadius = ScreenUtils.dp2px(8);
        setMarginBottom(200);
    }

    @Override
    protected final int getImplLayoutId() {
        return R.layout._dialog_layout_bottom_over_drag_view;
    }

    @Override
    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    protected abstract int getContentLayoutId();

    @Override
    protected DialogAnimator onCreateDialogAnimator(ViewGroup contentView) {
        return null;
    }

    @Override
    protected DialogAnimator onCreateShadowAnimator(FrameLayout flContainer) {
        return null;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        OverDragLayout bottomPopupContainer = (OverDragLayout) getImplView();
        bottomPopupContainer.setShowDuration(getShowAnimDuration());
        bottomPopupContainer.setDismissDuration(getDismissAnimDuration());
        contentView = (ViewGroup) getLayoutInflater().inflate(getContentLayoutId(), null, false);

        FrameLayout flContainer = new FrameLayout(context);
        flContainer.addView(contentView);
        bottomPopupContainer.addView(flContainer);

        bottomPopupContainer.bindContentView(contentView);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(DialogThemeUtils.getDialogBackgroundColor(context));
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(new float[]{ cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0, 0, 0, 0 });
        flContainer.setBackground(drawable);

        super.initLayoutParams(contentView);
        contentView.setClickable(false);


        bottomPopupContainer.enableDrag(true);
        bottomPopupContainer.dismissOnTouchOutside(true);
        bottomPopupContainer.handleTouchOutsideEvent(true);
        bottomPopupContainer.hasShadowBg(true);

        bottomPopupContainer.setOnCloseListener(new OverDragLayout.OnCloseListener() {
            @Override
            public void onClose() {
                postOnEnterAnimationEnd(() -> {
                    if (isDismissing) {
                        return;
                    }
                    isDismissing = true;
                    OverDragBottomDialogFragment.super.doDismissAnimation();
                    popThis();
                    onDismiss();
                });
            }
            @Override
            public void onOpen() {

            }
        });

        bottomPopupContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void initLayoutParams(ViewGroup view) {
//        if (!isDragDialog()) {
//            super.initLayoutParams(view);
//        }
    }

    @Override
    public void doShowAnimation() {
        super.doShowAnimation();
        if (getImplView() instanceof OverDragLayout) {
            ((OverDragLayout) getImplView()).open();
        }
    }

    @Override
    public void doDismissAnimation() {
        super.doDismissAnimation();
        if (getImplView() instanceof OverDragLayout) {
            ((OverDragLayout) getImplView()).close();
        }
    }

    public T setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        return self();
    }

    public T setCornerRadiusDp(float cornerRadiusDp) {
        return setCornerRadius(ScreenUtils.dp2px(cornerRadiusDp));
    }

}
