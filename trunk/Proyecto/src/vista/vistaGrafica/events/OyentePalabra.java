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

	@SuppressWarnings("unused")
	private String palabra;
	private JDialog dialog;
	@SuppressWarnings("unused")
	private AutomataCanvas canvas;
	private JTextField nomArs;
	
	
	public OyentePalabra(AutomataCanvas c){canvas = c;}
	
	public void mouseClicked(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {}
	
		public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		JOptionPane pane=new JOptionPane();
		//mensajero=Mensajero.getInstancia();
		dialog=pane.createDialog("ARISTA");
		JPanel panelD=new JPanel(new GridLayout(4,1));
		JPanel panelC=new JPanel(new GridLayout(1,4));
		JLabel etiqN=new JLabel("JLABEL");
		nomArs=new JTextField();
		nomArs.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					palabra = nomArs.getText();
					dialog.setVisible(false);
				}
			}
		});
		JPanel panelB=  new JPanel();
		JButton aceptar=new JButton("ACEPTAR");
		aceptar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				palabra = nomArs.getText();
				dialog.setVisible(false);
			}
		});
		JButton cancelar=new JButton("CANCELAR");
		
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
	}
	
	
}
