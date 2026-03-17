package com.pax.android.supporthub.sdk.apis.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pax.market.api.sdk.java.base.dto.SdkObject;

import java.util.List;

public class TicketActivityInfo extends SdkObject {
    @Expose @SerializedName("id")
    private Long id;
    @Expose @SerializedName("oldStatus")
    private String oldStatus;
    @Expose @SerializedName("newStatus")
    private String newStatus;
    @Expose @SerializedName("oldPriority")
    private String oldPriority;
    @Expose @SerializedName("newPriority")
    private String newPriority;
    @Expose @SerializedName("oldAssignee")
    private UserInfo oldAssignee;
    @Expose @SerializedName("newAssignee")
    private UserInfo newAssignee;
    @Expose @SerializedName("actionType")
    private int actionType;
    @Expose @SerializedName("content")
    private String content;// 工单标题、编辑工单字段类型、新工单编号、评论内容
    @Expose @SerializedName("userInfo")
    private UserInfo userInfo;
    @Expose @SerializedName("screenshotFileIds")
    private List<String> screenshotFileIds;
    @Expose @SerializedName("createdDate")
    private Long createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getOldPriority() {
        return oldPriority;
    }

    public void setOldPriority(String oldPriority) {
        this.oldPriority = oldPriority;
    }

    public String getNewPriority() {
        return newPriority;
    }

    public void setNewPriority(String newPriority) {
        this.newPriority = newPriority;
    }

    public UserInfo getOldAssignee() {
        return oldAssignee;
    }

    public void setOldAssignee(UserInfo oldAssignee) {
        this.oldAssignee = oldAssignee;
    }

    public UserInfo getNewAssignee() {
        return newAssignee;
    }

    public void setNewAssignee(UserInfo newAssignee) {
        this.newAssignee = newAssignee;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<String> getScreenshotFileIds() {
        return screenshotFileIds;
    }

    public void setScreenshotFileIds(List<String> screenshotFileIds) {
        this.screenshotFileIds = screenshotFileIds;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "TicketActivityInfo{" +
                "id=" + id +
                ", oldStatus='" + oldStatus + '\'' +
                ", newStatus='" + newStatus + '\'' +
                ", oldPriority='" + oldPriority + '\'' +
                ", newPriority='" + newPriority + '\'' +
                ", oldAssignee=" + oldAssignee +
                ", newAssignee=" + newAssignee +
                ", actionType=" + actionType +
                ", content='" + content + '\'' +
                ", userInfo=" + userInfo +
                ", screenshotFileIds=" + screenshotFileIds +
                ", createdDate=" + createdDate +
                '}';
    }
}
