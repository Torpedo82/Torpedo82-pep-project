package DAO;

import Util.ConnectionUtil;
import Model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDAO {
    public Account getAccountByUsername(String username){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM account WHERE username = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                                 rs.getString("username"), 
                                 rs.getString("password"));
                return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }



    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();

        //try block as cannot start with resources
        try{
            String sql = "INSERT INTO account (username, password) VALUES(?, ?);";
            
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ps.executeUpdate();

            //get generated key to make complete object
            ResultSet pkRS = ps.getGeneratedKeys();
           
            if (pkRS.next()){
                int generatedAccountKey = (int) pkRS.getLong(1);
                return new Account(generatedAccountKey, account.getUsername(), account.getPassword());
            }

        }catch(SQLException e){
            //in a feature complete api would use logger but for current purposes printing to console will suffice
            System.out.println(e.getMessage());
        }
        //if operation fails no object will be returned and that will indicate operation failure
        return null;
    }

}
