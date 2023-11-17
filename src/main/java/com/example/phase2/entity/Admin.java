package com.example.phase2.entity;


import com.example.phase2.entity.enumeration.Role;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
public class Admin extends User{
    private static Admin instance;

    protected Admin() {
        super();
    }

    private Admin(String firstName, String lastName, String email, String password, Role role, Boolean isEnabled) {
        super(firstName, lastName, email, password, role, isEnabled);
    }

    public static synchronized Admin getInstance() {
        if (instance == null) {
            instance = new Admin("Ali" , "Mousavi" , "AliMousavi1234@gmail.com" , "1234qwer" , Role.ROLE_ADMIN , true);
        }
        return instance;
    }
}
