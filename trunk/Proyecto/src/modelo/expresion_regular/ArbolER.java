package modelo.expresion_regular;

/**
 * Interfaz que define la funcionalidad de los ñrbole sintñcticos para
 * expresiones regulares. 
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public interface ArbolER {
	
	/**
	 * Mñtodo accesro del hijo derecho del ñrbol, que a su vez
	 * serñ tambiñen un ñrbol del mismo tipo
	 * @return al ñrbo,que representa el hijo derecho
	 */
	public ArbolER getHijoDR();
	
	/**
	 * Mñtodo accesro del hijo izquierdo del ñrbol, que a su vez
	 * serñ tambiñen un ñrbol del mismo tipo
	 * @return al ñrbo,que representa el hijo izquierdo
	 */
	public ArbolER getHijoIZ();
	
	/**
	 * Mñtodo modificador del hijo derecho del ñrbol
	 * @param hijoDR el ñrbol que serñ el hijo derecho
	 */
	public void setHijoDR(ArbolER hijoDR);
	
	/**
	 * Mñtodo modificador del hijo izquierdo del ñrbol
	 * @param hijoIZ el ñrbol que serñ el hijo izquierdo
	 */
	public void setHijoIZ(ArbolER hijoIZ);
	
	/**
	 * Mñtodo accesor del carñceter de la rañz del ñrbol
	 * @return el carñcter que contiene la rañz
	 */
	public String getRaiz();
	
	/**
	 * Mñtodo modificador del carñceter de la rañz del ñrbol
	 * @param raiz el nuevo carñcter que serñ la rañz del ñrbol
	 */
	public void setTextoRaiz(String raiz);
	
	/**
	 * Mñtodo de conversiñn a cadena de caracteres del ñrbol
	 * @return representaciñn en modo texto del ñrbol
	 */
	public String toString();
	
	/**
	 * Mñtodo accesro de la expresiñn regular que origina al ñrbol
	 * @return la cadena que representa la expresiñn regular
	 */
	public String getER();
	
	/**
	 * Mñtodo modificador de la expresiñn regular            
	 * @param er la nuava expresiñn regular
	 */
	public void setER(String er);

}
