/**
 * 
 */
package lanzador;

import vista.*;
import vista.vistaGrafica.*;
import controlador.*;

/**
 * Ejecuta la aplicaciñn: MAIN PRINCIPAL
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */ 
public class Lanzador {

	private static final long serialVersionUID = 1L;
	
	public Lanzador(){
	}
	
	/**
	 * Main princiapl que crea el lanzador y llama a ejecutar
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Lanzador l=new Lanzador();
		l.ejecuta();
	}
	
	/**
	 * Mñtodo que lleva a cabo la inicializaciñn de la vista grñfica
	 * y el controlador y los asocia
	 */
	public void ejecuta() {
		// TODO Auto-generated method stub
		Controlador controlador=Controlador_imp.getInstancia();
		Vista vista1=new VistaGrafica();
		controlador.registraVista(vista1);
		
	}

}
