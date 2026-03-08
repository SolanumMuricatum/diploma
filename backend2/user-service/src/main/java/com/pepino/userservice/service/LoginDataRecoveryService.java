package com.pepino.userservice.service;

import com.pepino.userservice.config.props.AwsSesProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.SendEmailRequest;
import software.amazon.awssdk.services.sesv2.model.SesV2Exception;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginDataRecoveryService {
    private final SesV2Client sesV2Client;
    private final AwsSesProps awsConfigProps;

    public void sendEmail(String targetEmail, String login, String password) {
        final SendEmailRequest emailRequest = SendEmailRequest.builder()
                .fromEmailAddress(awsConfigProps.getSenderAddress())
                .destination(d -> d.toAddresses(targetEmail))
                .content(c -> c.simple(s -> s
                        .subject(sub -> sub.data("SharePhoto: данные для входа"))
                        .body(b -> b.text(t -> t.data("Логин для входа: " + login + "; " +
                                "пароль для входа: " + password + ". Обязательно смените пароль после входа!!!" )))
                ))
                .build();

        try {
            sesV2Client.sendEmail(emailRequest);
        } catch (SesV2Exception exception) {
            log.error(
                    "Ses error for notification with target {}: {}",
                    targetEmail,
                    exception.awsErrorDetails().errorMessage()
            );
        } catch (Exception exception) {
            log.error(
                    "Error for notification with target {}: {}",
                    targetEmail,
                    exception.getMessage()
            );
        }
    }
}
