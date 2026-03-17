package com.pax.android.supporthub.sdk.apis.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadTicketDto {
    @Expose @SerializedName("issueTitle")
    private String issueTitle;
    @Expose @SerializedName("issueDescription")
    private String issueDescription;
    @Expose @SerializedName("contactName")
    private String contactName;
    @Expose @SerializedName("email")
    private String email;
    @Expose @SerializedName("phone")
    private String phone;

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

    @Override
    public String toString() {
        return "UploadTicketDto{" +
                "issueTitle='" + issueTitle + '\'' +
                ", issueDescription='" + issueDescription + '\'' +
                ", contactName='" + contactName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
