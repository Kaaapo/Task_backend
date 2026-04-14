package com.taskmanager.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

/**
 * Gmail muestra la contraseña de aplicación con espacios; el cliente SMTP suele necesitarla sin espacios.
 */
@Component
public class GmailSmtpPasswordSanitizer implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof JavaMailSenderImpl impl) {
            String p = impl.getPassword();
            if (p != null && p.contains(" ")) {
                impl.setPassword(p.replaceAll("\\s+", ""));
            }
        }
        return bean;
    }
}
