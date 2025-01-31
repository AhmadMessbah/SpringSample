package com.mftplus.springsample.customer;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String listCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(required = false) Boolean fragment,
            Model model
    ) {
        // اعتبارسنجی پارامترها
        if(size <= 0) size = 10;
        if(!Arrays.asList("firstName", "lastName").contains(sortBy)) {
            sortBy = "firstName";
        }

        // ایجاد صفحه‌بندی با مرتب‌سازی
        Sort sort = Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Customer> customers = customerService.getAllCustomers(pageable);

        model.addAttribute("customers", customers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", customers.getTotalPages());

        return fragment != null && fragment ?
                "fragments/customers-table :: customers-table" :
                "customers";
    }

    // افزودن یا ویرایش مشتری
    @PostMapping
    @ResponseBody
    public ResponseEntity<?> saveCustomer(
            @Valid @RequestBody Customer customer,
            BindingResult bindingResult
    ) {
        System.out.println("save");
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Customer savedCustomer = customerService.saveCustomer(customer);
            return ResponseEntity.ok(savedCustomer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // حذف مشتری
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        try {
            System.out.println("delete");
            customerService.deleteCustomer(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

//    @RequestMapping(value="/test", method = RequestMethod.GET)
//    public ModelAndView testView(){
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("customers");
//        modelAndView.addObject("customer", new Customer());
//        return modelAndView;
//    }
}