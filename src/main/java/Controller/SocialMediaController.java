package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    //class variables that allow using the services
    AccountService accountService;
    MessageService messageService;

    //constructor to initialize the services
    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountIDHandler);
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postNewMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIDHandler);
        app.delete("/messages/{message_id}", this::removeMessageByIDHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    //Handler for getting a Message record in JSON format by message_id
    private void getMessageByIDHandler(Context ctx) throws JsonProcessingException{
        int id = Integer.parseInt(ctx.pathParam("message_id"));

        ObjectMapper mapper = new ObjectMapper();

        Message message = messageService.getMessageByID(id);
        
        //return Message JSON or blank body, both status 200
        if (message != null){
            ctx.json(mapper.writeValueAsString(message)).status(200);
        }
        else{
            ctx.status(200);
        }
    }

    //handler for obtaining list of all Message records in JSON format
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messages = messageService.getAllMessages();

        //always return either blank or populated list in JSON with status code 200
        ctx.json(mapper.writeValueAsString(messages)).status(200);
    }

    //Handler for getting a list all Message record made by a specific account in JSON format
    private void getAllMessagesByAccountIDHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("account_id"));

        List<Message> accountMessages = messageService.getAllMessagesByAccountID(id);

        //always return either blank or populated list with status code 200
        ctx.json(mapper.writeValueAsString(accountMessages)).status(200);
    }

    //Handler for creating a new Account record and returning the new account in JSON format
    private void postAccountHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        Account addedAccount = accountService.addAccount(account);

        //return JSON account with status 200 or 400 error
        if(addedAccount != null){
            ctx.json(mapper.writeValueAsString(addedAccount)).status(200);
        }
        else{
            ctx.status(400);
        }
    }

    //Handler for verifying login and if verified returning Account record in JSON format
    private void postLoginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        Account verifiedAccount = accountService.loginAccount(account);

        //return Account record as JSON with status code 200 or status 401
        if(verifiedAccount != null){
            ctx.json(mapper.writeValueAsString(verifiedAccount)).status(200);
        }
        else{
            ctx.status(401);
        }
    }

    //Handler for adding new Message record to DB and returning said record in JSON format
    private void postNewMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        Message addedMessage = messageService.addMessage(message);

        //return 200 with Message record as JSON or 400
        if(addedMessage != null){
            ctx.json(mapper.writeValueAsString(addedMessage)).status(200);
        }
        else{
            ctx.status(400);
        }
    }

    //Handler for updating Message record by using message_id and returning it in JSON format
    private void updateMessageByIDHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message messageText = mapper.readValue(ctx.body(), Message.class);

        Message updatedMessage = messageService.updateMessageByID(id, messageText.getMessage_text());

        //return either Message record JSON with status 200 or status 400
        if (updatedMessage != null){
            ctx.json(mapper.writeValueAsString(updatedMessage)).status(200);
        }
        else{
            ctx.status(400);
        }
    }

    //Handler for removing a Message record by its message_id and returning the entry in JSON format
    private void removeMessageByIDHandler(Context ctx) throws JsonProcessingException{
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();

        Message deletedMessage = messageService.removeMessageByID(id);

        //return either Message record in JSON with status 200 or blank body with 200
        if (deletedMessage != null){
            ctx.json(mapper.writeValueAsString(deletedMessage)).status(200);
        }
        else{
            ctx.status(200);
        }
    }

}