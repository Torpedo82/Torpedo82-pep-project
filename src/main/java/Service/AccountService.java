package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    //for mocking AccountDAO if needed for tests
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    //adding an account
    public Account addAccount(Account account){
        //check if account details are valid
        //username NOT blank/null, password >= 4 long and does not already exist

        if (accountDAO.getAccountByUsername(account.getUsername()) != null){
            return null;
        }
        
        if (account.getUsername().length() > 1){
            if (account.getPassword().length() >= 4){
                return accountDAO.insertAccount(account);
            }
        }

        //if no insertion possible return null to indicate error
        return null;

    }
}
