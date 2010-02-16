/**
 * 
 */
package modelo;

/**
 * Clase de excepciñn definida en al aplicaciñn y que se lanza siempre que se
 * en cuentra un problema en la misma
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class AutomatasException extends Exception {
	
	private static final long serialVersionUID = 1L;
	protected String mensaje;
	
	/**
	 * Constructor de la excepciñn con el mensaje que se le
	 * pasa por parñmetro
	 * @param s mensaje de la excepciñn
	 */
	public AutomatasException(String s){
		mensaje=s;
	}
	
	/**
	 * Mñtodo accesor del mensaje
	 * @return mensaje de la excepciñn
	 */
	public String getMensaje(){
		return mensaje;
	}

}

