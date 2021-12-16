import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Empleado extends Persona implements Guardable{
    private int sueldo;

    public int getSueldo() {
        return sueldo;
    }

    public Empleado(String nombre, String rut, int sueldo) {
        super(nombre, rut);
        this.sueldo=sueldo;

        String url = "jdbc:mysql://localhost/hostal";
        String usuario = "root";
        String contraseña = "";
        try (Connection con= DriverManager.getConnection(url, usuario, contraseña)){
            Statement e=con.createStatement();
            e.execute("insert into empleado(nombre_empleado,rut_empleado,sueldo) values('"+nombre+"','"+rut+"','"+sueldo+"')");
            System.out.println("Empleado ingresada correctamente");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public String guardadEnHistorial() {
        String retorno="se a añadido un empleado de nombre: "+getNombre()+" y rut: "+getRut()+" con un sueldo de: "+getSueldo()+" pesos.";
        return retorno;
    }
}
