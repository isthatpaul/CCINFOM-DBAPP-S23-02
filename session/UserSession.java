package session;

import Model.Staff;

/**
 * Simple singleton-style holder for the currently logged-in Staff user.
 * Usage:
 *  - UserSession.set(staff);
 *  - if (UserSession.isLoggedIn()) { ... }
 *  - UserSession.get() to access the Staff record
 *  - UserSession.getRole(), getStaffId() helpers
 *  - UserSession.clear() on logout
 */
public final class UserSession {
    private static Staff current;

    private UserSession() {}

    public static void set(Staff staff) {
        current = staff;
    }

    public static Staff get() {
        return current;
    }

    public static boolean isLoggedIn() {
        return current != null;
    }

    public static String getRole() {
        return current != null ? current.role() : null;
    }

    public static int getStaffId() {
        return current != null ? current.staffID() : 0;
    }

    public static String getUsername() {
        return current != null ? current.username() : null;
    }

    public static void clear() {
        current = null;
    }
}