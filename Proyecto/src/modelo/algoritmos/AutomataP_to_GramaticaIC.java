/**
 * 
 */
package modelo.algoritmos;

import accesoBD.Mensajero;
import controlador.Controlador;
import modelo.Algoritmo;
import modelo.automatas.*;
//import modelo.expresion_regular.ExpresionRegular;
import modelo.gramatica.GramaticaIC;

/**
 * @author Rocio BarrigŁete, Mario Huete, Luis San Juan
 *
 */
public class AutomataP_to_GramaticaIC implements Algoritmo {

	private String xml;
	private Automata automataEntrada;
	private GramaticaIC gic;
	private Controlador controlador;
	private Mensajero mensajero;
	private String Gramatica;
	
	private AutomataPila resultadosParciales;
	/**
	 * 
	 */
	public AutomataP_to_GramaticaIC() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see modelo.Algoritmo#ejecutar(boolean)
	 */
	@Override
	public Automata ejecutar(boolean muestraPasos) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see modelo.Algoritmo#getXML()
	 */
	@Override
	public String getXML() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see modelo.Algoritmo#registraControlador(controlador.Controlador)
	 */
	@Override
	public void registraControlador(Controlador controlador) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
