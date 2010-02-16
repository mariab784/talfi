/**
 * 
 */
package accesoBD.usuariosBD;

/**
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class EncriptadorString {
	
	private static EncriptadorString encriptador;
	
	/**
	 * Mñtodo singleton que devuelve la instacina ñnica de la 
	 * clase de encriptacion de String de la base de datos de 
	 * usuarios de la aplicaciñn.
	 * @return instancia de EncriptadorString
	 */
	public static EncriptadorString getInstancia(){
		if(encriptador==null) encriptador=new EncriptadorString();
		return encriptador;
	}
	
	/**
	 * Encripta un texto
	 * @param texto texto que se quiere encriptar
	 * @return texto encriptado
	 */
	public String encriptar(String texto) {
		char c;
		String salida="";
		
		for(int i=0;i<texto.length();i++) {
			c=texto.charAt(i);
			int k=(int)c;
			k=(k+5)% 255;
			String b=new Character((char)k).toString();
			salida+=b;
		}
		return salida;
	}
	
	/**
	 * Desencripta un texto
	 * @param texto texto que queremos desencriptar
	 * @return texto desencriptado
	 */
	public String desencriptar(String texto) {
		char c;
		String salida="";
		
		for(int i=0;i<texto.length();i++) {
			c=texto.charAt(i);
			int k=(int)c;
			k=(k-5)% 255;
			String b=new Character((char)k).toString();
			salida+=b;
		}
		return salida;
	}
	

}
