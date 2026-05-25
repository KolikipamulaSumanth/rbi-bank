package rbibank.web.app.dto;

import rbibank.web.app.entity.RequestStatus;
import rbibank.web.app.entity.RequestType;

public class ServiceRequestDto {
    private RequestType requestType;
    private RequestStatus status;
    private String requestData;
    private String assignedTo;
    private String approvalRemarks;

    @java.lang.SuppressWarnings("all")
    public RequestType getRequestType() {
        return this.requestType;
    }

    @java.lang.SuppressWarnings("all")
    public RequestStatus getStatus() {
        return this.status;
    }

    @java.lang.SuppressWarnings("all")
    public String getRequestData() {
        return this.requestData;
    }

    @java.lang.SuppressWarnings("all")
    public String getAssignedTo() {
        return this.assignedTo;
    }

    @java.lang.SuppressWarnings("all")
    public String getApprovalRemarks() {
        return this.approvalRemarks;
    }

    @java.lang.SuppressWarnings("all")
    public void setRequestType(final RequestType requestType) {
        this.requestType = requestType;
    }

    @java.lang.SuppressWarnings("all")
    public void setStatus(final RequestStatus status) {
        this.status = status;
    }

    @java.lang.SuppressWarnings("all")
    public void setRequestData(final String requestData) {
        this.requestData = requestData;
    }

    @java.lang.SuppressWarnings("all")
    public void setAssignedTo(final String assignedTo) {
        this.assignedTo = assignedTo;
    }

    @java.lang.SuppressWarnings("all")
    public void setApprovalRemarks(final String approvalRemarks) {
        this.approvalRemarks = approvalRemarks;
    }
}
