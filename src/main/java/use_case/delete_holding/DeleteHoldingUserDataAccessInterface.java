package use_case.delete_holding;

import entity.User;

public interface DeleteHoldingUserDataAccessInterface {
    User get(String username);
    String getCurrentUser();
    void save(User user);
}