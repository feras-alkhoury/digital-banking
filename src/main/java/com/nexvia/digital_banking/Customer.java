package com.nexvia.digital_banking;

import jakarta.persistence.Column; 
import jakarta.persistence.Entity; 
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Id; 
import jakarta.persistence.Table;

@Entity 
@Table(name = "customers") 
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @Column(unique = true, nullable = false)
    private String email;

}