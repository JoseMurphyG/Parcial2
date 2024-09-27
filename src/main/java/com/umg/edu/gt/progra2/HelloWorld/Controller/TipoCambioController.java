package com.umg.edu.gt.progra2.HelloWorld.Controller;

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

    /**
     * Endpoint to fetch the exchange rate range from the SOAP service.
     *
     * @param fechaInicio The start date for the exchange rate range (format: yyyy-MM-dd).
     * @param fechaFin The end date for the exchange rate range (format: yyyy-MM-dd).
     * @return JSON response with the exchange rate data for the given range.
     */
    @GetMapping("/tipoCambioRango")
    public ResponseEntity<Object> obtenerTipoCambioRango(@RequestParam String fechaInicio, @RequestParam String fechaFin) {
        // Llamada al servicio SOAP para obtener el tipo de cambio en el rango de fechas
        String response = tipoCambioSoapService.obtenerTipoCambioRango(fechaInicio, fechaFin);

        // Si la respuesta contiene un error, devolver un estado 500 con el mensaje de error
        if (response.contains("Error")) {
            return ResponseEntity.status(500).body("Error: " + response);
        }

        try {
            // Convertir la respuesta SOAP XML en un objeto JSON
            JSONObject xmlJSONObj = XML.toJSONObject(response);

            // Extraer los datos específicos del resultado (ajustar según la estructura de tu XML SOAP)
            JSONObject tipoCambioRangoResult = xmlJSONObj.getJSONObject("soap:Envelope")
                    .getJSONObject("soap:Body")
                    .getJSONObject("TipoCambioRangoResponse")
                    .getJSONObject("TipoCambioRangoResult");

            // Log para depuración (opcional)
            System.out.println("TipoCambioRangoResult: " + tipoCambioRangoResult);

            // Devolver el objeto JSON extraído como respuesta
            return ResponseEntity.ok(tipoCambioRangoResult);

        } catch (Exception e) {
            // Imprimir el stack trace en caso de error y devolver una respuesta de error
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error procesando la respuesta XML: " + e.getMessage());
        }
    }
}
