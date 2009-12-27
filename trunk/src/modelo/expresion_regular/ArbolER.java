package modelo.expresion_regular;

/**
 * Interfaz que define la funcionalidad de los �rbole sint�cticos para
 * expresiones regulares. 
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public interface ArbolER {
	
	/**
	 * M�todo accesro del hijo derecho del �rbol, que a su vez
	 * ser� tambi�en un �rbol del mismo tipo
	 * @return al �rbo,que representa el hijo derecho
	 */
	public ArbolER getHijoDR();
	
	/**
	 * M�todo accesro del hijo izquierdo del �rbol, que a su vez
	 * ser� tambi�en un �rbol del mismo tipo
	 * @return al �rbo,que representa el hijo izquierdo
	 */
	public ArbolER getHijoIZ();
	
	/**
	 * M�todo modificador del hijo derecho del �rbol
	 * @param hijoDR el �rbol que ser� el hijo derecho
	 */
	public void setHijoDR(ArbolER hijoDR);
	
	/**
	 * M�todo modificador del hijo izquierdo del �rbol
	 * @param hijoIZ el �rbol que ser� el hijo izquierdo
	 */
	public void setHijoIZ(ArbolER hijoIZ);
	
	/**
	 * M�todo accesor del car�ceter de la ra�z del �rbol
	 * @return el car�cter que contiene la ra�z
	 */
	public String getRaiz();
	
	/**
	 * M�todo modificador del car�ceter de la ra�z del �rbol
	 * @param raiz el nuevo car�cter que ser� la ra�z del �rbol
	 */
	public void setTextoRaiz(String raiz);
	
	/**
	 * M�todo de conversi�n a cadena de caracteres del �rbol
	 * @return representaci�n en modo texto del �rbol
	 */
	public String toString();
	
	/**
	 * M�todo accesro de la expresi�n regular que origina al �rbol
	 * @return la cadena que representa la expresi�n regular
	 */
	public String getER();
	
	/**
	 * M�todo modificador de la expresi�n regular            
	 * @param er la nuava expresi�n regular
	 */
	public void setER(String er);

}
