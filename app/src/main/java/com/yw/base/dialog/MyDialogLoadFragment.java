package com.yw.base.dialog;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yw.base.R;
import com.yw.base.base.BaseDialogFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyDialogLoadFragment extends BaseDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setCancelable(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        super.onCreateView(inflater,container,savedInstanceState);
        return view;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_dialog_load;
    }


}
