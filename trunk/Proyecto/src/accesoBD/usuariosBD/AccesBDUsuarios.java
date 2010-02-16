/**
 * 
 */
package accesoBD.usuariosBD;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Iterator;
import modelo.AutomatasException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import accesoBD.Mensajero;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;

/**
 * Clase que encapsula la gestiñn de la base de datos de usuarios, tanto para acceder
 * como para modificar cualquier parte de la misma.
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class AccesBDUsuarios {
	
	
	private static AccesBDUsuarios bd;
	private EncriptadorString encriptador;
	private Mensajero m;
	
	/**
	 * Mñtodo singleton que devuelve la instacina ñnica de la 
	 * clase de acceso a la base de datos de usuarios de la aplicaciñn.
	 * @return instancia de AccesBDUsuarios
	 */
	public static AccesBDUsuarios getInstancia(){
		if(bd==null) bd=new AccesBDUsuarios();
		return bd;
	}
	
	
	/**
	 * Mñtodo que busca un usuario apartir de su nombre
	 * @param nombre nombre del usuario que se quiere saber la contraseña
	 * @return Usuario al que pertenece el nombre, nul si no hay ninguno
	 * @throws AutomatasException lanza la excepcion si hay algun problema
	 * con el fichero de la base de datos o al parsearlo.
	 */
	public Usuario buscarUsuario(String nombre) throws AutomatasException {
		Usuario usuario=null;
		encriptador=EncriptadorString.getInstancia();
		String ruta="XML/usuarios_ejercicios/bdUsuarios.xml";
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		try {
			parser.parse(new InputSource(new FileInputStream(ruta)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.noarchivo",2));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.sax",2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.entsalida",2));
		}
		Document documento = parser.getDocument();
		
		NodeList tipo = documento.getElementsByTagName("usuario");
		for (int i = 0; i <tipo.getLength(); i++) {
			String nombren=encriptador.desencriptar(tipo.item(i).getChildNodes().item(1).getTextContent());
			if (nombre.equals(nombren)) {
				String pass=encriptador.desencriptar(tipo.item(i).getChildNodes().item(3).getTextContent());
				int dni=Integer.parseInt(encriptador.desencriptar(tipo.item(i).getChildNodes().item(5).getTextContent()));
				String consultados=encriptador.desencriptar(tipo.item(i).getChildNodes().item(7).getTextContent());
				String bien=encriptador.desencriptar(tipo.item(i).getChildNodes().item(9).getTextContent());
				String mal=encriptador.desencriptar(tipo.item(i).getChildNodes().item(11).getTextContent());
			 
					ArrayList<String> listaConsultados=new ArrayList<String>();
					StringTokenizer st=new StringTokenizer(consultados);
					while(st.hasMoreTokens()) {
						listaConsultados.add(st.nextToken(","));
					}
					ArrayList<String> listaBien=new ArrayList<String>();
					st=new StringTokenizer(bien);
					while(st.hasMoreTokens()) {
						listaBien.add(st.nextToken(","));
					}
					ArrayList<String> listaMal=new ArrayList<String>();
					st=new StringTokenizer(mal);
					while(st.hasMoreTokens()) {
						listaMal.add(st.nextToken(","));
					}
			 
					usuario=new Usuario(nombre,dni,pass,listaConsultados,listaBien,listaMal);
					return usuario;
			}
		}
		return null;
	}
	
	/**
	 * Busca un usuario por el dni registrado en la base de datos
	 * @param dni dni por el que se va a realizar la bñsqueda
	 * @return usuario al que corresponde el dni, null si no hay ninguno
	 * @throws AutomatasException lanza la excepcion si hay algun problema
	 * con el fichero de la base de datos o al parsearlo.
	 */
	public Usuario buscarUsuarioDNI(int dni) throws AutomatasException {
		Usuario usuario=null;
		String ruta="XML/usuarios_ejercicios/bdUsuarios.xml";
		Mensajero mensajero=Mensajero.getInstancia();
		encriptador=EncriptadorString.getInstancia();
		DOMParser parser = new DOMParser();
		try {
			parser.parse(new InputSource(new FileInputStream(ruta)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.noarchivo",2));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.sax",2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.entsalida",2));
		}
		Document documento = parser.getDocument();
		
		NodeList tipo = documento.getElementsByTagName("usuario");

		for (int i = 0; i <tipo.getLength(); i++) {
			int dnin=Integer.parseInt(encriptador.desencriptar(tipo.item(i).getChildNodes().item(5).getTextContent()));
			if (dni==dnin) {
				String nombre=encriptador.desencriptar(tipo.item(i).getChildNodes().item(1).getTextContent());
				String pass=encriptador.desencriptar(tipo.item(i).getChildNodes().item(3).getTextContent());
				String consultados=encriptador.desencriptar(tipo.item(i).getChildNodes().item(7).getTextContent());
				String bien=encriptador.desencriptar(tipo.item(i).getChildNodes().item(9).getTextContent());
				String mal=encriptador.desencriptar(tipo.item(i).getChildNodes().item(11).getTextContent());
			 
					ArrayList<String> listaConsultados=new ArrayList<String>();
					StringTokenizer st=new StringTokenizer(consultados);
					while(st.hasMoreTokens()) {
						listaConsultados.add(st.nextToken(","));
					}
					ArrayList<String> listaBien=new ArrayList<String>();
					st=new StringTokenizer(bien);
					while(st.hasMoreTokens()) {
						listaBien.add(st.nextToken(","));
					}
					ArrayList<String> listaMal=new ArrayList<String>();
					st=new StringTokenizer(mal);
					while(st.hasMoreTokens()) {
						listaMal.add(st.nextToken(","));
					}
			 
					usuario=new Usuario(nombre,dni,pass,listaConsultados,listaBien,listaMal);
					return usuario;
			}
		}
		return null;
	}
	
	/**
	 * Mñtodo que devuelve la contraseña de un usuario a apartir de su nombre
	 * @param nombre nombre del usuario que se quiere saber la contraseña
	 * @return String con la contraseña del usuario
	 * @throws AutomatasException lanza la excepcion si hay algun problema
	 * con el fichero de la base de datos o al parsearlo.
	 */
	public String buscarUsuarioContrase�a(String nombre) throws AutomatasException {
		String ruta="XML/usuarios_ejercicios/bdUsuarios.xml";
		Mensajero mensajero=Mensajero.getInstancia();
		encriptador=EncriptadorString.getInstancia();
		DOMParser parser = new DOMParser();
		try {
			parser.parse(new InputSource(new FileInputStream(ruta)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.noarchivo",2));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.sax",2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.entsalida",2));
		}
		Document documento = parser.getDocument();
		
		NodeList tipo = documento.getElementsByTagName("usuario");
		for (int i = 0; i <tipo.getLength(); i++) {
			String n=tipo.item(i).getChildNodes().item(1).getTextContent();
			if (encriptador.desencriptar(n).equals(nombre)) {
				tipo = documento.getElementsByTagName("pass");
				return encriptador.desencriptar(tipo.item(i).getTextContent());
			}
		}
		return null;
	}
	
	
	/**
	 * Mñtodo que actualiza un usuario que ha cambiado sus datos registrados
	 * en la base de datos.
	 * @param antiguo usuario antiguo que se va a actualizar
	 * @param nuevo nuevos datos del usuario a actualizar
	 * @throws AutomatasException se lanza si hay algñn problema de entrada/salida 
	 * con el fichero que contiene la base de datos
	 */
	public void actualizarUsuario(Usuario antiguo,Usuario nuevo) throws AutomatasException {
		String fichero=new String();
		encriptador=EncriptadorString.getInstancia();
		fichero+="<usuarios>\n";
		ArrayList<Usuario> usuarios=listarUsuarios();
		Iterator<Usuario> iUsr=usuarios.iterator();
		while(iUsr.hasNext()){
			Usuario usr=iUsr.next();
			if(Integer.parseInt(encriptador.desencriptar(((Integer)usr.getDni()).toString()))!=antiguo.getDni()){
				fichero+="\t<usuario>\n\t\t<nombre>"+usr.getNombre()+"</nombre>\n";
				fichero+="\t\t<pass>"+usr.getPassword()+"</pass>\n";
				fichero+="\t\t<dni>"+usr.getDni()+"</dni>\n";
				fichero+="\t\t<ejercicios_consultados>"+destokenizar(usr.getEjerciciosConsultados())+"</ejercicios_consultados>\n";
				fichero+="\t\t<ejercicios_bien>"+destokenizar(usr.getEjerciciosBien())+"</ejercicios_bien>\n";
				fichero+="\t\t<ejercicios_mal>"+destokenizar(usr.getEjerciciosMal())+"</ejercicios_mal>\n";
				fichero+="\t</usuario>\n";
			} else {
				fichero+="\t<usuario>\n\t\t<nombre>"+encriptador.encriptar(nuevo.getNombre())+"</nombre>\n";
				fichero+="\t\t<pass>"+encriptador.encriptar(nuevo.getPassword())+"</pass>\n";
				fichero+="\t\t<dni>"+encriptador.encriptar(((Integer)nuevo.getDni()).toString())+"</dni>\n";
				fichero+="\t\t<ejercicios_consultados>"+encriptador.encriptar(destokenizar(nuevo.getEjerciciosConsultados()))+"</ejercicios_consultados>\n";
				fichero+="\t\t<ejercicios_bien>"+encriptador.encriptar(destokenizar(nuevo.getEjerciciosBien()))+"</ejercicios_bien>\n";
				fichero+="\t\t<ejercicios_mal>"+encriptador.encriptar(destokenizar(nuevo.getEjerciciosMal()))+"</ejercicios_mal>\n";
				fichero+="\t</usuario>\n";
			}
		}
		fichero+="</usuarios>";
		String ruta="XML/usuarios_ejercicios/bdUsuarios.xml";
		File file = new File (ruta);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(fichero);
			bw.close();
		} catch(IOException e){
			m=Mensajero.getInstancia();
			throw new AutomatasException(m.devuelveMensaje("parser.entsalida",2));
		}
		
	}
	
	/**
	 * Mñtodo para insertar un usuario en la base de datos
	 * @param nuevo usuario que se va a insertar
	 * @throws AutomatasException se lanza si hay algñn problema de entrada/salida 
	 * con el fichero que contiene la base de datos 
	 */
	public void insetarUsuario(Usuario nuevo) throws AutomatasException{
		String fichero=new String();
		encriptador=EncriptadorString.getInstancia();
		fichero+="<usuarios>";
		ArrayList<Usuario> usuarios=listarUsuarios();
		Iterator<Usuario> iUsr=usuarios.iterator();
		while(iUsr.hasNext()){
			Usuario usr=iUsr.next();
			fichero+="\t<usuario>\n\t\t<nombre>"+usr.getNombre()+"</nombre>\n";
			fichero+="\t\t<pass>"+usr.getPassword()+"</pass>\n";
			fichero+="\t\t<dni>"+usr.getDni()+"</dni>\n";
			fichero+="\t\t<ejercicios_consultados>"+destokenizar(usr.getEjerciciosConsultados())+"</ejercicios_consultados>\n";
			fichero+="\t\t<ejercicios_bien>"+destokenizar(usr.getEjerciciosBien())+"</ejercicios_bien>\n";
			fichero+="\t\t<ejercicios_mal>"+destokenizar(usr.getEjerciciosMal())+"</ejercicios_mal>\n";
			fichero+="\t</usuario>\n";
		}
		fichero+="\t<usuario>\n\t\t<nombre>"+encriptador.encriptar(nuevo.getNombre())+"</nombre>\n";
		fichero+="\t\t<pass>"+encriptador.encriptar(nuevo.getPassword())+"</pass>\n";
		fichero+="\t\t<dni>"+encriptador.encriptar(((Integer)nuevo.getDni()).toString())+"</dni>\n";
		fichero+="\t\t<ejercicios_consultados>"+encriptador.encriptar(destokenizar(nuevo.getEjerciciosConsultados()))+"</ejercicios_consultados>\n";
		fichero+="\t\t<ejercicios_bien>"+encriptador.encriptar(destokenizar(nuevo.getEjerciciosBien()))+"</ejercicios_bien>\n";
		fichero+="\t\t<ejercicios_mal>"+encriptador.encriptar(destokenizar(nuevo.getEjerciciosMal()))+"</ejercicios_mal>\n";
		fichero+="\t</usuario>\n";
		fichero+="</usuarios>";
		String ruta="XML/usuarios_ejercicios/bdUsuarios.xml";
		File file = new File (ruta);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(fichero);
			bw.close();
		} catch(IOException e){
			m=Mensajero.getInstancia();
			throw new AutomatasException(m.devuelveMensaje("parser.entsalida",2));
		}
	}
	
	/**
	 * Tokeniza a partir de una lista de String un String con los elementos
	 * de la lista concatenedos y separados por comas. 
	 * @param ejercicios ArryaList que contiene los elementos
	 * @return String con los elementos separados pro comas
	 */
	public String destokenizar(ArrayList<String> ejercicios) {
		// TODO Auto-generated method stub
		Iterator<String> iEjr=ejercicios.iterator();
		String ejr=new String();
		while(iEjr.hasNext()){
			if(!ejr.equals("")) ejr+=", ";
			ejr+=iEjr.next();
		}
		return ejr;
	}
	
	
	/**
	 * Mñtodo para listar todos los usuarios registrados en la base de 
	 * datos de la aplicaciñn
	 * @return devuevle un ArrayList de con los usuarios
	 * @throws AutomatasException
	 */
	public ArrayList<Usuario> listarUsuarios() throws AutomatasException {
		// TODO Auto-generated method stub
		String ruta="XML/usuarios_ejercicios/bdUsuarios.xml";
		ArrayList<Usuario> ret=new ArrayList<Usuario>();
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		try {
			parser.parse(new InputSource(new FileInputStream(ruta)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.noarchivo",2));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.sax",2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.entsalida",2));
		}
		Document documento = parser.getDocument();
		
		NodeList tipo = documento.getElementsByTagName("usuario");
		for (int i = 0; i <tipo.getLength(); i++) {
			String nomb=tipo.item(i).getChildNodes().item(1).getTextContent();
			String pass=tipo.item(i).getChildNodes().item(3).getTextContent();
			int dni=Integer.parseInt(tipo.item(i).getChildNodes().item(5).getTextContent());
			String ejrCons=tipo.item(i).getChildNodes().item(7).getTextContent();
			String ejrBien=tipo.item(i).getChildNodes().item(9).getTextContent();
			String ejrMal=tipo.item(i).getChildNodes().item(11).getTextContent();
			ArrayList<String> ejerC=tokenizar(ejrCons);
			ArrayList<String> ejerB=tokenizar(ejrBien);
			ArrayList<String> ejerM=tokenizar(ejrMal);
			ret.add(new Usuario(nomb,dni,pass,ejerC,ejerB,ejerM));
		}
		return ret;
	}
	
	/**
	 * Devuelve un ArrasyList de String a aprtir de un String
	 * con varios elementos separados por comas
	 * @param ejr String que contiene los elementos
	 * @return lista con los elementos del String como elementos en la lista
	 */
	public ArrayList<String> tokenizar(String ejr) {
		// TODO Auto-generated method stub
		StringTokenizer st=new StringTokenizer(ejr,",");
		ArrayList<String> retorno=new ArrayList<String>();
		while(st.hasMoreTokens()){
			retorno.add(st.nextToken());
		}
		return retorno;
	}
	
	/**
	 * Pasa a String una cadena de caracteres char
	 * @param password cadena que pasaremos a String
	 * @return String con la cadena de caracteres
	 */
	public String aString(char[] password) {
		// TODO Auto-generated method stub
		int i=0;
		String res=new String();
		while(i<password.length){
			res+=password[i];
			i++;
		}
		return res;
	}
	
	

}
