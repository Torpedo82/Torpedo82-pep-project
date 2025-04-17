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

    //logging into account
    public Account loginAccount(Account account){
        Account verificatedAccount = accountDAO.getAccountByLogin(account);

        if (verificatedAccount != null){
            return verificatedAccount;
        }

        //return null if credentials match no account
        return null;
    }

    //adding an account
    public Account addAccount(Account account){
        //check if account details are valid
        //username NOT blank/null, password >= 4 long and does not already exist
        
        if (account.getUsername().length() > 1 && account.getPassword().length() >= 4){
            if (accountDAO.getAccountByUsername(account.getUsername()) == null){
                return accountDAO.insertAccount(account);
            }
        }

        //if no insertion possible return null to indicate error
        return null;

    }
}
