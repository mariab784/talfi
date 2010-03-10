/**
 * 
 */
package modelo.gramatica;

import java.util.ArrayList;

/**
 * @author Rocio Barrigüete, Mario Huete, Luis San Juan
 *
 */
public class Produccion {
	
	private ArrayList<String> concatenacion;
	
	/**
	 * 
	 */
	public Produccion() {
		// TODO Auto-generated constructor stub
		this.concatenacion = new ArrayList<String>();
	}

	/**
	 * @return the concatenacion
	 */
	public ArrayList<String> getConcatenacion() {
		return concatenacion;
	}

	/**
	 * @param concatenacion the concatenacion to set
	 */
	public void setConcatenacion(ArrayList<String> concatenacion) {
		this.concatenacion = concatenacion;
	}
	
	public void anadeCadena (String cadena){
		this.concatenacion.add(cadena);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
