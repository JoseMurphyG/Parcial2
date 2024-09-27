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

@Service
public class TipoCambioSoapService {

    private static final Logger logger = LogManager.getLogger(TipoCambioSoapService.class);

    @Autowired
    private RestTemplate restTemplate;

    public String obtenerTipoCambioRango(String fechaInicio, String fechaFin) {
        logger.info("Iniciando solicitud para obtener el tipo de cambio entre {} y {}", fechaInicio, fechaFin);

        String fechaInicioFormatted = formatDate(fechaInicio);
        String fechaFinFormatted = formatDate(fechaFin);

        // Estructura del SOAP request corregida
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
        // Convertir yyyy-MM-dd a dd/MM/yyyy
        String[] parts = date.split("-");
        if (parts.length == 3) {
            logger.debug("Formato de fecha antes de la conversión: {}", date);
            String formattedDate = parts[2] + "/" + parts[1] + "/" + parts[0];
            logger.debug("Formato de fecha después de la conversión: {}", formattedDate);
            return formattedDate;
        } else {
            logger.warn("Formato de fecha incorrecto: {}", date);
            return date; // Devolver la fecha original si el formato es incorrecto
        }
    }
}
