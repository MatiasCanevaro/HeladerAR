package ar.utn.frba.dds.modulos.notificador;

import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AdapterConcretoTwilio implements AdapterNotificador{

    public void notificar(Mensaje mensaje, String receptor) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("./src/main/resources/archivoDeConfiguracion.properties")){
            properties.load(fis);
            String ACCOUNT_SID = properties.getProperty("twilio_account_SID");
            String AUTH_TOKEN = properties.getProperty("twilio_auth_token");
            String PHONE_NUMBER = properties.getProperty("twilio_phone_number");
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message.creator( new com.twilio.type.PhoneNumber(receptor),
                    new com.twilio.type.PhoneNumber(PHONE_NUMBER),
                    mensaje.getCuerpo()).create();
            System.out.println("El mensaje fue enviado correctamente.");
        } catch (TwilioException e) {
            System.out.println("Error al enviar el whatsapp: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
