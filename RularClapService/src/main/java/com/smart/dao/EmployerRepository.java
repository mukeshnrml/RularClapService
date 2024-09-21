package com.smart.dao;







import org.springframework.data.jpa.repository.JpaRepository;
import com.smart.entities.Employer;

public interface EmployerRepository extends JpaRepository<Employer, Integer> {
    Employer findByEmail(String email);  // For login purposes
}
