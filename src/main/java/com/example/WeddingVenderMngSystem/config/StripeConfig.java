package com.example.WeddingVenderMngSystem.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class StripeConfig {

    @Value("${stripe.secret.key:sk_test_your_secret_key_here}")
    private String secretKey;

    @PostConstruct
    public void initSecretKey() {
        Stripe.apiKey = secretKey;
    }
}
