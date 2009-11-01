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
	 * Metodo accesor de la expresión regular
	 * @return la expresión regular almacenada
	 */
	public String getExpresionRegular();
	
	/**
	 * Método accesor del árbol sin´tactico de la expresión
	 * @return el arbol sintáctico de la expresión
	 */
	public ArbolER getArbolER();
	
	/**
	 * Método accesor del alfabeto de la expresión
	 * @return el alfabeto de la expresión
	 */
	public Alfabeto getAlfabeto();
	
	/**
	 * Método modificador del alfabeto de la expresion
	 * @param alfabeto lista de latras que conforman un alfabeto
	 */
	public void setAlfabeto(Alfabeto alfabeto);
}
