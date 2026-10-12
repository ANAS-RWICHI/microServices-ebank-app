package org.example.ebankservice.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.*;
import org.example.ebankservice.Model.Customer;

import java.util.Date;
@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class BankAccount {
    @Id
    private String id;
    private double balance;
    private Date createdAt;
    private long customerId;
    private String type;
    @Transient
    private Customer customer;
}
