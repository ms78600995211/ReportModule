/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;



public class EmailSender1 {

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //get Session   
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("smartmeter.apogee@gmail.com", "ytphvceqdmsdwbby");
            }
        });
      
        //compose message    
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("smartmeter.apogee@gmail.com"));
            // Add multiple recipients to the message
            InternetAddress[] recipientAddresses = new InternetAddress[1];
            recipientAddresses[0] = new InternetAddress("mohitvishwakarma6jan@gmail.com");
            message.setRecipients(Message.RecipientType.TO, recipientAddresses);
            message.setSubject("this is for testing");
            Multipart multipart = new MimeMultipart();
            MimeBodyPart contentPart = new MimeBodyPart();
            contentPart.setText("this is for understanding", "utf-8", "html");
            multipart.addBodyPart(contentPart);
            message.setContent(multipart);
            Transport.send(message);
            System.out.println("message sent successfully");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }





    }





