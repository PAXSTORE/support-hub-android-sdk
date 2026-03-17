package com.pax.android.supporthub.sdk.apis.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pax.market.api.sdk.java.base.dto.SdkObject;

import java.util.List;

public class SupportHubTicketDetailInfo extends SdkObject {
    @Expose @SerializedName("id")
    private Long id;
    @Expose @SerializedName("serialNo")
    private String serialNo;
    @Expose @SerializedName("source")
    private String source;
    @Expose @SerializedName("createdDate")
    private Long createdDate;
    @Expose @SerializedName("updatedDate")
    private Long updatedDate;
    @Expose @SerializedName("status")
    private String status;
    @Expose @SerializedName("ticketNumber")
    private String ticketNumber;
    @Expose @SerializedName("issueTitle")
    private String issueTitle;
    @Expose @SerializedName("issueDescription")
    private String issueDescription;
    @Expose @SerializedName("screenshot1")
    private String screenshot1;
    @Expose @SerializedName("screenshot2")
    private String screenshot2;
    @Expose @SerializedName("screenshot3")
    private String screenshot3;
    @Expose @SerializedName("screenshot4")
    private String screenshot4;
    @Expose @SerializedName("screenshot5")
    private String screenshot5;
    @Expose @SerializedName("contactName")
    private String contactName;
    @Expose @SerializedName("email")
    private String email;
    @Expose @SerializedName("phone")
    private String phone;
    @Expose @SerializedName("activityInfoList")
    private List<TicketActivityInfo> activityInfoList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getIssueTitle() {
        return issueTitle;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getScreenshot1() {
        return screenshot1;
    }

    public void setScreenshot1(String screenshot1) {
        this.screenshot1 = screenshot1;
    }

    public String getScreenshot2() {
        return screenshot2;
    }

    public void setScreenshot2(String screenshot2) {
        this.screenshot2 = screenshot2;
    }

    public String getScreenshot3() {
        return screenshot3;
    }

    public void setScreenshot3(String screenshot3) {
        this.screenshot3 = screenshot3;
    }

    public String getScreenshot4() {
        return screenshot4;
    }

    public void setScreenshot4(String screenshot4) {
        this.screenshot4 = screenshot4;
    }

    public String getScreenshot5() {
        return screenshot5;
    }

    public void setScreenshot5(String screenshot5) {
        this.screenshot5 = screenshot5;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<TicketActivityInfo> getActivityInfoList() {
        return activityInfoList;
    }

    public void setActivityInfoList(List<TicketActivityInfo> activityInfoList) {
        this.activityInfoList = activityInfoList;
    }

    @Override
    public String toString() {
        return "SupportHubTicketDetailInfo{" +
                "id=" + id +
                ", serialNo='" + serialNo + '\'' +
                ", source='" + source + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", status='" + status + '\'' +
                ", ticketNumber='" + ticketNumber + '\'' +
                ", issueTitle='" + issueTitle + '\'' +
                ", issueDescription='" + issueDescription + '\'' +
                ", screenshot1='" + screenshot1 + '\'' +
                ", screenshot2='" + screenshot2 + '\'' +
                ", screenshot3='" + screenshot3 + '\'' +
                ", screenshot4='" + screenshot4 + '\'' +
                ", screenshot5='" + screenshot5 + '\'' +
                ", contactName='" + contactName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", activityInfoList=" + activityInfoList +
                '}';
    }
}
