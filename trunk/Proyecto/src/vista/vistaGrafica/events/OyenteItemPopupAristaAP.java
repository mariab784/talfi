package vista.vistaGrafica.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vista.vistaGrafica.AristaAP;

/**
 * Clase que se encarga de llamar a la ventana con la informci�n
 * de la arista del  automata de pila que se ha seleccionado con el menu de PopUp
 *  @author Roc�o Barrig�ete, Mario Huete, Luis San Juan
 *
 */
public class OyenteItemPopupAristaAP implements ActionListener {

	private AristaAP arista;
	private OyenteArista mouse;
	
	/**
	 * Constructor que recibe la arista seleccionada y el oyente de creaci�n del 
	 * di�logo ventana
	 * @param a arista seleccioanda
	 * @param m oyente que crear� la ventan de di�logo
	 */
	public OyenteItemPopupAristaAP(AristaAP a,OyenteArista m){
		arista=a;
		mouse=m;
	}
	
	/**
	 * Crea la ventna de di�logo con la info de la arista
	 *@param e evento de pulsaci�n
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		mouse.createDialogAristaAP(arista); 
		mouse.getCanvas().repaint();
	}

}
