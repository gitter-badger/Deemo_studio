
package com.sevenre.trackre.driver.views.lib;

public interface ObservableScrollViewCallbacks {
    void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging);

    void onDownMotionEvent();

    void onUpOrCancelMotionEvent(ScrollState scrollState);
}
