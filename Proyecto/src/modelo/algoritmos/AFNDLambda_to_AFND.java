package modelo.algoritmos;

import modelo.Algoritmo;
import modelo.automatas.Alfabeto;
import modelo.automatas.Automata;
import modelo.automatas.AutomataFND;
import modelo.automatas.AutomataFNDLambda;
import java.util.ArrayList;
import java.util.Iterator;
import accesoBD.Mensajero;
import controlador.Controlador;
import controlador.Controlador_imp;
/**
 * Algoritmo de transformaciñn de automatas no deterministas con
 * transiciones lambda a automatas no deterministas que no tienen 
 * transiciones lambda
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */


public class AFNDLambda_to_AFND implements Algoritmo {
	private Automata automataEntrada;
	private Automata automataSalida;
	private Mensajero m; 
	private Controlador controlador;
	private String xml;
	

	/**
	 * Constructor del algoritmo
	 * @param autEntrada automata que se va a transformar
	 */
	public AFNDLambda_to_AFND(Automata autEntrada) {
		m=new Mensajero();
		if(autEntrada instanceof AutomataFNDLambda) automataEntrada=autEntrada;
		else automataEntrada=null;
		automataSalida=new AutomataFND();
		xml="";
	}
	
	
	public void registraControlador(Controlador controlador) {
		this.controlador=controlador;
	}
	
	/**
	 * Funciñn principal que tranforma un automata con transiciones lambda a uno sin transiciones lambda
	 * @param pasos booleano para saber si transformar mostrando los pasos o no
	 */
	public Automata ejecutar(boolean pasos) {
		m=Mensajero.getInstancia();
		if (automataEntrada==null) {
			//error, automata de entrada no es AutomataFNDLambda
			controlador.trataMensaje(m.devuelveMensaje("AFNDlambda_to_AFND.error1", 2));
		}
		else {
			AutomataFNDLambda autEntrada=(AutomataFNDLambda)automataEntrada;
			ArrayList<String> listaEstados=autEntrada.getEstados();
			String estadoInicial=autEntrada.getEstadoInicial();
			
			ArrayList<ArrayList<String>> resulPar = new ArrayList<ArrayList<String>>();
			
			automataSalida.setEstados(listaEstados);
			automataSalida.setEstadoInicial(estadoInicial);
			
			Alfabeto listaLetrasMenosL=autEntrada.getAlfabetoMenosL();
			automataSalida.setAlfabeto(listaLetrasMenosL);
			
			for (int i=0;i<autEntrada.getEstados().size();i++){
				resulPar = resultadoPar(autEntrada.getEstados().get(i),autEntrada.getAlfabeto().dameListaLetrasmenosL());
			
					for (int a=0;a<autEntrada.getAlfabeto().dameListaLetrasmenosL().size();a++){
						if (resulPar.get(a)==null){}
						else for (int b=0;b<resulPar.get(a).size();b++){
								automataSalida.insertaArista(autEntrada.getEstados().get(i), resulPar.get(a).get(b), autEntrada.getAlfabeto().dameListaLetrasmenosL().get(a));
							}
					}
			}
			
			ArrayList<String> listaEstadosBien = new ArrayList<String>();
			ArrayList<String> temp = new ArrayList<String>();
			
			for (int i=0;i<automataSalida.getEstados().size();i++){
				for (int a=0;a<automataSalida.getAlfabeto().dameListaLetrasmenosL().size();a++){
					temp = automataSalida.deltaExtendida(automataSalida.getEstados().get(i), automataSalida.getAlfabeto().dameListaLetrasmenosL().get(a));
					if (temp != null){
						for (int b=0;b<temp.size();b++){
							if (!listaEstadosBien.contains(temp.get(b)))
								listaEstadosBien.add(temp.get(b));
						}
						if (!listaEstadosBien.contains(automataSalida.getEstados().get(i)))
							listaEstadosBien.add(automataSalida.getEstados().get(i));
					}
				}
			}
			automataSalida.setEstados(listaEstadosBien);
			
		controlador=Controlador_imp.getInstancia();
		controlador.trataMensaje(automataSalida.toString());
		controlador.trataMensaje(m.devuelveMensaje("AFNDLambdatoAFND.finalizado",2));
		controlador.trataMensaje(m.devuelveMensaje("AFNtoAFD.finalizado",2));
		xml+="<exit><steps>"+traducirPasos()+"</steps>";
		xml+="<result>"+traducirXML(automataSalida)+"</result></exit>";
	}	
		return automataSalida;
	}	
	/**
	 * Guardamos el automata para poder mostrarlo correctamente en formato XML
	 * 
	 * @return devolvemos un String con toda la informaciñn
	 */
	private String traducirPasos() {
		String fichero="<table>";
		fichero+="";
		Iterator<String> itEst=automataSalida.getEstados().iterator();
		while(itEst.hasNext()) {
			String estado=itEst.next();
			fichero+="<flecha>";
			fichero+="<estado>"+estado+"</estado>";
			Iterator<String> itLetras=automataSalida.getAlfabeto().dameListaLetrasmenosL().iterator();
			while(itLetras.hasNext()) {
				String letra=itLetras.next();
				String destino;
				ArrayList<String> dest=automataSalida.deltaExtendida(estado, letra);
				if (dest==null) destino="0";
				else destino=dest.toString();
				fichero+="<letra>"+letra+"</letra>";
				fichero+="<destino>"+destino+"</destino>";
			}
			fichero+="</flecha>";
		}

		fichero+="</table>";
		return fichero;
	}
	
