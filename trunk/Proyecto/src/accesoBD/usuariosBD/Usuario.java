/**
 * 
 */
package accesoBD.usuariosBD;

import java.util.ArrayList;

/**
 * Clase que agrupa los atributos del usuario registrado de la plicaciñn,
 * asñ como sus datos y la forma en que se actualizan. 
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
	 * Mñtodo singleton que devuelve la instacina ñnica de la 
	 * clase de usuario de la base de datos de usuarios de la aplicaciñn.
	 * @return instancia de Usuario vacño
	 */
	public static Usuario getInstancia(){
		if(usuario==null) usuario=new Usuario();
		return usuario;
	}
	
	
	/**
	 * Constructor de usuario vacño
	 */
	public Usuario() {
		
	}
	/**
	 * Constructor de un usuario usando los datos que entran como parñmetros
	 * @param nombre nombre del usuario
	 * @param dni nombre con el que se registra en la base de datos
	 * @param ejerciciosConsultados nñmero de ejrcicios consultados
	 * @param ejerciciosBien nñmero de ejercicios que ha rsuelto correctamente
	 * @param ejerciciosMal nñmero de ejercicios mal resueltos
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
	 * Mñtodo accesor del password del usuario 
	 * @return el password del usuario
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Mñtodo modificador del password de usuario
	 * @param password nuevo password del usuario
	 */
	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * Mñtodo accesor del nombre del usuario 
	 * @return el nombre del usuario
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 *  Mñtodo modificador del nombre de usuario
	 * @param nombre nuevo nombre del usuario
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * Mñtodo accesor del nombre de registro del usuario  
	 * @return nombre de registro el usuario
	 */
	public int getDni() {
		return dni;
	}
	
	/**
	 * Mñtodo accesor del nombre de registro del usuario 
	 * @param dni nuevo nombre de registro
	 */
	public void setDni(int dni) {
		this.dni = dni;
	}
	
	/**
	 * Mñtodo accesro de la lista de ejercicios consultados por el usuario
	 * @return la lista con los ejercicios consultados
	 */
	public ArrayList<String> getEjerciciosConsultados() {
		return ejerciciosConsultados;
	}
	
	/**
	 * Mñtodo modificador de los ejercicios consultados
	 * @param ejerciciosConsultados nueva lista de ejercicios consultados
	 */
	public void setEjerciciosConsultados(ArrayList<String> ejerciciosConsultados) {
		this.ejerciciosConsultados = ejerciciosConsultados;
	}
	
	/**
	 * Mñtodo accesor de ejercicios bien resueltos por el usuario
	 * @return lista con los ejercicios resueltos por el usuario correctamente
	 */
	public ArrayList<String> getEjerciciosBien() {
		return ejerciciosBien;
	}
	
	/**
	 * Mñtodo modificador de la lista de ejrcicios resueltos correctamente
	 * @param ejerciciosBien nueva lista con lo ejercicios bien resueltos
	 */
	public void setEjerciciosBien(ArrayList<String> ejerciciosBien) {
		this.ejerciciosBien = ejerciciosBien;
	}
	
	/**
	 * Mñtodo accesor de ejercicios mal resueltos por el usuario
	 * @return lista con los ejercicios resueltos por el usuario incorrectamente
	 */
	public ArrayList<String> getEjerciciosMal() {
		return ejerciciosMal;
	}
	
	/**
	 * Mñtodo modificador de los ejercicios mal resueltos
	 * @param ejerciciosMal nueva lista con los ejercicios mal resueltos
	 */
	public void setEjerciciosMal(ArrayList<String> ejerciciosMal) {
		this.ejerciciosMal = ejerciciosMal;
	}
	/**
	 * Mñtodo para añadir un ejercicio consultado al usuario
	 * @param ruta ruta del ejercicio a añadir
	 */
	public void addEjercicioConsultado(String ruta) {
		if (!ejerciciosConsultados.contains(ruta))
			this.ejerciciosConsultados.add(ruta);
	}
	/**
	 * Mñtodo para añadir un ejercicio bien resuelto al usuario
	 * @param ruta ruta del ejercicio a añadir
	 */
	public void addEjercicioBien(String ruta) {
		if (!ejerciciosBien.contains(ruta))
			this.ejerciciosBien.add(ruta);
	}
	/**
	 * Mñtodo para añadir un ejercicio mal resuelto al usuario
	 * @param ruta ruta del ejercicio a añadir
	 */
	public void addEjercicioMal(String ruta) {
		if (!ejerciciosMal.contains(ruta))
			this.ejerciciosMal.add(ruta);
	}
	

}
