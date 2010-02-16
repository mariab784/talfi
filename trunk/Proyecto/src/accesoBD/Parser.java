/**
 * 
 */
package accesoBD;

import modelo.AutomatasException;
import modelo.automatas.Automata;
import modelo.expresion_regular.ExpresionRegular;

/**
 * Interfaz que especifica las funciones que debe tener los parsers
 * de la aplicaciñn
 * @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public interface Parser {
	/**
	 * Extrae un automata de un xml
	 * @param ruta ruta del xml donde se buscarñ
	 * @return Automata resultado de la extracciñn
	 * @throws AutomatasException se lanza si hay errore al abrir o encontrar el archivo
	 */
	public Automata extraerAutomata(String ruta)throws AutomatasException;
	/**
	 * Extrae una expresion regular de un xml
	 * @param ruta ruta del xml donde se buscarñ
	 * @return ExpresionRegular resultado de la extracciñn
	 * @throws AutomatasException se lanza si hay errore al abrir o encontrar el archivo
	 */
	public ExpresionRegular extraerER(String ruta)throws AutomatasException;

} 
