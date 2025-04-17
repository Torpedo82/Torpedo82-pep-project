package Service;

import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;

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
