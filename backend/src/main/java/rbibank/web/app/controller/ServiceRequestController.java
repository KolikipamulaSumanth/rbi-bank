package rbibank.web.app.controller;

import rbibank.web.app.dto.ServiceRequestDto;
import rbibank.web.app.entity.BankServiceRequest;
import rbibank.web.app.entity.User;
import rbibank.web.app.service.ServiceRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/requests")
public class ServiceRequestController {
    private final ServiceRequestService serviceRequestService;

    @PostMapping
    public ResponseEntity<BankServiceRequest> create(@RequestBody ServiceRequestDto dto, Authentication authentication) {
        return ResponseEntity.ok(serviceRequestService.create(dto, (User) authentication.getPrincipal()));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<BankServiceRequest>> mine(Authentication authentication) {
        return ResponseEntity.ok(serviceRequestService.getCustomerRequests((User) authentication.getPrincipal()));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority(\'EMPLOYEE\',\'ADMIN\')")
    public ResponseEntity<List<BankServiceRequest>> all() {
        return ResponseEntity.ok(serviceRequestService.getAllRequests());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(\'EMPLOYEE\',\'ADMIN\')")
    public ResponseEntity<BankServiceRequest> update(@PathVariable String id, @RequestBody ServiceRequestDto dto, Authentication authentication) {
        return ResponseEntity.ok(serviceRequestService.updateStatus(id, dto, (User) authentication.getPrincipal()));
    }

    @java.lang.SuppressWarnings("all")
    public ServiceRequestController(final ServiceRequestService serviceRequestService) {
        this.serviceRequestService = serviceRequestService;
    }
}
