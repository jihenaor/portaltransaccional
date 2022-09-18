package com.serviciudad.compartido.propiedad;



import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.owasp.encoder.Encode;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class LimpiarXSS {

    private LimpiarXSS() {
        // privado
    }
    public static String limpiar(String value) {

        value = Encode.forHtml(value);

        //Convierte a codificaci�n utf-8
        value = value.trim();
        Charset charset = StandardCharsets.ISO_8859_1;
        value = new String(value.getBytes(charset), charset);

        value = StringEscapeUtils.unescapeHtml3(value);
        value = StringEscapeUtils.unescapeHtml4(value);
        value = StringEscapeUtils.unescapeJava(value);
        value = StringEscapeUtils.unescapeXml(value);
        value = StringEscapeUtils.unescapeJava(value);
        value = StringEscapeUtils.unescapeCsv(value);
        value = Encode.forJavaScript(value);

        //Sanea el los valores entrada de html y javascript para protecci�n
        //contra XSS
        value = Jsoup.clean(value, Whitelist.none());
        value = StringEscapeUtils.unescapeHtml4(value);
        value = StringEscapeUtils.unescapeHtml3(value);
        return value;
    }
}
