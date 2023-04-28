package ib.projekat.IBprojekat.service.impl;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import ib.projekat.IBprojekat.constant.GlobalConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ComunicationService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTextEmail(String toEmail, String subject, String emailContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ubert472@gmail.com"); // TODO NE DIRATI OVO POLJE
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(emailContent);

        mailSender.send(message);
    }

    public void sentPhoneMessage(String senderPhoneNumber, String receiverPhoneNumber, String textMessage) {

        Twilio.init(GlobalConstants.ACCOUNT_SID, GlobalConstants.AUTH_TOKEN);
        Message message = Message.creator(
                new PhoneNumber(receiverPhoneNumber),
                new PhoneNumber(senderPhoneNumber),
                textMessage).create();
    }
}
