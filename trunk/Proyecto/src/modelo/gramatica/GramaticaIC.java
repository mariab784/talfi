/**
 * 
 */
package modelo.gramatica;

import java.util.ArrayList;
import java.util.HashMap;
import vista.vistaGrafica.Estado;

import modelo.automatas.Alfabeto;
import modelo.automatas.Alfabeto_Pila;

/**
 * @author Rocio Barrigüete, Mario Huete, Luis San Juan
 *
 */
public class GramaticaIC extends Gramatica {

	/**
	 * 
	 */

	
	public GramaticaIC(){
		super();
	}
	
	public GramaticaIC(ArrayList<String> v, ArrayList<String> s, 
			HashMap<String,ArrayList<Produccion>> p,String vInicial){
		
		super(v,s,p,vInicial);
	}
	
	public GramaticaIC(Alfabeto alf, ArrayList<Estado> est,Alfabeto_Pila alfPila) {
		// TODO Auto-generated constructor stub
		super(alf,est,alfPila);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	

}
