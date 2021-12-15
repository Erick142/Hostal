import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner teclado=new Scanner(System.in);
        boolean error,cerrar=false,BDVacia=true;
        String url = "jdbc:mysql://localhost/hostal";
        String usuario = "root";
        String contraseña = "";
        do {
            error=false;
            try {
                int seleccion=0;
                System.out.println("MENU\n1-añadir empleado\n2-añadir cliente\n3-añadir habitacion\n4-hacer registro\n5-ver registros");
                seleccion= teclado.nextInt();
                String nombre="",rut="",estado="";
                int costo=0,capacidadPersonas=0;
                switch (seleccion){
                    case 1:
                        try {
                            System.out.println("ingrese nombre del empleado");
                            nombre=teclado.next();
                            System.out.println("ingrese rut del empleado");
                            rut=teclado.next();
                            new Empleado(nombre,rut);
                        }catch (Exception e){
                            teclado.next();
                            System.err.println("parametro invalido");
                            error=true;
                        }
                        break;
                    case 2:
                        try {
                            System.out.println("ingrese nombre del cliente");
                            nombre=teclado.next();
                            System.out.println("ingrese rut del cliente");
                            rut=teclado.next();
                            new Cliente(nombre,rut);
                        }catch (Exception e){
                            teclado.next();
                            System.err.println("parametro invalido");
                            error=true;
                        }
                        break;
                    case 3:
                        try {
                            System.out.println("costo de pieza");
                            costo=teclado.nextInt();
                            System.out.println("ingrese maximo de personas que soporta la lieza");
                            capacidadPersonas=teclado.nextInt();
                            new Pieza(costo,capacidadPersonas,"disponible");
                        }catch (Exception e){
                            teclado.next();
                            System.err.println("parametro invalido");
                            error=true;
                        }
                        break;
                    case 4:
                        try (Connection conn= DriverManager.getConnection(url,usuario,contraseña)){
                            Statement e=conn.createStatement();
                            Statement e3=conn.createStatement();
                            Statement e4= conn.createStatement();
                            ResultSet verclientes=e.executeQuery("select * from cliente");
                            ResultSet verempleados=e3.executeQuery("select * from empleado");
                            ResultSet verpieza=e4.executeQuery("select * from pieza");
                            if (!verclientes.next()||!verpieza.next()||!verempleados.next()){
                                if (!verclientes.next()){
                                    System.out.println("faltan clientes");
                                }
                                if (!verpieza.next()){
                                    System.out.println("falta una pieza");
                                }
                                if (!verempleados.next()){
                                    System.out.println("falta un empleado");
                                }
                            }
                            else {
                                int idEmpleado=0,idPieza=0;
                                String rutCliente="";
                                System.out.println("------------------------------------------------------\n                   EMPLEADO");
                                mostrarEmpleados();
                                System.out.println("------------------------------------------------------");
                                System.out.println("ingrese el id del empleado que realiza el registo");
                                idEmpleado= teclado.nextInt();
                                System.out.println("------------------------------------------------------\n                    CLIENTES");
                                mostrarClientes();
                                System.out.println("------------------------------------------------------");
                                System.out.println("ingrese el rut del del cliente");
                                rutCliente= teclado.next();
                                System.out.println("------------------------------------------------------\n                     PIEZAS");
                                mostrarPiezas();
                                System.out.println("------------------------------------------------------");
                                System.out.println("ingrese el id de la pieza que el cliente desea alquilar");
                                idPieza= teclado.nextInt();
                                try (Connection con= DriverManager.getConnection(url, usuario, contraseña)){
                                    Statement e2=con.createStatement();
                                    e2.execute("insert into registro(id_empleado,id_pieza,rut_cliente) values('"+idEmpleado+"','"+idPieza+"','"+rutCliente+"')");
                                    e2.execute("update pieza set disponibilidad='ocupada' where id_pieza="+"'"+idPieza+"'");
                                    System.out.println("registro creado correctamente");
                                }catch (SQLException es){
                                    es.printStackTrace();
                                    System.err.println("ingreso un dato que no existe");
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case 5:
                        try (Connection con= DriverManager.getConnection(url,usuario,contraseña)){
                            Statement e=con.createStatement();
                            Statement a=con.createStatement();
                            ResultSet mostrarregistros=e.executeQuery("select a.id_registro,b.nombre_cliente,c.nombre_empleado,d.id_pieza,d.precio_pieza,d.personas_pieza from registro a, cliente b, empleado c, pieza d where a.rut_cliente=b.rut_cliente and a.id_empleado=c.id_empleado and a.id_pieza=d.id_pieza");
                            ResultSet mostrarregistros2=a.executeQuery("select a.id_registro,b.nombre_cliente,c.nombre_empleado,d.id_pieza,d.precio_pieza,d.personas_pieza from registro a, cliente b, empleado c, pieza d where a.rut_cliente=b.rut_cliente and a.id_empleado=c.id_empleado and a.id_pieza=d.id_pieza");
                            if (mostrarregistros.next()){
                                while (mostrarregistros2.next()){
                                    System.out.println("registro numero:"+mostrarregistros.getInt("id_registro")+" -cliente: "+mostrarregistros.getString("nombre_cliente")+" -empleado: "+mostrarregistros.getString("nombre_empleado")+" -nro pieza: "+mostrarregistros.getInt("id_pieza")+" -costo: "+mostrarregistros.getInt("precio_pieza")+
                                            " -capacidad de personas: "+mostrarregistros.getInt("personas_pieza"));
                                }
                            }
                            else {
                                System.out.println("no hay registros");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case 6:
                        mostrarPiezas();
                        break;
                    case 9:
                        truncarBD();
                        break;
                    case 10:
                        cerrar=true;
                        break;
                }
            }catch (Exception e){
                teclado.next();
                error=true;
            }
        }while (error||!cerrar);
    }

    public static void truncarBD(){
        String url = "jdbc:mysql://localhost/hostal";
        String usuario = "root";
        String contraseña = "";

        try (Connection con= DriverManager.getConnection(url,usuario,contraseña)){
            Statement e=con.createStatement();
            //borrar y rehacer las tablas.
            e.executeUpdate("drop table registro");
            e.executeUpdate("drop table cliente");
            e.executeUpdate("drop table empleado");
            e.executeUpdate("drop table pieza");
            e.executeUpdate("create table pieza(id_pieza int not null auto_increment primary key, precio_pieza int not null, personas_pieza int not null, disponibilidad varchar(12) not null)");
            e.executeUpdate("create table cliente(rut_cliente varchar(12) not null primary key, nombre_cliente varchar(60) not null)");
            e.executeUpdate("create table empleado(id_empleado int not null auto_increment primary key, rut_empleado varchar(12) not null, nombre_empleado varchar(50) not null)");
            e.executeUpdate("create table registro(id_empleado int not null, id_pieza int not null, id_registro int not null auto_increment primary key, rut_cliente varchar(12) not null)");
            //añadir llaves foraneas.
            e.executeUpdate("alter table registro add constraint fk_empleado foreign key(id_empleado) references empleado(id_empleado)");
            e.executeUpdate("alter table registro add constraint fk_pieza foreign key(id_pieza) references pieza(id_pieza)");
            e.executeUpdate("alter table registro add constraint fk_rut_cliente foreign key(rut_cliente) references cliente(rut_cliente)");
            System.out.println("limpiado correctamente");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void mostrarEmpleados(){
        String url = "jdbc:mysql://localhost/hostal";
        String usuario = "root";
        String contraseña = "";

        try (Connection con= DriverManager.getConnection(url,usuario,contraseña)){
            Statement e=con.createStatement();
            ResultSet rs=e.executeQuery("select * from empleado");
            while (rs.next()){
                System.out.println("id:"+rs.getInt("id_empleado")+" -nombre: "+rs.getString("nombre_empleado")+" -rut: "+rs.getString("rut_empleado"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void mostrarClientes(){
        String url = "jdbc:mysql://localhost/hostal";
        String usuario = "root";
        String contraseña = "";

        try (Connection con= DriverManager.getConnection(url,usuario,contraseña)){
            Statement e=con.createStatement();
            ResultSet rs=e.executeQuery("select * from cliente");
            while (rs.next()){
                System.out.println("nombre:"+rs.getString("nombre_cliente")+" -rut: "+rs.getString("rut_cliente"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void mostrarPiezas(){
        String url = "jdbc:mysql://localhost/hostal";
        String usuario = "root";
        String contraseña = "";

        try (Connection con= DriverManager.getConnection(url,usuario,contraseña)){
            Statement e=con.createStatement();
            ResultSet rs=e.executeQuery("select * from pieza where disponibilidad='disponible'");
            while (rs.next()){
                System.out.println("id:"+rs.getInt("id_pieza")+" -precio: "+rs.getInt("precio_pieza")+" -capacidad de personas: "+rs.getInt("personas_pieza")+" -estado: "+rs.getString("disponibilidad"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
