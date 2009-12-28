package modelo.automatas;

import java.util.ArrayList;

/**
 * Interface que define las funciones de los alfabetos de pila la aplicación
 *  @author Rocío Barrigüete, Mario Huete, Luis San Juan
 *
 */
public interface Alfabeto_Pila {
	
	/**
	 * Devuelve la lista de letras
	 * @return Lista de letras
	 */
	public ArrayList<String> dameListaLetras();
	
	/**
	 * Añade una letra a la lista de letras
	 * @param l la nueva letra a añadir
	 */
	public void aniadirLetra(String l);
	
	/**
	 * Comprueba si una letra esta en el alfabeto
	 * @param l la letra que se quiere comprobar si está
	 * @return true: está, false: no est
	 */
	public boolean estaLetra(String l);
	
	/**
	 * Método que comprueba la igualdad de alfabetos
	 * @param a el alfabeto del que se quiere comprobar la igualdad
	 * @return true si son iguales, false en otro caso
	 */
	public boolean equals(Alfabeto_Pila a);
	
	/**
	 * Método que devuelve las letras del alfabeto en forma de lista ArrayList
	 * @return al ArrayList de letras del alfabeto
	 */
	public ArrayList<String> getListaLetras();

}
