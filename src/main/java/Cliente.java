import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Cliente extends Persona{

    public Cliente(String nombre, String rut) {
        super(nombre, rut);

        String url = "jdbc:mysql://localhost/hostal";
        String usuario = "root";
        String contraseña = "";
        try (Connection con= DriverManager.getConnection(url, usuario, contraseña)){
            Statement e=con.createStatement();
            e.execute("insert into cliente(nombre_cliente,rut_cliente) values('"+nombre+"','"+rut+"')");
            System.out.println("Cliente ingresada correctamente");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
