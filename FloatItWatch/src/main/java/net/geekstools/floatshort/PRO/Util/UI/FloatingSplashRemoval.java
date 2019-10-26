package net.geekstools.floatshort.PRO.Util.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.ImageView;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;

public class FloatingSplashRemoval extends ImageView {

    FunctionsClass functionsClass;
    Context context;

    public FloatingSplashRemoval(Context context) {
        super(context);
    }

    public FloatingSplashRemoval(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = PublicVariable.contextStatic.getApplicationContext();
        functionsClass = new FunctionsClass(this.context);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("FloatingSplashRemoval");
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("FloatingSplashRemoval")) {
                    FloatingSplashRemoval.this.setVisibility(VISIBLE);
                    try {
                        functionsClass.circularRevealSplashScreenRemoval(
                                FloatingSplashRemoval.this,
                                FloatingSplash.xPostionRemoval + (FloatingSplash.HWRemoval / 2),
                                FloatingSplash.yPositionRemoval + (FloatingSplash.HWRemoval / 2)
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        this.context.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.TRANSPARENT);
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawCircle(functionsClass.displayX(), functionsClass.displayY(), functionsClass.displayY() * 2, paint);
    }
}
