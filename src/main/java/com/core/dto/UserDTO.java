package com.core.dto;


// (DTO - Data Transfer Object) 
    // Bir katmandan diğerine veri iletmek için kullanılır.
    // Örneğin, bir veritabanından çekilen kullanıcı bilgilerini içeren bir Base oluşturulur.

public class UserDTO {
    
    // Fields
    private long id;
    private String email;
    private String password;
    private String role;
    private String status;
    
    public UserDTO(long id, String email, String password, String role, String status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }   
    
    public UserDTO() {
        this.id = 0;
        this.email = "";
        this.password = "";
        this.role = "";
        this.status = "";
    }

}
