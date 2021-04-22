package com.zpj.fragmentation.dialog.impl;

public class BottomDragListDialogFragment<T> extends BaseListDialogFragment<T, BottomDragListDialogFragment<T>> {

    @Override
    final boolean isDrag() {
        return true;
    }

}
