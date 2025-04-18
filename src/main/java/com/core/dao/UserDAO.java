package com.core.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.core.dto.UserDTO;
import com.core.database.SingleDBConnection;
import com.core.interfaces.IDaoImplements;
import com.core.interfaces.ILogin;

public class UserDAO implements IDaoImplements<UserDTO>,  ILogin<UserDTO> {

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
        // Başkası veriyor bu yüzden injection pattern kullanıyoruz.
        this.connection = connection;
    }

    ///////////////////////////CRUD Methods/////////////////////////////////
    // Optional yapısı kullanılacak sebebini öğrenerek...
    // tablo olmadığı için cathc'e düşerse tablo tekrar oluşturulacak 
    public UserDTO create(UserDTO userDto) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userDto.getUsername());
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
                userDtos.add(new UserDTO(resultSet.getLong("id"), resultSet.getString("email"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("role"), resultSet.getBoolean("visible")));
            }
            return userDtos;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Optional yapısı kullanılacak sebebini öğrenerek...
    // tablo olmadığı için cathc'e düşerse tablo tekrar oluşturulacak 
    public UserDTO findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        return selectSingle(sql, email);
    }

    public UserDTO findById(long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return selectSingle(sql, id);
    }

    // Optional yapısı kullanılacak sebebini öğrenerek...
    // tablo olmadığı için cathc'e düşerse tablo tekrar oluşturulacak 
    public UserDTO update(UserDTO userDto) {
        UserDTO checkUser = findById(userDto.getId());
        if(checkUser != null){
            String sql = "UPDATE users SET username = ?, email = ?, password = ?, role = ?, status = ? WHERE id = ?";
            try(PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1, userDto.getUsername());
                statement.setString(2, userDto.getEmail());
                statement.setString(3, userDto.getPassword());
                statement.setString(4, userDto.getRole());
                statement.setLong(6, userDto.getId());
                statement.executeUpdate();

                return statement.getUpdateCount() > 0 ? userDto : null;
            } catch (SQLException e){

            }
        }
        return null;
    }

    // Optional yapısı kullanılacak sebebini öğrenerek...
    public UserDTO delete(long id) {
            UserDTO userDto = findById(id);
            if(userDto != null){
                String sql = "UPDATE users SET visible = ? WHERE id = ?";
            return selectSingle(sql, false, id);
        }
        return null;
    }
    

    ///////////////////////////Login Methods/////////////////////////////////
    public UserDTO loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        return selectSingle(sql, email, password);
    }


    ///////////////////////////Common Methods/////////////////////////////////

    @Override
    public UserDTO mapToUserDTO(ResultSet resultSet) throws SQLException {
        return new UserDTO(
            resultSet.getLong("id"), 
            resultSet.getString("email"), 
            resultSet.getString("username"), 
            resultSet.getString("password"), 
            resultSet.getString("role"), 
            resultSet.getBoolean("visible")
        );
    }

    @Override
    public UserDTO selectSingle(String sql, Object... params) {
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            for(int i = 0; i < params.length; i++){
                statement.setObject(i+1, params[i]);
            }
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return mapToUserDTO(resultSet);
            }
        } catch (SQLException e){
            e.printStackTrace(); // Hatayı görmek için
        }
        return null;
    }
}