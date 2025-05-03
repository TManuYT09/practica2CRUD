module org.example.practica_2 {
    requires org.mariadb.jdbc;
    requires static lombok;
    requires javafx.fxml;
    requires javafx.graphics;


    opens org.example.practica_2 to javafx.fxml;
    exports org.example.practica_2;
}