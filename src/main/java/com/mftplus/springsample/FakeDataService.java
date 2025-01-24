package com.mftplus.springsample;

import com.github.javafaker.Faker;
import com.mftplus.springsample.customer.Customer;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class FakeDataService {

    private final Faker faker1 = new Faker(new Locale("fa"));
    private final Faker faker2 = new Faker();

    public Customer createFakeCustomer() {
        return Customer
                .builder()
                .firstName(faker1.name().firstName())
                .lastName(faker1.name().lastName())
                .email(faker2.internet().emailAddress())
                .phone(faker1.phoneNumber().cellPhone().replaceAll("[\\s\\-().]", ""))
                .build();
    }
}
