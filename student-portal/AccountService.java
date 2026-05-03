package com.lbu.academy.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.Map;

@Service
public class AccountService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${finance.service.url:http://localhost:8081}")
    private String financeUrl;

    @Value("${library.service.url:http://localhost:8082}")
    private String libraryUrl;

    public void createFinanceAccount(String studentId) {
        try {
            restTemplate.postForObject(
                    financeUrl + "/accounts/",
                    Map.of("studentId", studentId),
                    Object.class
            );
        } catch (Exception e) {
            System.out.println("Finance account creation failed: " + e.getMessage());
        }
    }

    public void createInvoice(String studentId, double amount) {
        try {
            Map<String, Object> account = Map.of("studentId", studentId);
            Map<String, Object> body = Map.of(
                    "amount", amount,
                    "dueDate", LocalDate.now().plusYears(1).toString(),
                    "type", "TUITION_FEES",
                    "account", account
            );
            restTemplate.postForObject(
                    financeUrl + "/invoices/",
                    body,
                    Object.class
            );
        } catch (Exception e) {
            System.out.println("Invoice creation failed: " + e.getMessage());
        }
    }

    public boolean hasOutstandingBalance(String studentId) {
        try {
            Map response = restTemplate.getForObject(
                    financeUrl + "/accounts/student/" + studentId,
                    Map.class
            );
            return response != null && Boolean.TRUE.equals(response.get("hasOutstandingBalance"));
        } catch (Exception e) {
            System.out.println("Finance check failed: " + e.getMessage());
            return false;
        }
    }

    public void createLibraryAccount(String studentId) {
        try {
            restTemplate.postForObject(
                    libraryUrl + "/api/register",
                    Map.of("studentId", studentId),
                    Object.class
            );
        } catch (Exception e) {
            System.out.println("Library account creation failed: " + e.getMessage());
        }
    }
}