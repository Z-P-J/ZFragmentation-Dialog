package com.zpj.fragmentation.dialog.animator;

import com.zpj.fragmentation.dialog.base.BaseDialogFragment;

public interface DialogAnimator {

    void setShowDuration(long showAnimDuration);

    void setDismissDuration(long dismissAnimDuration);

    long getShowDuration();

    long getDismissDuration();

    void animateToShow(BaseDialogFragment<?> fragment);

    void animateToDismiss(BaseDialogFragment<?> fragment);

}
