package com.whatsnext.backend.dto;

import com.whatsnext.backend.entity.WatchStatus;

public class WatchStatusUpdateRequest {
    private WatchStatus status;

    public WatchStatusUpdateRequest() {
    }

    public WatchStatus getStatus() {
        return status;
    }

    public void setStatus(WatchStatus status) {
        this.status = status;
    }
}
