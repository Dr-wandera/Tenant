package com.Codeloom.Tenant.Models;

import com.Codeloom.Tenant.Enum.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userNumber;
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email required")
    @Email
    private String email;
    @Column(nullable = false)
    @NotBlank(message = "password required")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    @NotBlank(message = "FirstName required")
    private String firstName;
    @Column(nullable = false)
    @NotBlank(message = "Last name required")
    private String lastName;
    @Column(nullable = false, unique = true)
    @Size(min = 10, message = "phone number must be at least 10 characters long")
    @NotBlank(message = "phone number required")

    private  String phoneNumber;
    @NotBlank(message = "Id_number required")
    @Column(nullable = false, unique = true)
    private String idNumber;
    private boolean active;
}
