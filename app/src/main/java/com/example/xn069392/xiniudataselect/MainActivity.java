package com.example.xn069392.xiniudataselect;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView mPop;
    private TextView tv_center;
    WheelRangeMain wheelRangeMainDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPop = (TextView) findViewById(R.id.showPop);
        tv_center = (TextView) findViewById(R.id.text_center);
        mPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
            }
        });
    }

    /**
     * 显示弹窗
     */
    private void showPopWindow() {
        // 获取当前屏幕的实际像素宽度
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(outMetrics);
        int width = outMetrics.widthPixels;

        View menuView = LayoutInflater.from(this).inflate(R.layout.show_range_popup_window, null);
        final PopupWindow mPopupWindow = new PopupWindow(menuView, width, ActionBar.LayoutParams.WRAP_CONTENT);
        DisplayMetrics metric = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        wheelRangeMainDate = new WheelRangeMain(menuView, true);
        wheelRangeMainDate.screenHeight = metric.heightPixels;//动态获取屏幕的像素点高度

        // 获得当前时间
        String time = DateUtils.currentMonth().toString();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-DD")) {
            try {
                calendar.setTime(new Date(time));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final String nowDate = format.format(new Date());
        wheelRangeMainDate.initDateTimePicker(year, month, day);
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(tv_center, Gravity.BOTTOM, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        backgroundAlpha(0.6f);

        TextView img_cancel = (TextView) menuView.findViewById(R.id.img_cancel);
        TextView img_ensure = (TextView) menuView.findViewById(R.id.img_ensure);

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
        img_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String beginTime = wheelRangeMainDate.getDate();
                mPop.setText(DateUtils.formateStringH(beginTime, DateUtils.yyyyMMddHHmm));
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    /**
     * 设置背景颜色变灰
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }
}
