package com.example.phase2.entity;


import jakarta.persistence.Entity;

@Entity
public class Admin extends User{
    private static Admin instance;

    protected Admin() {
        super();
    }

    public Admin(String firstName, String lastName, String email) {
        super(firstName, lastName, email);
    }

    public static synchronized Admin getInstance() {
        if (instance == null) {
            instance = new Admin("Ali" , "Mousavi" , "AliMousavi1234@gmail.com");
        }
        return instance;
    }
}
