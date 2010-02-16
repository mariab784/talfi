package vista.vistaGrafica;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import modelo.AutomatasException;

import accesoBD.Mensajero;
import accesoBD.usuariosBD.AccesBDUsuarios;
import accesoBD.usuariosBD.Usuario;

/**
 * Clase que se encarga de mostrar el campo de texto para buscar
 * a un usuario e inicia la bñaqueda
 * @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class BuscaUsuario extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextField dni;
	private JButton aceptar;
	private JButton cancelar;
	private JDialog parent;
	private Mensajero m;
	
	/**
	 * Lanza un panel que permite buscar los datos de un usuario
	 * @param padre JDialog que llama a  buscar usuario y donde se imprimirñ
	 * la informaciñn de usuario que se ha buscado
	 */
	public BuscaUsuario(JDialog padre){
		super(new BorderLayout());
		parent=padre;
		m=Mensajero.getInstancia();
		JLabel e=new JLabel(m.devuelveMensaje("vista.buscaDNI",2));
		JPanel panelB=new JPanel();
		JPanel panelI=new JPanel();
		dni=new JTextField(20);
		aceptar=new JButton(m.devuelveMensaje("vista.aceptar",2));
		aceptar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					Usuario u=AccesBDUsuarios.getInstancia().buscarUsuarioDNI(Integer.parseInt(dni.getText()));
					if(u!=null){
						parent.setVisible(false);
						parent.setContentPane(new DatosUsuario(parent,u));
						parent.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
						parent.setSize(new Dimension(600,200));
						parent.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.nouser",2),m.devuelveMensaje("vista.nousuario",2),JOptionPane.INFORMATION_MESSAGE);
					}
				} catch(AutomatasException ex){
					JOptionPane.showMessageDialog(null,ex.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
				} catch (NumberFormatException exc){
					JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.nodni",2),"Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		cancelar=new JButton(m.devuelveMensaje("vista.cancelar",2));
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parent.setVisible(false);
			}
		});
		panelI.add(e);
		panelI.add(dni);
		panelB.add(aceptar);
		panelB.add(cancelar);
		add(panelI, BorderLayout.CENTER);
		add(panelB,BorderLayout.SOUTH);
	}

}
