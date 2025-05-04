package org.example.pract2;

import org.mariadb.jdbc.Statement;

import java.sql.*;
import java.util.ArrayList;

public class Main {
    static ArrayList<Estudiante> listaEstudiantes = new ArrayList<>();

    public static void main(String[] args) {
        Connection bd = conexion();
        System.out.println("Realizando consultas...");
        consulta_a_lista(bd);
        desconectar(bd);
    }

    public static Connection conexion() {
        Connection conexion;
        String host = "jdbc:mariadb://localhost:3306/";
        String user = "root";
        String psw = "";
        String bd = "prueba";
        System.out.println("Conectando...");

        try {
            conexion = DriverManager.getConnection(host+bd,user,psw);
            System.out.println("Conexión realizada con éxito.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        return conexion;
    }

    public static void desconectar(Connection conexion){

        System.out.println("Desconectando...");

        try {
            conexion.close();
            System.out.println("Conexión finalizada.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void consulta_a_lista (Connection conexion){

        listaEstudiantes.clear();

        String query = "SELECT * FROM estudiantes";

        //necesitamos el dos variables de tipo Statement y ResultSet para realizar la consulta y guardar la respuesta
        Statement stmt;
        ResultSet respuesta;

        try {
            stmt = (Statement) conexion.createStatement();
            respuesta = stmt.executeQuery(query);

            while (respuesta.next()){ //recorremos todas las filas existentes en la tabla
                int nia = respuesta.getInt("nia");
                String nombre = respuesta.getString("nombre");
                Date fecha_nacimiento = respuesta.getDate("fecha_nacimiento");
                listaEstudiantes.add(new Estudiante(nia,nombre,fecha_nacimiento));
            }

            System.out.println(listaEstudiantes);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void insertar(Connection conexion){

        System.out.println("Insertando...");

        String query = "INSERT INTO estudiantes (nia, nombre, fecha_nacimiento) VALUES (43214321, 'Patricia', '1900-04-19');";

        Statement stmt;

        try {
            stmt = (Statement) conexion.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }


    public static void modificar(Connection conexion){

        System.out.println("Modificando...");

        String query = "UPDATE estudiantes SET nombre = 'Patri' WHERE nombre = 'Patricia'";

        Statement stmt;

        try {
            stmt = (Statement) conexion.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public static void borrar(Connection conexion){

        System.out.println("Borrando...");

        String query = "DELETE FROM estudiantes WHERE nombre = 'Patri'";

        Statement stmt;

        try {
            stmt = (Statement) conexion.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
