package com.bank.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {


    private static final Properties properties = new Properties();


    static {

        try {

            InputStream input =
                    DatabaseConnection.class
                            .getClassLoader()
                            .getResourceAsStream(
                                    "application.properties"
                            );


            if(input == null){
                throw new RuntimeException(
                        "application.properties not found"
                );
            }


            properties.load(input);


        } catch(Exception e){

            throw new RuntimeException(
                    "Could not load database configuration",
                    e
            );

        }

    }



    public static Connection getConnection()
            throws SQLException {


        return DriverManager.getConnection(

                properties.getProperty("db.url"),

                properties.getProperty("db.username"),

                properties.getProperty("db.password")

        );

    }

}