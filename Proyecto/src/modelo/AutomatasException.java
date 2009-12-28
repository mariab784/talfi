/**
 * 
 */
package modelo;

/**
 * Clase de excepci�n definida en al aplicaci�n y que se lanza siempre que se
 * en cuentra un problema en la misma
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class AutomatasException extends Exception {
	
	private static final long serialVersionUID = 1L;
	protected String mensaje;
	
	/**
	 * Constructor de la excepci�n con el mensaje que se le
	 * pasa por par�metro
	 * @param s mensaje de la excepci�n
	 */
	public AutomatasException(String s){
		mensaje=s;
	}
	
	/**
	 * M�todo accesor del mensaje
	 * @return mensaje de la excepci�n
	 */
	public String getMensaje(){
		return mensaje;
	}

}

