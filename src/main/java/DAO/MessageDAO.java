package DAO;

import Util.ConnectionUtil;
import Model.Message;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MessageDAO {


    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();

        //try block as cannot start with resources
        try{
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?);";

            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            ps.executeUpdate();
            
            //get generated key
            ResultSet pkRS = ps.getGeneratedKeys();

            if (pkRS.next()){
                int generatedAccountKey = (int) pkRS.getLong(1);
                return new Message(generatedAccountKey, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        //if message was not inserted then return null
        return null;
    }
}
