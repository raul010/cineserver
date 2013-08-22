package br.com.cineagora.model.element;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.cineagora.model.Cinema;

@Entity
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="CinemaRegion", include="all")
@XmlRootElement
@Table(name="cinema_element")
//Estrategia de cache reaproveitada/herdada do pai (Cinema)
public class CinemaElement extends Cinema {
}
