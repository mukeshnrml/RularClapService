package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smart.service.MailService;

@Controller
public class MailController {


	 @Autowired
	    private MailService mailService;

	 	    @PostMapping("/send")
	    public String sendMessage(@RequestParam String name,
	                              @RequestParam String email,
	                              @RequestParam String subject,
	                              @RequestParam String message,
	                              Model model) {
	        String to = "sanjukumar894971@gmail.com"; // Replace with your email address
	        String fullMessage = String.format("Name: %s\nEmail: %s\nMessage: %s", name, email, message);

	        mailService.sendEmail(to, subject, fullMessage);

	        model.addAttribute("message", "Your message has been sent successfully!");
	        return "contact"; // Redirect or show the same page with a success message
	    }

	
}
