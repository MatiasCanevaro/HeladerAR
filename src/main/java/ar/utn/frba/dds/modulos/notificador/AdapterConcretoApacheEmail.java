package ar.utn.frba.dds.modulos.notificador;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.mail.Email;

import static java.lang.Integer.parseInt;

public class AdapterConcretoApacheEmail implements AdapterNotificador{
    public void notificar(Mensaje mensaje, String receptor){
        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream("./src/main/resources/archivoDeConfiguracion.properties")) {
            properties.load(fis);
            String username = properties.getProperty("notificador_username");
            String password = properties.getProperty("notificador_password");
            String fromEmail = properties.getProperty("notificador_fromEmail");
            String host = properties.getProperty("notificador_host");
            int port = parseInt(properties.getProperty("notificador_port"));
            Email email = new SimpleEmail();
            email.setHostName(host);
            email.setSmtpPort(port);
            email.setAuthenticator(new DefaultAuthenticator(username, password));
            email.setStartTLSEnabled(true);
            email.setFrom(fromEmail);
            email.setSubject(mensaje.getAsunto());
            email.setMsg(mensaje.getCuerpo());
            email.addTo(receptor);
            email.send();
            System.out.println("El correo electronico fue enviado correctamente.");
        } catch (EmailException e) {
            System.out.println("Error al enviar el correo electronico: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}