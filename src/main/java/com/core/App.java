package com.core;

import java.io.IOException;
import java.util.List;

import com.core.dao.UserDAO;
import com.core.dto.UserDTO;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
        UserDAO userDao = new UserDAO();
        userDao.create(new UserDTO(1, "test@test.com", "test", "test", "test", "test", true));
        List<UserDTO> users = userDao.list();
        System.out.println(users);
    }
}
