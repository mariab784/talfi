/**
 * 
 */
package vista;

import controlador.Controlador;
import modelo.AutomatasException;
import modelo.automatas.Alfabeto;
import modelo.automatas.Automata;


/**
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public interface Vista {
	
	/**
	 * Ejecuta la vista
	 */
	public void ejecuta();
	/**
	 * Trata el mensaje
	 * @param mensaje mensaje a tratar
	 */
	public void trataMensaje(String mensaje);
	/**
	 * Devuelve el controlador
	 * @return Controlador controlador asociado a la vista
	 */
	public Controlador getControlador();
	/**
	 * Muestra una web html con la ruta como parametro
	 * @param pasos true si se muestran los pasos, false si no
	 * @param rutahtml ruta del archivo html de la p�gina web
	 */
	public void muestraHtml(boolean pasos, String rutahtml);
	/**
	 * Establece la expresion regular actual
	 * @param exp cadena que identifica la expresion regular del automata en el panel
	 */
	public void setExpresion(String exp);
	
	/**
	 * Borra la expresi�n que haya en la vista gr�fica
	 */
	public void deleteExpresion();
	/**
	 * Traduce a xml un automata
	 * @param exp expresion regular del automata si la hay
	 * @param alfabeto alfabeto del automta 
	 * @return xml con el automata
	 * @throws AutomatasException lanza la excepci�n si no se ha podido parsear el
	 * automata en el xml
	 */
	public String traducirXML(String exp, Alfabeto alfabeto)throws AutomatasException;
	
	/**
	 * Desactiva los botones de accion sobre el panel
	 */
	public void desactivaToogleButtons();
	
	/**
	 * Activa los botones de accion sobre el panel
	 */
	public void activaToogleButtons();
	
	/**
	 * M�todo que reconstruye la vista por completo de nuevo.
	 */
	public void reconstruir();

	/**
	 * M�todo que modifica el automata de la vista
	 * @param a nuevo automata de la vista
	 */
	public void setAutomata(Automata a);
	
	/**
	 * M�todo que reconstruye el panel del usuario con
	 * la informaci�n actualizada
	 */
	public void reconstruirPanelUsuario();
	
	/**
	 * M�todo que reconstruye el panel JTree de ejemplos/ejercicios con
	 * la informaci�n actualizada de los nuevo ejemplos/ejercicios
	 */
	void reconstruirPanelJTree();
	
	/**
	 * M�todo que comprueba si se puede dibujar en la vista
	 * @return true si se puede dibujar, false si no
	 */
	public boolean dibujar();
	
	/**
	 * Metodo que devuelve la ruta del �ltimo directorio usado para abriro guardar
	 * @return cadena con la ruta absoluta del directorio
	 */
	public String getRutaPredef();

	/**
	 * Metodo que establece la nueva ruta del ultimo directorio usado para abriro guardar
	 * @param rutaPredef cadena con la nueva ruta
	 */
	public void setRutaPredef(String rutaPredef);
	
	/**
	 * Metodo que devuelve el foco a la vista
	 */
	public void requestFocus();
	
	/**
	 * Activa el boton de pegar automata
	 */
	public void setPegar();

}
