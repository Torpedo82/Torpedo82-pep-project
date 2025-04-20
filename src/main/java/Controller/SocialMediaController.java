package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.util.List;

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
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postNewMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::removeMessageByIDHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountIDHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void getAllMessagesByAccountIDHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("account_id"));

        List<Message> accountMessages = messageService.getAllMessagesByAccountID(id);

        ctx.json(mapper.writeValueAsString(accountMessages)).status(200);
    }

    private void updateMessageByIDHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message messageText = mapper.readValue(ctx.body(), Message.class);

        Message updatedMessage = messageService.updateMessageByID(id, messageText.getMessage_text());

        if (updatedMessage != null){
            ctx.json(mapper.writeValueAsString(updatedMessage)).status(200);
        }
        else{
            ctx.status(400);
        }
    }

    private void removeMessageByIDHandler(Context ctx) throws JsonProcessingException{
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();

        Message deletedMessage = messageService.removeMessageByID(id);

        if (deletedMessage != null){
            ctx.json(mapper.writeValueAsString(deletedMessage)).status(200);
        }
        else{
            ctx.status(200);
        }
    }

    private void getMessageByIDHandler(Context ctx) throws JsonProcessingException{
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();

        Message message = messageService.getMessageByID(id);
        
        if (message != null){
            ctx.json(mapper.writeValueAsString(message)).status(200);
        }
        else{
            ctx.status(200);
        }
    }

    private void postAccountHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        //call the service and obtain result
        Account addedAccount = accountService.addAccount(account);

        //return json account or 400 error
        if(addedAccount != null){
            ctx.json(mapper.writeValueAsString(addedAccount)).status(200);
        }
        else{
            ctx.status(400);
        }
    }

    private void postLoginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        Account verifiedAccount = accountService.loginAccount(account);

        //return 200 or 401
        if(verifiedAccount != null){
            ctx.json(mapper.writeValueAsString(verifiedAccount)).status(200);
        }
        else{
            ctx.status(401);
        }
    }

    private void postNewMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        Message addedMessage = messageService.addMessage(message);

        //return 200 or 400
        if(addedMessage != null){
            ctx.json(mapper.writeValueAsString(addedMessage)).status(200);
        }
        else{
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messages = messageService.getAllMessages();

        ctx.json(mapper.writeValueAsString(messages)).status(200);
    }


}