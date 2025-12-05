package com.zephyr.service.impl;

import com.zephyr.service.EmailService;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class EmailServiceImpl implements EmailService {

    private static final String MI_CORREO = "zephyr.aero.notifications@gmail.com";
    private static final String MI_CONTRASENA_APP = "iblc axco tcwu xwqt";

    private final Properties props;

    public EmailServiceImpl() {
        props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
    }

    @Override
    public void enviarEmailConAdjunto(String destinatario, String asunto, String cuerpoMensaje, String rutaArchivoAdjunto) {

        Session session = Session.getInstance(props, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MI_CORREO, MI_CONTRASENA_APP);
            }
        });

        try {

            // crear  emnsaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MI_CORREO));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);

            // cuerpo mensjae
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(cuerpoMensaje);

            //crear el adjunto
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(rutaArchivoAdjunto);
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(source.getName());

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);

            // enviaar correo
            Transport.send(message);

            System.out.println("Email enviado correctamente a " + destinatario + " !");

        } catch (MessagingException e) {
            System.err.println("Error al enviar el Email: " + e.getMessage());
            e.printStackTrace();

        }
    }

}
