package com.smart.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.entities.EmployerOrder;
import com.smart.service.PaymentService;

@Controller
@CrossOrigin
public class PaymentController {

    @Autowired
    private PaymentService service;

    @GetMapping("/payment.html")
    public String showPage(Model model) {
        model.addAttribute("employerOrder", new EmployerOrder());
        return "payment"; // Thymeleaf template name
    }

    @PostMapping(value = "/create-order", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<EmployerOrder> createOrder(@RequestBody EmployerOrder employerOrder) throws Exception {
        EmployerOrder orderResp = service.createOrder(employerOrder);
        return new ResponseEntity<>(orderResp, HttpStatus.OK);
    }

    @PostMapping("/payment-callback")
    public String handlePaymentCallback(@RequestParam Map<String, String> respPayload, Model model) {
        EmployerOrder updatedOrder = service.verifyPaymentAndUpdateOrderStatus(respPayload);
        model.addAttribute("order", updatedOrder);
        return "success";
    }
}
