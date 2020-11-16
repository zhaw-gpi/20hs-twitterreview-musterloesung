package ch.zhaw.gpi.twitterreview;

import javax.inject.Named;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Named("sendMailAdapter")
public class SendMailDelegate implements JavaDelegate {

    @Value("${mail.overrideAddress}")
    private String overrideAddress;

    @Value("${spring.mail.username}")
private String username;

@Autowired
private JavaMailSender javaMailSender;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String to = (String) execution.getVariable("email");
        String checkResult = (String) execution.getVariable("checkResult");
        String checkResultComment = (String) execution.getVariable("checkResultComment");
        String fullName = (String) execution.getVariable("userFullName");
        String tweetContent = (String) execution.getVariable("tweetContent");

        String cc = "kommabt@firma.ch";

        if (!overrideAddress.isEmpty()) {
            to = overrideAddress;
            cc = overrideAddress;
        }

        String anrede = "Hallo " + fullName + "\n\n";
        String gruss = "\n\nIhre Prozessplattform";

        String subject;
        String hauptteil;

        if (checkResult.equals("rejected")) {
            subject = "Tweet abgelehnt";
            hauptteil = "Die folgende Tweet-Anfrage wurde abgelehnt:\n" + tweetContent + "\nDie Begründung ist: "
                    + checkResultComment;
        } else {
            subject = "Tweet veröffentlicht";
            hauptteil = "Die folgende Tweet-Anfrage wurde veröffentlicht:\n" + tweetContent;
        }

        String body = anrede + hauptteil + gruss;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        
        mailMessage.setFrom(username);
        mailMessage.setReplyTo(to);
        mailMessage.setTo(to);
        mailMessage.setCc(cc);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        
        javaMailSender.send(mailMessage);

    }

}
