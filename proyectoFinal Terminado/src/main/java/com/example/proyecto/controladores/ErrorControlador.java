/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.proyecto.controladores;

import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author user
 */

@Controller
public class ErrorControlador implements ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {

        ModelAndView errorPage = new ModelAndView("error");

        String mensajedeError = "";

        int codigodeError = getErrorCode(httpRequest);

        switch (codigodeError) {
            case 400: {
                mensajedeError = "El recurso solicitado no existe.";
                break;
            }
            case 403: {
                mensajedeError = "No tiene permisos para acceder al recurso.";
                break;
            }
            case 401: {
                mensajedeError = "No se encuentra autorizado.";
                break;
            }
            case 404: {
                mensajedeError = "El recurso solicitado no fue encontrado.";
                break;
            }
            case 500: {
                mensajedeError = "Ocurrió un error interno culpa a MAX!!!.";
                break;
            }
        }

        errorPage.addObject("codigo", codigodeError);
        errorPage.addObject("mensaje", mensajedeError);
        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
    }

    public String getErrorPath() {
        return "/error.html";
    }

}
