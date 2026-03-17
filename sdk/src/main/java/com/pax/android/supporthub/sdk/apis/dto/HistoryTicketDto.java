package com.pax.android.supporthub.sdk.apis.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pax.market.api.sdk.java.base.dto.SdkObject;

public class HistoryTicketDto extends SdkObject {
    @Expose
    @SerializedName("id")
    private Long id;
    @Expose
    @SerializedName("ticketNumber")
    private String ticketNumber;
    @Expose
    @SerializedName("ticketTitle")
    private String ticketTitle;
    @Expose
    @SerializedName("ticketDescription")
    private String ticketDescription;
    @Expose
    @SerializedName("createdDate")
    private Long createdDate;
    @Expose
    @SerializedName("status")
    private String status; // status:("O","I","H","C")

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getTicketTitle() {
        return ticketTitle;
    }

    public void setTicketTitle(String ticketTitle) {
        this.ticketTitle = ticketTitle;
    }

    public String getTicketDescription() {
        return ticketDescription;
    }

    public void setTicketDescription(String ticketDescription) {
        this.ticketDescription = ticketDescription;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "HistoryTicketDto{" +
                "id=" + id +
                ", ticketNumber='" + ticketNumber + '\'' +
                ", ticketTitle='" + ticketTitle + '\'' +
                ", ticketDescription='" + ticketDescription + '\'' +
                ", createdDate=" + createdDate +
                ", status='" + status + '\'' +
                '}';
    }
}
