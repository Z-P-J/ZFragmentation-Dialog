package com.zpj.fragmentation.dialog.imageviewer;

public interface OnDragChangeListener {
    void onRelease();
    void onDragChange(int dy, float scale, float fraction);
}
