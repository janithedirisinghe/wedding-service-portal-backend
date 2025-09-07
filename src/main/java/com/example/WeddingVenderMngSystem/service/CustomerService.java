package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.CustomerDTO;
import com.example.WeddingVenderMngSystem.dto.CustomerStatsDTO;
import com.example.WeddingVenderMngSystem.entity.Customer;
import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.repository.BookingRepository;
import com.example.WeddingVenderMngSystem.repository.CustomerRepository;
import com.example.WeddingVenderMngSystem.repository.FollowerRepository;
import com.example.WeddingVenderMngSystem.repository.ReviewRepository;
import com.example.WeddingVenderMngSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupabaseStorageService supabaseStorageService;

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookingRepository bookingRepository;

        /**
         * Admin: Get all customers with full details
         */
        public List<com.example.WeddingVenderMngSystem.dto.admin.AdminCustomerDetailsDTO> getAllCustomerDetailsForAdmin() {
            List<Customer> customers = customerRepository.findAll();
            return customers.stream().map(customer -> {
                com.example.WeddingVenderMngSystem.dto.admin.AdminCustomerDetailsDTO dto = new com.example.WeddingVenderMngSystem.dto.admin.AdminCustomerDetailsDTO();
                dto.setCustomerId(customer.getCustomerId());
                dto.setIsActive(customer.getIsActive());
                dto.setFirstName(customer.getFirstName());
                dto.setLastName(customer.getLastName());
                dto.setDateOfBirth(customer.getDateOfBirth());
                dto.setPhoneNumber(customer.getPhoneNumber());
                dto.setBio(customer.getBio());
                dto.setAddress(customer.getAddress());
                dto.setCity(customer.getCity());
                dto.setCountry(customer.getCountry());
                dto.setLocation(customer.getLocation());
                dto.setWeddingDate(customer.getWeddingDate());
                dto.setBudget(customer.getBudget());
                dto.setProfileImageUrl(customer.getProfileImageUrl());
                dto.setPreferredVendorTypes(customer.getPreferredVendorTypes());
                if (customer.getUser() != null) {
                    dto.setUserId(customer.getUser().getUserId());
                    dto.setUserName(customer.getUser().getUsername());
                    dto.setUserEmail(customer.getUser().getEmail());
                }
                if (customer.getFollowers() != null) {
                    dto.setFollowerCount(customer.getFollowers().size());
                } else {
                    dto.setFollowerCount(0);
                }
                return dto;
            }).collect(java.util.stream.Collectors.toList());
        }

        /**
         * Admin: Toggle isActive flag for a customer
         */
        public boolean toggleCustomerActiveFlag(Long customerId, boolean isActive) {
            Customer customer = getCustomerById(customerId);
            customer.setIsActive(isActive);
            customerRepository.save(customer);
            return Boolean.TRUE.equals(customer.getIsActive());
        }

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

    public CustomerStatsDTO getCustomerStatsByUserId(Long userId) {
        Customer customer = getCustomerByUserId(userId);
        Long customerId = customer.getCustomerId();

        Long favoritesCount = followerRepository.countActiveFollowingsByCustomerId(customerId);
        Long reviewsCount = reviewRepository.countByCustomer_CustomerId(customerId);
        Long bookingsCount = bookingRepository.countByCustomer_CustomerId(customerId);

        return new CustomerStatsDTO(favoritesCount, reviewsCount, bookingsCount);
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
        
        // Profile Image (only update if provided in DTO)
        if (customerDTO.getProfileImageUrl() != null) {
            customer.setProfileImageUrl(customerDTO.getProfileImageUrl());
        }
        
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
        
        // Profile Image (only update if provided in DTO)
        if (customerDTO.getProfileImageUrl() != null) {
            customer.setProfileImageUrl(customerDTO.getProfileImageUrl());
        }
        
        // Vendor Preferences
        customer.setPreferredVendorTypes(customerDTO.getPreferredVendorTypes());
        
        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDTO(updatedCustomer);
    }

    public CustomerDTO updateCustomerWithImageByUserId(Long userId, CustomerDTO customerDTO, MultipartFile profileImage) {
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
        
        // Handle profile image upload
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String fileName = "customer_profile_" + userId + "_" + System.currentTimeMillis() + "_" + 
                                profileImage.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
                String imageUrl = supabaseStorageService.uploadFile(profileImage, fileName);
                customer.setProfileImageUrl(imageUrl);
                customerDTO.setProfileImageUrl(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload profile image: " + e.getMessage());
            }
        }
        
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
        
        // Profile Image
        dto.setProfileImageUrl(customer.getProfileImageUrl());
        
        // Vendor Preferences
        dto.setPreferredVendorTypes(customer.getPreferredVendorTypes());
        
        if (customer.getUser() != null) {
            dto.setUserName(customer.getUser().getUsername());
            dto.setUserEmail(customer.getUser().getEmail());
        }
        
        return dto;
    }
}
