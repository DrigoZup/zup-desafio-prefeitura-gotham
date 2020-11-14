package br.com.zup.prefeitura.enums;

public enum AreaAtuante {
	
	 EDUCACAO("educacao"), SAUDE("saude"), ESPORTES("esportes"),
	    TURISMO("turismo"), CULTURA("cultura"), OBRAS("obras");

	    private String value;

	   private AreaAtuante(String value) {
	        this.value = value;
	    }

	    public String getValue() {
	        return value;
	    }

	    @Override
	    public String toString() {
	        return this.value;
	    }

}
