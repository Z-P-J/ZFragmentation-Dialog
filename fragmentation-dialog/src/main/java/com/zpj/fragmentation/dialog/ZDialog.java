package com.zpj.fragmentation.dialog;

import com.zpj.fragmentation.dialog.impl.AlertDialogFragment;
import com.zpj.fragmentation.dialog.impl.ArrowMenuDialogFragment;
import com.zpj.fragmentation.dialog.impl.AttachListDialogFragment;
import com.zpj.fragmentation.dialog.impl.BottomDragListDialogFragment;
import com.zpj.fragmentation.dialog.impl.BottomDragSelectDialogFragment;
import com.zpj.fragmentation.dialog.impl.CheckDialogFragment;
import com.zpj.fragmentation.dialog.impl.ImageViewerDialogFragment;
import com.zpj.fragmentation.dialog.impl.InputDialogFragment;
import com.zpj.fragmentation.dialog.impl.ListDialogFragment;
import com.zpj.fragmentation.dialog.impl.LoadingDialogFragment;
import com.zpj.fragmentation.dialog.impl.SelectDialogFragment;
import com.zpj.fragmentation.dialog.impl.SimpleSelectDialogFragment;

public class ZDialog {

    public static AlertDialogFragment alert() {
        return new AlertDialogFragment();
    }

    public static CheckDialogFragment check() {
        return new CheckDialogFragment();
    }

    public static InputDialogFragment input() {
        return new InputDialogFragment();
    }

    public static LoadingDialogFragment loading() {
        return new LoadingDialogFragment();
    }

    public static <T> ImageViewerDialogFragment<T> imageViewer(Class<T> tClass) {
        return new ImageViewerDialogFragment<>();
    }

    public static ImageViewerDialogFragment<String> imageViewer() {
        return imageViewer(String.class);
    }

    public static <T> SelectDialogFragment<T> select(Class<T> tClass) {
        return new SelectDialogFragment<>();
    }

    public static SelectDialogFragment<String> select() {
        return select(String.class);
    }

    public static SimpleSelectDialogFragment simpleSelect() {
        return new SimpleSelectDialogFragment();
    }

    public static <T> ListDialogFragment<T> list(Class<T> tClass) {
        return new ListDialogFragment<>();
    }

    public static ListDialogFragment<String> list() {
        return new ListDialogFragment<>();
    }

    public static <T> BottomDragSelectDialogFragment<T> bottomSelect(Class<T> tClass) {
        return new BottomDragSelectDialogFragment<>();
    }

    public static BottomDragSelectDialogFragment<String> bottomSelect() {
        return bottomSelect(String.class);
    }

    public static <T> BottomDragListDialogFragment<T> bottomList(Class<T> tClass) {
        return new BottomDragListDialogFragment<>();
    }

    public static BottomDragListDialogFragment<String> bottomList() {
        return bottomList(String.class);
    }

    public static ArrowMenuDialogFragment arrowMenu() {
        return new ArrowMenuDialogFragment();
    }

    public static <T> AttachListDialogFragment<T> attach(Class<T> tClass) {
        return new AttachListDialogFragment<T>();
    }

    public static AttachListDialogFragment<String> attach() {
        return attach(String.class);
    }

}
