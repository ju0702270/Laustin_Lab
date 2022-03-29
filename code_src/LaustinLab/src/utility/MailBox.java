
package utility;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 23/02/2021
 *     Modification : 17/03/2021
 *     Revision : 1.0
 *     Description : Classe utile pour l'envoi de mail
 */
public class MailBox {
    private final String serverHost;
    private final String serverPort;
    private final String password;
    private final String username;

    /**
     * Constructeur
     */
    public MailBox(){
        this.username = "pharmacolaustin@gmail.com";
        this.password = "MyP@$$w0rD.7931";
        this.serverHost = "smtp.gmail.com";
        this.serverPort = "587";
    }

    /**
     * Méthode d'envoi d'un email vers 1 destinataire avec sujet et contenu en text/plain
     * @param mailTo : les adresses mails destinataires
     * @param subjet : le sujet du mail
     * @param msg : le contenu texte du mail
     */
    public void sendTxt(String mailTo, String subjet, String msg){
        this.sendTxt(new String[]{mailTo}, null, subjet,msg, "text/plain");
    }
    /**
     * Méthode d'envoi d'un email vers 1 ou plusieurs destinataire avec 1 ou plusieurs mail en Cc
     * Possibilité d'envoyer un type html au lieu de text simple
     * Exemle: contentType : text/html, text,plain, ...
     * @param mailTo : les adresses mails destinataires
     * @param mailCc : les adresses mails en copie
     * @param subjet : le sujet du mail
     * @param msg : le contenu texte du mail
     * @param contentType : le type de contenu
     */
    public void sendTxt(String[] mailTo, String[] mailCc, String subjet, String msg, String contentType){
        Properties props = new Properties();
        props.put("mail.smtp.host", this.serverHost);
        props.put("mail.smtp.socketFactory.port", this.serverPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.SocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", this.serverPort);
        props.put("mail.smtp.ssl.enable", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        Session session = Session.getInstance(props);
        try {
            Address[] multipleAddress = new Address[mailTo.length];
            for (int i = 0; i<mailTo.length; i++ ) {
                multipleAddress[i]= new InternetAddress(mailTo[i]);
            }
            Address[] multipleCc;
            if (mailCc == null){
                multipleCc = null;
            }else{
                multipleCc = new Address[mailCc.length];
                for (int i = 0; i<mailCc.length; i++ ) {
                    multipleCc[i]= new InternetAddress(mailCc[i]);
                }
            }
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.username));
            message.setRecipients(Message.RecipientType.TO,multipleAddress);
            message.setRecipients(Message.RecipientType.CC,multipleCc);
            message.setContent(msg, contentType); // ex: text/html, text/plain
            message.setSubject(subjet);
            Transport transport = null;
            try {
                transport = session.getTransport("smtp");
                transport.connect(this.username, this.password);
                transport.sendMessage(message, multipleAddress);
                if (multipleCc != null ){
                    transport.sendMessage(message, multipleCc);
                }

            } catch (MessagingException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (transport != null) {
                        transport.close();
                    }
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
