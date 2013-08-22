package br.com.cineagora.util.registros;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

public class HibernateAwareObjectMapper_OLD extends ObjectMapper {
    public HibernateAwareObjectMapper_OLD() {
        registerModule(new Hibernate4Module());
    }
}
