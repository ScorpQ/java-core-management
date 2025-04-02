package com.core;

import com.sun.net.httpserver.HttpServer;

import database.SingleDBConnection;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        SingleDBConnection.getInstance();
    }
}
