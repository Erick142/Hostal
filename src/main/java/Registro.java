import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Registro {
    private int idEmpleado;
    private String rutCliente;
    private int idPieza;

    public Registro(int idEmpleado, String rutCliente, int idPieza) {
        this.idEmpleado = idEmpleado;
        this.rutCliente = rutCliente;
        this.idPieza = idPieza;

        String url = "jdbc:mysql://localhost/hostal";
        String usuario = "root";
        String contraseña = "";
        try (Connection con= DriverManager.getConnection(url, usuario, contraseña)){
            Statement e=con.createStatement();
            e.execute("insert into registro(id_empleado,rut_cliente,id_pieza) values("+idEmpleado+",'"+rutCliente+"',"+idPieza+")");
            System.out.println("registro guardado");
        }catch (SQLException e){
        }
    }
}
