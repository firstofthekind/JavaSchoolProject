package com.firstofthekind.javaschoolproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.dto.TariffJsonDto;
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

    /**
     * The activemq server is used to send tariff data
     * to the second application. To begin with, a connection
     * is established, then a session and a queue are created.
     * Then the tariff data is converted to json format and sent to the queue.
     * @param tariffJsonDtoList - data about the tariffs to be sent to the second application.
     */

    public void sendMessage(List<TariffJsonDto> tariffJsonDtoList) {
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
            String textMessage = convertToJson(tariffJsonDtoList);
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

    public String convertToJson(List<TariffJsonDto> dto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString(dto);
    }
}
