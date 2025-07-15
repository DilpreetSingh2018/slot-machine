package com.example.Slot_Machine.model;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String username;
    @OneToOne(cascade = CascadeType.ALL)
    private Wallet wallet;
    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    // Constructors, getters, setters
}
