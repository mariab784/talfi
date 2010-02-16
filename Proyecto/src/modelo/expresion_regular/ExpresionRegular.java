package modelo.expresion_regular;

import modelo.automatas.Alfabeto;

/**
 * Interfaz que define los metodos que debe implementar las expresiones
 * regulares.
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public interface ExpresionRegular {

	/**
	 * Metodo accesor de la expresiñn regular
	 * @return la expresiñn regular almacenada
	 */
	public String getExpresionRegular();
	
	/**
	 * Mñtodo accesor del ñrbol sinñtactico de la expresiñn
	 * @return el arbol sintñctico de la expresiñn
	 */
	public ArbolER getArbolER();
	
	/**
	 * Mñtodo accesor del alfabeto de la expresiñn
	 * @return el alfabeto de la expresiñn
	 */
	public Alfabeto getAlfabeto();
	
	/**
	 * Mñtodo modificador del alfabeto de la expresion
	 * @param alfabeto lista de latras que conforman un alfabeto
	 */
	public void setAlfabeto(Alfabeto alfabeto);
}
