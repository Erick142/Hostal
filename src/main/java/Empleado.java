import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Empleado extends Persona{

    public Empleado(String nombre, String rut) {
        super(nombre, rut);

        String url = "jdbc:mysql://localhost/hostal";
        String usuario = "root";
        String contraseña = "";
        try (Connection con= DriverManager.getConnection(url, usuario, contraseña)){
            Statement e=con.createStatement();
            e.execute("insert into empleado(nombre_empleado,rut_empleado) values('"+nombre+"','"+rut+"')");
            System.out.println("Empleado ingresada correctamente");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
