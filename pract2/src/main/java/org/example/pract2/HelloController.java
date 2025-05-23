package org.example.pract2;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.PerspectiveCamera;
import javafx.scene.control.*;
import org.mariadb.jdbc.Statement;

import java.sql.*;
import java.time.LocalDate;

import static java.lang.Integer.valueOf;

public class HelloController {
    //Contenido
    @FXML
    private TextField niaTextField;
    @FXML
    private TextField nombreTextField;
    @FXML
    private DatePicker fechaDatePicker;
    //Mostrario
    @FXML
    private TableView<Estudiante> tablaBBBDD;
    @FXML
    private TableColumn<Estudiante,Integer> columnaNia;
    @FXML
    private TableColumn<Estudiante,String> columnaNombre;
    @FXML
    private TableColumn<Estudiante,Date> columnaFecha;

    private static ObservableList<Estudiante> listaEstudiantes = FXCollections.observableArrayList();

    @FXML
    private Button botonInsertar;
    @FXML
    private Button botonGuardar;

    @FXML
    public void initialize(){
        Connection bd = conexion();

        columnaNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        columnaNia.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getNia()).asObject());
        columnaFecha.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getFecha_nacimiento()));

        tablaBBBDD.setItems(listaEstudiantes);
        consulta_a_lista(bd);
    }

    @FXML
    private void insertarEstudiante(){
        Connection bd = conexion();

        int nia;
        try{
            nia = Integer.parseInt(niaTextField.getText());
        }catch (NumberFormatException e){
            System.out.println("NIA inválido");
            return;
        }
        String nombre = nombreTextField.getText();
        LocalDate temp = fechaDatePicker.getValue();
        Date fecha = Date.valueOf(temp);

        System.out.println("Insertando...");

        String query = "INSERT INTO estudiantes (nia, nombre, fecha_nacimiento) VALUES ("+nia+", '"+nombre+"', '"+fecha+"');";

        Statement stmt;

        try {
            stmt = (Statement) bd.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        Estudiante estudiante = new Estudiante(nia,nombre,fecha);

        listaEstudiantes.add(estudiante);
        nombreTextField.clear();
        niaTextField.clear();
    }

    @FXML
    private void eliminarEstudiante(){
        Connection bd = conexion();

        Estudiante seleccionado = tablaBBBDD.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            System.out.println("Borrando...");

            int nia = seleccionado.getNia();
            String nombre = seleccionado.getNombre();
            Date fecha = seleccionado.getFecha_nacimiento();

            String query = "DELETE FROM estudiantes WHERE nia="+nia;

            Statement stmt;

            try {
                stmt = (Statement) bd.createStatement();
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }

            Estudiante estudiante = new Estudiante(nia,nombre,fecha);

            listaEstudiantes.remove(estudiante);
        }else{
            System.out.println("No hay ninguna fila seleccionada.");
        }

        nombreTextField.clear();
        niaTextField.clear();

    }

    @FXML
    private void editarEstudiante(){
        Estudiante seleccionado = tablaBBBDD.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            niaTextField.setText(String.valueOf(seleccionado.getNia()));
            nombreTextField.setText(seleccionado.getNombre());
            fechaDatePicker.setValue(seleccionado.getFecha_nacimiento().toLocalDate());
            botonGuardar.setDisable(true);
            botonInsertar.setDisable(false);
        }else{
            System.out.println("No hay ninguna fila seleccionada.");
        }
    }

    @FXML
    private void actualizarEstudiante(){
        Connection bd = conexion();

        Estudiante seleccionado = tablaBBBDD.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            int nia_old = seleccionado.getNia();
            String nombre_old = seleccionado.getNombre();
            Date fecha_old = seleccionado.getFecha_nacimiento();

            int nia_new;
            try{
                nia_new = Integer.parseInt(niaTextField.getText());
            }catch (NumberFormatException e){
                System.out.println("NIA inválido");
                return;
            }
            String nombre_new = nombreTextField.getText();
            LocalDate temp = fechaDatePicker.getValue();
            Date fecha_new = Date.valueOf(temp);

            System.out.println("Modificando...");

            String query = "UPDATE estudiantes SET nombre = '"+nombre_new+"' and nia = '"+nia_new+"' and fecha_nacimiento='"+fecha_new+"' WHERE nia = "+nia_old;

            Statement stmt;

            try {
                stmt = (Statement) bd.createStatement();
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }

            botonGuardar.setDisable(false);
            botonInsertar.setDisable(true);
        } else {
            System.out.println("No hay ninguna fila seleccionada.");
        }

        nombreTextField.clear();
        niaTextField.clear();

    }

    public static Connection conexion() {
        Connection conexion;
        String host = "jdbc:mariadb://localhost:3307/";
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

    public static void consulta_a_lista (Connection conexion){

        String query = "SELECT * FROM estudiantes";

        //necesitamos el dos variables de tipo Statement y ResultSet para realizar la consulta y guardar la respuesta
        Statement stmt;
        ResultSet respuesta;

        try {
            stmt = (Statement) conexion.createStatement();
            respuesta = stmt.executeQuery(query);

            while (respuesta.next()){ //recorremos todas las filas existentes en la tabla
                Integer nia = respuesta.getInt("nia");
                String nombre = respuesta.getString("nombre");
                Date fecha_nacimiento = respuesta.getDate("fecha_nacimiento");
                listaEstudiantes.add(new Estudiante(nia,nombre,fecha_nacimiento));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}