import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Pieza implements Guardable{
    private int precio;
    private int capacidadPersonas;
    private String estado;

    public Pieza(int precio, int capacidadPersonas, String estado) {
        this.precio = precio;
        this.capacidadPersonas = capacidadPersonas;
        this.estado = estado;

        String url = "jdbc:mysql://localhost/hostal";
        String usuario = "root";
        String contraseña = "";
        try (Connection con= DriverManager.getConnection(url, usuario, contraseña)){
            Statement e=con.createStatement();
            e.execute("insert into pieza(precio_pieza,personas_pieza,disponibilidad) values('"+precio+"','"+capacidadPersonas+"','"+estado+"')");
            System.out.println("Pieza ingresada correctamente");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public String guardadEnHistorial() {
        String retorno="se a creado una pieza de costo: "+this.precio+" pesos por dia y con capacidad de "+this.capacidadPersonas+" personas.";;
        return retorno;
    }
}
