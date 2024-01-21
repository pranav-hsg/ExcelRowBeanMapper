import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class UserData10Rows {
    static List<UserInfo> userInfos = new ArrayList<>();
    static {
        userInfos.add( new UserInfo(
                "John Cena",
                LocalDate.of(1977, 4, 23),
                1234,
                2.22,
                new BigDecimal("3334444"),
                UserInfo.Gender.MALE,
                UserInfo.UserType.ADMIN,
                LocalDateTime.of(LocalDate.of(2022, 1, 2), LocalTime.of(15, 30, 45)),
                true,
                new UserInfo.Name("Queen", "Silpa", new UserInfo.Status(false, "test note"))
        ));
        userInfos.add( new UserInfo(
                "Alice Wonderland",
                LocalDate.of(1985, 8, 15),
                5678,
                1.78,
                new BigDecimal("4567890"),
                UserInfo.Gender.FEMALE,
                UserInfo.UserType.STANDARD,
                LocalDateTime.of(LocalDate.of(2022, 1, 3), LocalTime.of(15, 30, 45)),
                false,
                new UserInfo.Name("King", "Wonderland", new UserInfo.Status(false, "another note"))
        ));
        userInfos.add(new UserInfo(
                "Bob Builder",
                LocalDate.of(1990, 12, 7),
                4321,
                1.95,
                new BigDecimal("9876543"),
                UserInfo.Gender.MALE,
                UserInfo.UserType.GUEST,
                LocalDateTime.of(LocalDate.of(2022, 1, 4), LocalTime.of(15, 30, 45)),
                true,
                new UserInfo.Name("Mary", "Builder", new UserInfo.Status(false, "yet another note"))
        ));
        userInfos.add(new UserInfo(
                "Eve Explorer",
                LocalDate.of(1982, 5, 19),
                8765,
                1.60,
                new BigDecimal("1122334"),
                UserInfo.Gender.FEMALE,
                UserInfo.UserType.ADMIN,
                LocalDateTime.of(LocalDate.of(2022, 1, 5), LocalTime.of(15, 30, 45)),
                false,
                new UserInfo.Name("Adam", "Explorer", new UserInfo.Status(false, "some note"))
        ));
        userInfos.add(new UserInfo(
                "Charlie Chaplin",
                LocalDate.of(1970, 11, 3),
                2468,
                1.70,
                new BigDecimal("5544332"),
                UserInfo.Gender.MALE,
                UserInfo.UserType.STANDARD,
                LocalDateTime.of(LocalDate.of(2022, 1, 6), LocalTime.of(15, 30, 45)),
                true,
                new UserInfo.Name("Daisy", "Chaplin", new UserInfo.Status(false, "random note"))
        ));
        userInfos.add(new UserInfo(
                "Zack Zebra",
                LocalDate.of(1988, 9, 28),
                1357,
                1.45,
                new BigDecimal("9876123"),
                UserInfo.Gender.FEMALE,
                UserInfo.UserType.GUEST,
                LocalDateTime.of(LocalDate.of(2022, 1, 7), LocalTime.of(15, 30, 45)),
                false,
                new UserInfo.Name("Zoe", "Zebra", new UserInfo.Status(false, "test test note"))
        ));
        userInfos.add(new UserInfo(
                "Olivia Owl",
                LocalDate.of(1975, 2, 14),
                9876,
                1.80,
                new BigDecimal("7890123"),
                UserInfo.Gender.MALE,
                UserInfo.UserType.ADMIN,
                LocalDateTime.of(LocalDate.of(2022, 1, 8), LocalTime.of(15, 30, 45)),
                true,
                new UserInfo.Name("Oscar", "Owl", new UserInfo.Status(false, "another test note"))
        ));
        userInfos.add(new UserInfo(
                "Peter Penguin",
                LocalDate.of(1995, 6, 2),
                5432,
                1.55,
                new BigDecimal("3456789"),
                UserInfo.Gender.FEMALE,
                UserInfo.UserType.STANDARD,
                LocalDateTime.of(LocalDate.of(2022, 1, 9), LocalTime.of(15, 30, 45)),
                false,
                new UserInfo.Name("Pam", "Penguin", new UserInfo.Status(false, "yet another test note"))
        ));
        userInfos.add(new UserInfo(
                "Susan Snake",
                LocalDate.of(1980, 3, 9),
                7890,
                1.68,
                new BigDecimal("8765432"),
                UserInfo.Gender.MALE,
                UserInfo.UserType.GUEST,
                LocalDateTime.of(LocalDate.of(2022, 1, 10), LocalTime.of(15, 30, 45)),
                true,
                new UserInfo.Name("Sam", "Snake", new UserInfo.Status(false, "random test note"))
        ));
        userInfos.add(new UserInfo(
                "Tom Turtle",
                LocalDate.of(2000, 11, 11),
                6543,
                1.88,
                new BigDecimal("2345678"),
                UserInfo.Gender.FEMALE,
                UserInfo.UserType.ADMIN,
                LocalDateTime.of(LocalDate.of(2022, 1, 11), LocalTime.of(15, 30, 45)),
                false,
                new UserInfo.Name("Tina", "Turtle", new UserInfo.Status(false, "test test test note"))
        ));
    }





}
