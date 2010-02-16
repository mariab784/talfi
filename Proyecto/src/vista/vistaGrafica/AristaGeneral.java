package vista.vistaGrafica;

public class AristaGeneral {

	protected String etiqueta;
	protected boolean marcada;
	
	public AristaGeneral(String etiqueta){
		
		this.etiqueta=etiqueta;
		marcada=false;
	}
	
	public String getEtiqueta() {
		return etiqueta;
	}
	
	/**
	 * Mñtodo accesro de la caracterñstica de la arista
	 * @return true si la arista esta seleccionada
	 */
	public boolean getMarcada() {
		return marcada;
	}
	
	/**
	 * Marca (o desmarca) la arista
	 * @param marcada si true marca la arista, si false la desmarca
	 */
	public void setMarcada(boolean marcada) {
		this.marcada=marcada;
	}
}
