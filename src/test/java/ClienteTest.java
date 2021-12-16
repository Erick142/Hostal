import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void guardadEnHistorial() {
        List<String> historialtest=new ArrayList<>();
        List<String> historialExpected=new ArrayList<>();

        historialExpected.add("se a añadido un cliente de nombre: pedrito y rut: 77777777-7.");

        historialtest.add(new Cliente("pedrito","77777777-7").guardadEnHistorial());
        assertEquals(historialExpected,historialtest);
    }
}