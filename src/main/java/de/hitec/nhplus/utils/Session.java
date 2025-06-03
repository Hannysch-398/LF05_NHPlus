package de.hitec.nhplus.utils;

import de.hitec.nhplus.model.User;

/**
 * The Session class manages the current user session.
 * It provides static access to the logged-in {@link User}, allowing other classes
 * to query the current user, their role, or log them out.
 */

public class Session {

    /** The currently logged-in user. */

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

        return currentUser.getRole().equals("admin");
    }

    public static void clear() {
        currentUser = null;
    }
}
