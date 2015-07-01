/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mailsearchcore;

import entities.Campaign;
import entities.Email;
import java.util.ArrayList;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

/**
 *
 * @author paul
 */
public class Mailer extends Thread {
    private static final String USERNAME = "paul.fiorentino@epsi.fr";
    private static final String PASSWORD = "775SYH";
    
    private final CampaignManager campaignManager;
    private final int threadNumber;
    
    private boolean interrputFlag = false;
    
    public Mailer(CampaignManager campaignManager,int threadNumber) {
        this.campaignManager = campaignManager;
        this.threadNumber = threadNumber;
    }
    
    public int getThreadNumber() {
        return threadNumber;
    }
    
    public void shutdown() {
        interrputFlag = true;
        System.out.println("Mailer "+threadNumber+" shutting down...");
    }
    
    public boolean isShuttingDown() {
        return interrputFlag;
    }
    
    @Override
    public void run() {
        while(!interrputFlag){
            Campaign campaign = campaignManager.getMailingCampaign();
        
            try {
                if (campaign != null){
                    if (campaign.getMailContent() == null || campaign.getMailContent().isEmpty() || 
                        campaign.getMailObject() == null || campaign.getMailObject().isEmpty()) {
                        System.out.println("Mailer "+threadNumber+": Error incorrect campaign configuration...");
                        campaignManager.declareCampaignAsFailed(campaign);
                    } else {
                        ArrayList<Email> emails = new ArrayList<>(campaign.getEmailCollection());
                        if (emails.isEmpty()) {
                            System.out.println("Mailer "+threadNumber+": Error no emails found for this campaign...");
                            campaignManager.declareCampaignAsFailed(campaign);
                        } else {
                            System.out.println("Mailer "+threadNumber+": Start mailing campaign "+campaign.getKeyword());
                            
                            for (Email email : emails){
                                sendMail(email.getEmail(), campaign.getMailObject(), campaign.getMailContent(), campaign.getMailFileContent(), campaign.getMailFileName());
                                sleep(1000);
                            }
                            
                            campaignManager.noticeMailingDone(campaign);
                        }
                    }
                } else {
                    System.out.println("Mailer "+threadNumber+": No mail campaign to process...");
                }
                
                sleep(2000);
            } catch (InterruptedException ex) {
                System.out.println("Mailer "+threadNumber+": Awaken during sleep...");
            }
        }
    }
    
    private void sendMail(String to, String subject, String text) {
        this.sendMail(to, subject, text, null, null);
    }
    
    private void sendMail(String to, String subject, String text, byte[] data, String filename) {
        String from = "paul.fiorentino@epsi.fr";
        final String username = USERNAME;
        final String password = PASSWORD;

        String host = "smtp.office365.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
        new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message;
            
            if (data != null && filename != null){
                message = generateAttachmentMessage(to, subject, text, data, filename, from, session);
            } else {
                message = generateTextMessage(to, subject, text, from, session);
            }
           
           Transport.send(message);

           System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
              throw new RuntimeException(e);
        }
    }
    
    private Message generateTextMessage(String to, String subject, String text, String from, Session session) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));

        message.setRecipients(Message.RecipientType.TO,
        InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(text);
        
        return message;
    }
    
    private Message generateAttachmentMessage(String to, String subject, String text, byte[] data, String filename, String from, Session session) throws MessagingException {
         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress(from));
         message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(to));

         message.setSubject(subject);
         BodyPart messageBodyPart = new MimeBodyPart();
         messageBodyPart.setText(text);
         Multipart multipart = new MimeMultipart();
         multipart.addBodyPart(messageBodyPart);

         messageBodyPart = new MimeBodyPart();
         DataSource source = new ByteArrayDataSource(data, "application/x-any");
         messageBodyPart.setDataHandler(new DataHandler(source));
         messageBodyPart.setFileName(filename);
         multipart.addBodyPart(messageBodyPart);

         // Send the complete message parts
         message.setContent(multipart);
         
         return message;
    }
}
