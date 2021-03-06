import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Hostal {
    public static void main(String[] args) {
        List<String> historial=new ArrayList<>();
        int nroPiezas=1;
        List<Pieza> piezasIniciales=new ArrayList<>();
        Scanner teclado = new Scanner(System.in);
        boolean cerrar = false;
        String url = "jdbc:mysql://localhost/hostal";
        String usuario = "root";
        String contraseña = "";
        boolean error2=false;
        do {
            error2=false;
            try {
                System.out.println("ingrese nro de piezas del hostal");
                nroPiezas= teclado.nextInt();
            }catch (Exception e){
                error2=true;
                teclado.next();
                System.err.println("ingreso un valor que no es int");
            }
        }while (error2);
        piezasIniciales=generarHostal(nroPiezas);
        do {
            try {
                int seleccion = 0;
                System.out.println("--------------------------------------------------------\n" +
                        "MENU\n1-añadir empleado\n2-añadir cliente\n3-añadir pieza\n4-hacer registro\n5-ver registro\n6-ver piezas\n7-ver historial de esta sesion\n8-eliminar un registro\n9-limpiar base de datos\n10-salir" +
                        "\n--------------------------------------------------------");
                seleccion = teclado.nextInt();
                String nombre = "", rut = "";
                int costo = 0, capacidadPersonas = 0,sueldo=0;
                switch (seleccion) {
                    case 1:
                        try {
                            System.out.println("ingrese nombre del empleado");
                            nombre = teclado.next();
                            System.out.println("ingrese rut del empleado");
                            rut = teclado.next();
                            System.out.println("ingrese sueldo del empleado");
                            sueldo= teclado.nextInt();
                            historial.add(new Empleado(nombre,rut,sueldo).guardadEnHistorial());
                        } catch (InputMismatchException e) {
                            System.err.println("usted ingreso un valor ivalido");
                            teclado.next();


                        }
                        catch (Exception e){
                            teclado.next();
                            System.err.println("ocurrio un error inesperado");
                        }
                        break;
                    case 2:
                        try {
                            System.out.println("ingrese nombre del cliente");
                            nombre = teclado.next();
                            System.out.println("ingrese rut del cliente");
                            rut = teclado.next();
                            historial.add(new Cliente(nombre,rut).guardadEnHistorial());
                        } catch (Exception e) {
                            teclado.next();
                            System.err.println("parametro invalido");

                        }
                        break;
                    case 3:
                        try {
                            System.out.println("costo por dia de la pieza");
                            costo = teclado.nextInt();
                            System.out.println("ingrese maximo de personas que soporta la pieza");
                            capacidadPersonas = teclado.nextInt();
                            historial.add(new Pieza(costo, capacidadPersonas, "disponible").guardadEnHistorial());
                        } catch (Exception e) {
                            teclado.next();
                            System.err.println("parametro invalido");

                        }
                        break;
                    case 4:
                        try (Connection conn = DriverManager.getConnection(url, usuario, contraseña)) {
                            Statement e = conn.createStatement();
                            Statement e3 = conn.createStatement();
                            Statement e4 = conn.createStatement();
                            Statement a = conn.createStatement();
                            Statement a1 = conn.createStatement();
                            Statement a2 = conn.createStatement();
                            ResultSet verclientes = e.executeQuery("select * from cliente");
                            ResultSet verempleados = e3.executeQuery("select * from empleado");
                            ResultSet verpieza = e4.executeQuery("select * from pieza");
                            ResultSet verclientes2 = a.executeQuery("select * from cliente");
                            ResultSet verempleados2 = a1.executeQuery("select * from empleado");
                            ResultSet verpieza2 = a2.executeQuery("select * from pieza");
                            if (!verclientes.next() || !verpieza.next() || !verempleados.next()) {
                                if (!verclientes2.next()) {
                                    System.out.println("faltan clientes");
                                }
                                if (!verpieza2.next()) {
                                    System.out.println("faltan pieza");
                                }
                                if (!verempleados2.next()) {
                                    System.out.println("faltan empleados");
                                }
                            } else {
                                int idEmpleado = 0, idPieza = 0;
                                String rutCliente = "";
                                System.out.println("------------------------------------------------------\n                   EMPLEADO");
                                mostrarEmpleados();
                                System.out.println("------------------------------------------------------");
                                System.out.println("ingrese el id del empleado que realiza el registo");
                                idEmpleado = teclado.nextInt();
                                System.out.println("------------------------------------------------------\n                    CLIENTES");
                                mostrarClientes();
                                System.out.println("------------------------------------------------------");
                                System.out.println("ingrese el rut del del cliente");
                                rutCliente = teclado.next();
                                System.out.println("------------------------------------------------------\n                     PIEZAS");
                                mostrarPiezas("select * from pieza where disponibilidad='disponible'");
                                System.out.println("------------------------------------------------------");
                                System.out.println("ingrese el id de la pieza que el cliente desea alquilar");
                                idPieza = teclado.nextInt();
                                historial.add("el empleado de id: "+idEmpleado+" a registrado al cliente de rut: "+rutCliente+" en la pieza nro "+idPieza+".");
                                try (Connection con = DriverManager.getConnection(url, usuario, contraseña)) {
                                    Statement e2 = con.createStatement();
                                    new Registro(idEmpleado,rutCliente,idPieza);
                                    e2.execute("update pieza set disponibilidad='ocupada' where id_pieza=" + "'" + idPieza + "'");
                                } catch (SQLException es) {
                                    System.err.println("ingreso un dato que no existe");
                                }
                            }
                        } catch (SQLException e) {
                            System.err.println("ingreso un dato que no existe");
                        }
                        break;
                    case 5:
                        mostrarRegistros();
                        break;
                    case 6:
                        System.out.println("cantidad de piezas: "+piezasIniciales.size());
                        mostrarPiezas("select * from pieza");
                        break;
                    case 7:
                        System.out.println("cambios realizados");
                        historial.stream().forEach(System.out::println);
                        System.out.println("en este inicio de sesion se realizaron "+historial.size()+" cambios");
                        break;
                    case 8:
                        mostrarRegistros();
                        int id_registro=0,id_pieza=0;
                        System.out.println("ingrese la id del registro que desea eliminar");
                        id_registro= teclado.nextInt();
                        System.out.println("ingrese id de la pieza");
                        id_pieza= teclado.nextInt();
                        try (Connection con = DriverManager.getConnection(url, usuario, contraseña)) {
                            Statement e = con.createStatement();
                            e.executeUpdate("delete from registro where id_registro="+"'"+id_registro+"'");
                            System.out.println("registro eliminado correctamente");
                            historial.add("se a eliminado el registro de id: "+id_registro);
                        } catch (SQLException e) {
                            System.err.println("no se pudo eliminar el registro");
                        }
                        try (Connection conn = DriverManager.getConnection(url, usuario, contraseña)) {
                            Statement e = conn.createStatement();
                            e.execute("update pieza set disponibilidad='disponible' where id_pieza=" + "'" + id_pieza + "'");
                            System.out.println("pieza disponible");
                            historial.add("se a liberado la pieza nro: "+id_pieza);
                        } catch (SQLException e) {
                            System.err.println("no se pudo actualizar la pieza");
                        }
                        break;
                    case 9:
                        truncarBD();
                        historial.add("la base de datos fue limpiada");
                        piezasIniciales.clear();
                        break;
                    case 10:
                        cerrar = true;
                        break;
                }
            } catch (Exception e) {
                teclado.next();
            }
        } while (!cerrar);
    }

    public static void truncarBD() {
        String url = "jdbc:mysql://localhost/hostal";
        String usuario = "root";
        String contraseña = "";

        try (Connection con = DriverManager.getConnection(url, usuario, contraseña)) {
            Statement e = con.createStatement();
            //borrar y rehacer las tablas.
            e.executeUpdate("drop table registro");
            e.executeUpdate("drop table cliente");
            e.executeUpdate("drop table empleado");
            e.executeUpdate("drop table pieza");
            e.executeUpdate("create table pieza(id_pieza int not null auto_increment primary key, precio_pieza int not null, personas_pieza int not null, disponibilidad varchar(12) not null)");
            e.executeUpdate("create table cliente(rut_cliente varchar(12) not null primary key, nombre_cliente varchar(60) not null)");
            e.executeUpdate("create table empleado(id_empleado int not null auto_increment primary key, rut_empleado varchar(12) not null, nombre_empleado varchar(50) not null,sueldo int not null)");
            e.executeUpdate("create table registro(id_empleado int not null, id_pieza int not null, id_registro int not null auto_increment primary key, rut_cliente varchar(12) not null)");
            //añadir llaves foraneas.
            e.executeUpdate("alter table registro add constraint fk_empleado foreign key(id_empleado) references empleado(id_empleado)");
            e.executeUpdate("alter table registro add constraint fk_pieza foreign key(id_pieza) references pieza(id_pieza)");
            e.executeUpdate("alter table registro add constraint fk_rut_cliente foreign key(rut_cliente) references cliente(rut_cliente)");
            System.out.println("limpiado correctamente");
        } catch (SQLException e) {
            System.err.println("no se pudo limpiar la base de datos");
        }
    }

    public static void mostrarEmpleados() {
        String url = "jdbc:mysql://localhost/hostal";
        String usuario = "root";
        String contraseña = "";

        try (Connection con = DriverManager.getConnection(url, usuario, contraseña)) {
            Statement e = con.createStatement();
            ResultSet rs = e.executeQuery("select * from empleado");
            while (rs.next()) {
                System.out.println("id:" + rs.getInt("id_empleado") + " -nombre: " + rs.getString("nombre_empleado") + " -rut: " + rs.getString("rut_empleado"));
            }
        } catch (SQLException e) {
            System.err.println("no se pudo acceder a los empleados");
        }
    }

    public static void mostrarClientes() {
        String url = "jdbc:mysql://localhost/hostal";
        String usuario = "root";
        String contraseña = "";

        try (Connection con = DriverManager.getConnection(url, usuario, contraseña)) {
            Statement e = con.createStatement();
            ResultSet rs = e.executeQuery("select * from cliente");
            while (rs.next()) {
                System.out.println("nombre:" + rs.getString("nombre_cliente") + " -rut: " + rs.getString("rut_cliente"));
            }
        } catch (SQLException e) {
            System.err.println("no se pudo acceder a los clientes");
        }
    }

    public static void mostrarPiezas(String Query) {
        String url = "jdbc:mysql://localhost/hostal";
        String usuario = "root";
        String contraseña = "";

        try (Connection con = DriverManager.getConnection(url, usuario, contraseña)) {
            Statement e = con.createStatement();
            ResultSet rs = e.executeQuery(Query);
            while (rs.next()) {
                System.out.println("id:" + rs.getInt("id_pieza") + " -precio: " + rs.getInt("precio_pieza") + " -capacidad de personas: " + rs.getInt("personas_pieza") + " -estado: " + rs.getString("disponibilidad"));
            }
        } catch (SQLException e) {
            System.err.println("no se pudo acceder a las piezas");
        }
    }
    public static void mostrarRegistros(){
        String url = "jdbc:mysql://localhost/hostal";
        String usuario = "root";
        String contraseña = "";
        try (Connection con = DriverManager.getConnection(url, usuario, contraseña)) {
            Statement e = con.createStatement();
            Statement a = con.createStatement();
            ResultSet mostrarregistros = e.executeQuery("select a.id_registro,b.nombre_cliente,c.nombre_empleado,d.id_pieza,d.precio_pieza,d.personas_pieza from registro a, cliente b, empleado c, pieza d where a.rut_cliente=b.rut_cliente and a.id_empleado=c.id_empleado and a.id_pieza=d.id_pieza");
            ResultSet mostrarregistros2 = a.executeQuery("select a.id_registro,b.nombre_cliente,c.nombre_empleado,d.id_pieza,d.precio_pieza,d.personas_pieza from registro a, cliente b, empleado c, pieza d where a.rut_cliente=b.rut_cliente and a.id_empleado=c.id_empleado and a.id_pieza=d.id_pieza");

            while (mostrarregistros.next()) {
                System.out.println("registro numero:" + mostrarregistros.getInt("id_registro") + " -cliente: " + mostrarregistros.getString("nombre_cliente") + " -empleado: " + mostrarregistros.getString("nombre_empleado") + " -nro pieza: " + mostrarregistros.getInt("id_pieza") + " -costo: " + mostrarregistros.getInt("precio_pieza") +
                        " -capacidad de personas: " + mostrarregistros.getInt("personas_pieza"));
            }
            if (!mostrarregistros2.next()) {
                System.out.println("registro vacio");
            }
        } catch (SQLException e) {
            System.err.println("no se pudo acceder a los registros");
        }
    }
    public static ArrayList generarHostal(int nroPiezas){
        ArrayList<Pieza> array=new ArrayList<>();
        for (int i=0;i<nroPiezas;i++){
            array.add(new Pieza(-1));
        }
        System.out.println("piezas generadas: "+nroPiezas);
        return array;
    }
}
