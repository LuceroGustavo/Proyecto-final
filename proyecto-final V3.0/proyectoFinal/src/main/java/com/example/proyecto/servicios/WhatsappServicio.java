/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.proyecto.servicios;

import static com.example.proyecto.controladores.mensajeControlador.ACCOUNT_SID;
import static com.example.proyecto.controladores.mensajeControlador.AUTH_TOKEN;
import com.example.proyecto.excepciones.MiException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.stereotype.Service;

/**
 *
 * @author AMD
 */
@Service
public class WhatsappServicio {
    
            public static final String ACCOUNT_SID = "ACcc20b39020dc7cd0bdd6879a64d90e78";  /*twilio cid de max*/
  public static final String AUTH_TOKEN = "3fced43a36732efdb30779f9c07c3a69"; /*token de max*/
      
    public String enviarMensaje(String numero, String mensaje) throws MiException{
        
    String destinatario="whatsapp:+549"+numero;
     
        try {
          
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
      Message message = Message.creator(
      new com.twilio.type.PhoneNumber(destinatario),
      new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
      mensaje).create();

    return "EL MENSAJE SE ENVIO CON EXITO AL NUMERO VINCULADO AL MAIL";
        } catch (Exception e) {
            throw new MiException("ERROR AL ENVIAR EL TOKEN "+e.toString());
        }

                   
    }
}
