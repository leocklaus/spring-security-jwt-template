package io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.service;

import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.Code;
import io.github.leocklaus.template_project_with_jwt_security_and_activation.domain.entity.CodeType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendCodeEmail(
            String to,
            String username,
            CodeType type,
            Code code
    ) throws MessagingException {
        String templateName = type.getTemplateName();


        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", type.getUrl());
        properties.put("activation_code", code.getCode());

        Context context = new Context();
        context.setVariables(properties);

        String template = templateEngine.process(templateName, context);

        sendEmail(to, type.getSubject(), template);
    }

    @Async
    public void sendEmail(
            String to,
            String subject,
            String template
    ) throws MessagingException, MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name()
        );


        helper.setFrom("leocklaus@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);

        helper.setText(template, true);

        mailSender.send(mimeMessage);
    }
}
