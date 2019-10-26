package net.geekstools.floatshort.PRO.Util.NavAdapter;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewSmoothLayoutGrid extends GridLayoutManager {

    private static final float MILLISECONDS_PER_INCH = 27f;
    private Context context;

    public RecycleViewSmoothLayoutGrid(Context context, int NumColumn, int Orientation, boolean reverseLayout) {
        super(context, NumColumn, Orientation, reverseLayout);
        this.context = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, final int position) {

        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(context) {
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return RecycleViewSmoothLayoutGrid.this.computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }
        };

        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }
}
