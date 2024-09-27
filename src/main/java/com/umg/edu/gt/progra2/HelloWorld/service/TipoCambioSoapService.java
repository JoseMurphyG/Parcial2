package com.umg.edu.gt.progra2.HelloWorld.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class TipoCambioSoapService {

    private static final Logger logger = LogManager.getLogger(TipoCambioSoapService.class);

    @Autowired
    private RestTemplate restTemplate;

    public String obtenerTipoCambioRango(String fechaInicio, String fechaFin) {
        // No es necesario formatear aquí si el DTO ya lo hace
        String fechaInicioFormatted = fechaInicio;
        String fechaFinFormatted = fechaFin;

        // Estructura del SOAP request
        String soapRequest =
                "<soap:Envelope xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/' " +
                        "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
                        "xmlns:xsd='http://www.w3.org/2001/XMLSchema'>" +
                        "<soap:Body>" +
                        "<TipoCambioRango xmlns='http://www.banguat.gob.gt/variables/ws/'>" +
                        "<fechaInicial>" + fechaInicioFormatted + "</fechaInicial>" +
                        "<fechaFinal>" + fechaFinFormatted + "</fechaFinal>" +
                        "</TipoCambioRango>" +
                        "</soap:Body>" +
                        "</soap:Envelope>";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);
        headers.add("SOAPAction", "http://www.banguat.gob.gt/variables/ws/TipoCambioRango");

        HttpEntity<String> requestEntity = new HttpEntity<>(soapRequest, headers);

        try {
            logger.info("Enviando solicitud SOAP a {}", "https://www.banguat.gob.gt/variables/ws/TipoCambio.asmx");
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    "https://www.banguat.gob.gt/variables/ws/TipoCambio.asmx",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            logger.info("Respuesta recibida correctamente");
            return responseEntity.getBody();
        } catch (Exception e) {
            logger.error("Error en la solicitud SOAP: {}", e.getMessage(), e);
            return "Error en la solicitud SOAP: " + e.getMessage();
        }
    }

    private String formatDate(String date) {
        try {
            // Convertir yyyy-MM-dd a dd/MM/yyyy
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(date, inputFormatter);
            return localDate.format(outputFormatter);
        } catch (Exception e) {
            logger.warn("Error al formatear la fecha: {}", e.getMessage(), e);
            return date; // Devolver la fecha original si hay un error en el formato
        }
    }
}
