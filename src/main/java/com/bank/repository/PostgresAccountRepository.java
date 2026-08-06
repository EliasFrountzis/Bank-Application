package com.bank.repository;

import com.bank.database.DatabaseConnection;
import com.bank.model.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresAccountRepository implements AccountRepository {


    @Override
    public Account save(Account account) {

        String sql =
                "INSERT INTO accounts(owner, balance) VALUES (?, ?) RETURNING id";


        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql)) {


            statement.setString(
                    1,
                    account.getOwner()
            );


            statement.setDouble(
                    2,
                    account.getBalance()
            );


            ResultSet result =
                    statement.executeQuery();


            if(result.next()) {

                int id =
                        result.getInt("id");


                return new Account(
                        id,
                        account.getOwner(),
                        account.getBalance()
                );

            }


        } catch(SQLException e) {

            throw new RuntimeException(e);

        }


        return null;

    }



    @Override
    public List<Account> findAll() {


        List<Account> accounts =
                new ArrayList<>();


        String sql =
                "SELECT * FROM accounts";


        try(Connection connection = DatabaseConnection.getConnection();
            Statement statement =
                    connection.createStatement()) {


            ResultSet result =
                    statement.executeQuery(sql);


            while(result.next()) {


                accounts.add(
                        new Account(
                                result.getInt("id"),
                                result.getString("owner"),
                                result.getDouble("balance")
                        )
                );

            }


        } catch(SQLException e) {

            throw new RuntimeException(e);

        }


        return accounts;

    }



    @Override
    public Account findById(int id) {


        String sql =
                "SELECT * FROM accounts WHERE id = ?";


        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql)) {


            statement.setInt(
                    1,
                    id
            );


            ResultSet result =
                    statement.executeQuery();


            if(result.next()) {


                return new Account(
                        result.getInt("id"),
                        result.getString("owner"),
                        result.getDouble("balance")
                );

            }


        } catch(SQLException e) {

            throw new RuntimeException(e);

        }


        return null;

    }

}