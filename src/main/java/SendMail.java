
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class SendMail {
    public static void main(String[] args) throws Exception {

        Properties prop = new Properties();
        String propFileName = "config.properties";
        InputStream inputStream = new FileInputStream("config.properties");

        System.out.println("reading properties from " + propFileName);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' in current folder");
        }

        final String host = prop.getProperty("host");
        final int port = Integer.valueOf(prop.getProperty("port"));
        final String user = prop.getProperty("user");
        final String password = prop.getProperty("password");
        final String userFrom = prop.getProperty("user.from");
        final String to = prop.getProperty("user.to");
        final boolean smtps = Boolean.valueOf(prop.getProperty("use.smtps"));
        final boolean starttls = Boolean.valueOf(prop.getProperty("use.starttls"));

        String protocol = "smtp";
        System.out.println("Sending message...");
        try {
            // Get the session object
            Properties props = new Properties();
            if (smtps) {
                props.setProperty("mail.smtps.auth", "true");
                protocol = "smtps";
            }
            if (starttls) {
                props.setProperty("mail.smtp.auth", "true");
                props.setProperty("mail.smtp.starttls.enable", "true");
            }
            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(true);
            Transport transport = mailSession.getTransport(protocol);

            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject("Test Email");
            message.setContent("Test Body", "text/plain");

            message.setFrom(new InternetAddress(userFrom));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            transport.connect(host, port, user, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            System.out.println("Message sent successfully...");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}