/**
 * 
 */
package modelo.ejercicios;

import modelo.AutomatasException;
import modelo.automatas.Alfabeto;

/**
 * Interfaz que define las funcions de los ejercicios en la aplicación
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public interface Ejercicio {
	
	/**
	 * Devuelve el enunciado de un ejercicio
	 * @return cadena de texto con el enunciado
	 */
	public String getEnunciado();
	
	/**
	 * Devuelve la entrada de un ejercicio
	 * @return Entrada entrada del ejercicio
	 */
	public Object getEntrada();
	/**
	 * Devuelve el resultado de un ejercicio
	 * @return Resultado obtiene el resultado de la correción
	 */
	public Object getResultado();
	/**
	 * Devuelve el alfabeto de un ejercicio
	 * @return Alfabeto alfabeto almacenado en el ejercicio
	 */
	public Alfabeto getAlfabeto();
	
	/**
	 * Autocorrige el ejercicio
	 * @param respuesta respuesta del usuario
	 * @return true: bien hecho, false: mal hecho
	 * @throws AutomatasException lanza la excepción si hay algún error
	 * al aplicar alguno de los algoritmos
	 */
	public boolean corregir(Object respuesta)throws AutomatasException;
	
	/**
	 * Devuelve el tipo del ejercicio
	 * @return Tipo String que identifica el tipo del ejercicio
	 */
	public String getTipo();
	
	/**
	 * Devuelve la ruta del archivo que contiene al ejercicio
	 * @return Tipo String que identifica la ruta del ejercicio
	 */
	public String getRuta();
	
	/**
	 * Establece la ruta del archivo que contiene al ejercicio
	 * @param ruta ruta que donde se almacena el ejercicio
	 */
	public void setRuta(String ruta);

}
