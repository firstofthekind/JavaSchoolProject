package com.firstofthekind.javaschoolproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.firstofthekind.javaschoolproject.dto.TariffDto;
import com.firstofthekind.javaschoolproject.dto.TariffJsonDto;
import com.firstofthekind.javaschoolproject.service.ContractService;
import com.firstofthekind.javaschoolproject.service.MessageSender;
import com.firstofthekind.javaschoolproject.service.TariffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class HotTariffController {
    private final TariffService service;
    private final ContractService contractService;
    public final MessageSender messageSender;

    @GetMapping(value = "/hottariff", produces = { "application/json;**charset=UTF-8**" })
    public String sendTariffs() throws JsonProcessingException {
        List<TariffJsonDto> tariffDtos = service.getTariffsWithCount();
        messageSender.sendMessage(tariffDtos);
        return messageSender.convertToJson(tariffDtos);
    }
}
