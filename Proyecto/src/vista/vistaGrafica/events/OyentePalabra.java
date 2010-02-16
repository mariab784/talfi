package vista.vistaGrafica.events;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import vista.vistaGrafica.AutomataCanvas;

/****************************************************************************/
/****************************************************************************/


public class OyentePalabra extends MouseAdapter /*implements ActionListener*/{

	private String palabra;
	private JDialog dialog;
	private AutomataCanvas canvas;
	//private String nombreArista;
	private JTextField nomArs;
	
	
	public OyentePalabra(AutomataCanvas c){canvas = c;}
	
	public void mouseClicked(MouseEvent e) {System.out.println("BIEEEN!");}
	
//	public void mouseDragged(MouseEvent e) {System.out.println("uHH!");}
	public void mousePressed(MouseEvent e) {System.out.println("aHH!");}
//	public void mouseReleased(MouseEvent e) { System.out.println("OHH!");}
	
		public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		//hay ke meter una cajita para meter la palabra.. COMO????

		JOptionPane pane=new JOptionPane();
		//mensajero=Mensajero.getInstancia();
		dialog=pane.createDialog("ARISTA");
		JPanel panelD=new JPanel(new GridLayout(4,1));
		JPanel panelC=new JPanel(new GridLayout(1,4));
		JLabel etiqN=new JLabel("JLABEL");
		/*JTextField*/ nomArs=new JTextField();
		nomArs.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					palabra = nomArs.getText();
					
				 	//if (AutomataCanvas.getAP().reconocePalabra(palabra)) System.out.println("BIEEEN!");
					dialog.setVisible(false);
				}
			}
		});
		JPanel panelB=  new JPanel();
		JButton aceptar=new JButton("ACEPTAR"/*mensajero.devuelveMensaje("vista.aceptar",2)*/);
		aceptar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				palabra = nomArs.getText();
				//if (canvas.compruebaPalabra(palabra)) System.out.println("BIEEEN!");
				dialog.setVisible(false);
			}
		});
		JButton cancelar=new JButton("CANCELAR"/*mensajero.devuelveMensaje("vista.cancelar",2)*/);
		
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialog.setVisible(false);
			}
		});
		panelB.add(aceptar);
		panelB.add(cancelar);
		panelD.add(etiqN);
		panelD.add(nomArs);
		panelD.add(panelC);
		panelD.add(panelB);
		dialog.setContentPane(panelD);
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		dialog.setSize(new Dimension(400,150));
		dialog.setVisible(true);
		//return d;

	}
	
	
}
