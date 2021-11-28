package com.firstofthekind.javaschoolproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.firstofthekind.javaschoolproject.dto.TariffDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class MessageSender {
    private final ActiveMQConnectionFactory connectionFactory;

    public void sendMessage(List<TariffDto> standDto) {
        try {
            Connection connection = connectionFactory.createQueueConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            log.info("Session created > " + session.toString());
            Queue queue = session.createQueue("QUEUE");
            log.info("Queue created > " + queue.getQueueName());

            MessageProducer messageProducer = session.createProducer(queue);
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

            ActiveMQTextMessage mqTextMessage = (ActiveMQTextMessage) session.createTextMessage();
            String textMessage = convertToJson(standDto);
            mqTextMessage.setText(textMessage);
            messageProducer.send(mqTextMessage);
            log.info("Message sent > " + textMessage);
            session.close();
            connection.close();
        }
        catch (Exception e) {
            log.error("Error sending message", e);
        }
    }
    public String convertToJson(List<TariffDto> dto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString(dto);
    }
}
