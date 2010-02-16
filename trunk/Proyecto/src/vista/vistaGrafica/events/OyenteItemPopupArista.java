package vista.vistaGrafica.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vista.vistaGrafica.Arista;

/**
 * Clase que se encarga de llamar a la ventana con la informciñn
 * de la arista que se ha seleccionado con el menu de PopUp
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class OyenteItemPopupArista implements ActionListener {

	private Arista arista;
	private OyenteArista mouse;
	
	/**
	 * Constructor que recibe la arista seleccionada y el oyente de creaciñn del 
	 * diñlogo ventana
	 * @param a arista seleccioanda
	 * @param m oyente que crearñ la ventan de diñlogo
	 */
	public OyenteItemPopupArista(Arista a,OyenteArista m){
		arista=a;
		mouse=m;
	}
	
	/**
	 * Crea la ventna de diñlogo con la info de la arista
	 *@param e evento de pulsaciñn
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		mouse.createDialogArista(arista);
		mouse.getCanvas().repaint();
	}

}
