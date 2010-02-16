package modelo.expresion_regular;

/**
 * 
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class ArbolER_imp implements ArbolER{

	private String raiz;
	private ArbolER hijoIzq;
	private ArbolER hijoDer;
	private String expresion;
	
	/**
	 * Constructor que inicializa el ñrbol vacño
	 */
	public ArbolER_imp(){
		this.raiz=null;
		hijoDer=null;
		hijoIzq=null;
	}
	
	/**
	 * Constructor q e genera el ñrbol con el nodo rañz y los
	 * hijos vacños
	 * @param raiz el carñcter que tendrñ el ñrbol en la rañz
	 */
	public ArbolER_imp(String raiz){
		this.raiz=raiz;
		hijoDer=null;
		hijoIzq=null;
	}
	
	/**
	 * Contructor del ñrbol completo con la rañz y los dos hijos
	 * que se le pasan como parñmetros
	 * @param hijoIz nuevo hijo izquierdo
	 * @param hijoDr nuevo hijo derecho
	 * @param raiz nuevo carñcter en la rañz
	 */
	public ArbolER_imp(ArbolER hijoIz, ArbolER hijoDr, String raiz){
		this.raiz=raiz;
		hijoIzq=hijoIz;
		hijoDer=hijoDr;
	}
	
	
	public ArbolER getHijoDR() {
		// TODO Auto-generated method stub
		return hijoDer;
	}

	
	public ArbolER getHijoIZ() {
		// TODO Auto-generated method stub
		return hijoIzq;
	}

	
	public String getRaiz() {
		// TODO Auto-generated method stub
		return raiz;
	}

	
	public void setHijoDR(ArbolER hijoDR) {
		// TODO Auto-generated method stub
		hijoDer=hijoDR;
	}

	
	public void setHijoIZ(ArbolER hijoIZ) {
		// TODO Auto-generated method stub
		hijoIzq=hijoIZ;
	}

	
	public void setTextoRaiz(String raiz) {
		// TODO Auto-generated method stub
		this.raiz=raiz;
	}
	
	
	public String toString(){
		// TODO Auto-generated method stub
		if(this.hijoDer!=null && this.hijoIzq!=null){
			return "( raiz "+getRaiz()+" ( "+" hijo izquierdo: "+
			getHijoIZ().toString()+" ; hijo derecho: "+getHijoDR().toString()+" ) )";
		} else {
			if(this.hijoDer==null && this.hijoIzq!=null){
				return "( raiz "+getRaiz()+" ( "+" hijo izquierdo: "+getHijoIZ().toString()+" ; hijo derecho: null ) )";
			}else {
				if(this.hijoIzq==null && this.hijoDer!=null){
					return "( raiz "+getRaiz()+" ( "+" hijo izquierdo: null ; hijo derecho: "+getHijoDR()+ " ) )";
				} else{
					return "( raiz "+getRaiz()+" ( "+" hijo izquierdo: null ; hijo derecho: null ) )";
				}
			}
		}
	}

	
	public String getER() {
		// TODO Auto-generated method stub
		return expresion;
	}

	
	public void setER(String er) {
		// TODO Auto-generated method stub
		expresion=er;
	}
	
	
}
