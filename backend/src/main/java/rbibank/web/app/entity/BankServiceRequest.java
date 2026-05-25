package rbibank.web.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
public class BankServiceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false, unique = true)
    private String requestId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType requestType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;
    @Column(length = 5000)
    private String requestData;
    private String reviewedBy;
    private String assignedTo;
    private String approvalRemarks;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private User customer;

    @java.lang.SuppressWarnings("all")
    public static class BankServiceRequestBuilder {
        @java.lang.SuppressWarnings("all")
        private String id;
        @java.lang.SuppressWarnings("all")
        private String requestId;
        @java.lang.SuppressWarnings("all")
        private RequestType requestType;
        @java.lang.SuppressWarnings("all")
        private RequestStatus status;
        @java.lang.SuppressWarnings("all")
        private String requestData;
        @java.lang.SuppressWarnings("all")
        private String reviewedBy;
        @java.lang.SuppressWarnings("all")
        private String assignedTo;
        @java.lang.SuppressWarnings("all")
        private String approvalRemarks;
        @java.lang.SuppressWarnings("all")
        private LocalDateTime createdAt;
        @java.lang.SuppressWarnings("all")
        private LocalDateTime updatedAt;
        @java.lang.SuppressWarnings("all")
        private User customer;

        @java.lang.SuppressWarnings("all")
        BankServiceRequestBuilder() {
        }

        @java.lang.SuppressWarnings("all")
        public BankServiceRequest.BankServiceRequestBuilder id(final String id) {
            this.id = id;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public BankServiceRequest.BankServiceRequestBuilder requestId(final String requestId) {
            this.requestId = requestId;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public BankServiceRequest.BankServiceRequestBuilder requestType(final RequestType requestType) {
            this.requestType = requestType;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public BankServiceRequest.BankServiceRequestBuilder status(final RequestStatus status) {
            this.status = status;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public BankServiceRequest.BankServiceRequestBuilder requestData(final String requestData) {
            this.requestData = requestData;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public BankServiceRequest.BankServiceRequestBuilder reviewedBy(final String reviewedBy) {
            this.reviewedBy = reviewedBy;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public BankServiceRequest.BankServiceRequestBuilder assignedTo(final String assignedTo) {
            this.assignedTo = assignedTo;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public BankServiceRequest.BankServiceRequestBuilder approvalRemarks(final String approvalRemarks) {
            this.approvalRemarks = approvalRemarks;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public BankServiceRequest.BankServiceRequestBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public BankServiceRequest.BankServiceRequestBuilder updatedAt(final LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        @JsonIgnore
        @java.lang.SuppressWarnings("all")
        public BankServiceRequest.BankServiceRequestBuilder customer(final User customer) {
            this.customer = customer;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public BankServiceRequest build() {
            return new BankServiceRequest(this.id, this.requestId, this.requestType, this.status, this.requestData, this.reviewedBy, this.assignedTo, this.approvalRemarks, this.createdAt, this.updatedAt, this.customer);
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public java.lang.String toString() {
            return "BankServiceRequest.BankServiceRequestBuilder(id=" + this.id + ", requestId=" + this.requestId + ", requestType=" + this.requestType + ", status=" + this.status + ", requestData=" + this.requestData + ", reviewedBy=" + this.reviewedBy + ", assignedTo=" + this.assignedTo + ", approvalRemarks=" + this.approvalRemarks + ", createdAt=" + this.createdAt + ", updatedAt=" + this.updatedAt + ", customer=" + this.customer + ")";
        }
    }

    @java.lang.SuppressWarnings("all")
    public static BankServiceRequest.BankServiceRequestBuilder builder() {
        return new BankServiceRequest.BankServiceRequestBuilder();
    }

    @java.lang.SuppressWarnings("all")
    public String getId() {
        return this.id;
    }

    @java.lang.SuppressWarnings("all")
    public String getRequestId() {
        return this.requestId;
    }

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
    public String getReviewedBy() {
        return this.reviewedBy;
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
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @java.lang.SuppressWarnings("all")
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @java.lang.SuppressWarnings("all")
    public User getCustomer() {
        return this.customer;
    }

    @java.lang.SuppressWarnings("all")
    public void setId(final String id) {
        this.id = id;
    }

    @java.lang.SuppressWarnings("all")
    public void setRequestId(final String requestId) {
        this.requestId = requestId;
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
    public void setReviewedBy(final String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    @java.lang.SuppressWarnings("all")
    public void setAssignedTo(final String assignedTo) {
        this.assignedTo = assignedTo;
    }

    @java.lang.SuppressWarnings("all")
    public void setApprovalRemarks(final String approvalRemarks) {
        this.approvalRemarks = approvalRemarks;
    }

    @java.lang.SuppressWarnings("all")
    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @java.lang.SuppressWarnings("all")
    public void setUpdatedAt(final LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonIgnore
    @java.lang.SuppressWarnings("all")
    public void setCustomer(final User customer) {
        this.customer = customer;
    }

    @java.lang.SuppressWarnings("all")
    public BankServiceRequest(final String id, final String requestId, final RequestType requestType, final RequestStatus status, final String requestData, final String reviewedBy, final String assignedTo, final String approvalRemarks, final LocalDateTime createdAt, final LocalDateTime updatedAt, final User customer) {
        this.id = id;
        this.requestId = requestId;
        this.requestType = requestType;
        this.status = status;
        this.requestData = requestData;
        this.reviewedBy = reviewedBy;
        this.assignedTo = assignedTo;
        this.approvalRemarks = approvalRemarks;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.customer = customer;
    }

    @java.lang.SuppressWarnings("all")
    public BankServiceRequest() {
    }
}
