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
	 * @param rutahtml ruta del archivo html de la pñgina web
	 */
	public void muestraHtml(boolean pasos, String rutahtml);
	/**
	 * Establece la expresion regular actual
	 * @param exp cadena que identifica la expresion regular del automata en el panel
	 */
	public void setExpresion(String exp);
	
	/**
	 * Borra la expresiñn que haya en la vista grñfica
	 */
	public void deleteExpresion();
	/**
	 * Traduce a xml un automata
	 * @param exp expresion regular del automata si la hay
	 * @param alfabeto alfabeto del automta 
	 * @return xml con el automata
	 * @throws AutomatasException lanza la excepciñn si no se ha podido parsear el
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
	 * Mñtodo que reconstruye la vista por completo de nuevo.
	 */
	public void reconstruir();

	/**
	 * Mñtodo que modifica el automata de la vista
	 * @param a nuevo automata de la vista
	 */
	public void setAutomata(Automata a);
	
	/**
	 * Mñtodo que reconstruye el panel del usuario con
	 * la informaciñn actualizada
	 */
	public void reconstruirPanelUsuario();
	
	/**
	 * Mñtodo que reconstruye el panel JTree de ejemplos/ejercicios con
	 * la informaciñn actualizada de los nuevo ejemplos/ejercicios
	 */
	void reconstruirPanelJTree();
	
	/**
	 * Mñtodo que comprueba si se puede dibujar en la vista
	 * @return true si se puede dibujar, false si no
	 */
	public boolean dibujar();
	
	/**
	 * Metodo que devuelve la ruta del ñltimo directorio usado para abriro guardar
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
