import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Builder
public class UserInfo {
    private String name;
    private LocalDate birthDate;
    private Integer phone;
    private Double heightInMeters;
    private BigDecimal amountBalance;
    private Gender gender;
    private UserType userType;
    private LocalDateTime registrationTime;
    private boolean isActive;
    private Name motherInfo;
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    @EqualsAndHashCode
    public static class Name {
        private String firstName;
        private String lastName;
        private Status status;
    }
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    @EqualsAndHashCode
    public static class Status {
        private boolean healthy;
        private String note;
    }
    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    public enum UserType {
        STANDARD,
        ADMIN,
        GUEST
    }
}
