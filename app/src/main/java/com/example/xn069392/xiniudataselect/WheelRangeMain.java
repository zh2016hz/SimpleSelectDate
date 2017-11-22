package com.example.xn069392.xiniudataselect;

import android.view.View;

import java.util.Arrays;
import java.util.List;


public class WheelRangeMain {

	private View view;
	private int mDay;
	private RangeWheelView wv_year;
	private RangeWheelView wv_month;
	private RangeWheelView wv_day;
	public int screenHeight; //动态获取屏幕高度
	private boolean hasSelectTime;
	private static int START_YEAR = 1990, END_YEAR = 2100;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public WheelRangeMain(View view, boolean hasSelectTime) {
		super();
		this.view = view;
		this.hasSelectTime = hasSelectTime;
		setView(view);
	}


	/**
	 * @Description: 弹出日期时间选择器
	 */
	public void initDateTimePicker(int year, final int month, int day) {
		/** 起始时间不设置就用默认的时间*/
		START_YEAR = 1930;
		END_YEAR = year + 2;
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
		String[] months_little = {"4", "6", "9", "11"};
		// 数组转集合
		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		// 年
		wv_year = (RangeWheelView) view.findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(
				START_YEAR, END_YEAR));
		wv_year.setCyclic(false);
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

		// 月
		wv_month = (RangeWheelView) view.findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(
				1, 12));
		wv_month.setCyclic(true);
		wv_month.setCurrentItem(month);

		// 日
		wv_day = (RangeWheelView) view.findViewById(R.id.day);
		wv_day.setCyclic(true);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(
					1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(
					1, 30));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(
						1, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(
						1, 28));
		}
		mDay = day;
		wv_day.setCurrentItem(day - 1);

		// 添加"年"监听
		OnWheelRangeChangedListener wheelListener_year = new OnWheelRangeChangedListener() {
			public void onChanged(RangeWheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				boolean isLeapYear = (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
						.getCurrentItem() + START_YEAR) % 100 != 0)
						|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0);
				judgeMonth(list_big, list_little, month_num, isLeapYear);
			}
		};
		// 添加"月"监听
		OnWheelRangeChangedListener wheelListener_month = new OnWheelRangeChangedListener() {
			public void onChanged(RangeWheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				boolean isLeapYear = (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
						.getCurrentItem() + START_YEAR) % 100 != 0)
						|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0);

				judgeMonth(list_big, list_little, month_num, isLeapYear);
				// 判断大小月及是否闰年,用来确定"日"的数据

			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize;
		if (hasSelectTime) {
			textSize = (screenHeight / 140) *5;
		} else {
			textSize = (screenHeight / 140) *5;
		}
		wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;
	}

	/**
	 * 处理监听
	 * @param list_big
	 * @param list_little
	 * @param month_num
	 * @param isLeapYear
	 */
	public void judgeMonth(List<String> list_big, List<String> list_little, int month_num, boolean isLeapYear) {
		if (list_big.contains(String.valueOf(month_num))) {
			wv_day.setAdapter(new NumericWheelAdapter(
					1, 31));

		} else if (list_little.contains(String.valueOf(month_num))) {
			wv_day.setAdapter(new NumericWheelAdapter(
					1, 30));
			if (wv_day.getCurrentItem() > 29) {
				wv_day.setCurrentItem(29);
			}
		} else if (wv_month.getCurrentItem() == 1) {
			if (isLeapYear) {
				if (isLeapYear && wv_day.getCurrentItem() > 28) {
					wv_day.setCurrentItem(28);
				}
				wv_day.setAdapter(new NumericWheelAdapter(
						1, 29));
			} else {
				wv_day.setAdapter(new NumericWheelAdapter(
						1, 28));
				if (wv_day.getCurrentItem() > 27) {
					wv_day.setCurrentItem(27);
				}
			}
		}
	}

	/**
	 * 该方法公外面调用，传回时间
	 * @return
	 */
	public String getDate() {
		StringBuffer sb = new StringBuffer();
		String strMon;
		String strDay;
		int month = wv_month.getCurrentItem() + 1;
		int day = wv_day.getCurrentItem() + 1;
		if (month <= 9) {
			strMon = "0" + month;
		} else {
			strMon = String.valueOf(month);
		}
		if (day <= 9) {
			strDay = "0" + day;
		} else {
			strDay = String.valueOf(day);
		}
		if (!hasSelectTime) {

			sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
					.append(strMon).append("-")
					.append(strDay);
		} else {
			sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
					.append(strMon).append("-")
					.append(strDay);
		}
		return sb.toString();
	}

}
