package com.bank.repository;

import com.bank.database.DatabaseConnection;
import com.bank.model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresTransactionRepository implements TransactionRepository {


    @Override
    public Transaction save(Transaction transaction) {

        String sql =
                """
                INSERT INTO transactions
                (from_account, to_account, amount, timestamp)
                VALUES (?, ?, ?, ?)
                RETURNING id
                """;


        try(Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)) {


            statement.setInt(
                    1,
                    transaction.getFromAccount()
            );


            statement.setInt(
                    2,
                    transaction.getToAccount()
            );


            statement.setDouble(
                    3,
                    transaction.getAmount()
            );


            statement.setTimestamp(
        4,
        Timestamp.valueOf(
                transaction.getTimestamp()
                        .replace("T", " ")
        )
);

            ResultSet result =
                    statement.executeQuery();


            if(result.next()) {

                return new Transaction(
                        result.getInt("id"),
                        transaction.getFromAccount(),
                        transaction.getToAccount(),
                        transaction.getAmount()
                );

            }


        } catch(SQLException e) {

            throw new RuntimeException(e);

        }


        return null;

    }



    @Override
    public List<Transaction> findAll() {

        List<Transaction> transactions =
                new ArrayList<>();


        String sql =
                "SELECT * FROM transactions";


        try(Connection connection =
                DatabaseConnection.getConnection();

            Statement statement =
                connection.createStatement()) {


            ResultSet result =
                    statement.executeQuery(sql);


            while(result.next()) {


                transactions.add(
                        new Transaction(
                                result.getInt("id"),
                                result.getInt("from_account"),
                                result.getInt("to_account"),
                                result.getDouble("amount")
                        )
                );

            }


        } catch(SQLException e) {

            throw new RuntimeException(e);

        }


        return transactions;

    }



    @Override
    public List<Transaction> findByAccountId(int accountId) {


        List<Transaction> transactions =
                new ArrayList<>();


        String sql =
                """
                SELECT * FROM transactions
                WHERE from_account = ?
                OR to_account = ?
                """;


        try(Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)) {


            statement.setInt(1, accountId);
            statement.setInt(2, accountId);


            ResultSet result =
                    statement.executeQuery();


            while(result.next()) {


                transactions.add(
                        new Transaction(
                                result.getInt("id"),
                                result.getInt("from_account"),
                                result.getInt("to_account"),
                                result.getDouble("amount")
                        )
                );

            }


        } catch(SQLException e) {

            throw new RuntimeException(e);

        }


        return transactions;

    }

}