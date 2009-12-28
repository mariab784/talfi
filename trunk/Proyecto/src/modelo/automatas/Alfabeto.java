/**
 * 
 */
package modelo.automatas;

import java.util.ArrayList;

/**
 * Clase que define las funciones de los alfabetos de la plicación
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public interface Alfabeto {
	
	/**
	 * Devuelve la lista de letras
	 * @return Lista de letras
	 */
	public ArrayList<String> dameListaLetras();
	
	/**
	 * Devuelve la lista de letras menos lambda representada
	 * por el carácter "/"
	 * @return lista de letras menos la lambda
	 */
	public ArrayList<String> dameListaLetrasmenosL();
	
	/**
	 * Borra del alfabeto el carácter lambda si está en él
	 */
	public void dameAlfabetoMenosL();
	
	/**
	 * Añade una letra a la lista de letras
	 * @param l la nueva letra a añadir
	 */
	public void aniadirLetra(String l);
	
	/**
	 * Elimina del alfabeto la letra de la posición pasada 
	 * @param pos posición de la letra a eliminar
	 */
	public void eliminaPos(int pos);
	
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
	public boolean equals(Alfabeto a);
	
	/**
	 * Método que devuelve las letras del alfabeto en forma de lista ArrayList
	 * @return al ArrayList de letras del alfabeto
	 */
	public ArrayList<String> getListaLetras();
	
	/**
	 * Método que devuelve el número de letras que tiene el alfabeto
	 * @return número de letras totales
	 */
	public int cuentaLetras();
	
	/**
	 * Método que devuelve la letra de la posición pasada
	 * @param pos posición de la que se quiere averiguar la letra
	 * @return la letra de la posición pasada
	 */
	public String dameLetraPos(int pos);
	
}
