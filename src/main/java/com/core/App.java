package com.core;

import com.core.router.*;
import java.io.IOException;
import java.sql.SQLException;
import com.core.database.Initilize;

public class App    
{
    public static void main( String[] args ) throws IOException
    {
        try {
            Initilize.dataSet();
            MainRouter register = new MainRouter();
            register.start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
