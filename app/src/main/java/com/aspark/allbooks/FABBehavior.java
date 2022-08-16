package com.aspark.allbooks;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FABBehavior extends FloatingActionButton.Behavior {

    final String TAG ="FABBehavior";

    // Without this constructor. ERROR shown will be "Could not inflate Behavior subclass com.aspark.allbooks.FABBehavior"
    public FABBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    //TODO learn about these methods from google documentation .

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child,
                                       @NonNull View directTargetChild, @NonNull View target, int axes, int type) {

//        Log.d(TAG, "onStartNestedScroll: called");

        //TODO WTF does this do?
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {

        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);

//        Log.d(TAG, "onNestedScroll: called");

    if (dyConsumed >0 && child.getVisibility() == View.VISIBLE) {

        child.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override
            public void onHidden(FloatingActionButton fab) {
                super.onHidden(fab);

                Log.d(TAG, "onHidden: make FAB Go");
                fab.setVisibility(View.INVISIBLE);
            }
        });
    }
    else if ( dyConsumed <0 && child.getVisibility() != View.VISIBLE){

        Log.d(TAG, "onNestedScroll: GET FAB");
        child.show();
    }



    }
}
