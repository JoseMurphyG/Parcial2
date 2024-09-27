package com.umg.edu.gt.progra2.HelloWorld.Controller;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.format.DateTimeFormatter;

import com.umg.edu.gt.progra2.HelloWorld.service.TipoCambioSoapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.json.JSONObject;
import org.json.XML;


@RestController
public class TipoCambioController {

    @Autowired
    private TipoCambioSoapService tipoCambioSoapService;


    @GetMapping("/tipoCambioRango")
    public ResponseEntity<Object> obtenerTipoCambioRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        // Crear un formateador para el formato dd/MM/yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Convertir LocalDate a String en formato 'dd/MM/yyyy'
        String fechaInicioStr = fechaInicio.format(formatter);
        String fechaFinStr = fechaFin.format(formatter);

        // Llamada al servicio SOAP con el formato correcto de fecha
        String response = tipoCambioSoapService.obtenerTipoCambioRango(fechaInicioStr, fechaFinStr);


        if (response.contains("Error")) {
            return ResponseEntity.status(500).body("Error: " + response);
        }

        try {

            JSONObject xmlJSONObj = XML.toJSONObject(response);


            JSONObject tipoCambioRangoResult = xmlJSONObj.getJSONObject("soap:Envelope")
                    .getJSONObject("soap:Body")
                    .getJSONObject("TipoCambioRangoResponse")
                    .getJSONObject("TipoCambioRangoResult");

            // Log para depuración (opcional)
            System.out.println("TipoCambioRangoResult: " + tipoCambioRangoResult);

            return ResponseEntity.ok(tipoCambioRangoResult);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error procesando la respuesta XML: " + e.getMessage());
        }
    }
}