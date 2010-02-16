/**
 * 
 */
package controlador;

import vista.Vista;
import modelo.AutomatasException;

/**
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public interface Controlador {
	
	/**
	 * Ejecuta una la query que se pasa,ejemplo: TALF -p -m [rutaxml]
	 * @param query lñnea de comandos que entra
	 * @throws AutomatasException lanza la excpeciñn si ocurre cualquier 
	 * problema al analizar la query o en la ejecuciñn de los algortimos.  
	 */
	public void ejecutaQuery(String query)throws AutomatasException;
	/**
	 * Registra la vista en juego en la aplicacion
	 * @param v vista que se desea registrar
	 */
	public void registraVista(Vista v);
	/**
	 * Trata un mensaje
	 * @param mensaje mensaje para tratar
	 */
	public void trataMensaje(String mensaje);
	/**
	 * Establece el idioma de la aplicacion
	 * @param idioma nuevo idioma de la aplicaciñn
	 */
	public void setIdioma(boolean idioma);

	/**
	 * Devuelve la salida de un algoritmo que se acaba de ejecutar
	 * @return Salida resultado del algoritmo
	 */
	public Object getSalida();
	
	/**
	 * Devuelve la salidaxml de un algoritmo
	 * @return salidaxml fichero xml con el resultado del algoritmo
	 */
	public String salidaXML();
}
