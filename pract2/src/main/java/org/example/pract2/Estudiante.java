package org.example.pract2;

import lombok.*;

import java.sql.Date;

@Getter
@AllArgsConstructor
@ToString
public class Estudiante {
    private Integer nia;
    private String nombre;
    private Date fecha_nacimiento;
}
