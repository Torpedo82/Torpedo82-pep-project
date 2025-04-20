package DAO;

import Util.ConnectionUtil;
import Model.Message;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class MessageDAO {

    //Retrieve a message by its ID
    public Message getMessageByID(int messageID){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM message WHERE message_id = ?;";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, messageID);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                return new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"), 
                    rs.getString("message_text"), 
                    rs.getLong("time_posted_epoch")
                );
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        //if no message found should return null
        return null;
    }

    //Retrieve all messages
    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();


        //can use statement as no parameters are needed from user so no risk of sql injection
        try{
            String sql = "SELECT * FROM message;";
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                messages.add(new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"), 
                    rs.getString("message_text"), 
                    rs.getLong("time_posted_epoch")
                ));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return messages;
    }


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
