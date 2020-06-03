package ControlPanel.GUI.BillboardsPane;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class EditBillboardsTest {

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }
    @Test
    public void color(){
        Color color;
        color =new Color(255,0,0);
        assertEquals("#FF0000",EditBillboards.convertColorToHexadecimal(color));

    }


}