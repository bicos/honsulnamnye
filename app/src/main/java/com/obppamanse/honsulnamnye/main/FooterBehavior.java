package com.obppamanse.honsulnamnye.main;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by raehyeong.park on 2017. 6. 20..
 */

public class FooterBehavior extends CoordinatorLayout.Behavior {

    private int totalDy = 0;

    public FooterBehavior() {
    }

    public FooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
//        totalDy += dy;
        if (dy > 0) {
            // scroll down
            totalDy = Math.min(totalDy + dy, child.getMeasuredHeight());
//            child.animate().translationY(Math.max(totalDy, (child.getMeasuredHeight())));
        } else if (dy < 0) {
            // scroll up
            totalDy = Math.max(totalDy + dy, 0);
//            child.animate().translationY(Math.min(totalDy, 0));
        }

        child.animate().setDuration(0).translationY(totalDy);
    }
}
