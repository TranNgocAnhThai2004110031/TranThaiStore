package com.tranthai.tranthaistore.dto;

import lombok.Data;

@Data
public class UserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    // private String username;
    private String password;
    private String confirmPassword;
    
    public UserDTO() {
    }

    public UserDTO(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
    
}
