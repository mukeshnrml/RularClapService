package com.smart.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employer_order")
public class EmployerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;
    private String name;
    private String email;
    private String phno;
    private String skill;
    private Integer amount;
    private String orderStatus;
    private String razorPayOrderId;

    // Getters and setters...
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhno() { return phno; }
    public void setPhno(String phno) { this.phno = phno; }
    public String getSkill() { return skill; }
    public void setSkill(String skill) { this.skill = skill; }
    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    public String getRazorPayOrderId() { return razorPayOrderId; }
    public void setRazorPayOrderId(String razorPayOrderId) { this.razorPayOrderId = razorPayOrderId; }
}
