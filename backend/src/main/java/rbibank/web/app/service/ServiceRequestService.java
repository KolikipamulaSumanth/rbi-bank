package rbibank.web.app.service;

import rbibank.web.app.dto.ServiceRequestDto;
import rbibank.web.app.entity.BankServiceRequest;
import rbibank.web.app.entity.RequestStatus;
import rbibank.web.app.entity.RequestType;
import rbibank.web.app.entity.User;
import rbibank.web.app.repository.ServiceRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.util.List;

@Service
@Transactional
public class ServiceRequestService {
    private final ServiceRequestRepository serviceRequestRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public BankServiceRequest create(ServiceRequestDto dto, User customer) {
        if (dto.getRequestType() == null) {
            throw new IllegalArgumentException("Request type is required");
        }
        if (dto.getRequestData() == null || dto.getRequestData().isBlank()) {
            throw new IllegalArgumentException("Request details are required");
        }
        BankServiceRequest request = BankServiceRequest.builder().requestId(generateRequestId(dto.getRequestType())).requestType(dto.getRequestType()).requestData(dto.getRequestData()).status(RequestStatus.SUBMITTED).customer(customer).reviewedBy("Pending").approvalRemarks("Request submitted successfully").build();
        return serviceRequestRepository.save(request);
    }

    public List<BankServiceRequest> getCustomerRequests(User customer) {
        return serviceRequestRepository.findAllByCustomerUidOrderByCreatedAtDesc(customer.getUid());
    }

    public List<BankServiceRequest> getAllRequests() {
        return serviceRequestRepository.findAllByOrderByCreatedAtDesc();
    }

    public BankServiceRequest updateStatus(String id, ServiceRequestDto dto, User employee) {
        BankServiceRequest request = serviceRequestRepository.findById(id).orElseThrow();
        if (dto.getStatus() != null) {
            request.setStatus(dto.getStatus());
        }
        if (dto.getApprovalRemarks() != null) {
            request.setApprovalRemarks(dto.getApprovalRemarks());
        }
        if (dto.getAssignedTo() != null) {
            request.setAssignedTo(dto.getAssignedTo());
        }
        request.setReviewedBy(employee.getFirstname() + " " + employee.getLastname());
        return serviceRequestRepository.save(request);
    }

    private String generateRequestId(RequestType type) {
        String prefix = switch (type) {
            case CHEQUE_BOOK -> "CHQ";
            case FIXED_DEPOSIT -> "FD";
            case LOAN -> "LN";
            case SUPPORT_TICKET -> "SUP";
            case BLOCK_DEBIT_CARD -> "CARD";
        };
        String requestId;
        do {
            requestId = prefix + (100000 + secureRandom.nextInt(900000));
        } while (serviceRequestRepository.existsByRequestId(requestId));
        return requestId;
    }

    @java.lang.SuppressWarnings("all")
    public ServiceRequestService(final ServiceRequestRepository serviceRequestRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
    }
}
