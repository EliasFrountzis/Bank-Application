package com.bank.repository;

import com.bank.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;


public class PostgresTransferRepository
        implements TransferRepository {


    @Override
    public void transfer(
            int fromAccount,
            int toAccount,
            double amount
    ) {


        String withdrawSql =
                """
                UPDATE accounts
                SET balance = balance - ?
                WHERE id = ?
                """;


        String depositSql =
                """
                UPDATE accounts
                SET balance = balance + ?
                WHERE id = ?
                """;


        String transactionSql =
                """
                INSERT INTO transactions
                (from_account, to_account, amount, timestamp)
                VALUES (?, ?, ?, ?)
                """;


        try(Connection connection =
                DatabaseConnection.getConnection()) {


            connection.setAutoCommit(false);


            try {


                PreparedStatement withdraw =
                        connection.prepareStatement(withdrawSql);

                withdraw.setDouble(1, amount);
                withdraw.setInt(2, fromAccount);

                withdraw.executeUpdate();



                PreparedStatement deposit =
                        connection.prepareStatement(depositSql);

                deposit.setDouble(1, amount);
                deposit.setInt(2, toAccount);

                deposit.executeUpdate();



                PreparedStatement transaction =
                        connection.prepareStatement(transactionSql);

                transaction.setInt(1, fromAccount);
                transaction.setInt(2, toAccount);
                transaction.setDouble(3, amount);

                transaction.setTimestamp(
                        4,
                        Timestamp.valueOf(
                                LocalDateTime.now()
                        )
                );

                transaction.executeUpdate();



                connection.commit();


            } catch(Exception e){

                connection.rollback();

                throw e;

            }


        } catch(Exception e){

            throw new RuntimeException(
                    "Transfer failed",
                    e
            );

        }


    }


}