package com.vg.Controller;

import com.vg.Models.Correo;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.Serializable;
import java.util.Properties;

@Named(value = "correoController")
@SessionScoped
public class CorreoController implements Serializable {

    // Modelo usado para enviar correos
    Correo c = new Correo();

    public void enviar() throws Exception {
        try {
            // Variable para almacenar el mensaje a enviar
//            c.setMensaje("Mensaje a enviar");
            // Variable para almacenar el correo de destino
//            c.setDestino("jcondori@vallegrande.edu.pe");
            // Ejecutamos metodo para enviar el correo
            enviarCorreo();
            // Limpiamos el modelo de correo
            c = new Correo();
            //Mandamos un mensaje de accion correcta
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Enviado Correctamente"));
        } catch (Exception e) {
            throw e;
        }
    }

    // Configuraciones
    public void enviarCorreo() throws Exception {
        // Correo de salida
        c.setUsuarioCorreo("jvaleriom@vallegrande.edu.pe");
        // Contraseña generada en el correo de salida
        c.setContrasenia("yo soy inevitable");
        // El asunto del correo que se enviara - Si deseas puedes hacerlo dinamico
        c.setAsunto("Sistema de Control de notas y evaluaciones CAME");
//        c.setAsunto(c.getAsunto());
        // Por si deseas adjuntar algun archivo
        c.setNombreArchivo("");
        c.setRutArchivo("");
        //Ejecuta el metodo de envío y obtiene respuesta(True / False) si se envio correctamente
        if (!enviarCorreo(c)) {
            // En caso sea false arroja un mensaje de error
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error"));
        }
    }

    public boolean enviarCorreo(Correo c) throws Exception {
        try {
            Properties p = new Properties();
            p.put("mail.smtp.host", "smtp.gmail.com");
            p.setProperty("mail.smtp.starttls.enable", "true");
            p.setProperty("mail.smtp.port", "587");
            p.setProperty("mail.smtp.user", c.getUsuarioCorreo());
            p.setProperty("mail.smtp.auth", "true");

            Session s = Session.getDefaultInstance(p, null);
            BodyPart texto = new MimeBodyPart();
            texto.setText(c.getMensaje());
            BodyPart adjunto = new MimeBodyPart();

            if (!c.getRutArchivo().equals("")) {
                adjunto.setDataHandler(new DataHandler(new FileDataSource(c.getRutArchivo())));
                adjunto.setFileName(c.getNombreArchivo());
            }
            MimeMultipart m = new MimeMultipart();
            m.addBodyPart(texto);

            if (!c.getRutArchivo().equals("")) {
                m.addBodyPart(adjunto);
            }
            Message mensaje = new MimeMessage(s);
            mensaje.setFrom(new InternetAddress(c.getUsuarioCorreo()));
            // Validamos que haya un correo y lo seteamos para enviar
            if (c.getDestino() != null && c.getDestino().length() > 10) {
                mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(c.getDestino()));
            }
            // Seteamos el asunto del correo
            mensaje.setSubject(c.getAsunto());
            // Adjuntamos archivo si es que lo hay
            mensaje.setContent(m);

            // Metodo de envio
            Transport t = s.getTransport("smtp");
            t.connect(c.getUsuarioCorreo(), c.getContrasenia());
            t.sendMessage(mensaje, mensaje.getAllRecipients());
            t.close();

            return true;
        } catch (MessagingException e) {
            System.out.println("error: " + e);
            return false;
        }
    }

    /*
     * Getter and Setter
     * */
    public Correo getC() {
        return c;
    }

    public void setC(Correo c) {
        this.c = c;
    }

}
