package io.weichao.transparent_log.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class NoDispatchRelativeLayout extends RelativeLayout {
    public NoDispatchRelativeLayout(Context context) {
        super(context);
    }

    public NoDispatchRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoDispatchRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }
}