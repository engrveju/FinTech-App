package com.bank.FinTech.models;

import com.bank.FinTech.enums.Role;
import com.bank.FinTech.enums.UserStatus;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_tbl")
public class Users extends BaseClass implements UserDetails {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(length = 11, unique = true, nullable = false)
    @Size(min = 11, max = 11, message = "BVN must be 11 characters")
    private String bvn;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 11)
    @Size(min = 11, max = 11, message = "Phone number must be 11 digits")
    private String phoneNumber;

    @Column(nullable = false)
    @Size(min = 8, message = "Password must not be less than 8 characters")
    private String password;

   // @Column(length = 6)
    @Size(min = 6, message = "Transaction pin must be 6 characters")
    private String transactionPin;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.INACTIVE;

    @OneToOne(mappedBy = "users")
    private Wallet wallet;

    @OneToMany(mappedBy = "users")
    private List<Transaction> transaction;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean locked ;
    private boolean enabled ;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}

