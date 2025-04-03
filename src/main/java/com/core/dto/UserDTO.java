package com.core.dto;


// (DTO - Data Transfer Object) 
    // Bir katmandan diğerine veri iletmek için kullanılır.
    // Örneğin, bir veritabanından çekilen kullanıcı bilgilerini içeren bir Base oluşturulur.

public class UserDTO {
    
    // Fields
    private long id;
    private String email;
    private String username;
    private String password;
    private String role;
    private String status;
    
    public UserDTO(long id, String email, String username, String password, String role, String status) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
    }   
    
    public UserDTO() {
        this.id = 0;
        this.email = "";
        this.username = "";
        this.password = "";
        this.role = "";
        this.status = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }   

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }   

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }   

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;   
    }
}
