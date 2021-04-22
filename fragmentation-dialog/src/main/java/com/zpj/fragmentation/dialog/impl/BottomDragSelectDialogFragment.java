package com.zpj.fragmentation.dialog.impl;

public class BottomDragSelectDialogFragment<T>
        extends BaseSelectDialogFragment<T, BottomDragSelectDialogFragment<T>> {

    @Override
    final boolean isDrag() {
        return true;
    }
}
