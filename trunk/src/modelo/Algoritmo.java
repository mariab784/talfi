/**
 * 
 */
package modelo;

import controlador.Controlador;
import modelo.automatas.Automata;
/**
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public interface Algoritmo {

	/**
	 * Ejecuta el algoritmo
	 * @param muestraPasos decide si por pasos o no
	 * @return Automata resultante
	 */
	public Automata ejecutar(boolean muestraPasos);
	/**
	 * Registra el controlador
	 * @param controlador
	 */
	public void registraControlador(Controlador controlador);
	/**
	 * Devuelve el xml resultante
	 * @return xml
	 */
	public String getXML();
}
