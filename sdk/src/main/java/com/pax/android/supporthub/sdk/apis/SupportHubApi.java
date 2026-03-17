package com.pax.android.supporthub.sdk.apis;

import android.content.Context;
import android.util.Log;


import com.pax.android.supporthub.sdk.apis.constant.CommonApiConstants;
import com.pax.android.supporthub.sdk.apis.constant.CommonConstants;
import com.pax.android.supporthub.sdk.apis.dto.HistoryTicketDto;
import com.pax.android.supporthub.sdk.apis.dto.PageInfoDto;
import com.pax.android.supporthub.sdk.apis.dto.SupportHubTicketDetailInfo;
import com.pax.android.supporthub.sdk.apis.dto.SupportPhoneDto;
import com.pax.android.supporthub.sdk.apis.dto.TicketNumberDto;
import com.pax.android.supporthub.sdk.apis.dto.UploadTicketDto;
import com.pax.android.supporthub.sdk.apis.utils.FileUtil;
import com.pax.market.api.sdk.java.base.api.BaseApi;
import com.pax.market.api.sdk.java.base.constant.Constants;
import com.pax.market.api.sdk.java.base.request.SdkRequest;
import com.pax.market.api.sdk.java.base.util.JsonUtils;

import java.io.File;
import java.net.URLConnection;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SupportHubApi extends BaseApi {
    protected static String getSupportPhoneUrl = "v1/3rdApps/supportHub/number";

    protected static String uploadTicketUrl = "v1/3rdApps/supportHub/ticket/sync";

    protected static String getHistoryTicketListUrl = "v1/3rdApps/supportHub/tickets";

    protected static String getTicketDetailUrl = "v1/3rdApps/supportHub/tickets/{ticketId}/detail";


    public SupportHubApi(String baseUrl, String appKey, String appSecret, String terminalSN) {
        super(baseUrl, appKey, appSecret, terminalSN);
    }

    /**
     * 上报工单
     *
     * @param uploadTicketDto ticket dto
     * @param imageFile       uploaded picture -- Supported image formats for compression -- jpg、jpeg、png; At most five pieces
     * @return {@link TicketNumberDto}
     */
    public TicketNumberDto uploadTicket(UploadTicketDto uploadTicketDto, List<File> imageFile, Context context, boolean needCompress) {
        SdkRequest request = new SdkRequest(uploadTicketUrl);
        request.addHeader(CommonApiConstants.HEADER_Language, String.valueOf(Locale.getDefault()));
        request.setRequestMethod(SdkRequest.RequestMethod.MULTIPART_POST);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (imageFile == null || imageFile. isEmpty()) {
            builder.addFormDataPart("photos", "");
        } else {
            TicketNumberDto dto = new TicketNumberDto();
            if (imageFile.size() > CommonConstants.MIX_PICTURE_SIZE) {
                dto.setBusinessCode(CommonConstants.MORE_PICTURE_SIZE_ERR_CODE);
                dto.setMessage(CommonConstants.MORE_PICTURE_SIZE_ERR_MSG);
                return dto;
            }
            List<File> compressImages = imageFile;
            if (needCompress) {
                for (File file : imageFile) {
                    if (!FileUtil.filterPictureType(file.getName())) {
                        dto.setBusinessCode(CommonConstants.FILTER_PICTURE_ERR_CODE);
                        dto.setMessage(CommonConstants.FILTER_PICTURE_ERR_MSG);
                        return dto;
                    }
                }
                compressImages = FileUtil.compressImagesIfNeeded(context, imageFile);
            }
            for (File image : compressImages) {
                MediaType type = MediaType.parse(
                        URLConnection.guessContentTypeFromName(image.getName()));

                if (type == null) type = MediaType.parse("image/png");
                builder.addFormDataPart(
                        "photos",
                        image.getName(),
                        RequestBody.create(image, type)
                );
            }
        }
        RequestBody ticketRequestPart =
                RequestBody.create(
                        JsonUtils.toJson(uploadTicketDto),
                        MediaType.parse("application/json; charset=utf-8")
                );
        builder.addFormDataPart("ticketRequestJson", null, ticketRequestPart);
        RequestBody requestBody = builder.build();
        request.setMultipartRequestBody(requestBody);
        request.setRequestBody(JsonUtils.toJson(uploadTicketDto));
        request.addHeader(Constants.REQ_HEADER_SN, getTerminalSN());
        return JsonUtils.fromJson(call(request), TicketNumberDto.class);
    }

    /**
     * 获取support电话
     *
     * @return {@link SupportPhoneDto}
     */
    public SupportPhoneDto getSupportPhone() {
        Log.w("TAG", "api thread: " + Thread.currentThread().getName());
        SdkRequest request = new SdkRequest(getSupportPhoneUrl);
        request.addHeader(CommonApiConstants.HEADER_Language, String.valueOf(Locale.getDefault()));
        request.setRequestMethod(SdkRequest.RequestMethod.GET);
        request.addHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
        request.addHeader(Constants.REQ_HEADER_SN, getTerminalSN());
        return JsonUtils.fromJson(call(request), SupportPhoneDto.class);
    }


    /**
     * 获取历史工单记录列表
     *
     * @param search      搜索内容
     * @param status      工单状态("O","I","H","C")
     * @param startTime   开始时间
     * @param endTime     截止时间
     * @param startOffset 开始位置
     * @param limit       查询数目
     * @return {@link PageInfoDto <HistoryTicketDto>}
     */
    public PageInfoDto<HistoryTicketDto> getHistoryTicketList(String search, String status, Long startTime,
                                                              Long endTime, int startOffset, int limit, String order) {
        SdkRequest request = new SdkRequest(getHistoryTicketListUrl);
        request.setRequestMethod(SdkRequest.RequestMethod.GET);
        request.addHeader(CommonApiConstants.HEADER_Language, String.valueOf(Locale.getDefault()));
        request.addHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
        request.addHeader(Constants.REQ_HEADER_SN, getTerminalSN());
        request.addRequestParam(CommonApiConstants.PARAM_SEARCH, search == null ? "" : search);
        request.addRequestParam(CommonApiConstants.PARAM_STATUS, status == null ? "" : status);
        request.addRequestParam(CommonApiConstants.PARAM_START_TIME, startTime == null ? "" : String.valueOf(startTime));
        request.addRequestParam(CommonApiConstants.PARAM_END_TIME, endTime == null ? "" : String.valueOf(endTime));
        request.addRequestParam(CommonApiConstants.PARAM_START_OFFSET, String.valueOf(startOffset));
        request.addRequestParam(CommonApiConstants.PARAM_LIMIT, String.valueOf(limit));
        request.addRequestParam(CommonApiConstants.PARAM_ORDER, order);
        return JsonUtils.fromJson(call(request), PageInfoDto.class, HistoryTicketDto.class);

    }

    public void setBaseUrl(String baseUrl) {
        super.getDefaultClient().setBaseUrl(baseUrl);
    }

    public SupportHubTicketDetailInfo getTicketDetail(Long ticketId) {
    String requestUrl = getTicketDetailUrl.replace("{ticketId}", String.valueOf(ticketId));
    SdkRequest request = new SdkRequest(requestUrl);
    request.addHeader(CommonApiConstants.HEADER_Language, String.valueOf(Locale.getDefault()));
    request.setRequestMethod(SdkRequest.RequestMethod.GET);
    request.addHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
    request.addHeader(Constants.REQ_HEADER_SN, getTerminalSN());
    request.addHeader(CommonApiConstants.HEADER_ALGORITHM, "hmac");
    return JsonUtils.fromJson(call(request), SupportHubTicketDetailInfo.class);
}





}
