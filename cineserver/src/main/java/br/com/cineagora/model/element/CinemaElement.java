package br.com.cineagora.model.element;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.cineagora.model.Cinema;
import br.com.cineagora.util.enums.Estado;

@Entity
@XmlRootElement
@Table(name="cinema_element")
public class CinemaElement extends Cinema  {
	private static final long serialVersionUID = -4951491940470026797L;
	
}
