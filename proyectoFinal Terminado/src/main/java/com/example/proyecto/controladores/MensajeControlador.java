/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.proyecto.controladores;

import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.math.BigDecimal;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author user
 */
@Controller
@RequestMapping("/mensaje")
public class MensajeControlador {

        public static final String ACCOUNT_SID = "ACcc20b39020dc7cd0bdd6879a64d90e78";
    /*twilio cid de max*/
    public static final String AUTH_TOKEN = "20dc60757c01d4969f33bf19e0b65851";

    /*token de max*/


    @GetMapping("/crear")
    public String crear() {
        return "mensaje.html";
    }

    @PostMapping("/enviar")
    public String registro(String numero, String mensaje) {
        String destinatario = "whatsapp:+549" + numero;

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(destinatario),
                new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
                mensaje).create();
        
        return "mensaje.html";
    }
}
