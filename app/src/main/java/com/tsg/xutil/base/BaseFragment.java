package com.tsg.xutil.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by xiaoAwei on 2017/4/1.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        show();
    }

    protected abstract void initView(View view);

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {//隐藏了
            hideIputKeyboard(getContext());
            onPause();
            hide();
        } else {//显示了
            show();
        }
    }

    protected void hide() {

    }

    protected void show() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 回退键
     */
    public void onBackPressed() {

    }

    /**
     * 隐藏软键盘
     *
     * @param context
     */
    public void hideIputKeyboard(final Context context) {
        final Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                InputMethodManager mInputKeyBoard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (activity.getCurrentFocus() != null) {
                    mInputKeyBoard.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        hideIputKeyboard(getContext());
    }

}
