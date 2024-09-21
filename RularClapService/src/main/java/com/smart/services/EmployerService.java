package com.smart.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import com.smart.entities.Contact;
import com.smart.entities.Employer;
import com.smart.dao.ContactRepository;
import com.smart.dao.EmployerRepository;

@Service
public class EmployerService {

    @Autowired
    private EmployerRepository employerRepository;

    public Employer registerEmployer(Employer employer) {
        return employerRepository.save(employer);
    }

    public Employer loginEmployer(String email, String password) {
        Employer employer = employerRepository.findByEmail(email);
        if (employer != null && employer.getPassword().equals(password)) {
            return employer;  // Login success
        }
        return null;  // Login failure
    }
    
    

    @Autowired
    private ContactRepository contactRepository;

    // Other methods for employer registration and login...

    // Method to fetch all contacts
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();  // Fetch all contacts from the database
    }

	
}
