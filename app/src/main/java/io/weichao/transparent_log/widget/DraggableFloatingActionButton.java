package io.weichao.transparent_log.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class DraggableFloatingActionButton extends FloatingActionButton {
    private static final String TAG = "DraggableFloatingActionButton";

    private static final int TIME = 200;

    private int parentHeight;
    private int parentWidth;

    private long mDownTime;

    private int lastX;
    private int lastY;

    private boolean isDrag;

    private Listener listener;

    public DraggableFloatingActionButton(Context context) {
        super(context);
    }

    public DraggableFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DraggableFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int rawX = (int) event.getRawX();
        final int rawY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownTime = SystemClock.elapsedRealtime();
                Log.d(TAG, "ACTION_DOWN:" + mDownTime);
                setPressed(true);
                isDrag = false;
                lastX = rawX;
                lastY = rawY;
                if (getParent() != null) {
                    final ViewGroup parent = (ViewGroup) getParent();
                    parentHeight = parent.getHeight();
                    parentWidth = parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (parentHeight <= 0 || parentWidth == 0) {
                    isDrag = false;
                    break;
                } else {
                    isDrag = true;
                }

                int dx = rawX - lastX;
                int dy = rawY - lastY;

                // 这里修复一些华为手机无法触发点击事件
                int distance = (int) Math.sqrt(dx * dx + dy * dy);
                if (distance == 0) {
                    isDrag = false;
                    break;
                }

                float x = getX() + dx;
                float y = getY() + dy;
                setX(x);
                setY(y);

                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                if (!isNotDrag()) {
                    setPressed(false);
                }
                final long upTime = SystemClock.elapsedRealtime();
                Log.v(TAG, "ACTION_UP:" + upTime);
                final long passTime = upTime - mDownTime;
                Log.v(TAG, "passTime:" + passTime);
                if (passTime < TIME) {
                    if (listener != null) {
                        listener.onClick();
                    }
                }
                break;
        }
        return !isNotDrag() || super.onTouchEvent(event);
    }

    private boolean isNotDrag() {
        return !isDrag && (getX() == 0 || (getX() == parentWidth - getWidth()));
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onClick();
    }
}