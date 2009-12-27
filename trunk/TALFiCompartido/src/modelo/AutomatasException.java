/**
 * 
 */
package modelo;

/**
 * Clase de excepción definida en al aplicación y que se lanza siempre que se
 * en cuentra un problema en la misma
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class AutomatasException extends Exception {
	
	private static final long serialVersionUID = 1L;
	protected String mensaje;
	
	/**
	 * Constructor de la excepción con el mensaje que se le
	 * pasa por parámetro
	 * @param s mensaje de la excepción
	 */
	public AutomatasException(String s){
		mensaje=s;
	}
	
	/**
	 * Método accesor del mensaje
	 * @return mensaje de la excepción
	 */
	public String getMensaje(){
		return mensaje;
	}

}

