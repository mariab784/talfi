package modelo.algoritmos;

public class Registro {
	private String paso;
	private boolean marcado;
	private String estaditos; //Indica porque estados se ha marcado la tabla
	
	public Registro (String pas, boolean marca, String porestos){
		paso = pas;
		marcado = marca;
		estaditos = porestos;
	}
	
	public void setPaso (String pas){
		paso = pas;
	}
	public String getPaso (){
		return paso;
	}
	
	public void setMarcado (boolean marca){
		marcado = marca;
	}
	
	public boolean getMarcado (){
		return marcado;
	}
	
	public String getEstados(){
		return estaditos;
	}
	
	public void setEstados(String porestos){ //porquñ estados lo meto
		estaditos = porestos;
	}
	
}


