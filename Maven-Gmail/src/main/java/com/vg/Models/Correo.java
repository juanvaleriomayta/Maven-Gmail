package com.vg.Models;

import lombok.Data;

@Data
public class Correo {

    private String usuarioCorreo;
    private String contrasenia;
    private String rutArchivo;
    private String nombreArchivo;
    private String destino;
    private String asunto;
    private String mensaje;

}
