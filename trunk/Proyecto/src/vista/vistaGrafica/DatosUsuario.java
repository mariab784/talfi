package vista.vistaGrafica;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import modelo.AutomatasException;
import accesoBD.Mensajero;
import accesoBD.usuariosBD.AccesBDUsuarios;
import accesoBD.usuariosBD.Usuario;

/**
 * Clase que se encarga de la muestra de la informaciñn del usuaurio de
 * la aplicaciñn que recibe en el constructor
 * @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class DatosUsuario extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JDialog parent;
	private JButton nuevo;
	private JTextField nombre;
	private JTextField password;
	private JTextField dni;
	private JTextField ejerCons;
	private JTextField ejerBien;
	private JTextField ejerMal;
	private Usuario usr;
	private Mensajero m;
	
	/**
	 * Lanza un panel que visualiza los datos de un usuario
	 * @param padre ventana que muestra los datos del usuario
	 * @param user usuario del que se muestran los datos
	 */
	public DatosUsuario(JDialog padre,Usuario user){
		super(new BorderLayout());
		usr=user;
		parent=padre;
		m=Mensajero.getInstancia();
		nuevo=new JButton(m.devuelveMensaje("vista.aceptar",2));
		nuevo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parent.setVisible(false);
			}
		});
		JLabel n=new JLabel(m.devuelveMensaje("vista.nombre",2));
		nombre=new JTextField(20);
		nombre.setText(usr.getNombre());
		nombre.setEditable(false);
		nombre.setEnabled(true);
		JLabel p=new JLabel(m.devuelveMensaje("vista.contraseña",2));
		password=new JTextField(20);
		password.setEditable(false);
		password.setEnabled(true);
		try {
			String contraseña=AccesBDUsuarios.getInstancia().buscarUsuarioContraseña(usr.getNombre());
			password.setText(contraseña);
			JLabel d=new JLabel(m.devuelveMensaje("vista.dni2",2));
			dni=new JTextField(20);
			Integer i=(Integer)usr.getDni();
			dni.setText(i.toString());
			dni.setEditable(false);
			dni.setEnabled(true);
			JLabel ec= new JLabel(m.devuelveMensaje("usuarios.cons",1));
			ejerCons= new JTextField(20);
			ejerCons.setEditable(false);
			ejerCons.setEnabled(true);
			JScrollBar scrollBar1 = new JScrollBar(JScrollBar.HORIZONTAL);
		    JPanel panel1 = new JPanel();
		    panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		    BoundedRangeModel brm1 = ejerCons.getHorizontalVisibility();
		    scrollBar1.setModel(brm1);
		    panel1.add(ejerCons);
		    panel1.add(scrollBar1);
			ejerCons.setText(AccesBDUsuarios.getInstancia().destokenizar(usr.getEjerciciosConsultados()));
			JLabel eb= new JLabel(m.devuelveMensaje("usuarios.bien",1));
			ejerBien= new JTextField(20);
			ejerBien.setEditable(false);
			ejerBien.setEnabled(true);
			JScrollBar scrollBar2 = new JScrollBar(JScrollBar.HORIZONTAL);
		    JPanel panel2 = new JPanel();
		    panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		    BoundedRangeModel brm2 = ejerBien.getHorizontalVisibility();
		    scrollBar2.setModel(brm2);
		    panel2.add(ejerBien);
		    panel2.add(scrollBar2);
			ejerBien.setText(AccesBDUsuarios.getInstancia().destokenizar(usr.getEjerciciosBien()));
			JLabel em= new JLabel(m.devuelveMensaje("usuarios.mal",1));
			ejerMal= new JTextField(20);
			ejerMal.setEditable(false);
			ejerMal.setEnabled(true);
			JScrollBar scrollBar3 = new JScrollBar(JScrollBar.HORIZONTAL);
		    JPanel panel3 = new JPanel();
		    panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
		    BoundedRangeModel brm3 = ejerMal.getHorizontalVisibility();
		    scrollBar3.setModel(brm3);
		    panel3.add(ejerMal);
		    panel3.add(scrollBar3);
			ejerMal.setText(AccesBDUsuarios.getInstancia().destokenizar(usr.getEjerciciosMal()));
			JPanel panelI=new JPanel(new GridLayout(6,2));
			panelI.add(n);
			panelI.add(nombre);
			panelI.add(d);
			panelI.add(dni);
			panelI.add(p);
			panelI.add(password);
			panelI.add(ec);
			panelI.add(panel1);
			panelI.add(eb);
			panelI.add(panel2);
			panelI.add(em);
			panelI.add(panel3);
			JPanel panelB=new JPanel();
			panelB.add(nuevo);
			add(panelI, BorderLayout.CENTER);
			add(panelB,BorderLayout.SOUTH);
		} catch(AutomatasException ex){
			JOptionPane.showMessageDialog(null,ex.getMensaje(),"error",JOptionPane.ERROR_MESSAGE);
		}
	}

}
