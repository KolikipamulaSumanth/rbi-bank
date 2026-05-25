package rbibank.web.app.repository;

import rbibank.web.app.entity.BankServiceRequest;
import rbibank.web.app.entity.RequestStatus;
import rbibank.web.app.entity.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRequestRepository extends JpaRepository<BankServiceRequest, String> {
    boolean existsByRequestId(String requestId);

    List<BankServiceRequest> findAllByCustomerUidOrderByCreatedAtDesc(String uid);

    List<BankServiceRequest> findAllByOrderByCreatedAtDesc();

    List<BankServiceRequest> findAllByRequestTypeAndStatusOrderByCreatedAtDesc(RequestType requestType, RequestStatus status);
}
