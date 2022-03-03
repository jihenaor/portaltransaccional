package com.serviciudad;

import com.serviciudad.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduledFixedRateExample {
    @Autowired
    FacturaService facturaService;

    @Scheduled(cron = "0 30 23 */1 * *")
    public void seleccionarPagosPendientes()  {
        facturaService.seleccionarPagosPendientes();
    }

    /*
    @Scheduled(cron = "0 * / 30 * * * *")
    public void seleccionarPagosAprobadosSinregistrar()  {
        facturaService.seleccionarPagosAprobadosSinRegistrar();
    }
    */
}