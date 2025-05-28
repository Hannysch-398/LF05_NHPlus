package de.hitec.nhplus.utils;

import de.hitec.nhplus.model.User;

public class Session {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : null;
    }


    public static boolean isAdmin() {
       /*return "admin".equals(getCurrentUsername());
        User user = getCurrentUser();
        return user.getAdmin();*/
        return currentUser.getRole().equals("admin");
    }

    public static void clear() {
        currentUser = null;
    }
}
