/**
 * 
 */
package modelo.algoritmos;

import java.util.ArrayList;
import java.util.Iterator;

import accesoBD.Mensajero;
import controlador.Controlador;
import controlador.Controlador_imp;
import modelo.Algoritmo;
import modelo.automatas.*;
//import modelo.expresion_regular.ExpresionRegular;
import modelo.gramatica.GramaticaIC;
import modelo.gramatica.Produccion;

/**
 * @author Rocio Barrigüete, Mario Huete, Luis San Juan
 *
 */
public class AutomataP_to_GramaticaIC implements Algoritmo {

	private String xml;
	private AutomataPila automataEntrada;
	private GramaticaIC gic;
	private Controlador controlador;
	private Mensajero mensajero;
	private String Gramatica;
	
	private AutomataPila resultadosParciales;
	/**
	 * 
	 */
	public AutomataP_to_GramaticaIC(Automata a) {
		// TODO Auto-generated constructor stub
		automataEntrada=(AutomataPila) a;
		mensajero=Mensajero.getInstancia();
		xml=new String();
		controlador=Controlador_imp.getInstancia();
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

	public GramaticaIC AP_Gramatica(){
		
		
		GramaticaIC gic = new GramaticaIC();
		gic.setVariableInicial("S");
		ArrayList<String> estados = this.automataEntrada.getEstados();
		Iterator<String> it = estados.iterator();
		while (it.hasNext()){
			Produccion p = new Produccion();
			p.anadeCadena(this.automataEntrada.getEstadoInicial()+"Z"+it.next());
			gic.anadeProduccion("S", p);
		}
		/* Aquí hemos Iniciado la gramatica, con simbolo inicial S 
		 * y añadiendo todos los estados con simbolo de pila)*/
		
		
		ArrayList<String> estados2 = this.automataEntrada.getEstados();
		ArrayList<String> estados3 = this.automataEntrada.getEstados();
		Iterator<String> it2 = estados2.iterator();
		while (it2.hasNext()){
			
		}
		return gic;
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
