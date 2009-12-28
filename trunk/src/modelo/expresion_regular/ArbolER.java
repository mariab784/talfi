package modelo.expresion_regular;

/**
 * Interfaz que define la funcionalidad de los árbole sintácticos para
 * expresiones regulares. 
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public interface ArbolER {
	
	/**
	 * Método accesro del hijo derecho del árbol, que a su vez
	 * será tambiçen un árbol del mismo tipo
	 * @return al árbo,que representa el hijo derecho
	 */
	public ArbolER getHijoDR();
	
	/**
	 * Método accesro del hijo izquierdo del árbol, que a su vez
	 * será tambiçen un árbol del mismo tipo
	 * @return al árbo,que representa el hijo izquierdo
	 */
	public ArbolER getHijoIZ();
	
	/**
	 * Método modificador del hijo derecho del árbol
	 * @param hijoDR el árbol que será el hijo derecho
	 */
	public void setHijoDR(ArbolER hijoDR);
	
	/**
	 * Método modificador del hijo izquierdo del árbol
	 * @param hijoIZ el árbol que será el hijo izquierdo
	 */
	public void setHijoIZ(ArbolER hijoIZ);
	
	/**
	 * Método accesor del caráceter de la raíz del árbol
	 * @return el carácter que contiene la raíz
	 */
	public String getRaiz();
	
	/**
	 * Método modificador del caráceter de la raíz del árbol
	 * @param raiz el nuevo carácter que será la raíz del árbol
	 */
	public void setTextoRaiz(String raiz);
	
	/**
	 * Método de conversión a cadena de caracteres del árbol
	 * @return representación en modo texto del árbol
	 */
	public String toString();
	
	/**
	 * Método accesro de la expresión regular que origina al árbol
	 * @return la cadena que representa la expresión regular
	 */
	public String getER();
	
	/**
	 * Método modificador de la expresión regular            
	 * @param er la nuava expresión regular
	 */
	public void setER(String er);

}
