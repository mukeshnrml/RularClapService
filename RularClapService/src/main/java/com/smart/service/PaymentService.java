package com.smart.service;

import java.util.Map;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.smart.dao.EmployerOrderRepo;
import com.smart.entities.EmployerOrder;

@Service
public class PaymentService {

    @Autowired
    private EmployerOrderRepo orderRepo;

    private RazorpayClient client;

    @Value("${razorpay.key}")
    private String keyId;

    @Value("${razorpay.secret}")
    private String keySecret;

    @PostConstruct
    public void init() throws RazorpayException {
        this.client = new RazorpayClient(keyId, keySecret);
    }

    public EmployerOrder createOrder(EmployerOrder employerOrder) throws RazorpayException {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", employerOrder.getAmount() * 100); // amount in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", employerOrder.getEmail());

        Order razorPayOrder = client.Orders.create(orderRequest);
        employerOrder.setRazorPayOrderId(razorPayOrder.get("id"));
        employerOrder.setOrderStatus(razorPayOrder.get("status"));

        return orderRepo.save(employerOrder);
    }

    public EmployerOrder verifyPaymentAndUpdateOrderStatus(Map<String, String> respPayload) {
        EmployerOrder employerOrder = null;
        try {
            String razorpayOrderId = respPayload.get("razorpay_order_id");
            String razorpayPaymentId = respPayload.get("razorpay_payment_id");
            String razorpaySignature = respPayload.get("razorpay_signature");

            boolean isValidSignature = verifySignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);

            if (isValidSignature) {
                employerOrder = orderRepo.findByRazorPayOrderId(razorpayOrderId);
                if (employerOrder != null) {
                    employerOrder.setOrderStatus("CONFIRMED");
                    orderRepo.save(employerOrder);
                }
            } else {
                System.out.println("Invalid signature");
            }
        } catch (RazorpayException e) {
            e.printStackTrace();
        }
        return employerOrder;
    }

    private boolean verifySignature(String orderId, String paymentId, String signature) throws RazorpayException {
        String generatedSignature = HmacSHA256(orderId + "|" + paymentId, keySecret);
        return generatedSignature.equals(signature);
    }

    private String HmacSHA256(String data, String key) throws RazorpayException {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.getBytes());
            return new String(org.apache.commons.codec.binary.Hex.encodeHex(hash));
        } catch (Exception e) {
            throw new RazorpayException("Failed to calculate signature.", e);
        }
    }
}
