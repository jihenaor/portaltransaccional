package com.serviciudad.models.factura.application.ConsultaFacturaTipo;

import com.serviciudad.compartido.exceptions.BusinessException;
import com.serviciudad.models.factura.application.ports.FacturaResponse;
import com.serviciudad.models.factura.infraestructure.web.adapters.FacturaTipoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@Slf4j
public class ConsultaFacturaTipoQuery {
    private final ConsultaFacturaTipoService consultaFacturaTipoService;

    public ConsultaFacturaTipoQuery(ConsultaFacturaTipoService consultaFacturaTipoService) {
        this.consultaFacturaTipoService = consultaFacturaTipoService;
    }

    public FacturaResponse handle(FacturaTipoRequest FacturaTipoRequest) throws BusinessException {
        validarDatos(FacturaTipoRequest);

        return consultaFacturaTipoService.consultarFacturaTipo(FacturaTipoRequest);
    }

    private void validarDatos(FacturaTipoRequest facturaTipoRequest) throws BusinessException {
        String mensajeBussinesError = "Los parametros de seleccion son incorrectos e.";
        String msgLog = facturaTipoRequest.toString() + ". ";

        if (facturaTipoRequest.getCodsuscrip() == null || facturaTipoRequest.getCodsuscrip().isEmpty()) {
            msgLog += "Código de suscriptor nulo o vacío";
            log.error(msgLog);

            throw new BusinessException(mensajeBussinesError);
        }

        if (facturaTipoRequest.getTipoFactura() == null) {
            msgLog += "El tipo de factura no puede ser nulo.";
            log.error(msgLog);
            throw new BusinessException(mensajeBussinesError);
        }

        switch (facturaTipoRequest.getTipoFactura()) {
            case "0":
                break;
            case "1":
            case "6":
                if (facturaTipoRequest.getValor() == null || facturaTipoRequest.getValor() == 0) {
                    msgLog += "El valor no puede ser nulo o cero para el tipo de factura "  + facturaTipoRequest.getTipoFactura();
                    log.error(msgLog);
                    throw new BusinessException(mensajeBussinesError);
                }
                if (facturaTipoRequest.getNumerofactura() == null || facturaTipoRequest.getNumerofactura().isEmpty()) {
                    msgLog += "El número de factura no puede ser nulo o vacío para el tipo de factura " + facturaTipoRequest.getTipoFactura();
                    log.error(msgLog + "." + facturaTipoRequest.toString());
                    throw new BusinessException(mensajeBussinesError);
                }

            default:
                if (facturaTipoRequest.getNumerofactura() == null || facturaTipoRequest.getNumerofactura().isEmpty()) {
                    msgLog += "El número de factura no puede ser nulo o vacío para el tipo de factura " + facturaTipoRequest.getTipoFactura() + ".";
                    log.error(msgLog);
                    throw new BusinessException(mensajeBussinesError + log);
                }
        }
    }
}
