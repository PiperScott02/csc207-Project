package use_case.login;

import entity.User;

/**
 * Output Data for the Login Use Case.
 */
public class LoginOutputData {

    private final String username;
    private final User user;
    private final boolean useCaseFailed;

    public LoginOutputData(String username, User user, boolean useCaseFailed) {
        this.username = username;
        this.user = user;
        this.useCaseFailed = useCaseFailed;
    }

    public String getUsername() {
        return username;
    }

    public User getUser() {
        return user;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}