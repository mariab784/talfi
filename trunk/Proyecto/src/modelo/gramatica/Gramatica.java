/**
 * 
 */
package modelo.gramatica;
import java.util.*;

import vista.vistaGrafica.Estado;

import modelo.automatas.Alfabeto;
import modelo.automatas.Alfabeto_Pila;

/**
 * @author Rocio Barrigüete, Mario Huete, Luis San Juan
 *
 */
public abstract class Gramatica {
	
	private ArrayList<String> variables;
	private ArrayList<String> simbolos;
	private HashMap<String,ArrayList<Produccion>> producciones;
	private String variableInicial; //meter en xml como con Z y lambda? de momento pongo S a pincho
	
	public Gramatica(){}
	
	public Gramatica(Alfabeto alf, ArrayList<Estado> est,Alfabeto_Pila alfPila){
		
		variables = new ArrayList<String>();
		simbolos = alf.getListaLetras();
		variableInicial = "S";
		creaVariables(est, alfPila);
	}
	
	private void creaVariables(ArrayList<Estado> est,Alfabeto_Pila alfPila){
		
		variables.add(variableInicial);
		
		
		int numEstados = est.size(); int tamAlfPila = alfPila.getListaLetras().size();
		String e1; String sp;
		
		for (int i = 0; i < numEstados; i++){
			e1 = est.get(i).getEtiqueta();
			for (int j = 0; j < tamAlfPila; j++){
				sp = alfPila.getListaLetras().get(j);
				for (int k = 0; k < numEstados; k++){
					variables.add("["+e1+sp+est.get(k).getEtiqueta()+"]");
					
				}
		
			}
	}
	}
	
	/**
	 * @return the variables
	 */
	public ArrayList<String> getVariables() {
		return variables;
	}
	/**
	 * @param variables the variables to set
	 */
	public void setVariables(ArrayList<String> variables) {
		this.variables = variables;
	}
	/**
	 * @return the simbolos
	 */
	public ArrayList<String> getSimbolos() {
		return simbolos;
	}
	/**
	 * @param simbolos the simbolos to set
	 */
	public void setSimbolos(ArrayList<String> simbolos) {
		this.simbolos = simbolos;
	}
	/**
	 * @return the producciones
	 */
	public HashMap<String, ArrayList<Produccion>> getProducciones() {
		return producciones;
	}
	/**
	 * @param producciones the producciones to set
	 */
	public void setProducciones(HashMap<String, ArrayList<Produccion>> producciones) {
		this.producciones = producciones;
	}
	/**
	 * @return the variableInicial
	 */
	public String getVariableInicial() {
		return variableInicial;
	}
	/**
	 * @param variableInicial the variableInicial to set
	 */
	public void setVariableInicial(String variableInicial) {
		this.variableInicial = variableInicial;
	}
	
	/**
	 * 
	 * @param clave
	 * @param p
	 * Nuestro método para combinar dentro de la tabla
	 */
	public void anadeProduccion (String clave, Produccion p){
		if (producciones.containsKey(clave)){
			producciones.get(clave).add(p);
		}
		else {
			ArrayList<Produccion> nuevoP = new ArrayList<Produccion>();
			nuevoP.add(p);
			producciones.put(clave, nuevoP);
		}
	}
	
	
	
	

	
	

}
