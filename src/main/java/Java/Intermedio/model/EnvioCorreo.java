package Java.Intermedio.model;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.util.Properties;

public class EnvioCorreo {
    private final String emailFrom = "tofergo2407@gmail.com";
    private final String password = "ikxu mpmu imnw atcd";
    private Properties properties;
    private Session session;

    public EnvioCorreo() {
        properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, password);
            }
        });
    }

    public boolean enviarCorreoConAdjunto(String mailTo, String subject, String content, File archivoTxt) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailFrom));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
            message.setSubject(subject);

            MimeBodyPart textoParte = new MimeBodyPart();
            textoParte.setText(content, "utf-8");

            MimeBodyPart archivoParte = new MimeBodyPart();
            archivoParte.attachFile(archivoTxt);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textoParte);
            multipart.addBodyPart(archivoParte);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Correo con archivo .txt enviado correctamente");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

