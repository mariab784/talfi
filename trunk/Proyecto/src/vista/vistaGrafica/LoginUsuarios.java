package vista.vistaGrafica;

import java.awt.Dimension;
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
import modelo.AutomatasException;
import accesoBD.Mensajero;
import accesoBD.usuariosBD.*;


/**
 * Clase que muestra el panel de incio de sesiñn de usuarios si los datos son
 * correctos buscñndolos en la base de datos, o permite
 * pasar al registro de un nuevo usuario
 * @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class LoginUsuarios extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JDialog parent;
	private JButton login;
	private JButton registra;
	private JTextField usuario;
	private JPasswordField password;
	private Mensajero m;
	
	/**
	 * Construye el panel de login con el boton de registrar un nuevo usuario
	 * @param padre ventana a la que se le añade al panel de login
	 */
	public LoginUsuarios(JDialog padre){
		super();
		parent=padre;
		m=Mensajero.getInstancia();
		JPanel pBotones= new JPanel();
		login=new JButton("Login");
		login.addActionListener(new OyenteLogin());
		registra=new JButton(m.devuelveMensaje("vista.reg",2)); 
		registra.addActionListener(new OyenteRegistra());
		usuario=new JTextField(20);
		usuario.addKeyListener(new OyenteLoginKey());
		password=new JPasswordField(20);
		password.addKeyListener(new OyenteLoginKey());
		pBotones.add(login);
		pBotones.add(registra);
		JLabel usu=new JLabel(m.devuelveMensaje("vista.nombreU",2));
		JPanel usur=new JPanel();
		JLabel pas=new JLabel(m.devuelveMensaje("vista.contraseña",2));
		JPanel pass=new JPanel();
		usur.add(usu);
		usur.add(usuario);
		pass.add(pas);
		pass.add(password);
		add(usur);
		add(pass);
		add(pBotones);
	}
	
	/**
	 * Clase oyente para hacer login al pulsar el boton que comprueba los datos
	 * del usuario en la base de datos
	 * @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
	 *
	 */
	public class OyenteLogin implements ActionListener{
		
		/**
		 * Mñtodo que implementa la comprobaciñn de los datos introducidos
		 * y si son correctos inicia sesion 
		 * @param e evento de pulsaciñn sobre el boton
		 */
		public void actionPerformed(ActionEvent e){
			String nombre=usuario.getText();
			char[] pass=password.getPassword();
			String passw="";
			for(int i=0;i<pass.length;i++)
				passw+=pass[i];
			try {
				String contrase�aBD=AccesBDUsuarios.getInstancia().buscarUsuarioContrase�a(nombre);
				boolean correcto=true;
				correcto=passw.equals(contrase�aBD);
				if(!correcto)
					JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.datos",2),"Error",JOptionPane.ERROR_MESSAGE);
				else {
					parent.setVisible(false);
					Usuario usr=AccesBDUsuarios.getInstancia().buscarUsuario(nombre);
					Usuario.getInstancia().setNombre(nombre);
					Usuario.getInstancia().setDni(usr.getDni());
					Usuario.getInstancia().setPassword(usr.getPassword());
					Usuario.getInstancia().setEjerciciosConsultados(usr.getEjerciciosConsultados());
					Usuario.getInstancia().setEjerciciosBien(usr.getEjerciciosBien());
					Usuario.getInstancia().setEjerciciosMal(usr.getEjerciciosMal());
				}
			} catch(AutomatasException ex){
				JOptionPane.showMessageDialog(null,ex.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Clase oyente para hacer login al pulsar intro en el campo de texto y que 
	 * comprueba los datos del usauario en la base de datos
	 * @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
	 *
	 */
	public class OyenteLoginKey extends KeyAdapter{
		
		/**
		 * Mñtodo que implementa la comprobaciñn de los datos introducidos
		 * y si son correctos inicia sesion 
		 * @param e evento de pulsaciñn de tecla sobre el campo de texto
		 */
		public void keyPressed(KeyEvent e){
			if(e.getKeyCode()==KeyEvent.VK_ENTER){
				String nombre=usuario.getText();
				char[] pass=password.getPassword();
				String passw="";
				for(int i=0;i<pass.length;i++)
					passw+=pass[i];
				try {
					String contrase�aBD=AccesBDUsuarios.getInstancia().buscarUsuarioContrase�a(nombre);
					boolean correcto=true;
					correcto=passw.equals(contrase�aBD);
					if(!correcto)
						JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.datos",2),"Error",JOptionPane.ERROR_MESSAGE);
					else {
						parent.setVisible(false);
						Usuario usr=AccesBDUsuarios.getInstancia().buscarUsuario(nombre);
						Usuario.getInstancia().setNombre(nombre);
						Usuario.getInstancia().setDni(usr.getDni());
						Usuario.getInstancia().setPassword(usr.getPassword());
						Usuario.getInstancia().setEjerciciosConsultados(usr.getEjerciciosConsultados());
						Usuario.getInstancia().setEjerciciosBien(usr.getEjerciciosBien());
						Usuario.getInstancia().setEjerciciosMal(usr.getEjerciciosMal());
					}
				} catch(AutomatasException ex){
					JOptionPane.showMessageDialog(null,ex.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	/**
	 * Clase oyente que muestra el panel de registro de nuevo usuario
	 * @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
	 *
	 */
	public class OyenteRegistra implements ActionListener{
		
		/**
		 * Mñtodo que implementa la creaciñn del panel de registro de usuario
		 * @param e evento de pulsaciñn sobre el boton de registro
		 */
		public void actionPerformed(ActionEvent e){
			m=Mensajero.getInstancia();
			JOptionPane pane=new JOptionPane();
			JDialog dialog=pane.createDialog(m.devuelveMensaje("vista.nuevo",2));
			RegUsuario r=new RegUsuario(parent,dialog);
			dialog.setContentPane(r);
			dialog.setSize(new Dimension(500,160));
			dialog.setVisible(true);
		}
	}

}
