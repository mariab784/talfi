package modelo.algoritmos;

import java.util.ArrayList;

import java.util.Iterator;
import accesoBD.Mensajero;
import controlador.Controlador;
import controlador.Controlador_imp;
import modelo.automatas.*;
import modelo.Algoritmo;
import modelo.expresion_regular.*;



/**
 *  Algortimo de transformaciñn de expresiñn regular a automata finito
 *  no determinista con lambdas usando el mñtodo de Kleene
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */

public class ERToAFNDLambda implements Algoritmo {

	private ArbolER arbol;
	private int numPasos;
	private Automata automataSalida;
	private Alfabeto alfabeto;
	private int estados;
	private Controlador controlador;
	private String XML;
	private Mensajero m;
	
	/**
	 * Constructor del algortimo al que le llegan el arbol sintñctico y el alfabeto
	 * @param a arbol sintñactico de la expresiñn regular
	 * @param b alfabeto de la expresiñn
	 */
	public ERToAFNDLambda(ArbolER a, Alfabeto b){
		arbol=a;
		alfabeto =b;
		numPasos=1;
		estados=0;
		m=Mensajero.getInstancia();
		controlador=Controlador_imp.getInstancia();
	}
	
	/**
	 * Mñtodo pincipal del algoritmo que lanza la ejecuciñn recursiva
	 * recorriendo el arbol de la expresiñn.
	 * @param muestraPasos true si se deben mostrar los pasos, false si no.
	 * @return Automata el automata lambda que reconoce la expresiñn
	 */
	public Automata ejecutar(boolean muestraPasos){
		if(muestraPasos){
			XML="<steps>\n\t<RExpr>\n\t\t<item>";
			XML+=arbol.getER()+"</item>\n\t</RExpr>\n\t";
			XML+="<initialTree>\n\t\t<item>";
			XML+=arbol.toString();
			XML+="</item>\n\t</initialTree>";
		}
		controlador.trataMensaje(m.devuelveMensaje("ERToLambda.inicio",2).toUpperCase());
		automataSalida =recorreARB(muestraPasos,arbol);
		if(muestraPasos)XML+="\n</steps>";
		controlador.trataMensaje(m.devuelveMensaje("ERToLambda.fin", 2).toUpperCase());
		return automataSalida;
	}
	
