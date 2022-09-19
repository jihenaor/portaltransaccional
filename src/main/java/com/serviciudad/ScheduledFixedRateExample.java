package com.serviciudad;

import com.serviciudad.service.FacturaEvertecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduledFixedRateExample {
    @Autowired
    FacturaEvertecService facturaService;

    @Scheduled(cron = "0 54 23 */1 * *")
    public void seleccionarPagosPendientes()  {
        facturaService.seleccionarPagosPendientes();
    }


    @Scheduled(cron = "0 */30 * * * *")
    public void seleccionarPagosAprobadosSinregistrar()  {
        facturaService.seleccionarPagosAprobadosSinRegistrar();
    }

}