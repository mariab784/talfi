package vista.vistaGrafica.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vista.vistaGrafica.Estado;

/**
 * Clase que se encarga de llamar a la ventana con la informci�n
 * del estado que se ha seleccionado con el menu de PopUp
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class OyenteItemPopupEstado implements ActionListener {

	private Estado estado;
	private OyenteArista mouse;
	
	/**
	 * Constructor que recibe el estado seleccionado y el oyente de creaci�n del 
	 * di�logo ventana
	 * @param e estado seleccioando
	 * @param m oyente que crear� la ventan de di�logo
	 */
	public OyenteItemPopupEstado(Estado e,OyenteArista m){
		estado=e;
		mouse=m;
	}
	
	/**
	 * Crea la ventna de di�logo con la info del estado
	 *@param e evento de pulsaci�n
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		mouse.createDialogEstado(estado);
		mouse.getCanvas().repaint();
	}

}
