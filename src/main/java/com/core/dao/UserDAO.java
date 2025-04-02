package com.core.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.core.dto.UserDTO;
import database.SingleDBConnection;

public class UserDAO {

    // 1. ENCAPSULATION: private ile erişim kısıtlanıyor. Doğrudan değiştiremeiyz değerlerini.  Bu yüzden zaten en başta kapsülleme her türlü var.
    // 2. COMPOSITION: "UserDAO'nun bir Connection'ı var" (has-a ilişkisi)
    // Şimdi kritik nokta şu:
    // Eğer connection'ı constructor içinde new ile oluşturuyorsanız veya kendisi sabit bir yerden oluşturuyorsanız -> Bu composition 'PATTERN'
    // Eğer connection'ı dışarıdan alıyorsanız, dinamik bir connection oluşturuyorsanız -> Bu injection 'PATTERN'
    private Connection connection; // COMPOSITION her halükarda var sadece pattern'ler farklı olabilir.

    public UserDAO() {
        this.connection = SingleDBConnection.getInstance().getConnection(); // COMPOSITION PATTERN
    }
    // Yani şu an yukarıda composition pattern kullanıyoruz. Çünkü connection'ı constructor içinde sabit bir yerden alıyoruz.
    
    // Aşağıda ise injection pattern kullanıyoruz. Çünkü connection'ı dışarıdan alıyoruz. AMA HALA COMPOSITION VAR ÇÜNKÜ USERDAO HAS A 'CONNECTION'.
    public UserDAO(Connection connection) {
        // Başkası veriyor
        this.connection = connection;
    }

    public UserDTO create(UserDTO userDto) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userDto.getName());
            statement.setString(2, userDto.getEmail());
            statement.setString(3, userDto.getPassword());
            statement.executeUpdate();

            int affectedRows = statement.getUpdateCount();  
            if (affectedRows > 0) {
                long id = statement.getGeneratedKeys().getLong(1);
                userDto.setId(id);
                return userDto;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    // Burada option kullanmak neye yarar ki ?
    public List<UserDTO> list() {
        String sql = "SELECT * FROM users";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<UserDTO> userDtos = new ArrayList<>();
            while (resultSet.next()) {
                userDtos.add(new UserDTO(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password")));
            }
            return userDtos;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    
}