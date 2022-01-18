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

    @Scheduled(cron = "0 */2 * * * *")
    public void scheduleFixedRateTaskAsync()  {
        facturaService.seleccionarPagosPendientes();
    }

}