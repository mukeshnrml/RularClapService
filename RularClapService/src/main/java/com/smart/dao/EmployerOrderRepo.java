package com.smart.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smart.entities.EmployerOrder;

public interface EmployerOrderRepo extends JpaRepository<EmployerOrder, Long> {
    EmployerOrder findByRazorPayOrderId(String razorPayOrderId);
}
