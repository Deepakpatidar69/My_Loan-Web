package com.web.my.loan.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @NotBlank(message = "first name can't be Null")
    @Column(name = "FirstName", length = 50, nullable = false, unique = false, updatable = true)
    private String firstName;

    @NotBlank(message = "last name can't be Null")
    @Column(name = "LastName", length = 50, nullable = false, unique = false, updatable = true)
    private String lastName;

    @Size(min = 10, max = 10, message = "mobile number must be 10 digit!!")
    @Column(name = "MobileNumber", nullable = false, unique = true, updatable = true)
    private String mobileNumber;

    @Column(name = "Gender", nullable = false, length = 7, unique = false, updatable = true)
    private String gender;

    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    @Column(name = "Username", length = 60, nullable = false, unique = true, updatable = true)
    private String userName;

    @Length(max = 60)
    @Column(name = "Password", nullable = false, unique = false, updatable = true)
    private String password;

    @Lob
    @Column(name = "UserImage", nullable = true, unique = false, updatable = true, columnDefinition = "LONGBLOB")
    private byte[] image;

    @Transient
    private MultipartFile img;

    @Column(name = "Role", length = 10, unique = false, updatable = true)
    private String userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Address.class, orphanRemoval = true)
    @JsonIgnoreProperties("user")
    private List<Address> address = new ArrayList<>();

    @OneToMany(mappedBy = "endUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("endUser")
    private List<ApprovedLoan> approveList = new ArrayList<>();

    @OneToMany(mappedBy = "endUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("endUser")
    private List<BorrowedLoan> borrowList = new ArrayList<>();

}
