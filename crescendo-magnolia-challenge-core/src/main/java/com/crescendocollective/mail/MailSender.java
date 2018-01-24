package com.crescendocollective.mail;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {

    private String username;
    private String password;
    private String smtpServer;
    private String smtpPort;
    private String smtpAuth;
    private String smtpTlsEnabled;


    public MailSender(String username, String password, String smtpServer,
                        String smtpPort, String smtpAuth, String smtpTlsEnabled) {
        this.username = username;
        this.password = password;
        this.smtpServer = smtpServer;
        this.smtpPort = smtpPort;
        this.smtpAuth = smtpAuth;
        this.smtpTlsEnabled = smtpTlsEnabled;
    }

    public void sendEmail(String to, String from, String body) {
        Properties props = new Properties();

        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", smtpTlsEnabled);

        sendMessage(props, from, to, body);
    }

    private void sendMessage(Properties props, String from, String to, String body) {
        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setSubject("Mailing request");
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setText(body);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
