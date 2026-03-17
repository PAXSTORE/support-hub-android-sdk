package com.pax.android.supporthub.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pax.android.supporthub.app.util.DateUtil;
import com.pax.android.supporthub.app.widget.FilterDialog;
import com.pax.android.supporthub.sdk.SupportHubSdk;
import com.pax.android.supporthub.sdk.apis.dto.HistoryTicketDto;
import com.pax.android.supporthub.sdk.apis.dto.PageInfoDto;
import com.pax.android.supporthub.sdk.apis.dto.SupportHubTicketDetailInfo;
import com.pax.android.supporthub.sdk.apis.dto.SupportPhoneDto;


public class MainActivity extends AppCompatActivity implements FilterDialog.OnStateChangeListener{
    private Button btnUploadTicket, btnGetPhone, btnGetTicketDetail, btnGetHistoryTicket, filterBtn;
    private EditText edTicketId, edStartOffset, edLimit;
    private TextView searchContent;
    private TextView tvResult;
    private FilterDialog filterDialog;
    private FilterDialog.FilterState mFilterState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initClickMethod();
    }
    private void initView() {
        btnUploadTicket = findViewById(R.id.btn_upload_ticket);
        tvResult = findViewById(R.id.tv_result);
        btnGetPhone = findViewById(R.id.btn_get_support_phone);
        btnGetTicketDetail = findViewById(R.id.btn_get_ticket_detail);
        btnGetHistoryTicket = findViewById(R.id.btn_get_history_tickets);
        filterBtn = findViewById(R.id.btn_set_date);
        edTicketId = findViewById(R.id.ed_ticket_id);
        edStartOffset = findViewById(R.id.ed_start_offset);
        edLimit = findViewById(R.id.ed_page_size);
        searchContent = findViewById(R.id.ed_search_content);
    }

    private void initClickMethod() {
        btnUploadTicket.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ReportTicketActivity.class);
            startActivity(intent);
        });
        // 绑定点击事件
        btnGetPhone.setOnClickListener(v -> callGetPhoneApi());
        btnGetTicketDetail.setOnClickListener(v -> callGetTicketDetailApi());
        btnGetHistoryTicket.setOnClickListener(v -> callGetHistoryTicketListApi());

        filterDialog = new FilterDialog(this);

        // 设置状态变化监听器
        filterDialog.setOnStateChangeListener(this);

        // 恢复已保存的状态（如果有）
        if (mFilterState != null) {
            filterDialog.restoreState(mFilterState);
        }
        filterBtn.setOnClickListener(view3 -> filterDialog.show());
        filterDialog.setOnButtonClickListener(new FilterDialog.OnButtonClickListener() {
            @Override
            public void confirmClick() {
                callGetHistoryTicketListApi();
            }

            @Override
            public void resetClick() {
                mFilterState = new FilterDialog.FilterState();
                mFilterState.selectedStatus = getString(R.string.status_select_all);
                mFilterState.selectedIndex = 0;

            }
        });
    }

    private void callGetPhoneApi() {
        Thread thread =  new Thread(() -> {
            try {
                tvResult.setText("calling getSupportPhone api ...");
                SupportPhoneDto phone = SupportHubSdk.getInstance().supportHubApi().getSupportPhone();
                runOnUiThread(() -> {
                    if (phone.getBusinessCode() != 0) {
                        tvResult.setText(phone.getMessage());
                    } else {
                        tvResult.setText(phone.toString());
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> tvResult.setText("callGetPhoneApi err: " + e.getMessage()));
            }
        });
        thread.start();
    }

    private void callGetHistoryTicketListApi() {
        new Thread(() -> {
            try {
                tvResult.setText("calling getHistoryTicketList api ...");
                Long start = DateUtil.getTimeStamp(TextUtils.isEmpty(filterDialog.getStartTime()) ? filterDialog.getEndTime() : filterDialog.getStartTime());
                start = (start == null) ? null : start + 1000L;
                Long end = DateUtil.getTimeStamp(TextUtils.isEmpty(filterDialog.getEndTime()) ? filterDialog.getStartTime() : filterDialog.getEndTime());

                PageInfoDto<HistoryTicketDto> detailInfo = SupportHubSdk.getInstance().supportHubApi().getHistoryTicketList
                        (searchContent.getText().toString(), getSearchStatus(filterDialog.getSelectedStatus()), start,end,
                                Integer.parseInt(edStartOffset.getText().toString()),
                                Integer.parseInt(edLimit.getText().toString()), null);
                runOnUiThread(() -> {
                    if (detailInfo.getBusinessCode() != 0) {
                        tvResult.setText(detailInfo.getMessage());
                    } else {
                        tvResult.setText(detailInfo.getList().toString());
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> tvResult.setText("callGetTicketDetailApi err: " + e.getMessage()));
            }
        }).start();
    }

    public static final String SEARCH_STATUS_O = "O";
    public static final String SEARCH_STATUS_I = "I";
    public static final String SEARCH_STATUS_H = "H";
    public static final String SEARCH_STATUS_C = "C";
    private String getSearchStatus(String status) {
        String searchStatus = null;
        if (status.equals(getString(R.string.status_select_open))) {
            searchStatus = SEARCH_STATUS_O;
        } else if (status.equals(getString(R.string.status_select_processing))) {
            searchStatus = SEARCH_STATUS_I;
        } else if (status.equals(getString(R.string.status_select_hold))) {
            searchStatus = SEARCH_STATUS_H;
        } else if (status.equals(getString(R.string.status_select_close))) {
            searchStatus = SEARCH_STATUS_C;
        }
        return searchStatus;
    }

    private void callGetTicketDetailApi() {
        new Thread(() -> {
            try {
                SupportHubTicketDetailInfo detailInfo = SupportHubSdk.getInstance().supportHubApi().getTicketDetail(Long.valueOf(edTicketId.getText().toString()));
                runOnUiThread(() -> tvResult.setText(detailInfo.toString()));
            } catch (Exception e) {
                runOnUiThread(() -> tvResult.setText("callGetTicketDetailApi err: " + e.getMessage()));
            }
        }).start();
    }

    // 实现状态变化监听接口方法
    @Override
    public void onStateChanged(FilterDialog.FilterState state) {
        // 保存状态
        this.mFilterState = state;
    }

    @Override
    public FilterDialog.FilterState onStateRequested() {
        // 返回当前保存的状态
        return mFilterState;
    }
}
