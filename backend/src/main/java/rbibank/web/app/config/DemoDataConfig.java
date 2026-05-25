package rbibank.web.app.config;

import rbibank.web.app.dto.AccountDto;
import rbibank.web.app.dto.UserDto;
import rbibank.web.app.entity.Card;
import rbibank.web.app.entity.User;
import rbibank.web.app.repository.CardRepository;
import rbibank.web.app.repository.UserRepository;
import rbibank.web.app.service.AccountService;
import rbibank.web.app.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class DemoDataConfig implements ApplicationRunner {
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final UserService userService;
    private final AccountService accountService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User customer = ensureUser("customer", "USER", "Aarav", "Sharma", "ABCDE1234F", 9876543210L);
        User nisha = ensureUser("nisha", "USER", "Nisha", "Iyer", "PQRSX2190A", 9812345670L);
        User rahul = ensureUser("rahul", "USER", "Rahul", "Nair", "LMNOP9087C", 9900011112L);
        User priya = ensureUser("priya", "USER", "Priya", "Menon", "GHIKL4321M", 9898989898L);
        ensureUser("employee", "EMPLOYEE", "Meera", "Rao", "EMPPR1234K", 9123456780L);
        ensureUser("cashier", "EMPLOYEE", "Kabir", "Das", "EMPPR6789L", 9123456781L);
        ensureUser("manager", "ADMIN", "Vikram", "Manager", "ADMPR1234K", 9000000001L);
        ensureUser("admin", "ADMIN", "Ananya", "Branchhead", "ADMPR5678N", 9000000002L);
        ensureAccount(customer, "SAVINGS", 25000.0);
        ensureAccount(nisha, "SAVINGS", 142000.0);
        ensureAccount(rahul, "CURRENT", 6400.0);
        ensureAccount(priya, "SAVINGS", 78000.0);
        ensureCard(customer, 4213768450129012L);
        ensureCard(nisha, 4213768450124821L);
        ensureCard(rahul, 4213768450127436L);
        ensureCard(priya, 4213768450125724L);
    }

    private User ensureUser(String username, String role, String firstname, String lastname, String pan, long phone) {
        User existing = userRepository.findByUsernameIgnoreCase(username);
        if (existing != null) {
            return existing;
        }
        UserDto dto = new UserDto();
        dto.setUsername(username);
        dto.setPassword("Password123");
        dto.setFirstname(firstname);
        dto.setLastname(lastname);
        dto.setTel(phone);
        dto.setEmail(username + "@rbibank.local");
        dto.setAddress("12 Banking Street, Mumbai, Maharashtra");
        dto.setPanNumber(pan);
        dto.setAadhaarNumber("123456789012");
        dto.setRole(role);
        dto.setKycStatus("VERIFIED");
        return userService.createStaff(dto, role);
    }

    private void ensureAccount(User user, String accountType, double openingBalance) throws Exception {
        if (accountService.getUserAccounts(user.getUid()).isEmpty()) {
            accountService.createAccount(new AccountDto("INR", "Indian Rupee", 'R', accountType, openingBalance), user);
        }
    }

    private void ensureCard(User user, long cardNumber) {
        if (cardRepository.findByOwnerUid(user.getUid()).isPresent()) {
            return;
        }
        Card card = Card.builder().cardHolder(user.getFirstname() + " " + user.getLastname()).cardNumber(cardNumber).balance(0.0).exp(LocalDateTime.now().plusYears(5)).cvv("123").pin("0000").billingAddress(user.getAddress()).owner(user).build();
        cardRepository.save(card);
    }

    @java.lang.SuppressWarnings("all")
    public DemoDataConfig(final UserRepository userRepository, final CardRepository cardRepository, final UserService userService, final AccountService accountService) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.userService = userService;
        this.accountService = accountService;
    }
}
