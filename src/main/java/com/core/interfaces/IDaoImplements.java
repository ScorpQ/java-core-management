package com.core.interfaces;

import com.core.database.SingleDBConnection;

import java.sql.Connection;

public interface IDaoImplements<T> extends ICrud<T>, IGenericsMethod<T> {

    default Connection iDaoImplementsDatabaseConnection() {
        return SingleDBConnection.getInstance().getConnection();
    }
}