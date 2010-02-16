package vista.vistaGrafica;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import vista.Vista;

import modelo.AutomatasException;

import accesoBD.Mensajero;
import accesoBD.usuariosBD.AccesBDUsuarios;
import accesoBD.usuariosBD.Usuario;

/**
 * Clase que gestiona la actualizaciñn de usuarios
 * @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class ActualizaUsuario extends JPanel {
	
private static final long serialVersionUID = 1L;
	
	private Vista vista;
	private JDialog parent;
	private JButton nuevo;
	private JButton cancelar;
	private JTextField nombre;
	private JPasswordField password;
	private JPasswordField repassword;
	private JTextField dni;
	private Usuario usr;
	private Mensajero m;
	
	/**
	 * Solicita los datos para la actualizacion de un usuario
	 * @param padre ventana de actualizaciñn
	 * @param user usuario que se va actualizar, son sus datos antiguos
	 */
	public ActualizaUsuario(JDialog padre,Usuario user,Vista v){
		super(new BorderLayout());
		usr=user;
		vista=v;
		parent=padre;
		m=Mensajero.getInstancia();
		nuevo=new JButton(m.devuelveMensaje("vista.actualizar",2));
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
								if(u==null || u.getNombre().equals(usr.getNombre())){
									AccesBDUsuarios.getInstancia().actualizarUsuario(usr,new Usuario(nomb,dniUser,pass,usr.getEjerciciosConsultados(),
											usr.getEjerciciosBien(),usr.getEjerciciosMal()));
									Usuario.getInstancia().setNombre(nomb);
									Usuario.getInstancia().setDni(dniUser);
									Usuario.getInstancia().setPassword(pass);
									Usuario.getInstancia().setEjerciciosConsultados(usr.getEjerciciosConsultados());
									Usuario.getInstancia().setEjerciciosBien(usr.getEjerciciosBien());
									Usuario.getInstancia().setEjerciciosMal(usr.getEjerciciosMal());
									parent.setVisible(false);
									vista.reconstruirPanelUsuario();
								} else {
									JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.regDNI",2),"Error",JOptionPane.ERROR_MESSAGE);
								}
							} else
								JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.noDNI",2),"Error",JOptionPane.ERROR_MESSAGE);
						} catch(NumberFormatException ex){
							JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.numDNI",2),"Error",JOptionPane.ERROR_MESSAGE);
						}catch(AutomatasException ex){
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
			}
		});
		JLabel n=new JLabel(m.devuelveMensaje("vista.nombre",2));
		nombre=new JTextField(20);
		nombre.setText(usr.getNombre());
		nombre.addKeyListener(new OyenteKey());
		JLabel p=new JLabel(m.devuelveMensaje("vista.contraseña",2));
		password=new JPasswordField(20);
		try {
			String contraseña=AccesBDUsuarios.getInstancia().buscarUsuarioContraseña(usr.getNombre());
			password.setText(contraseña);
			password.addKeyListener(new OyenteKey());
			JLabel rp=new JLabel(m.devuelveMensaje("vista.repetir",2));
			repassword=new JPasswordField(20);
			repassword.setText(contraseña);
			repassword.addKeyListener(new OyenteKey());
			JLabel d=new JLabel(m.devuelveMensaje("vista.dni",2));
			dni=new JTextField(20);
			Integer i=(Integer)usr.getDni();
			dni.addKeyListener(new OyenteKey());
			dni.setText(i.toString());
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
		} catch(AutomatasException ex){
			JOptionPane.showMessageDialog(null,ex.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Clase que se encarga de detectar las pulsaciones de teclas sobre los campos de texto
	 * para actulizar el usuario
	 * @author Miguel Ballesteros, Josñ Antnio Blanes, Samer Nabhan
	 *
	 */
	public class OyenteKey extends KeyAdapter{
		
		@Override
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
								if(u==null || u.getNombre().equals(usr.getNombre())){
									AccesBDUsuarios.getInstancia().actualizarUsuario(usr,new Usuario(nomb,dniUser,pass,usr.getEjerciciosConsultados(),
											usr.getEjerciciosBien(),usr.getEjerciciosMal()));
									Usuario.getInstancia().setNombre(nomb);
									Usuario.getInstancia().setDni(dniUser);
									Usuario.getInstancia().setPassword(pass);
									Usuario.getInstancia().setEjerciciosConsultados(usr.getEjerciciosConsultados());
									Usuario.getInstancia().setEjerciciosBien(usr.getEjerciciosBien());
									Usuario.getInstancia().setEjerciciosMal(usr.getEjerciciosMal());
									parent.setVisible(false);
									vista.reconstruir();
								} else {
									JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.regDNI",2),"Error",JOptionPane.ERROR_MESSAGE);
								}
							} else
								JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.noDNI",2),"Error",JOptionPane.ERROR_MESSAGE);
						} catch(NumberFormatException ex){
							JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.numDNI",2),"Error",JOptionPane.ERROR_MESSAGE);
						}catch(AutomatasException ex){
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