	private AutomataFNDLambda recorreARB(boolean muestraPasos,/*AutomataFNDLambda automata,*/ ArbolER arb){
		AutomataFNDLambda auto = new AutomataFNDLambda();
		ArrayList<String> lfin= new ArrayList<String>();
		auto.setAlfabeto(alfabeto);
		String c = arb.getRaiz();
		if(c.equals(".")){
			AutomataFNDLambda ai = recorreARB(muestraPasos,arb.getHijoIZ());
			AutomataFNDLambda ad = recorreARB(muestraPasos,arb.getHijoDR());
			//// se añaden los estados y transiciones de los anteriores
			auto=copiaAutomata(ai,auto);
			auto=copiaAutomata(ad,auto);
			//// estado inicial el del primer automata
			auto.setEstadoInicial(ai.getEstadoInicial());
			/// transiciones del estado final del primero an inicial del 2ñ automata
			Iterator<String> iest=ai.getEstadosFinales().iterator();
			while(iest.hasNext()){
				auto.insertaArista(iest.next(),ad.getEstadoInicial(),"/");
			}
			////estado final los del 2ñ automata
			lfin=ad.getEstadosFinales();
			auto.setEstadosFinales(lfin);
			if(muestraPasos){
			  imprimirPaso(auto,m.devuelveMensaje("ERToLambda.paso",2)+" "+numPasos+": "+m.devuelveMensaje("ERToLambda.concat",2));
			}
			controlador.trataMensaje(m.devuelveMensaje("ERToLambda.concat", 2));
			controlador.trataMensaje(auto.toString());
			numPasos++;
			return auto;
		}
		if(c.equals("+")){
			AutomataFNDLambda ai = recorreARB(muestraPasos,arb.getHijoIZ());
			AutomataFNDLambda ad = recorreARB(muestraPasos,arb.getHijoDR());
			//// se añaden los estados y transiciones de los anteriores
			auto=copiaAutomata(ai,auto);
			auto=copiaAutomata(ad,auto);
			//// se añaden los nuevo estados inicial y final ñnicos
			String s= "S"+estados;
			estados++;
			String sf = "S"+estados;
			estados++;
			auto.insertaVertice(s);
			auto.insertaVertice(sf);
			//// nuevo estado inicial y sus transiciones desde los anteriores
			auto.setEstadoInicial(s);
			auto.insertaArista(s,ai.getEstadoInicial(),"/");
			auto.insertaArista(s,ad.getEstadoInicial(),"/");
			//// nuevo estado final y sus transiciones desde los anteriores
			lfin.add(sf);
			auto=modificaFinales(auto,sf,ai.getEstadosFinales());
			auto=modificaFinales(auto,sf,ad.getEstadosFinales());
			auto.setEstadosFinales(lfin);
			if(muestraPasos){
				  imprimirPaso(auto,m.devuelveMensaje("ERToLambda.paso",2)+" "+numPasos+": "+m.devuelveMensaje("ERToLambda.union",2));
				}
			controlador.trataMensaje(m.devuelveMensaje("ERToLambda.union", 2));
			controlador.trataMensaje(auto.toString());
			numPasos++;
			return auto;
		} 
		if(c.equals("^+")){
			AutomataFNDLambda ai = recorreARB(muestraPasos,arb.getHijoIZ());
			//// se añaden los estados y transiciones del anterior
			auto=copiaAutomata(ai,auto);
			//// se añaden el nuevo estado inicial 
			String s= "S"+estados;
			estados++;
			auto.insertaVertice(s);
			//// nuevo estado inicial y su transiciones al anterior
			auto.setEstadoInicial(s);
			auto.insertaArista(s,ai.getEstadoInicial(),"/");
			//// mismos estados finales y sus transiciones al inicial
			auto=modificaFinales(auto,s,ai.getEstadosFinales());
			auto.setEstadosFinales(ai.getEstadosFinales());
			if(muestraPasos){
				  imprimirPaso(auto,m.devuelveMensaje("ERToLambda.paso",2)+" "+numPasos+": "+m.devuelveMensaje("ERToLambda.unRepeticion",2));
				}
			controlador.trataMensaje(m.devuelveMensaje("ERToLambda.UnaRepeticion", 2));
			controlador.trataMensaje(auto.toString());
			numPasos++;
			return auto;
		}
		if(c.equals("*")){
			AutomataFNDLambda ai = recorreARB(muestraPasos,arb.getHijoIZ());
			//// se añaden los estados y transiciones del anterior
			auto=copiaAutomata(ai,auto);
			//// se añaden el nuevo estado inicial que tambiñn es final
			String s= "S"+estados;
			estados++;
			auto.insertaVertice(s);
			//// nuevo estado inicial y su transiciones al anterior
			auto.setEstadoInicial(s);
			auto.insertaArista(s,ai.getEstadoInicial(),"/");
			//// mismos estados finales y sus transiciones al inicial
			lfin.add(s);
			auto.setEstadosFinales(lfin);
			auto=modificaFinales(auto,s,ai.getEstadosFinales());
			if(muestraPasos){
				  imprimirPaso(auto,m.devuelveMensaje("ERToLambda.paso",2)+" "+numPasos+": "+m.devuelveMensaje("ERToLambda.asterisco",2));
				}
			controlador.trataMensaje(m.devuelveMensaje("ERToLambda.asterisco", 2));
			controlador.trataMensaje(auto.toString());
			numPasos++;
			return auto;
		}
		/*if(c.equals("/")){
			/// unico estado inicial y final con transicines / a ñl mismo
			String s= "S"+estados;
			estados++;
			auto.insertaVertice(s);
			//// nuevo estado inicial y sus trasiciones / a ñl mismo
			auto.setEstadoInicial(s);
			auto.insertaArista(s,s,"/");
			//// unico estado final
			lfin.add(s);
			auto.setEstadosFinales(lfin);
			if(muestraPasos){
				  imprimirPaso(auto,m.devuelveMensaje("ERToLambda.paso",2)+" "+numPasos+": "+m.devuelveMensaje("ERToLambda.lambda",2));
				}
			controlador.trataMensaje(m.devuelveMensaje("ERToLambda.lambda", 2));
			controlador.trataMensaje(auto.toString());
			numPasos++;
			return auto;
		}*/
		if(c.equals("%")){
			/// unico estado inicial con transicines / a ñl mismo
			String s= "S"+estados;
			estados++;;
			auto.insertaVertice(s);
			//// nuevo estado inicial y sus transiciones / a ñl mismo
			auto.setEstadoInicial(s);
			auto.insertaArista(s,s,"/");
			//// no hay estados finales
			auto.setEstadosFinales(lfin);
			if(muestraPasos){
				  imprimirPaso(auto,m.devuelveMensaje("ERToLambda.paso",2)+" "+numPasos+": "+m.devuelveMensaje("ERToLambda.vacio",2));
				}
			controlador.trataMensaje(m.devuelveMensaje("ERToLambda.vacio", 2));
			controlador.trataMensaje(auto.toString());
			numPasos++;
			return auto;
			
		}
		if(alfabeto.estaLetra(c)){
			String s = "S"+estados;
			estados++;
			auto.insertaVertice(s);
			String s1 = "S"+estados;
			estados++;
			auto.insertaVertice(s1);
			lfin.add(s1);
			auto.setEstadoInicial(s);
			auto.setEstadosFinales(lfin);
			auto.insertaArista(s,s1,c);
			if(muestraPasos){
				  imprimirPaso(auto,m.devuelveMensaje("ERToLambda.paso",2)+" "+numPasos+": "+m.devuelveMensaje("ERToLambda.letra",2));
				}
			controlador.trataMensaje(m.devuelveMensaje("ERToLambda.letra", 2));
			controlador.trataMensaje(auto.toString());
			numPasos++;
			return auto;
		}
		return null;
	}
	
