package net.geekstools.floatshort.PRO.Util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;

public class ShapeWear {
    private static int screenWidthPX = 0;
    private static int screenHeightPX = 0;
    private static OnSizeChangeListener onSizeChangeListener;
    private static ScreenShape shape = ScreenShape.UNDETECTED;
    private static OnShapeChangeListener onShapeChangeListener;

    private static void initShapeDetection(View view) {
        view.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                if (insets.isRound()) {
                    shape = ScreenShape.ROUND;
                    if (screenWidthPX == 320 && screenHeightPX == 290) {
                        shape = ScreenShape.MOTO_ROUND;
                    }
                } else {
                    shape = ScreenShape.RECTANGLE;
                }
                if (onShapeChangeListener != null) {
                    onShapeChangeListener.shapeDetected(getShape());
                }
                return insets;
            }
        });
    }

    public static void initShapeWear(Activity activity) {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        getScreenSize(wm);
        initShapeDetection(activity.getWindow().getDecorView().findViewById(android.R.id.content));
    }

    public static void initShapeWear(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        getScreenSize(wm);
        initShapeDetection(((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content));
    }

    private static void getScreenSize(WindowManager wm) {
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidthPX = size.x;
        screenHeightPX = size.y;
        if (onSizeChangeListener != null) {
            onSizeChangeListener.sizeDetected(screenWidthPX, screenHeightPX);
        }
    }

    @Deprecated
    public static boolean isRound() throws ScreenShapeNotDetectedException {
        if (shape == null || shape.equals(ScreenShape.UNDETECTED)) {
            throw new ScreenShapeNotDetectedException("ShapeWear still doesn't have correct screen shape at this point, subscribe to OnShapeChangeListener or call this method later on. Also you can call getShape() to get String representation, will return SHAPE_UNSURE if not specified.");
        } else if (shape.equals(ScreenShape.ROUND)) {
            return true;
        } else {
            return false;
        }
    }

    public static ScreenShape getShape() {
        return shape;
    }

    public static int getScreenWidthPX() {
        return screenWidthPX;
    }

    public static int getScreenHeightPX() {
        return screenHeightPX;
    }

    public static OnShapeChangeListener getOnShapeChangeListener() {
        return onShapeChangeListener;
    }

    public static void setOnShapeChangeListener(OnShapeChangeListener onShapeChangeListener) {
        ShapeWear.onShapeChangeListener = onShapeChangeListener;
        if (!getShape().equals(ScreenShape.UNDETECTED) && ShapeWear.onShapeChangeListener != null) {
            ShapeWear.onShapeChangeListener.shapeDetected(getShape());
        }
    }

    public static void setOnSizeChangeListener(OnSizeChangeListener onSizeChangeListener) {
        ShapeWear.onSizeChangeListener = onSizeChangeListener;
        if (ShapeWear.onSizeChangeListener != null && screenWidthPX != 0 && screenHeightPX != 0) {
            ShapeWear.onSizeChangeListener.sizeDetected(screenWidthPX, screenHeightPX);
        }
    }

    public static enum ScreenShape {ROUND, MOTO_ROUND, RECTANGLE, UNDETECTED}

    public interface OnShapeChangeListener {
        void shapeDetected(ScreenShape screenShape);
    }

    public interface OnSizeChangeListener {
        void sizeDetected(int widthPx, int heightPx);
    }

    public static class ScreenShapeNotDetectedException extends Exception {
        public ScreenShapeNotDetectedException() {
        }

        public ScreenShapeNotDetectedException(String detailMessage) {
            super(detailMessage);
        }
    }
}

