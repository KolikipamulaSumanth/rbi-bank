package rbibank.web.app.service;

import rbibank.web.app.dto.UserDto;
import rbibank.web.app.entity.KycStatus;
import rbibank.web.app.entity.User;
import rbibank.web.app.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rbibank.web.app.util.RandomUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public User registerUser(UserDto userDto) {
        User user = mapToUser(userDto);
        user.setRoles(List.of("USER"));
        return userRepository.save(user);
    }

    public User createCustomer(UserDto userDto) {
        User user = mapToUser(userDto);
        user.setRoles(List.of("USER"));
        return userRepository.save(user);
    }

    public User getUser(String uid) {
        return userRepository.findById(uid).orElseThrow();
    }

    public User createStaff(UserDto userDto, String role) {
        User user = mapToUser(userDto);
        user.setRoles(List.of(role));
        user.setKycStatus(KycStatus.VERIFIED);
        return userRepository.save(user);
    }

    public List<User> searchUsers(String query) {
        return userRepository.findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCaseOrPanNumberContainingIgnoreCase(query, query, query);
    }

    public User updateKyc(String uid, KycStatus status) {
        User user = userRepository.findById(uid).orElseThrow();
        user.setKycStatus(status);
        return userRepository.save(user);
    }

    public User resetPassword(String uid, String newPassword) {
        User user = userRepository.findById(uid).orElseThrow();
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public void changePassword(User user, String currentPassword, String newPassword) {
        if (currentPassword == null || currentPassword.isBlank()) {
            throw new IllegalArgumentException("Current password is required");
        }
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("New password must be at least 8 characters long");
        }
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public Map<String, Object> authenticateUser(UserDto userDto) {
        Map<String, Object> authObject = new HashMap<String, Object>();
        User user = (User) userDetailsService.loadUserByUsername(userDto.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        authObject.put("token", "Bearer ".concat(jwtService.generateToken(userDto.getUsername())));
        authObject.put("user", user);
        return authObject;
    }

    private User mapToUser(UserDto dto) {
        return User.builder().lastname(dto.getLastname()).firstname(dto.getFirstname()).username(dto.getUsername()).password(passwordEncoder.encode(dto.getPassword())).dob(dto.getDob()).roles(List.of(resolveRole(dto.getRole()))).tag("io_" + dto.getUsername()).customerId(generateCustomerId()).tel(dto.getTel()).email(dto.getEmail()).address(dto.getAddress()).panNumber(dto.getPanNumber()).aadhaarNumber(dto.getAadhaarNumber()).kycStatus(resolveKycStatus(dto.getKycStatus())).build();
    }

    private String resolveRole(String role) {
        if ("ADMIN".equalsIgnoreCase(role)) {
            return "ADMIN";
        }
        if ("EMPLOYEE".equalsIgnoreCase(role)) {
            return "EMPLOYEE";
        }
        return "USER";
    }

    private KycStatus resolveKycStatus(String status) {
        if (status == null || status.isBlank()) {
            return KycStatus.PENDING;
        }
        return KycStatus.valueOf(status.toUpperCase());
    }

    private String generateCustomerId() {
        String customerId;
        RandomUtil randomUtil = new RandomUtil();
        do {
            customerId = "RBI" + randomUtil.generateRandom(7);
        } while (userRepository.existsByCustomerId(customerId));
        return customerId;
    }

    @java.lang.SuppressWarnings("all")
    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder, final UserDetailsService userDetailsService, final AuthenticationManager authenticationManager, final JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }
}
