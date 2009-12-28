package vista.vistaGrafica;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

import accesoBD.Mensajero;

/**
 * Clase que crea el panel de introduci�n de exoresi�n regular y 
 * realiza las gestiones para crear el arbol inario correspondiente 
 * a la misma
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class PanelExpresionRegular extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private JButton aceptar;
	private JButton cancelar;
	private int opcion;
	private JTextField text;
	private JRadioButton si;
	private JRadioButton no;
	private boolean pasos;
	private JDialog padre;
	private String expresion;
	private Mensajero m;
	
	/**
	 * Constructor que crea el panel para introducir la expresi�n, dicho panel
	 * se a�adir� a la ventana que se le pasa como par�metro
	 * @param parent ventana sore la que se a�adir� el panel
	 */
	public PanelExpresionRegular(JDialog parent){
		padre=parent;
		opcion=0;
		pasos=false;
		m=Mensajero.getInstancia();
		JPanel botones=new JPanel();
		JLabel e=new JLabel(m.devuelveMensaje("vista.introER",2));
		aceptar=new JButton(m.devuelveMensaje("vista.aceptar",2));
		aceptar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				expresion=text.getText();
				if(expresion.equals("")) opcion=2;
				else opcion=1;
				padre.setVisible(false);
			}
		});
		cancelar=new JButton(m.devuelveMensaje("vista.cancelar",2));
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				opcion=0;
				padre.setVisible(false);
			}
		});
		text=new JTextField(25);
		text.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					expresion=text.getText();
					if(expresion.equals("")) opcion=2;
					else opcion=1;
					padre.setVisible(false);
				}
			}
		});
		ButtonGroup bg=new ButtonGroup();
		si=new JRadioButton(m.devuelveMensaje("pasos.con",2));
		si.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				pasos=true;
			}
		});
		no=new JRadioButton(m.devuelveMensaje("pasos.sin",2));
		no.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				pasos=false;
			}
		});
		no.setSelected(true);
		bg.add(si);
		bg.add(no);
		JPanel radio=new JPanel();
		radio.add(si);
		radio.add(no);
		this.add(e);
		this.add(text);
		this.add(radio);
		botones.add(aceptar);
		botones.add(cancelar);
		this.add(botones);
	}
	
	/**
	 * M�todo accesor de la variable opci�n que indica los posibles casos de introducc�n
	 * de exxpresi�n
	 * @return 1 si la expres�n es no vac�a, 2 si es vac�a y 0 si se cancela el algoritmo
	 */
	public int getOpcion(){
		return this.opcion;
	}
	
	/**
	 * M�todo accesro de la variable que indica si se realizar� el algoritmo
	 * de transformaci�n a automata por pasos
	 * @return true si se realiza por pasos, false si no
	 */
	public boolean isPasos(){
		return pasos;
	}
	
	/**
	 * M�todo que devuelve la expresi�n introducida
	 * @return espresi�n regular que se ha introducido
	 */
	public String getExp(){
		return expresion;
	}

}
