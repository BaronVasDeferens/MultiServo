package skot.multiservo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class TouchControl extends View {

    MainActivity activity;

    private int topOfLine = 0;

    public TouchControl(MainActivity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setStrokeWidth(5.0f);
        p.setAlpha(255);

        Rect drawMe = new Rect();
        Rect area = canvas.getClipBounds();
        drawMe.set(0, topOfLine, area.right, area.bottom);
        canvas.drawRect(drawMe, p);
    }

    public void setSlider(MotionEvent e) {
        MotionEvent.PointerCoords pc = new MotionEvent.PointerCoords();
        e.getPointerCoords(0, pc);
        topOfLine = (int) pc.y;

        System.out.println(topOfLine);
    }

}
