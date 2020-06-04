package UserManagement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class DataSecurityTest {

    @Test
    void hash() throws NoSuchAlgorithmException {
        String hash = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824";
        String string = "hello";
        String testHash = DataSecurity.hash(string);
        assertEquals(hash, testHash);
    }

}