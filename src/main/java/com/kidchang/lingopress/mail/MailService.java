package com.kidchang.lingopress.mail;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.BusinessException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public boolean sendMail(EmailMessage emailMessage) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();


        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.to()); // 메일 수신자
            mimeMessageHelper.setSubject(emailMessage.subject()); // 메일 제목
            mimeMessageHelper.setText(emailMessage.message(), true); // 메일 내용
            javaMailSender.send(mimeMessage);

            log.info("@@@ mail send success");

            return true;

        } catch (MessagingException e) {
            log.info("fail");
            throw new BusinessException(Code.MESSAGING_EXCEPTION);
        }
    }
}
