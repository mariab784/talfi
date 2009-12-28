/**
 * 
 */
package modelo.automatas;

import java.util.ArrayList;

/**
 * Clase que define las funciones de los alfabetos de la plicaci�n
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
	 * por el car�cter "/"
	 * @return lista de letras menos la lambda
	 */
	public ArrayList<String> dameListaLetrasmenosL();
	
	/**
	 * Borra del alfabeto el car�cter lambda si est� en �l
	 */
	public void dameAlfabetoMenosL();
	
	/**
	 * A�ade una letra a la lista de letras
	 * @param l la nueva letra a a�adir
	 */
	public void aniadirLetra(String l);
	
	/**
	 * Elimina del alfabeto la letra de la posici�n pasada 
	 * @param pos posici�n de la letra a eliminar
	 */
	public void eliminaPos(int pos);
	
	/**
	 * Comprueba si una letra esta en el alfabeto
	 * @param l la letra que se quiere comprobar si est�
	 * @return true: est�, false: no est
	 */
	public boolean estaLetra(String l);
	
	/**
	 * M�todo que comprueba la igualdad de alfabetos
	 * @param a el alfabeto del que se quiere comprobar la igualdad
	 * @return true si son iguales, false en otro caso
	 */
	public boolean equals(Alfabeto a);
	
	/**
	 * M�todo que devuelve las letras del alfabeto en forma de lista ArrayList
	 * @return al ArrayList de letras del alfabeto
	 */
	public ArrayList<String> getListaLetras();
	
	/**
	 * M�todo que devuelve el n�mero de letras que tiene el alfabeto
	 * @return n�mero de letras totales
	 */
	public int cuentaLetras();
	
	/**
	 * M�todo que devuelve la letra de la posici�n pasada
	 * @param pos posici�n de la que se quiere averiguar la letra
	 * @return la letra de la posici�n pasada
	 */
	public String dameLetraPos(int pos);
	
}
