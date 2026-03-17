package com.pax.android.supporthub.app.widget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.pax.android.supporthub.app.R;
import com.pax.android.supporthub.app.util.DateUtil;

import java.util.Calendar;
import java.util.Objects;

public class FilterDialog extends Dialog {
    private String startTime;
    private String endTime;
    private final Context context;
    private int selectedIndex = 0;
    private final TextView tvStart;
    private final TextView tvEnd;
    private final Button btnReset;
    private final Button btnConfirm;
    private final ImageView ivClose;
    private final String[] statuses;
    private String selectedStatus;
    private OnButtonClickListener mListener;
    private final LinearLayout llTagGroup;
    FlowLayout flowLayout;

    public interface OnButtonClickListener {
        void confirmClick();

        void resetClick();
    }

    private void initDatePicker(TextView editText, boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (isStartTime && !TextUtils.isEmpty(startTime)) {
            String[] dateParts = startTime.split("-");
            if (dateParts.length == 3) {
                year = Integer.parseInt(dateParts[0]);
                month = Integer.parseInt(dateParts[1]) - 1;
                day = Integer.parseInt(dateParts[2]);
            }
        } else if (!isStartTime && !TextUtils.isEmpty(endTime)) {
            String[] dateParts = endTime.split("-");
            if (dateParts.length == 3) {
                year = Integer.parseInt(dateParts[0]);
                month = Integer.parseInt(dateParts[1]) - 1;
                day = Integer.parseInt(dateParts[2]);
            }
        }

        DatePickerDialog dialog = new DatePickerDialog
                (context, R.style.BlueDatePickerTheme, (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    String time = DateUtil.getDateStr(calendar);
                    editText.setText(time);
                    if (isStartTime) {
                        setStartTime(time);
                        if (!TextUtils.isEmpty(endTime) && DateUtil.isDateBefore(endTime, time)) {
                            setEndTime(time);
                            if (tvEnd != null) {
                                tvEnd.setText(time);
                            }
                        }
                    } else {
                        setEndTime(time);
                        if (!TextUtils.isEmpty(startTime) && !DateUtil.isDateBefore(startTime, time)) {
                            setStartTime(time);
                            if (tvStart != null) {
                                tvStart.setText(time);
                            }
                        }
                    }
                }, year, month, day);

        // 动态设置日期选择器的限制范围
        if (isStartTime) {
            dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

            if (!TextUtils.isEmpty(endTime)) {
                Long timeStamp = DateUtil.getTimeStamp(endTime);
                if (null != timeStamp) {
                    dialog.getDatePicker().setMaxDate(timeStamp);
                }

            }
        } else {
            if (!TextUtils.isEmpty(startTime)) {
                Long timeStamp = DateUtil.getTimeStamp(startTime);
                if (null != timeStamp) {
                    dialog.getDatePicker().setMinDate(timeStamp);
                }

            }
            // 终止时间的最大日期为当前时间（不能选择未来）
            dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        }
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.btn_confirm), dialog);
        dialog.show();
    }


    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.mListener = listener;
    }

    public FilterDialog(@NonNull Context context) {
        super(context, R.style.NotTransparentDialogTheme);
        this.context = context;
        setContentView(R.layout.dialog_filter);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        statuses = new String[]{context.getString(R.string.status_select_all),
                context.getString(R.string.status_select_open),
                context.getString(R.string.status_select_processing),
                context.getString(R.string.status_select_hold),
                context.getString(R.string.status_select_close)};

        selectedStatus = context.getString(R.string.status_select_all);
        flowLayout = new FlowLayout(context, 2);
        llTagGroup = findViewById(R.id.llTagGroup);
        tvStart = findViewById(R.id.time_start);
        tvEnd = findViewById(R.id.time_end);
        btnReset = findViewById(R.id.btn_reset);
        btnConfirm = findViewById(R.id.btn_confirm);
        ivClose = findViewById(R.id.icon_close);

        addStatusTags();

        tvStart.setOnClickListener(view -> initDatePicker(tvStart, true));

        tvEnd.setOnClickListener(view -> initDatePicker(tvEnd, false));


        ivClose.setOnClickListener(v -> dismiss());
        btnReset.setOnClickListener(v -> {
            reset();
            dismiss();
            mListener.resetClick();
        });
        btnConfirm.setOnClickListener(v -> {
            String tvEndTime = tvEnd.getText().toString();
            String tvStartTime = tvStart.getText().toString();
            if (tvEndTime.equals(context.getString(R.string.dialog_end_time)) && !tvStartTime.equals(context.getString(R.string.dialog_start_time))) {
                endTime = startTime;
                tvEnd.setText(startTime);
            }
            if (tvStartTime.equals(context.getString(R.string.dialog_start_time)) && !tvEndTime.equals(context.getString(R.string.dialog_end_time))) {
                startTime = endTime;
                tvStart.setText(endTime);
            }

            mListener.confirmClick();
            dismiss();
        });
    }

    public String getSelectedStatus() {
        return selectedStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    @Override
    public void show() {
        if (mStateListener != null) {
            FilterState savedState = mStateListener.onStateRequested();
            if (savedState != null) {
                restoreState(savedState);
            }
        }
        super.show();
        WindowManager.LayoutParams layoutParams = Objects.requireNonNull(getWindow()).getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

    private void addStatusTags() {
        for (String s : statuses) {
            TextView tv = (TextView) LayoutInflater.from(flowLayout.getContext())
                    .inflate(R.layout.item_filter_status, flowLayout, false);
            tv.setText(s);
            tv.setSelected(s.equals(context.getString(R.string.status_select_all)));
            tv.setOnClickListener(v -> {
                // 单选逻辑
                for (int j = 0; j < flowLayout.getChildCount(); j++) {
                    if (tv == flowLayout.getChildAt(j)) {
                        flowLayout.getChildAt(j).setSelected(true);
                        selectedIndex = j;
                        selectedStatus = s;
                    } else flowLayout.getChildAt(j).setSelected(false);
                }
            });
            flowLayout.addView(tv);
        }
        llTagGroup.addView(flowLayout);
    }

    public void clearStatusSelection() {
        flowLayout.getChildAt(selectedIndex).setSelected(false);
        selectedIndex = 0;
        flowLayout.getChildAt(0).setSelected(true);
        selectedStatus = context.getString(R.string.status_select_all);
    }

    public void clearDate() {
        tvStart.setText(context.getString(R.string.dialog_start_time));
        tvEnd.setText(context.getString(R.string.dialog_end_time));
        startTime = null;
        endTime = null;
    }

    public interface OnStateChangeListener {
        void onStateChanged(FilterState state);

        FilterState onStateRequested();
    }

    private OnStateChangeListener mStateListener;

    public static class FilterState {
        public String startTime;
        public String endTime;
        public String selectedStatus;
        public int selectedIndex;

        public FilterState() {
        }

        public FilterState(String startTime, String endTime, String selectedStatus, int selectedIndex) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.selectedStatus = selectedStatus;
            this.selectedIndex = selectedIndex;
        }
    }

    public void setOnStateChangeListener(OnStateChangeListener listener) {
        this.mStateListener = listener;
    }

    public void restoreState(FilterState state) {
        if (state != null) {
            this.startTime = state.startTime;
            this.endTime = state.endTime;
            this.selectedStatus = state.selectedStatus;
            this.selectedIndex = state.selectedIndex;

            restoreUIState();
        }
    }

    public FilterState getCurrentState() {
        return new FilterState(startTime, endTime, selectedStatus, selectedIndex);
    }

    private void restoreUIState() {
        if (!TextUtils.isEmpty(startTime)) {
            tvStart.setText(startTime);
        } else {
            tvStart.setText(context.getString(R.string.dialog_start_time));
        }

        if (!TextUtils.isEmpty(endTime)) {
            tvEnd.setText(endTime);
        } else {
            tvEnd.setText(context.getString(R.string.dialog_end_time));
        }

        if (flowLayout != null && flowLayout.getChildCount() > selectedIndex) {
            for (int j = 0; j < flowLayout.getChildCount(); j++) {
                TextView child = (TextView) flowLayout.getChildAt(j);
                child.setSelected(j == selectedIndex);
            }
        }
    }

    @Override
    public void dismiss() {
        //  dismiss前保存状态
        if (mStateListener != null) {
            mStateListener.onStateChanged(getCurrentState());
        }
        super.dismiss();
    }

    private void reset() {
        clearStatusSelection();
        tvStart.setText(context.getString(R.string.dialog_start_time));
        tvEnd.setText(context.getString(R.string.dialog_end_time));
        startTime = null;
        endTime = null;

        if (mStateListener != null) {
            mStateListener.onStateChanged(getCurrentState());
        }
    }

}
