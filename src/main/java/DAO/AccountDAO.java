package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    //get account by account_id
    public Account getAccountByAccountID(int accountID){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM account WHERE account_id = ?;";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, accountID);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                return new Account(rs.getInt("account_id"), 
                    rs.getString("username"), 
                    rs.getString("password"));
            }
        }catch(SQLException e){
            //in a feature complete api would use logger but for current purposes printing to console will suffice
            System.out.println(e.getMessage());
        }

        //if no such entry
        return null;
    }

    /*
        have login be it's own method as in the future it might require more sophisticated methods for security purposes
        having it seperate will ensure that modifying this method will not impact any other part of the code
        will use prepared statement for protection against sql injection
    */
    public Account getAccountByLogin(Account account){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                return new Account(
                    rs.getInt("account_id"), 
                    rs.getString("username"), 
                    rs.getString("password"));
            }
        }catch(SQLException e){
            //in a feature complete api would use logger but for current purposes printing to console will suffice
            System.out.println(e.getMessage());
        }

        //return null if no such account found
        return null;
    }

    //Obtain Account record by using username, used for validation purposes. will use preparedstatement to protect against sql injection
    public Account getAccountByUsername(String username){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM account WHERE username = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"), 
                    rs.getString("password")
                    );

            }
        }catch(SQLException e){
            //in a feature complete api would use logger but for current purposes printing to console will suffice
            System.out.println(e.getMessage());
        }

        //return null if no such account found
        return null;
    }

    //insert new Account record by using Account object, use prepared statement to protect from sql injection
    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();

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
                return new Account(
                    generatedAccountKey, 
                    account.getUsername(), 
                    account.getPassword()
                    );
            }

        }catch(SQLException e){
            //in a feature complete api would use logger but for current purposes printing to console will suffice
            System.out.println(e.getMessage());
        }

        //if operation fails no object will be returned and that will indicate operation failure
        return null;
    }

}