	private void imprimirPaso(AutomataFNDLambda automata, String s){
		XML+="\n\t<step>\n\t\t<comment>\n\t\t\t<item>";
		XML+=s;
		XML+="</item>\n\t\t</comment>\n";
		XML+=traducirXML(automata);
		XML+="\n\t</step>";
		
	}
	
	private String traducirXML(AutomataFNDLambda automata) {
		String fichero="\t\t<authomata>\n\t\t\t<type>\n\t\t\t\t<item>";
		String tipoAutomatas="AutomataFNDLambda";
		fichero+=tipoAutomatas+"</item>\n\t\t\t</type>\n";
		Iterator<String> itAlfabeto=automata.getAlfabeto().dameListaLetras().iterator();
		fichero+="\t\t\t<alphabet>\n\t";
		while(itAlfabeto.hasNext()) {
			fichero+="\t\t\t<item>"+itAlfabeto.next()+"</item>\n\t";
		}
		fichero+="\t\t</alphabet>\n\t";
		//se genera la lista de estados y se guarda en el xml
		Iterator<String> itEst=automata.getEstados().iterator();
		fichero+="\t\t<states>\n\t";
		while(itEst.hasNext()) {
			fichero+="\t\t\t<state>"+itEst.next()+"</state>\n\t";
		}
		fichero+="\t\t</states>\n\t\t\t";
		//GENERACION DE LOS EsTADOS FINALES E INICIALES
		fichero+="<init>\n\t\t\t\t<state>"+automata.getEstadoInicial()+"</state>\n\t\t\t</init>\n\t\t\t";
		Iterator<String> itFin=automata.getEstadosFinales().iterator();
		fichero+="<finals>\n\t";
		while(itFin.hasNext()) {
			fichero+="\t\t\t<state>"+itFin.next()+"</state>\n\t\t";
		}
		fichero+="\t</finals>\n\t\t\t";
		//GENERACION DE LAS ARISTAS!
		fichero+="<arrows>\n\t";
		Iterator<String> itEstados=automata.getEstados().iterator();
		while(itEstados.hasNext()) {
			String e1=itEstados.next();
			Iterator<String> itAr=automata.getAristasVertice(e1).iterator();
			while(itAr.hasNext()){
				String letra=itAr.next();
				Iterator<String> itE=automata.deltaExtendida(e1, letra).iterator();
				while(itE.hasNext()){
					String e2=itE.next();
					fichero+="\t\t\t<arrow>\n\t";
					fichero+="\t\t\t\t<state>"+e1+"</state>\n\t";
					fichero+="\t\t\t\t<state>"+e2+"</state>\n\t";
					fichero+="\t\t\t\t<item>"+letra+"</item>\n\t\t\t\t</arrow>\n\t\t";
				}
			}
		}
		fichero+="\t</arrows>\n";
		fichero+="\t\t</authomata>";
		return fichero;
	}
	
	private static AutomataFNDLambda copiaAutomata(AutomataFNDLambda a1, AutomataFNDLambda a2){
		Iterator<String> iest = a1.getEstados().iterator();
		Iterator<String> iest2=null;
		ArrayList<String> a=null;
		Iterator<String> it=null;
		ArrayList<String> b=null;
		while(iest.hasNext()){
			String s=iest.next();
			a2.insertaVertice(s);
			a=a1.getAristasVertice(s);
			if(a!=null){
				iest2=a.iterator();
				while(iest2.hasNext()){
					String letra=iest2.next();
					b=a1.getAristasLetra(s,letra);
					it=b.iterator();
					while(it.hasNext()){
						String st=it.next();
						a2.insertaArista(s,st,letra);
					}
				}
			}
		}
		return a2;
	}
	
	private AutomataFNDLambda modificaFinales(AutomataFNDLambda auto,String nuevo, ArrayList<String> antiguos){
		Iterator<String> i= antiguos.iterator();
		while(i.hasNext()){
			String s=i.next();
			auto.insertaArista(s,nuevo,"/");
		}
		return auto;
	}

	
	public void registraControlador(Controlador controlador) {
		// TODO Auto-generated method stub
		this.controlador=controlador;
	}

	public String getXML() {
		return XML;
	}
	
}
