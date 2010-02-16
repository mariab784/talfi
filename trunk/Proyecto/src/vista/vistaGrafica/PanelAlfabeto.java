package vista.vistaGrafica;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.util.StringTokenizer;
import javax.swing.*;

import accesoBD.Mensajero;

import modelo.AutomatasException;
import modelo.automatas.Alfabeto;
import modelo.automatas.Alfabeto_imp;

/**
 *  Clase que implemta la creaciñn del panel de introducciñn del alfabeto para
 *  la expresiñn regular y crea tambiñn el alfabeto si todo va bien
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class PanelAlfabeto extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JTextField text;
	private JButton aceptar;
	private JButton cancelar;
	private boolean correcto;
	private JDialog dialog;
	private Alfabeto alfabeto;
	private Mensajero m;
	
	/**
	 * Constructor del panel de la introducciñn del alfabeto sobre la ventana
	 * que recibe y que posteriormente crea el alfabeto si estñ correctamente 
	 * introducido
	 * @param dialogo ventana a la que se le añadirñ el panel del alfabeto
	 */
	public PanelAlfabeto(JDialog dialogo){
		super();
		alfabeto=new Alfabeto_imp();
		dialog=dialogo;
		m=Mensajero.getInstancia();
		correcto=false;
		JLabel etiq=new JLabel(m.devuelveMensaje("vista.introAlf",2));
		aceptar=new JButton(m.devuelveMensaje("vista.aceptar",2));
		aceptar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					String s=text.getText();
					StringTokenizer st=new StringTokenizer(s,", ");
					while(st.hasMoreTokens()){
						String ss=st.nextToken();
						if(ss.equals("/"))
							throw new AutomatasException(m.devuelveMensaje("vista.lambda",2));
						if(ss.contains("/"))
							throw new AutomatasException(m.devuelveMensaje("vista.lambda",2));
						alfabeto.aniadirLetra(ss);
					}
					alfabeto.aniadirLetra("/");
					correcto=true;
					dialog.setVisible(false);
				} catch (AutomatasException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e1.getMensaje(),m.devuelveMensaje("vista.errAlf",2),JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		cancelar=new JButton(m.devuelveMensaje("vista.cancelar",2));
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				correcto=false;
				dialog.setVisible(false);
			}
		});
		JPanel panelB= new JPanel();
		panelB.add(aceptar);
		panelB.add(cancelar);
		text=new JTextField(40);
		text.setEditable(true);
		text.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					try {
						String s=text.getText();
						StringTokenizer st=new StringTokenizer(s,", ");
						while(st.hasMoreTokens()){
							String ss=st.nextToken();
							if(ss.equals("/"))
									throw new AutomatasException(m.devuelveMensaje("vista.lambda",2));
							if(ss.contains("/"))
								throw new AutomatasException(m.devuelveMensaje("vista.lambda",2));
							alfabeto.aniadirLetra(ss);
						}
						alfabeto.aniadirLetra("/");
						correcto=true;
						dialog.setVisible(false);
					} catch (AutomatasException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null,e1.getMensaje(),m.devuelveMensaje("vista.errAlf",2),JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		add(etiq);
		add(text);
		add(panelB);
	}
	
	/**
	 * Mñtodo accesor del atributo que indica si todo ha ido bien en la
	 * construcciñn del alfabeto
	 * @return true si ha ido bien, false si no
	 */
	public boolean getAceptar(){
		return correcto;
	}
	/**
	 * Devuelve el alfabeto introducido
	 * @return alfabeto que se ha introducido
	 */
	public Alfabeto getAlfabeto(){
		return alfabeto;
	}

}
