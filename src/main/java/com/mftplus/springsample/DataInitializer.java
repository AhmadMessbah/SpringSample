package com.mftplus.springsample;

import com.mftplus.springsample.customer.Customer;
import com.mftplus.springsample.customer.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final FakeDataService service;
    private final CustomerService customerService;

    public DataInitializer(FakeDataService service, CustomerService customerService) {
        this.service = service;
        this.customerService = customerService;
    }


    @Override
    public void run(String... args) throws Exception {
        int n = 25;
        for (int i = 1; i <= n; i++) {
            Customer customer = service.createFakeCustomer();
            customerService.saveCustomer(customer);
        }
        log.info("{} Customers created", n);
    }
}
