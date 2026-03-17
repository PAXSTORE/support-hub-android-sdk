package com.pax.android.supporthub.sdk.apis.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pax.market.api.sdk.java.base.dto.SdkObject;

import java.util.ArrayList;
import java.util.List;

public class PageInfoDto<T> extends SdkObject {
    private static final long serialVersionUID = 1L;

    @Expose @SerializedName("list")
    private List<T> list = new ArrayList<>();
    @Expose @SerializedName("totalCount")
    private long totalCount;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }


    @Override
    public String toString() {

        if(list == null) {
            return "PageInfoDto{" +
                    "list size=" + "null" +
                    ", totalCount=" + totalCount +
                    '}';
        }
        return "PageInfoDto{" +
                "list size=" + list.size() +
                ", totalCount=" + totalCount +
                '}';
    }
}
