package com.zpj.fragmentation.dialog.imageviewer;

public class ZImageViewer {

    public static <T> ImageViewerDialogFragment<T> create(Class<T> tClass) {
        return new ImageViewerDialogFragment<>();
    }

    public static ImageViewerDialogFragment<String> create() {
        return create(String.class);
    }

}
