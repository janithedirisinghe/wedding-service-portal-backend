package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.CustomerDTO;
import com.example.WeddingVenderMngSystem.entity.Customer;
import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.repository.CustomerRepository;
import com.example.WeddingVenderMngSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    public Customer registerCustomer(Long userId, Customer customerDetails) {
        // Check if user exists
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found!");
        }

        User user = userOptional.get();

        // Check if user is already linked to a customer
        if (user.getCustomer() != null) {
            throw new IllegalStateException("Customer already exists for this user!");
        }

        // Assign user to customer
        customerDetails.setUser(user);

        // Save customer details
        return customerRepository.save(customerDetails);
    }

    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));
    }

    public Customer getCustomerByUserId(Long userId) {
        return customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with User ID: " + userId));
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomerDTOById(Long customerId) {
        Customer customer = getCustomerById(customerId);
        return convertToDTO(customer);
    }

    public CustomerDTO getCustomerDTOByUserId(Long userId) {
        Customer customer = getCustomerByUserId(userId);
        return convertToDTO(customer);
    }

    public CustomerDTO updateCustomer(Long customerId, CustomerDTO customerDTO) {
        Customer customer = getCustomerById(customerId);
        
        // Personal Information
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setDateOfBirth(customerDTO.getDateOfBirth());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setBio(customerDTO.getBio());
        
        // Address Information
        customer.setAddress(customerDTO.getAddress());
        customer.setCity(customerDTO.getCity());
        customer.setCountry(customerDTO.getCountry());
        customer.setLocation(customerDTO.getLocation());
        
        // Wedding Information
        customer.setWeddingDate(customerDTO.getWeddingDate());
        customer.setBudget(customerDTO.getBudget());
        
        // Vendor Preferences
        customer.setPreferredVendorTypes(customerDTO.getPreferredVendorTypes());
        
        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDTO(updatedCustomer);
    }

    public CustomerDTO updateCustomerByUserId(Long userId, CustomerDTO customerDTO) {
        Customer customer = getCustomerByUserId(userId);
        
        // Personal Information
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setDateOfBirth(customerDTO.getDateOfBirth());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setBio(customerDTO.getBio());
        
        // Address Information
        customer.setAddress(customerDTO.getAddress());
        customer.setCity(customerDTO.getCity());
        customer.setCountry(customerDTO.getCountry());
        customer.setLocation(customerDTO.getLocation());
        
        // Wedding Information
        customer.setWeddingDate(customerDTO.getWeddingDate());
        customer.setBudget(customerDTO.getBudget());
        
        // Vendor Preferences
        customer.setPreferredVendorTypes(customerDTO.getPreferredVendorTypes());
        
        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDTO(updatedCustomer);
    }

    public void deleteCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException("Customer not found with ID: " + customerId);
        }
        customerRepository.deleteById(customerId);
    }

    public void deleteCustomerByUserId(Long userId) {
        Customer customer = getCustomerByUserId(userId);
        customerRepository.deleteById(customer.getCustomerId());
    }

    public Optional<Customer> findCustomerByUserId(Long userId) {
        return customerRepository.findByUser_UserId(userId);
    }

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(customer.getCustomerId());
        
        // Personal Information
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setDateOfBirth(customer.getDateOfBirth());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setBio(customer.getBio());
        
        // Address Information
        dto.setAddress(customer.getAddress());
        dto.setCity(customer.getCity());
        dto.setCountry(customer.getCountry());
        dto.setLocation(customer.getLocation());
        
        // Wedding Information
        dto.setWeddingDate(customer.getWeddingDate());
        dto.setBudget(customer.getBudget());
        
        // Vendor Preferences
        dto.setPreferredVendorTypes(customer.getPreferredVendorTypes());
        
        if (customer.getUser() != null) {
            dto.setUserName(customer.getUser().getUsername());
            dto.setUserEmail(customer.getUser().getEmail());
        }
        
        return dto;
    }
}
