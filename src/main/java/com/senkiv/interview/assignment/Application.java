package com.senkiv.interview.assignment;

import java.math.BigDecimal;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.senkiv.interview.assignment.domain.entity.User;
import com.senkiv.interview.assignment.repository.UserRepository;

@SpringBootApplication
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.debug("--Application Started--");
    }

    @Bean
    CommandLineRunner init(UserRepository userRepository) {
        User user1 = new User();
        user1.setUserId("1");
        user1.setUserCat("Regular");
        user1.setKycStatus("Approved");
        user1.setSex(User.Sex.MALE);
        user1.setFirstName("John");
        user1.setLastName("Johnson");
        user1.setStreet("12 str.");
        user1.setCity("Lviv");
        user1.setZip("77777");
        user1.setCountry("Ukraine");
        user1.setEmail("johnson@gmail.com");
        user1.setDob("1990-01-01");
        user1.setMobile("12344321");
        user1.setBalance(new BigDecimal(10000));
        user1.setBalanceCy("UAH");
        user1.setLocale("en_US");

        User user2 = new User();
        user2.setUserId("2");
        user2.setUserCat("Regular");
        user2.setKycStatus("Approved");
        user2.setSex(User.Sex.FEMALE);
        user2.setFirstName("Cate");
        user2.setLastName("Brown");
        user2.setStreet("Amazing str.");
        user2.setCity("Berlin");
        user2.setZip("12312");
        user2.setCountry("Germany");
        user2.setEmail("brown@gmail.com");
        user2.setDob("1978-03-05");
        user2.setMobile("67437826437");
        user2.setBalance(new BigDecimal(2000));
        user2.setBalanceCy("EUR");
        user2.setLocale("de_DE");

        User user3 = new User();
        user3.setUserId("3");
        user3.setUserCat("VIP");
        user3.setKycStatus("Approved");
        user3.setSex(User.Sex.UNKNOWN);
        user3.setFirstName("Bob");
        user3.setLastName("Muller");
        user3.setStreet("Yellow str.");
        user3.setCity("London");
        user3.setZip("34567");
        user3.setCountry("UK");
        user3.setEmail("muller@gmail.com");
        user3.setDob("1989-07-06");
        user3.setMobile("677654322");
        user3.setBalance(new BigDecimal(100));
        user3.setBalanceCy("USD");
        user3.setLocale("en_GB");
        return (evt) -> userRepository.save(Arrays.asList(user1, user2, user3));
    }
}
