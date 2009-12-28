package vista.vistaGrafica.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vista.vistaGrafica.Estado;

/**
 * Clase que se encarga de llamar a la ventana con la informción
 * del estado que se ha seleccionado con el menu de PopUp
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class OyenteItemPopupEstado implements ActionListener {

	private Estado estado;
	private OyenteArista mouse;
	
	/**
	 * Constructor que recibe el estado seleccionado y el oyente de creación del 
	 * diálogo ventana
	 * @param e estado seleccioando
	 * @param m oyente que creará la ventan de diálogo
	 */
	public OyenteItemPopupEstado(Estado e,OyenteArista m){
		estado=e;
		mouse=m;
	}
	
	/**
	 * Crea la ventna de diálogo con la info del estado
	 *@param e evento de pulsación
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		mouse.createDialogEstado(estado);
		mouse.getCanvas().repaint();
	}

}
