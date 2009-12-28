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
 * Clase que crea el panel que pregunta al usuario si desea a�adir el ejercicio
 * guardado a la Base de Datos, e inicia lo tr�mites para realizarlo en caso
 * afirmativo
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class PanelAniadirEjerBD extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private JDialog padre;
	private JButton aniadir;
	private JButton cancelar;
	private Vista vista;
	private Mensajero m;
	
	/**
	 * Constructor del panel que recibe la ventana sobre la que se a�adir� y
	 * el tipo de ejercicio y el texto que lo representa, as� como la vista
	 * gr�fica para actualizar el arbol de ejercicios
	 * @param parent ventna sobre la que se a�adir� el panel
	 * @param v vista gr�fica
	 * @param texto representaci�n del ejercicio en texto
	 * @param tipo tipo de ejercicio
	 */
	public PanelAniadirEjerBD(JDialog parent,Vista v,String texto,String tipo){
		super(new BorderLayout());
		padre=parent;
		vista=v;
		m=Mensajero.getInstancia();
		JPanel panelB= new JPanel();
		JLabel etiq=new JLabel(m.devuelveMensaje("vista.BDejer",2));
		aniadir=new JButton(m.devuelveMensaje("vista.aniadir",2));
		aniadir.addActionListener(new OyenteEjercicio(texto,tipo));
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
	 * Clase oyente del boton de aceptar inculir el ejercicio en la base de
	 * datos
	 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
	 *
	 */
	public class OyenteEjercicio implements ActionListener{
		
		private String tipo;
		private String texto;
		
		/**
		 * Constructor del oyente que recibe el texto del ejercicio y el tipo
		 * @param texto cadena que representa el ejercicio
		 * @param tipo tipo del ejercicio
		 */
		public OyenteEjercicio(String texto, String tipo){
			this.texto=texto;
			this.tipo=tipo;
		}
		
		/**
		 * M�todo que lleva a cabo la actualizaci�n del xml de ejemplos/ejercicios y
		 * la actulziaci�n de la vista gr�fica
		 * @param e evento de boton
		 */
		public void actionPerformed(ActionEvent e){
			try {
				AccesoAEjemplos.getInstancia().actualizaXMLEjercicios(texto,tipo);
				padre.setVisible(false);
				vista.reconstruirPanelJTree();
			} catch(AutomatasException ex){
				JOptionPane.showMessageDialog(null,ex.getMensaje()+"\n"+m.devuelveMensaje("vista.updateBD",2),"Error",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
