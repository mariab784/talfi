package vista.vistaGrafica;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.*;

import modelo.AutomatasException;

import accesoBD.Mensajero;
import accesoBD.usuariosBD.AccesBDUsuarios;
import accesoBD.usuariosBD.Usuario;

/**
 * Clase que se encarga de rear el panel de registro de usuario mediante la 
 * introducción de sus datos.
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class RegUsuario extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JDialog parent;
	private JButton nuevo;
	private JButton cancelar;
	private JTextField nombre;
	private JPasswordField password;
	private JPasswordField repassword;
	private JTextField dni;
	private JDialog abuelo;
	private Mensajero m;
	
	/**
	 * Constructor del panel de registro de usuario que se añadirá a la ventana que se pasa
	 * como parámero
	 * @param ab ventana anterior a la que se vuelve si se cancela el registro de usuario
	 * @param padre ventana a la que se añadirá el panel
	 */
	public RegUsuario(JDialog ab,JDialog padre){
		super(new BorderLayout());
		parent=padre;
		m=Mensajero.getInstancia();
		this.abuelo=ab;
		nuevo=new JButton(m.devuelveMensaje("vista.nuevo",2));
		nuevo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String nomb=nombre.getText();
				String pass=AccesBDUsuarios.getInstancia().aString(password.getPassword());
				String repass=AccesBDUsuarios.getInstancia().aString(repassword.getPassword());
				String dniU=dni.getText();
				if(!nomb.equals("")&&!dniU.equals("")&&!pass.equals("")&&!repass.equals("")){
					if(repass.equals(pass)){
						try {
							int dniUser=Integer.parseInt(dniU);
							if(dniU.length()==8){
								Usuario u=AccesBDUsuarios.getInstancia().buscarUsuarioDNI(dniUser);
								if(u==null){
									Usuario usr=new Usuario(nomb,dniUser,pass,new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>());
									AccesBDUsuarios.getInstancia().insetarUsuario(usr);
									Usuario.getInstancia().setNombre(nomb);
									Usuario.getInstancia().setDni(dniUser);
									Usuario.getInstancia().setPassword(pass);
									Usuario.getInstancia().setEjerciciosConsultados(new ArrayList<String>());
									Usuario.getInstancia().setEjerciciosBien(new ArrayList<String>());
									Usuario.getInstancia().setEjerciciosMal(new ArrayList<String>());
									parent.setVisible(false);
									abuelo.setVisible(false);
								} else {
									JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.regDNI",2),"Error",JOptionPane.ERROR_MESSAGE);
								}
							} else
								JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.noDNI",2),"Error",JOptionPane.ERROR_MESSAGE);
						} catch(NumberFormatException ex){
							JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.numDNI",2),"Error",JOptionPane.ERROR_MESSAGE);
						} catch(AutomatasException ex){
							JOptionPane.showMessageDialog(null,ex.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
						}
					} else
						JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.passrepass",2),"Error",JOptionPane.ERROR_MESSAGE);
				} else 
					JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.vacio",2),"Error",JOptionPane.ERROR_MESSAGE);
			}
		});
		cancelar=new JButton(m.devuelveMensaje("vista.cancelar",2));
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parent.setVisible(false);
				JOptionPane pane=new JOptionPane();
				parent=pane.createDialog(m.devuelveMensaje("vista.alta",2));
				LoginUsuarios login=new LoginUsuarios(parent);
				parent.setContentPane(login);
				parent.addWindowListener(new WindowAdapter(){
					public void windowClosing(WindowEvent ev){
						parent.setVisible(false);
						System.exit(0);
					}
				});
				parent.setSize(new Dimension(500,120));
				parent.setVisible(true);
			}
		});
		JLabel n=new JLabel(m.devuelveMensaje("vista.nombre",2));
		nombre=new JTextField(20);
		nombre.addKeyListener(new OyenteKey());
		JLabel p=new JLabel(m.devuelveMensaje("vista.contraseña",2));
		password=new JPasswordField(20);
		password.addKeyListener(new OyenteKey());
		JLabel rp=new JLabel(m.devuelveMensaje("vista.repetir",2));
		repassword=new JPasswordField(20);
		repassword.addKeyListener(new OyenteKey());
		JLabel d=new JLabel(m.devuelveMensaje("vista.dni",2));
		dni=new JTextField(20);
		dni.addKeyListener(new OyenteKey());
		JPanel panelI=new JPanel(new GridLayout(4,2));
		panelI.add(n);
		panelI.add(nombre);
		panelI.add(d);
		panelI.add(dni);
		panelI.add(p);
		panelI.add(password);
		panelI.add(rp);
		panelI.add(repassword);
		JPanel panelB=new JPanel();
		panelB.add(nuevo);
		panelB.add(cancelar);
		add(panelI, BorderLayout.CENTER);
		add(panelB,BorderLayout.SOUTH);
	}
	
	/**
	 *  Clase oyente que se encarga de añadir el nuevo usuario con sus datos introducidos
	 *  al pulsar la tecla intro sobre alguno de los campos de texto
	 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
	 *
	 */
	public class OyenteKey extends KeyAdapter{
		
		/**
		 * Método que detecta pulsación de teclado sobre los campos de texto y que añade
		 * el usuario a la base de datos si la ecla pulsada es intro y todos los datos
		 * está correctamente introducidos
		 * @param e evento de teclado  
		 */
		public void keyPressed(KeyEvent e){
			if(e.getKeyCode()==KeyEvent.VK_ENTER){
				String nomb=nombre.getText();
				String pass=AccesBDUsuarios.getInstancia().aString(password.getPassword());
				String repass=AccesBDUsuarios.getInstancia().aString(repassword.getPassword());
				String dniU=dni.getText();
				if(!nomb.equals("")&&!dniU.equals("")&&!pass.equals("")&&!repass.equals("")){
					if(repass.equals(pass)){
						try {
							int dniUser=Integer.parseInt(dniU);
							if(dniU.length()==8){
								Usuario u=AccesBDUsuarios.getInstancia().buscarUsuarioDNI(dniUser);
								if(u==null){
									Usuario usr=new Usuario(nomb,dniUser,pass,new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>());
									AccesBDUsuarios.getInstancia().insetarUsuario(usr);
									Usuario.getInstancia().setNombre(nomb);
									Usuario.getInstancia().setDni(dniUser);
									Usuario.getInstancia().setPassword(pass);
									Usuario.getInstancia().setEjerciciosConsultados(new ArrayList<String>());
									Usuario.getInstancia().setEjerciciosBien(new ArrayList<String>());
									Usuario.getInstancia().setEjerciciosMal(new ArrayList<String>());
									parent.setVisible(false);
									abuelo.setVisible(false);
								} else {
									JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.regDNI",2),"Error",JOptionPane.ERROR_MESSAGE);
								}
							} else
								JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.noDNI",2),"Error",JOptionPane.ERROR_MESSAGE);
						} catch(NumberFormatException ex){
							JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.numDNI",2),"Error",JOptionPane.ERROR_MESSAGE);
						} catch(AutomatasException ex){
							JOptionPane.showMessageDialog(null,ex.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
						}
					} else
						JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.passrepass",2),"Error",JOptionPane.ERROR_MESSAGE);
				} else 
					JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.vacio",2),"Error",JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}

}
