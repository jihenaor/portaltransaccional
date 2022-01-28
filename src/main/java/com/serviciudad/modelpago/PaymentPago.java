package com.serviciudad.modelpago;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public final class PaymentPago {
    private String reference;
    private String description;
    private Amount amount;
    private boolean allowPartial;
    private boolean subscribe;
    private Status status;
    private int internalReference;
    private String paymentMethod;
    private String paymentMethodName;
    private String issuerName;
    private String authorization;
    private String receipt;
    private String franchise;
    private boolean refunded;
    private List<ProcessorField> processorFields;
}
