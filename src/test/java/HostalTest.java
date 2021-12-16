import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
class HostalTest {

    String url = "jdbc:mysql://localhost/hostal";
    String usuario = "root";
    String contraseña = "";

    @BeforeEach
    void setUp() {
        new Cliente("Leopoldo","44444444-4");
        Hostal.truncarBD();
    }


    @Test
    void truncartest() {

        try (Connection con = DriverManager.getConnection(url, usuario, contraseña)) {
            Statement e = con.createStatement();
            ResultSet rs = e.executeQuery("select * from cliente");
            assertFalse(rs.next());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}