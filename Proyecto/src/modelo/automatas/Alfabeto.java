/**
 * 
 */
package modelo.automatas;

import java.util.ArrayList;

/**
 * Clase que define las funciones de los alfabetos de la plicaciñn
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
	 * por el carñcter "/"
	 * @return lista de letras menos la lambda
	 */
	public ArrayList<String> dameListaLetrasmenosL();
	
	/**
	 * Borra del alfabeto el carñcter lambda si estñ en ñl
	 */
	public void dameAlfabetoMenosL();
	
	/**
	 * Añade una letra a la lista de letras
	 * @param l la nueva letra a añadir
	 */
	public void aniadirLetra(String l);
	
	/**
	 * Elimina del alfabeto la letra de la posiciñn pasada 
	 * @param pos posiciñn de la letra a eliminar
	 */
	public void eliminaPos(int pos);
	
	/**
	 * Comprueba si una letra esta en el alfabeto
	 * @param l la letra que se quiere comprobar si estñ
	 * @return true: estñ, false: no est
	 */
	public boolean estaLetra(String l);
	
	/**
	 * Mñtodo que comprueba la igualdad de alfabetos
	 * @param a el alfabeto del que se quiere comprobar la igualdad
	 * @return true si son iguales, false en otro caso
	 */
	public boolean equals(Alfabeto a);
	
	/**
	 * Mñtodo que devuelve las letras del alfabeto en forma de lista ArrayList
	 * @return al ArrayList de letras del alfabeto
	 */
	public ArrayList<String> getListaLetras();
	
	/**
	 * Mñtodo que devuelve el nñmero de letras que tiene el alfabeto
	 * @return nñmero de letras totales
	 */
	public int cuentaLetras();
	
	/**
	 * Mñtodo que devuelve la letra de la posiciñn pasada
	 * @param pos posiciñn de la que se quiere averiguar la letra
	 * @return la letra de la posiciñn pasada
	 */
	public String dameLetraPos(int pos);
	 
	public String toString();
	
}
