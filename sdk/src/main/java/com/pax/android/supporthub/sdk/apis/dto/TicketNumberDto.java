package com.pax.android.supporthub.sdk.apis.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pax.market.api.sdk.java.base.dto.SdkObject;

public class TicketNumberDto extends SdkObject {
    @Expose @SerializedName("ticketNumber")
    private String ticketNumber;

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    @Override
    public String toString() {
        return "TicketNumberDto{" +
                "ticketNumber='" + ticketNumber + '\'' +
                '}';
    }
}
