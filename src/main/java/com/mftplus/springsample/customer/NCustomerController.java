package com.mftplus.springsample.customer;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/ncustomers")
@Slf4j
public class NCustomerController {
    private final CustomerService customerService;

    public NCustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String getCustomers(Model model) {
        model.addAttribute("customer", new Customer());
        return "ncustomer";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity addCustomer(@RequestBody Customer customer) {
        try {
            customerService.saveCustomer(customer);
            log.info("Customer added: " + customer);
            return ResponseEntity.ok(
                    Map.of(
                                    "info", "Customer added",
                                    "data", customer.toString())
                            .toString());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    Map.of(
                                    "error", "Customer not added",
                                    "message", e.getMessage())
                            .toString()
            );
        }
    }

//    @PostMapping
//    public String addCustomer(@Valid Customer customer, BindingResult bindingResult, Model model) {
//        if(bindingResult.hasErrors()) {
//            return "ncustomer";
//        }
//
//        customerService.saveCustomer(customer);
//        log.info("Customer added: " + customer);
////        return "redirect:/ncustomers";
//        return "fragment :: customers-table";
//    }
}
