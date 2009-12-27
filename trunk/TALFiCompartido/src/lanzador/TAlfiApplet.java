package lanzador;

import javax.swing.JApplet;


/**
 * This allows one to run JFLAP as an applet. All it actually does is display a
 * small message, and runs the application normally.
 * 
 * @author Thomas Finley
 */

public class TAlfiApplet extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This instantiates a new JFLAPplet.
	 */
	public TAlfiApplet() {
	}

	public void init() {
		System.out.println("HELLO WORLD");
		Lanzador l=new Lanzador();
		l.ejecuta();
	}
	 
}
