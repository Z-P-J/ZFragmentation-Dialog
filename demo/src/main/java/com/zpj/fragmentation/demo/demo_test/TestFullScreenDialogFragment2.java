package com.zpj.fragmentation.demo.demo_test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.zpj.fragmentation.demo.R;
import com.zpj.fragmentation.dialog.impl.FullScreenDialogFragment;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class TestFullScreenDialogFragment2 extends FullScreenDialogFragment {

    @Override
    protected int getImplLayoutId() {
        return R.layout.test_fragment_dialog_full;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
//        getContentView().setBackgroundColor(Color.GRAY);
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dismiss();
//                startWithPop(new MainFragment());
                Toast.makeText(context, "TestFullScreenDialogFragment2", Toast.LENGTH_SHORT).show();
//                start(new MainFragment());
                new TestFullScreenDialogFragment().show(context);
                dismiss();
            }

        });

        postDelayed(new Runnable() {
            @Override
            public void run() {
                setInterceptTouch(false);
            }
        }, 5000);

    }

    @Override
    protected void initLayoutParams(ViewGroup view) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = MATCH_PARENT;
        layoutParams.width = MATCH_PARENT;
    }
}
