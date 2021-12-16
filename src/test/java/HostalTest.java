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
    }

    @Test
    void truncarBD() {
        try (Connection con = DriverManager.getConnection(url, usuario, contraseña)) {
            Statement e = con.createStatement();
            //borrar y rehacer las tablas.
            e.executeUpdate("drop table registro");
            e.executeUpdate("drop table cliente");
            e.executeUpdate("drop table empleado");
            e.executeUpdate("drop table pieza");
            e.executeUpdate("create table pieza(id_pieza int not null auto_increment primary key, precio_pieza int not null, personas_pieza int not null, disponibilidad varchar(12) not null)");
            e.executeUpdate("create table cliente(rut_cliente varchar(12) not null primary key, nombre_cliente varchar(60) not null)");
            e.executeUpdate("create table empleado(id_empleado int not null auto_increment primary key, rut_empleado varchar(12) not null, nombre_empleado varchar(50) not null, sueldo int not null)");
            e.executeUpdate("create table registro(id_empleado int not null, id_pieza int not null, id_registro int not null auto_increment primary key, rut_cliente varchar(12) not null)");
            //añadir llaves foraneas.
            e.executeUpdate("alter table registro add constraint fk_empleado foreign key(id_empleado) references empleado(id_empleado)");
            e.executeUpdate("alter table registro add constraint fk_pieza foreign key(id_pieza) references pieza(id_pieza)");
            e.executeUpdate("alter table registro add constraint fk_rut_cliente foreign key(rut_cliente) references cliente(rut_cliente)");
            System.out.println("limpiado correctamente");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection con = DriverManager.getConnection(url, usuario, contraseña)) {
            Statement e = con.createStatement();
            ResultSet rs = e.executeQuery("select * from cliente");
            assertFalse(rs.next());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}