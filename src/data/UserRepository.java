package data;

import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static final List<User> USERS = new ArrayList<>();
    private static User currentUser = null;

    public static void add(User user) {
        USERS.add(user);
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}