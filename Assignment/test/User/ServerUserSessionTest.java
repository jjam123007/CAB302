package User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ServerUserSessionTest {

    private static String testUsername = "testUser";
    private static String testToken = "testToken";

    @BeforeEach
    void setMockServerUser() {
        ServerUserSession.addSession(testToken, testUsername);
    }

    @Test
    void getUsername() {
        assertEquals(testUsername, ServerUserSession.getUsername(testToken));
    }

    @Test
    void removeSession() {
        ServerUserSession.removeSession(testToken);
        assertFalse(ServerUserSession.isValid(testToken));
    }

    @Test
    void isValid() {
        assertTrue(ServerUserSession.isValid(testToken));
    }
}