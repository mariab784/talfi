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
	 * Metodo accesor de la expresi�n regular
	 * @return la expresi�n regular almacenada
	 */
	public String getExpresionRegular();
	
	/**
	 * M�todo accesor del �rbol sin�tactico de la expresi�n
	 * @return el arbol sint�ctico de la expresi�n
	 */
	public ArbolER getArbolER();
	
	/**
	 * M�todo accesor del alfabeto de la expresi�n
	 * @return el alfabeto de la expresi�n
	 */
	public Alfabeto getAlfabeto();
	
	/**
	 * M�todo modificador del alfabeto de la expresion
	 * @param alfabeto lista de latras que conforman un alfabeto
	 */
	public void setAlfabeto(Alfabeto alfabeto);
}