	/**
	 * Traducimos el automata saliente para poder mostrarlo correctamente en formato XML
	 * @param automata automata que queremos pasar a XML
	 * @return String con toda la informaciñn en formato XML
	 */
	private String traducirXML(Automata automata) {
		String fichero="<authomata>\n\t<type>\n\t\t<item>";
		String tipoAutomatas="AutomataFND";
		fichero+=tipoAutomatas+"</item>\n\t</type>\n";
		Iterator<String> itAlfabeto=automata.getAlfabeto().dameListaLetras().iterator();
		fichero+="\t<alphabet>\n\t";
		while(itAlfabeto.hasNext()) {
			fichero+="\t<item>"+itAlfabeto.next()+"</item>\n\t";
		}
		fichero+="</alphabet>\n\t";
		//se genera la lista de estados y se guarda en el xml
		Iterator<String> itEst=automata.getEstados().iterator();
		fichero+="<states>\n\t";
		while(itEst.hasNext()) {
			fichero+="\t<state>"+itEst.next()+"</state>\n\t";
		}
		fichero+="</states>\n\t";
		//GENERACION DE LOS EsTADOS FINALES E INICIALES
		fichero+="<init>\n\t\t<state>"+automata.getEstadoInicial()+"</state>\n\t</init>\n\t";
		Iterator<String> itFin=automata.getEstadosFinales().iterator();
		fichero+="<finals>\n\t";
		while(itFin.hasNext()) {
			fichero+="\t<state>"+itFin.next()+"</state>\n\t";
		}
		fichero+="</finals>\n\t";
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
					fichero+="\t<arrow>\n\t";
					fichero+="\t<state>"+e1+"</state>\n\t";
					fichero+="\t<state>"+e2+"</state>\n\t";
					fichero+="\t<item>"+letra+"</item>\n\t</arrow>\n\t";
					}
				}	
			}
			fichero+="</arrows>\n";
			fichero+="</authomata>";
			xml = fichero;
			return fichero;
	}
	
	/**
	 * Funciñn usada en ejecutar(). Da el resultado para cada estado con todas las letras
	 * @param v1 estado del que queremos saber el resultado parcial
	 * @param letras conjunto de letras del alfabeto que usaremos para conseguir el resultado
	 * @return ArrayList<ArrayList<String>> con la informaciñn de cada estado con todas las letras del alfabeto
	 */
	private ArrayList<ArrayList<String>> resultadoPar(String v1,ArrayList<String> letras) {
		
		ArrayList<ArrayList<String>> resultado = new ArrayList<ArrayList<String>>();
		ArrayList<String> aux = new ArrayList<String>();
		ArrayList<String> estadosF = new ArrayList<String>();
		AutomataFNDLambda autEntrada=(AutomataFNDLambda)automataEntrada;
		ArrayList<String> primerlambda = autEntrada.cierreLambda(v1);
	
		for (int a=0;a<autEntrada.getEstadosFinales().size();a++){
			if (v1.equals(autEntrada.getEstadosFinales().get(a))){
				for (int b=0;b<primerlambda.size();b++){
					if (!estadosF.contains(primerlambda.get(b)))
					//automataSalida.setEstadoFinal(primerlambda.get(b));
					estadosF.add(primerlambda.get(b));
				}
			}
			if (primerlambda.contains(autEntrada.getEstadosFinales().get(a)))
				if (!estadosF.contains(v1))
					//automataSalida.setEstadoFinal(v1);	
					estadosF.add(v1);
					
		}
		automataSalida.setEstadosFinales2(estadosF);
		for (int i=0;i<letras.size();i++){
			ArrayList<String> deltas = autEntrada.deltaExtendida2(primerlambda, letras.get(i));

			if (!deltas.isEmpty()){
				ArrayList<String> aux2 = new ArrayList<String>();
				for (int c=0;c<deltas.size();c++){		
					aux = autEntrada.cierreLambda(deltas.get(c));
						for (int u=0;u<aux.size();u++){
							if (!aux2.contains(aux.get(u)))
								aux2.add(aux.get(u));
						}
				}
				resultado.add(aux2);
			}
			else {
				resultado.add(i,null);
			}
		}
		return resultado;
	}
	
	
	/**
	 * Devuelve el XML que contiene la informaciñn del automata
	 */
	public String getXML() {
		// TODO Auto-generated method stub
		return xml; 
	}
	
}
