package com.pax.android.supporthub.app;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pax.android.supporthub.app.adapter.ImageAdapter;
import com.pax.android.supporthub.app.util.FileUtils;
import com.pax.android.supporthub.sdk.SupportHubSdk;

import com.pax.android.supporthub.sdk.apis.dto.TicketNumberDto;
import com.pax.android.supporthub.sdk.apis.dto.UploadTicketDto;
import com.pax.android.supporthub.sdk.apis.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReportTicketActivity extends AppCompatActivity {
    private static final String TAG = ReportTicketActivity.class.getSimpleName();
    private ActivityResultLauncher<Intent> selectImagesLauncher;
    private RecyclerView imagesRecyclerView;
    private EditText titleEdit;
    private EditText describeEdit;
    private EditText nameEdit;
    private EditText emailEdit;
    private EditText phoneEdit;
    private ImageAdapter imageAdapter;
    private TextView tvResult;
    private Button reportBtn, back;
    private SwitchCompat swCompress;
    private final List<Uri> selectedImageUris = new ArrayList<>();
    private boolean isCompress = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        showSelectImages();
        init();
        initClickMethod();
    }


    private void showSelectImages() {
        selectImagesLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        int newItemPosition = 0;
                        Intent data = result.getData();
                        if (data.getClipData() != null) {
                            ClipData clipData = data.getClipData();
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                Uri imageUri = clipData.getItemAt(i).getUri();
                                if (!selectedImageUris.contains(imageUri)) {
                                    selectedImageUris.add(imageUri);
                                    newItemPosition++;

                                }
                            }
                        } else if (data.getData() != null) {
                            Uri imageUri = data.getData();
                            if (!selectedImageUris.contains(imageUri)) {
                                selectedImageUris.add(imageUri);
                                newItemPosition = 1;
                            }
                        }

                        int startPosition = selectedImageUris.size() - newItemPosition;
                        imageAdapter.notifyItemRangeInserted(startPosition, newItemPosition);
                    }
                });
    }

    private void init() {
        back = findViewById(R.id.btn_back);
        tvResult = findViewById(R.id.tv_result);
        reportBtn = findViewById(R.id.report_button);
        titleEdit = findViewById(R.id.title_edit);
        describeEdit = findViewById(R.id.describe_edit);
        imagesRecyclerView = findViewById(R.id.rv_photos);
        nameEdit = findViewById(R.id.name_edit);
        emailEdit = findViewById(R.id.email_edit);
        phoneEdit = findViewById(R.id.phone_edit);
        swCompress = findViewById(R.id.sw_compress);
        setupRecyclerView();
    }


    private void initClickMethod() {
        imageAdapter.setOnAddButtonClickListener(this::openImagePicker);

        imageAdapter.setOnRemoveButtonClickListener(this::removePhoto);

        reportBtn.setOnClickListener(view -> uploadTicket());

        back.setOnClickListener(view -> finish());
    }

    private void removePhoto(int position) {
        selectedImageUris.remove(position);
        imageAdapter.notifyItemRemoved(position);
    }

    private void uploadTicket() {
        String title = titleEdit.getText().toString();
        String description = describeEdit.getText().toString();
        String contactName = nameEdit.getText().toString();
        String email = emailEdit.getText().toString();
        String phone = phoneEdit.getText().toString();
        UploadTicketDto dto = new UploadTicketDto();
        dto.setIssueTitle(title);
        dto.setIssueDescription(description);
        dto.setEmail(email);
        dto.setPhone(phone);
        dto.setContactName(contactName);
        swCompress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                isCompress = isChecked;
            }
        });
        Thread thread =  new Thread(() -> {
            try {
                Log.w(TAG,"calling uploadTicket api ... compress: " + isCompress);
                tvResult.setText("calling uploadTicket api ... compress: " + isCompress);
                TicketNumberDto ticketNumberDto = SupportHubSdk.getInstance().supportHubApi().uploadTicket(dto, getImageFile(selectedImageUris), this, isCompress);
                runOnUiThread(() -> {
                    if (ticketNumberDto.getBusinessCode() != 0) {
                        tvResult.setText(ticketNumberDto.getMessage());
                    } else {
                        tvResult.setText(ticketNumberDto.toString());
                        FileUtils.clearAllTempFiles(this);
                        FileUtil.clearCompressTempFiles(this);
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> tvResult.setText("callGetPhoneApi err: " + e.getMessage()));
            }
        });
        thread.start();

    }

    private List<File> getImageFile(List<Uri> imageUris) {
        List<File> files = new ArrayList<>();

        for (Uri uri : imageUris) {
            InputStream in = null;
            try {
                in = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                Log.w(TAG, "getImageFile err: " + e);
            }
            if (in == null) continue;

            // 写临时文件
            String name = FileUtils.getFileName(this, uri);
            File tmp = FileUtils.streamToTempFile(this, in, name, ".png");
            if (tmp != null) files.add(tmp);
        }
        return files;
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        imagesRecyclerView.setLayoutManager(layoutManager);
        imageAdapter = new ImageAdapter(selectedImageUris);
        imagesRecyclerView.setAdapter(imageAdapter);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        selectImagesLauncher.launch(Intent.createChooser(intent, "Select Pictures"));
    }

}
