package com.example.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

@Service
public class EmailService {

    private static TemplateEngine templateEngine;
    private static Context context;

    private JavaMailSender emailSender;

    private Logger LOG = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    public void setEmailSender(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void prepareAndSendEmail() throws MessagingException, UnsupportedEncodingException {

        String htmlTemplate = "templates/emailTemplate";
        String mailTo = "sitharabandara1997@gmail.com";
        initializeTemplateEngine();

        context.setVariable("sender", "Supuni Bandara");
        context.setVariable("mailTo", mailTo);

        String htmlBody = templateEngine.process(htmlTemplate, context);
        sendEmail(mailTo, "Thymeleaf Email Demo", htmlBody);
    }

    private void sendEmail(String mailTo, String subject, String mailBody)
            throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // ✅ Set sender name properly
        helper.setFrom("sitharabandara1997@gmail.com", "Supuni Bandara");
        helper.setTo(mailTo);
        helper.setSubject(subject);

        // ✅ Set email body
        helper.setText(mailBody, true);

        // ✅ Add inline image
        helper.addInline("logoImage", new ClassPathResource("static/images/logo.png"));

        emailSender.send(message);
        LOG.info("Email sent successfully to " + mailTo);
    }

    private static void initializeTemplateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode("HTML");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        context = new Context(Locale.US);
    }
}
