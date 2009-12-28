/**
 * 
 */
package accesoBD.usuariosBD;

import java.util.ArrayList;

/**
 * Clase que agrupa los atributos del usuario registrado de la plicaci�n,
 * as� como sus datos y la forma en que se actualizan. 
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class Usuario {
	
	private String nombre;
	private int dni;
	private String password;
	private ArrayList<String> ejerciciosConsultados;
	private ArrayList<String> ejerciciosBien;
	private ArrayList<String> ejerciciosMal;
	
	
	private static Usuario usuario;
	
	/**
	 * M�todo singleton que devuelve la instacina �nica de la 
	 * clase de usuario de la base de datos de usuarios de la aplicaci�n.
	 * @return instancia de Usuario vac�o
	 */
	public static Usuario getInstancia(){
		if(usuario==null) usuario=new Usuario();
		return usuario;
	}
	
	
	/**
	 * Constructor de usuario vac�o
	 */
	public Usuario() {
		
	}
	/**
	 * Constructor de un usuario usando los datos que entran como par�metros
	 * @param nombre nombre del usuario
	 * @param dni nombre con el que se registra en la base de datos
	 * @param ejerciciosConsultados n�mero de ejrcicios consultados
	 * @param ejerciciosBien n�mero de ejercicios que ha rsuelto correctamente
	 * @param ejerciciosMal n�mero de ejercicios mal resueltos
	 */
	public Usuario(String nombre, int dni,String password,
			ArrayList<String> ejerciciosConsultados,
			ArrayList<String> ejerciciosBien, ArrayList<String> ejerciciosMal) {
		super();
		this.nombre = nombre;
		this.dni = dni;
		this.password=password;
		this.ejerciciosConsultados = ejerciciosConsultados;
		this.ejerciciosBien = ejerciciosBien;
		this.ejerciciosMal = ejerciciosMal;
	}
	
	/**
	 * M�todo accesor del password del usuario 
	 * @return el password del usuario
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * M�todo modificador del password de usuario
	 * @param password nuevo password del usuario
	 */
	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * M�todo accesor del nombre del usuario 
	 * @return el nombre del usuario
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 *  M�todo modificador del nombre de usuario
	 * @param nombre nuevo nombre del usuario
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * M�todo accesor del nombre de registro del usuario  
	 * @return nombre de registro el usuario
	 */
	public int getDni() {
		return dni;
	}
	
	/**
	 * M�todo accesor del nombre de registro del usuario 
	 * @param dni nuevo nombre de registro
	 */
	public void setDni(int dni) {
		this.dni = dni;
	}
	
	/**
	 * M�todo accesro de la lista de ejercicios consultados por el usuario
	 * @return la lista con los ejercicios consultados
	 */
	public ArrayList<String> getEjerciciosConsultados() {
		return ejerciciosConsultados;
	}
	
	/**
	 * M�todo modificador de los ejercicios consultados
	 * @param ejerciciosConsultados nueva lista de ejercicios consultados
	 */
	public void setEjerciciosConsultados(ArrayList<String> ejerciciosConsultados) {
		this.ejerciciosConsultados = ejerciciosConsultados;
	}
	
	/**
	 * M�todo accesor de ejercicios bien resueltos por el usuario
	 * @return lista con los ejercicios resueltos por el usuario correctamente
	 */
	public ArrayList<String> getEjerciciosBien() {
		return ejerciciosBien;
	}
	
	/**
	 * M�todo modificador de la lista de ejrcicios resueltos correctamente
	 * @param ejerciciosBien nueva lista con lo ejercicios bien resueltos
	 */
	public void setEjerciciosBien(ArrayList<String> ejerciciosBien) {
		this.ejerciciosBien = ejerciciosBien;
	}
	
	/**
	 * M�todo accesor de ejercicios mal resueltos por el usuario
	 * @return lista con los ejercicios resueltos por el usuario incorrectamente
	 */
	public ArrayList<String> getEjerciciosMal() {
		return ejerciciosMal;
	}
	
	/**
	 * M�todo modificador de los ejercicios mal resueltos
	 * @param ejerciciosMal nueva lista con los ejercicios mal resueltos
	 */
	public void setEjerciciosMal(ArrayList<String> ejerciciosMal) {
		this.ejerciciosMal = ejerciciosMal;
	}
	/**
	 * M�todo para a�adir un ejercicio consultado al usuario
	 * @param ruta ruta del ejercicio a a�adir
	 */
	public void addEjercicioConsultado(String ruta) {
		if (!ejerciciosConsultados.contains(ruta))
			this.ejerciciosConsultados.add(ruta);
	}
	/**
	 * M�todo para a�adir un ejercicio bien resuelto al usuario
	 * @param ruta ruta del ejercicio a a�adir
	 */
	public void addEjercicioBien(String ruta) {
		if (!ejerciciosBien.contains(ruta))
			this.ejerciciosBien.add(ruta);
	}
	/**
	 * M�todo para a�adir un ejercicio mal resuelto al usuario
	 * @param ruta ruta del ejercicio a a�adir
	 */
	public void addEjercicioMal(String ruta) {
		if (!ejerciciosMal.contains(ruta))
			this.ejerciciosMal.add(ruta);
	}
	

}
