package vista.vistaGrafica;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import modelo.AutomatasException;

import vista.Vista;

import accesoBD.AccesoAEjemplos;
import accesoBD.Mensajero;

/**
 * Clase que crea el panel que pregunta al usuario si desea añadir el ejemplo
 * guardado a la Base de Datos, e inicia lo trñmites para realizarlo en caso
 * afirmativo
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class PanelAniadirEjemploBD extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JDialog padre;
	private JButton aniadir;
	private JButton cancelar;
	private Vista vista;
	private Mensajero m;
	
	/**
	 * Constructor del panel que recibe la ventana sobre la que se añadirñ y
	 * el tipo de ejemplo y el texto que lo representa, asñ como la vista
	 * grñfica para actualizar el arbol de ejemplos
	 * @param parent ventna sobre la que se añadirñ el panel
	 * @param v vista grñfica
	 * @param texto representaciñn del ejemplo en texto
	 * @param tipo tipo de ejemplo
	 */
	public PanelAniadirEjemploBD(JDialog parent,Vista v,String texto,String tipo){
		super(new BorderLayout());
		padre=parent;
		vista=v;
		m=Mensajero.getInstancia();
		JLabel etiq=new JLabel(m.devuelveMensaje("vista.BDej",2));
		JPanel panelB= new JPanel();
		aniadir=new JButton(m.devuelveMensaje("vista.aniadir",2));
		aniadir.addActionListener(new OyenteEjemplo(texto,tipo));
		cancelar=new JButton(m.devuelveMensaje("vista.noBD",2));
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				padre.setVisible(false);
			}
		});
		panelB.add(aniadir);
		panelB.add(cancelar);
		add(etiq);
		add(panelB,BorderLayout.SOUTH);
	}

	/**
	 * Clase oyente del boton de aceptar inculir el ejemplo en la base de
	 * datos
	 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
	 *
	 */
	public class OyenteEjemplo implements ActionListener{
		
		private String tipo;
		private String texto;
		
		/**
		 * Constructor del oyente que recibe el texto del ejemplo y el tipo
		 * @param texto cadena que representa el ejemplo
		 * @param tipo tipo del ejemplo
		 */
		public OyenteEjemplo(String texto, String tipo){
			this.texto=texto;
			this.tipo=tipo;
		}
		
		/**
		 * Mñtodo que lleva a cabo la actualizaciñn del xml de ejemplos/ejercicios y
		 * la actulziaciñn de la vista grñfica
		 * @param e evento de boton
		 */
		public void actionPerformed(ActionEvent e){
			try {
				AccesoAEjemplos.getInstancia().actualizaXMLEjemplos(texto,tipo);
				padre.setVisible(false);
				vista.reconstruirPanelJTree();
			} catch(AutomatasException ex){
				JOptionPane.showMessageDialog(null,ex.getMensaje()+"\n"+m.devuelveMensaje("vista.updateBD",2),"Error",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
