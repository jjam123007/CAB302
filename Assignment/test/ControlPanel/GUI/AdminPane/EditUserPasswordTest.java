package ControlPanel.GUI.AdminPane;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class EditUserPasswordTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
    @Test
    public void canChangePasswords() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        String password ="12345678";
        String passwordReenter ="12345678";
        assertTrue(EditUserPassword.canChangePasswords(password,passwordReenter));
    }

}