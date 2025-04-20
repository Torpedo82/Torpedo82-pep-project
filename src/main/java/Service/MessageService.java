package Service;

import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;
import java.util.List;


public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();

        //accountDAO should ONLY be used if needing to verify account exists
        accountDAO = new AccountDAO();
    }

    // for optionally mocking the MessageDAO
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        //accountDAO should ONLY be used if needing to verify account exists
        this.accountDAO = accountDAO;
    }

    //get all messages by account_id
    public List<Message> getAllMessagesByAccountID(int accountID){
        List<Message> accountMessages = messageDAO.getAllMessagesByAccountID(accountID);

        return accountMessages;
    }

    //update message by id and if update succeeds then return updated Message
    public Message updateMessageByID(int id, String messageText){
        int rowsUpdated = (messageText.length() > 0 && messageText.length() < 255) ? 
            messageDAO.updateMessageByID(id, messageText) : 0;

        // if (messageText.length() > 0 && messageText.length() < 255){
        //     rowsUpdated = messageDAO.updateMessageByID(id, messageText);
        // }

        if (rowsUpdated == 1){
            return messageDAO.getMessageByID(id);
        }

        //return null if no message updated OR issue retrieving record
        return null;
    }

    //delete message by ID while also returning item if it exists
    public Message removeMessageByID(int id){
        Message deletedMessage = messageDAO.getMessageByID(id);

        if (deletedMessage != null){
            messageDAO.removeMessageByID(id);
            return deletedMessage;
        }

        //if no such message return null
        return null;
    }

    //Retrieve message by its ID
    public Message getMessageByID(int id){
        return messageDAO.getMessageByID(id);
    }

    //retrieving all messages
    public List<Message> getAllMessages() {
        List<Message> messages = messageDAO.getAllMessages();

        return messages;
    }

    //adding a message
    public Message addMessage(Message message) {
        //check if constraints are met. message_text is NOT blank and under 255 length, posted_by is a real account
        if (message.getMessage_text().length() > 0 && message.getMessage_text().length() < 255){
            if (accountDAO.getAccountByAccountID(message.getPosted_by()) != null){
                return messageDAO.insertMessage(message);
            }
        }

        //if no insertion possible then return null
        return null;
    }

}
