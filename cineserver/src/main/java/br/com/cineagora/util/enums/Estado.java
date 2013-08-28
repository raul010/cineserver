package br.com.cineagora.util.enums;

public enum Estado {
	ACRE("Acre"),
	ALAGOAS("Alagoas"),
	AMAPA("Amap�"),
	AMAZONAS("Amazonas"),
	BAHIA("Bahia"),
	CEARA("Cear�"),
	DISTRITO_FEDERAL("Distrito Federal"),
	ESPIRITO_SANTO("Esp�rito Santo"),
	GOIAS("Goi�s"),
	MARANHAO("Maranh�o"),
	MATO_GROSSO("Mato Grosso"),
	MATO_GROSSO_DO_SUL("Mato Grosso do Sul"),
	MINAS_GERAIS("Minas Gerais"),
	PARA("Par�"),
	PARAIBA("Para�ba"),
	PARANA("Paran�"),
	PERNAMBUCO("Pernambuco"),
	PIAUI("Piau�"),
	RIO_DE_JANEIRO("Rio de Janeiro"),
	RIO_GRANDE_DO_NORTE("Rio Grande do Norte"),
	RIO_GRANDE_DO_SUL("Rio Grande do Sul"),
	RONDONIA("Rond�nia"),
	RORAIMA("Roraima"),
	SANTA_CATARINA("Santa Catarina"),
	SAO_PAULO("S�o Paulo"),
	SERGIPE("Sergipe"),
	TOCANTINS("Tocantins");

	private String geoNome;

	private Estado(String geoNome) {
		this.geoNome = geoNome;
	}

	public String getGeoNome() {
		return geoNome;
	}
}
