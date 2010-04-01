/**
 * 
 */
package modelo.algoritmos;

import java.util.ArrayList;
import java.util.Iterator;

import vista.vistaGrafica.AristaAP;

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

	private static final char comienzo = 'C';
	private String xml;
	private AutomataPila automataEntrada;
	private GramaticaIC gic;
	private Controlador controlador;
	private Mensajero mensajero;
	private String Gramatica;
	private String lambda;
	private String fondoPila;
	
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
		Mensajero mensajero = Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		fondoPila = mensajero.devuelveMensaje("simbolos.cima",4);
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
	 * @return Gramatica resultado de aplicar el algoritmo
	 * para hallar una gramatica indepenediente de contexto
	 * a partir de un automata de pila dado.
	 */
	@SuppressWarnings("unchecked")
	public GramaticaIC AP_Gramatica(){
		
		
		/*GramaticaIC*/ gic = new GramaticaIC();
		gic.setVariableInicial("S");
		ArrayList<String> estados = (ArrayList<String>) this.automataEntrada.getEstados().clone();
		Iterator<String> it = estados.iterator();
		gic.getVariables().add("S");//añadido
		while (it.hasNext()){
			Produccion p = new Produccion();
			p.anadeCadena("["+this.automataEntrada.getEstadoInicial()+fondoPila+it.next()+"]");
			
			gic.anadeProduccion("S", p);
		}
		/* Aquí hemos Iniciado la gramatica, con simbolo inicial S 
		 * y añadiendo todos los estados con simbolo de pila)*/
		
		//declaraciones de ArrayList Auxiliares y de iterators que comentamos despues
		ArrayList<String> estados2 = (ArrayList<String>) this.automataEntrada.getEstados().clone();
		ArrayList<String> estados3 = (ArrayList<String>) this.automataEntrada.getEstados().clone();
		ArrayList<String> estados4 = (ArrayList<String>) this.automataEntrada.getEstados().clone();
		Iterator<String> it2 = estados2.iterator();
		String estado = null;
		
		//Recorremos los estados "que ponemos despues del primer corchete"
		
		while (it2.hasNext()){
			estado = it2.next();
			ArrayList<String> alfabetoPila = this.automataEntrada.dameCimasEstado(estado);
			if(alfabetoPila!=null){
				Iterator<String> letra = alfabetoPila.iterator();
				String estaLetra = null;
				
				//recorremos las cimas posibles para ese estado en las transiciones
				
				Iterator<String> estadoF = estados4.iterator();
				//Para crear producciones solo en caso de que haya transiciones con esa cima de pila
				while (letra.hasNext()){
					estaLetra = letra.next();
					String actual;
					ArrayList<String> posibles = this.automataEntrada.dameLetraEstadoCima(estado, estaLetra); 
					//Los estados que pueden convertirse en finales
					while (estadoF.hasNext()){
						String estadoFF = estadoF.next();
						//genero el simbolo que será la clave de una nueva serie de producciones
						String simbolos = "["+estado+estaLetra+estadoFF+"]";
						if(posibles != null) {
							Iterator<String> itp = posibles.iterator();
							//Las posibles letras que llevan a transicion
							while (itp.hasNext()){
								actual = itp.next();
								Iterator<String> it3 = estados3.iterator();
								//Para generar todas las combinaciones posibles
								while (it3.hasNext()){
									String estado2 = it3.next();
									if (this.automataEntrada.dameFinPilaEstadoLetra(estado, estaLetra, actual) != null ){
										ArrayList<ArrayList<String>> listaApila = this.automataEntrada.dameFinPilaEstadoLetra(estado, estaLetra, actual);
										Iterator<ArrayList<String>> itApilas = listaApila.iterator();
										Produccion p = null;
										//Recorro los simbolos que apareceran en la cima de pila
										while (itApilas.hasNext()){
											p = new Produccion();
											ArrayList<String> futuras = itApilas.next();
											p.anadeCadena(actual);
											if (futuras.size() == 1){
												if (!futuras.get(0).equals(lambda)){
													p.anadeCadena("["+estado+futuras.get(0)+estado2+"]");
												}
											}
											else if (futuras.size()>0){
												p.anadeCadena("["+estado+futuras.get(0)+estado2+"]");
												p.anadeCadena("["+estado2+futuras.get(1)+estadoFF+"]");
												
											}
											boolean pasa = true;
											if(gic.getProducciones().get(simbolos) != null){
												
												ArrayList<Produccion> quita = gic.getProducciones().get(simbolos);
												Iterator<Produccion> it19 = quita.iterator();
												while (it19.hasNext()){
													Produccion aux = it19.next();
													if (aux.getConcatenacion().toString().contains(p.getConcatenacion().toString()))
														pasa = false;
												}
											}
											if(gic.getProducciones().get(simbolos) == null || pasa)	{
											gic.anadeProduccion(simbolos, p);
											//añadido
											if(!estaEnVariables(simbolos))
												gic.getVariables().add(simbolos); //añadido
											}
										}	
									}
								}
							}
						}
					}
				}
			}
		}
		traduceVariables();	
		return gic;
		
	}
	
	private void traduceVariables(){
		
		ArrayList<String> nVariables = new ArrayList<String>();
		nVariables.add(gic.getVariableInicial());
		System.out.println("Inicial!: " + nVariables);
		int tam = gic.getVariables().size();
		char ultima = comienzo;
		for(int i = 0; i <tam; i++){
			
			char nVar = new Character ((char)(ultima+1) ) ;
			ultima = nVar;
			String s = nVar + "";
			nVariables.add(s);
			//nVar =  nVarAux + "";
		}
		System.out.println("final!: " + nVariables);

	}
	
	private boolean estaEnVariables(String s){
		
		return gic.getVariables().contains(s);
	}

	/**
	 * @param args
	 */
	public GramaticaIC getGic(){
		if(gic == null){
//			gic = new GramaticaIC();
//			gic.setProducciones(producciones);
		System.out.println("GIC null");	
			
		}

		return gic;
	}
	
	public static void main(String[] args) {
		AutomataPila aut = new AutomataPila();
//		AutomataPila aut2 = new AutomataPila();
		//a.listaEstados.add(new Estado(0,0,"s1"));
aut.getEstados().add("s0");//		aut.getEstados().add("s1");
aut.getEstados().add("s1");//		aut.getEstados().add("s2");
aut.getEstados().add("s2");//		aut.getEstados().add("s3");

		//		aut.getEstados().add("s4");

aut.setEstadoInicial("s0");//		aut.setEstadoInicial("s1");
aut.setEstadoFinal("s2");//		aut.setEstadoFinal("s3");
//		aut.setEstadoFinal("s2");

/*		aut2.getEstados().add("s1");
		aut2.getEstados().add("s2");
		aut2.setEstadoInicial("s1");
		aut2.setEstadoFinal("s2");*/
		//System.out.println("ESTADOS: " + aut.getEstados());
		
		AristaAP arist;
		
		arist = new AristaAP(0,0,0,0,"s0","s0");//		arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("a");
		arist.setCimaPila("Z");
		arist.anadirPila("AZ");//		arist.anadirPila("Z");
		
		aut.anadeArista(arist);
		

		arist = new AristaAP(0,0,0,0,"s0","s0");		//arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("a");
		arist.setCimaPila("A");//		arist.setCimaPila("Z");
		arist.anadirPila("AA");//		arist.anadirPila("CZ");
		
		aut.anadeArista(arist);	
		
		arist = new AristaAP(0,0,0,0,"s0","s1");//		arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("b");//		arist.anadirSimbolo("0");
		arist.setCimaPila("A");//		arist.setCimaPila("C");
		arist.anadirPila("\\");//		arist.anadirPila("CC");
		
		aut.anadeArista(arist);	
	
		arist = new AristaAP(0,0,0,0,"s1","s1");//		arist = new AristaAP(0,0,0,0,"s2","s2");
		arist.anadirSimbolo("b");		//arist.anadirSimbolo("1");
		arist.setCimaPila("A");		//arist.setCimaPila("C");
		arist.anadirPila("\\");
		
		aut.anadeArista(arist);
		
		arist = new AristaAP(0,0,0,0,"s1","s2");//		arist = new AristaAP(0,0,0,0,"s1","s3");
//		arist.anadirSimbolo("0");
		arist.anadirSimbolo("b");		//arist.anadirSimbolo("1");
		arist.setCimaPila("Z");		//arist.setCimaPila("C");
		arist.anadirPila("\\");
		
		aut.anadeArista(arist);		

//		AristaAP borrada = 	aut.getAutomataPila().remove(2);
//		System.out.println("ARISTA borrada: " + borrada );

/*		arist = new AristaAP(0,0,0,0,"s3","s2");
		//arist.anadirSimbolo("0");
		arist.anadirSimbolo("\\");
		arist.setCimaPila("Z");
		arist.anadirPila("Z");
		
		aut.anadeArista(arist);
		
		arist = new AristaAP(0,0,0,0,"s3","s4");
		arist.anadirSimbolo("0");
		arist.anadirSimbolo("\\");
		arist.setCimaPila("Z");
		arist.anadirPila("Z");
		
		aut.anadeArista(arist);
		
		arist = new AristaAP(0,0,0,0,"s3","s4");
		arist.anadirSimbolo("0");
		arist.anadirSimbolo("\\");
		arist.setCimaPila("Z");
		arist.anadirPila("Z");
		
		aut.anadeArista(arist); 
		
//		aut2.anadeArista(arist);

		
		arist = new AristaAP(0,0,0,0,"s1","s2");
//		arist.anadirSimbolo("0");
		arist.anadirSimbolo("1");
		arist.setCimaPila("A");
		arist.anadirPila("Z");
		
		aut.anadeArista(arist);
		
/*		arist = new AristaAP(0,0,0,0,"s3","s2");
		arist.anadirSimbolo("\\");
		arist.setCimaPila("Z");
		arist.anadirPila("Z");

		
		aut.anadeArista(arist);
		

		
	/*	arist = new AristaAP(0,0,0,0,"s2","s2");
		arist.anadirSimbolo("b");
		arist.setCimaPila("Z"); 
		arist.anadirPila("\\");
		
		aut.anadeArista(arist);*/
		
/*		arist = new AristaAP(0,0,0,0,"s3","s4");
		arist.anadirSimbolo("\\");
		//System.out.println("LAMBDA: \\" );
		arist.setCimaPila("Z");
		arist.anadirPila("Z");
		
		aut.anadeArista(arist);*/
		 //una vez ordenado no puedes desordenar
		//System.out.println("ARISTAS: " + aut.getAutomataPila());

/*		ArrayList<String> lp = new  ArrayList<String>();
		lp.add("0");
		lp.add("000");
		lp.add("00");*/
	
//		AutomataPila.compruebaPalabras(aut, aut2, lp);
		Alfabeto_Pila alf = new AlfabetoPila_imp();
		alf.aniadirLetra("Z");
		alf.aniadirLetra("A");//		alf.aniadirLetra("C");
		aut.setAlfabetoPila(alf);
		AutomataP_to_GramaticaIC a = new AutomataP_to_GramaticaIC(aut);
		System.out.println(a.AP_Gramatica()/*.getProducciones().toString()*/);
		// TODO Auto-generated method subs

	}

}
