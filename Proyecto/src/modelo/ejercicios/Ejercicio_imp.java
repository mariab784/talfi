/**
 * 
 */
package modelo.ejercicios;
import modelo.*;
import modelo.algoritmos.AFNDLambda_to_AFND;
import modelo.algoritmos.AFN_to_AFD;
import modelo.algoritmos.AutomataP_to_GramaticaIC;
import modelo.algoritmos.Automatas_equivalentes;
import modelo.algoritmos.C_Y_K;
import modelo.algoritmos.ERToAFNDLambda;
import modelo.algoritmos.GIC_to_FNChomsky;
import modelo.automatas.Alfabeto;
import modelo.automatas.AlfabetoCinta;
import modelo.automatas.Alfabeto_Pila;
import modelo.automatas.Automata;
import modelo.automatas.AutomataFD;
import modelo.automatas.AutomataFND;
import modelo.automatas.AutomataFNDLambda;
import modelo.automatas.AutomataPila;
import modelo.automatas.MaquinaTuring;
import modelo.expresion_regular.ArbolER;
import modelo.expresion_regular.ExpresionRegular;
import modelo.expresion_regular.ExpresionRegularImpl;

/**
 * Clase que implementa las funciones asociadas a los ejercicios
 * de la aplicaciñn asñ como a su correciñn.
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class Ejercicio_imp implements Ejercicio{
	
	private String enunciado;
	private Object entrada;
	private Object resultado;
	private Alfabeto alf;
	private String tipo;
	private String ruta;
	private Alfabeto_Pila alfPila;
	private AlfabetoCinta alfCinta;
	private String pintar;
	private Object solucion;

	/**
	 * Constructor que crea un ejercicio con los parñmetros que se le pasan
	 * @param enunciado enunciado del ejrcicio
	 * @param entrada entrada del usuario
	 * @param resultado soluciñn del ejercicio
	 * @param alf alfabeto del ejercicio
	 * @param tipo identificador de cadena de texto con el tipo del ejercicio:
	 * "Lenguaje" o "Automatas"
	 */
	public Ejercicio_imp(String enunciado, Object entrada, Object resultado, Alfabeto alf,String tipo,String ruta) {
		this.enunciado=enunciado;
		this.entrada=entrada;
		this.resultado=resultado;
		this.alf=alf;
		this.tipo=tipo;
		this.ruta=ruta;
		alfPila = null;
	}
	
/*	public Ejercicio_imp(String enunciado, Object entrada, Object resultado, Alfabeto alf,String tipo,String ruta,String pintar) {
		
		this(enunciado,entrada,resultado,alf,tipo,ruta);
		this.pintar = pintar;
	}*/
	/**
	 * Constructor que crea un ejercicio con los parñmetros que se le pasan
	 * @param enunciado enunciado del ejrcicio
	 * @param entrada entrada del usuario
	 * @param resultado soluciñn del ejercicio
	 * @param alf alfabeto del ejercicio
	 * @param alfPila alfabeto de Pila del ejercicio
	 * @param tipo identificador de cadena de texto con el tipo del ejercicio:
	 * "Lenguaje" o "Automatas"
	 */
/*	public Ejercicio_imp(String enunciado, Object entrada, Object resultado, Alfabeto alf,Alfabeto_Pila alfPila, 
			String tipo, String ruta) {
		this(enunciado, entrada, resultado,alf, tipo,ruta);
		this.alfPila = alfPila;
	}*/

	public Ejercicio_imp(String enunciado, Object entrada, Object resultado, Alfabeto alf,Alfabeto_Pila alfPila, 
			String tipo, String ruta,String pintar) {
		this(enunciado, entrada, resultado,alf, tipo,ruta);
		this.alfPila = alfPila;
		this.pintar = pintar;
	}
	
	public Ejercicio_imp(String enunciado, Object entrada, Object resultado, Alfabeto alf,AlfabetoCinta alfCinta, 
			String tipo, String ruta,String pintar) {
		this(enunciado, entrada, resultado,alf,tipo,ruta);
		this.alfCinta = alfCinta;
		this.pintar = pintar;
	}
	
	public String getEnunciado() {
		// TODO Auto-generated method stub
		return enunciado;
	}

	
	public Object getEntrada() {
		// TODO Auto-generated method stub
		return entrada;
	}

	
	public Object getResultado() {
		// TODO Auto-generated method stub
		return resultado;
	}

	
	public boolean corregir(Object respuesta) throws AutomatasException {
		// TODO Auto-generated method stub
		boolean dePila = false;
		boolean deTuring = false;
		//System.out.println("imprimeme respuesta a ver..." + respuesta);
		if(respuesta instanceof AutomataPila){
			//System.out.println("imprimeme resultado a ver..." + resultado);

			System.out.println("imprimeme respuesta a ver..." + respuesta);
			System.out.println("listaPalabraskesi: " + ((AutomataPila)resultado).getListaPalabrasEj());
			System.out.println("listaPalabraskeno: " + ((AutomataPila)resultado).getListaPalabrasEjNo());
			AutomataP_to_GramaticaIC algapgic = new AutomataP_to_GramaticaIC((Automata)respuesta);
			GIC_to_FNChomsky gictofnc = new GIC_to_FNChomsky(algapgic.getGic(),true);
			C_Y_K cyk = new C_Y_K(((AutomataPila)resultado).getListaPalabrasEj(),
					((AutomataPila)resultado).getListaPalabrasEjNo(),gictofnc.getGramaticaSalida());
			
			System.out.println(cyk.getAceptadas()&& cyk.getNoAceptadas());
			return cyk.getAceptadas()&& cyk.getNoAceptadas();
		}
		else if(respuesta instanceof MaquinaTuring){System.out.println("SIIIIIIIIIIIIIITURING");
		return false;
		}
		else{
		
		
		System.out.println("imprimeme entrada a ver..." + entrada);
		System.out.println("imprimeme resultado a ver..." + resultado);
		
		
		if (respuesta instanceof String) {//ES UNA EXPRESION REGULAR!!!!
			ExpresionRegular er=null;
			try {
				//EN PRIMER LUGAR PASAMOS HASTA AFD LA ER QUE NOS DAN!
				er = new ExpresionRegularImpl(alf,(String)respuesta);
				ArbolER arbEr=er.getArbolER();
				Algoritmo algEr=new ERToAFNDLambda(arbEr,alf);
				Automata a1=algEr.ejecutar(false);
				//
				Algoritmo algAFNDlambda=new AFNDLambda_to_AFND(a1);
				Automata a2=algAFNDlambda.ejecutar(false);
				//
				Algoritmo algAFN_AFD=new AFN_to_AFD(a2);
				Automata a3=algAFN_AFD.ejecutar(false);
				AutomataPila ap1 = null;
				AutomataPila ap2 = null;
				MaquinaTuring mt1 = null;
				MaquinaTuring mt2 = null;
				//(YA TENEMOS EL AUTOMATA QUE VAMOS A COMPARAR PARA LA RESPUESTA QUE NOS DAN)
				////(ahora vamos a calcular el automata para el resultado que tenemos almacenado
				//que deberña ser un string! para poder compararlos despuñs
				if(resultado instanceof String ){
					er = new ExpresionRegularImpl(alf,(String)resultado);
					ArbolER arbErRes=er.getArbolER();
					algEr=new ERToAFNDLambda(arbErRes,alf);
					Automata a1Res=algEr.ejecutar(false);
					//
					algAFNDlambda=new AFNDLambda_to_AFND(a1Res);
					Automata a2Res=algAFNDlambda.ejecutar(false);
					//
					algAFN_AFD=new AFN_to_AFD(a2Res);
					Automata a3Res=algAFN_AFD.ejecutar(false);
					/////////////////////////////////////////////////////////////////////
					Automatas_equivalentes algEquivalencia=new Automatas_equivalentes();
					algEquivalencia.registraAutomata1(a3);
					algEquivalencia.registraAutomata2(a3Res);
					algEquivalencia.ejecutar(false);
					return algEquivalencia.getResultado();//devuelve el resultado de la correcion
				}
				/*if(resultado instanceof MaquinaTuring){
					System.out.println("ESTOY EN EJERCICIO_IMP CORREGIR SIN HACER");
					deTuring = true;
				}
				if(resultado instanceof AutomataPila){
					System.out.println("ESTOY EN EJERCICIO_IMP CORREGIR SIN HACER");
					dePila = true;
					
				}*/
				
				
				if(resultado instanceof AutomataFNDLambda){
					algAFNDlambda=new AFNDLambda_to_AFND((Automata)resultado);
					Automata a2Res=algAFNDlambda.ejecutar(false);
					//
					algAFN_AFD=new AFN_to_AFD(a2Res);
					Automata a3Res=algAFN_AFD.ejecutar(false);
					/////////////////////////////////////////////////////////////////////
					Automatas_equivalentes algEquivalencia=new Automatas_equivalentes();
					algEquivalencia.registraAutomata1(a3);
					algEquivalencia.registraAutomata2(a3Res);
					algEquivalencia.ejecutar(false);
					return algEquivalencia.getResultado();//devuelve el resultado de la correcion
				}
				if(resultado instanceof AutomataFND){
					algAFN_AFD=new AFN_to_AFD((Automata)resultado);
					Automata a3Res=algAFN_AFD.ejecutar(false);
					/////////////////////////////////////////////////////////////////////
					Automatas_equivalentes algEquivalencia=new Automatas_equivalentes();
					algEquivalencia.registraAutomata1(a3);
					algEquivalencia.registraAutomata2(a3Res);
					algEquivalencia.ejecutar(false);
					return algEquivalencia.getResultado();//devuelve el resultado de la correcion
				}
				if(resultado instanceof AutomataFD){
					Automatas_equivalentes algEquivalencia=new Automatas_equivalentes();
					algEquivalencia.registraAutomata1(a3);
					algEquivalencia.registraAutomata2((Automata)resultado);
					algEquivalencia.ejecutar(false);
					return algEquivalencia.getResultado();//devuelve el resultado de la correcion
				}
			} catch (AutomatasException e) {
				// TODO Auto-generated catch block
				throw e;
			}
		}
		else {
			//EN PRIMER LUGAR PASAMOS HASTA AFD LA ER QUE NOS DAN!
			Automata a=null;
			if (!(resultado instanceof AutomataFD)) {
				if (resultado instanceof AutomataFNDLambda) {
					Automata res=(Automata) resultado;
					Algoritmo algAFNDlambda=new AFNDLambda_to_AFND(res);
					Automata a2=algAFNDlambda.ejecutar(false);
					//
					Algoritmo algAFN_AFD=new AFN_to_AFD(a2);
					a=algAFN_AFD.ejecutar(false);
				}
				else {
					if(resultado instanceof String){
						try {
							ExpresionRegular er = new ExpresionRegularImpl(alf,(String)resultado);
							ArbolER arbErRes=er.getArbolER();
							Algoritmo algEr=new ERToAFNDLambda(arbErRes,alf);
							Automata a1Res=algEr.ejecutar(false);
							//
							Algoritmo algAFNDlambda=new AFNDLambda_to_AFND(a1Res);
							Automata a2Res=algAFNDlambda.ejecutar(false);
							//
							Algoritmo algAFN_AFD=new AFN_to_AFD(a2Res);
							a=algAFN_AFD.ejecutar(false);
						} catch(AutomatasException e){
							throw e;
						}
					}else{
						Algoritmo algAFN_AFD=new AFN_to_AFD((Automata)resultado);
						a=algAFN_AFD.ejecutar(false);
					}
				}
			}
			//AKI TENDREMOS KE HACER COSAS
			//
			//(YA TENEMOS EL AUTOMATA QUE VAMOS A COMPARAR PARA LA RESPUESTA QUE NOS DAN)
			////(ahora vamos a calcular el automata para la respuesta que nos han dado
			//para poder compararlos despuñs
				
				if (!(respuesta instanceof AutomataFD)) {
					if (respuesta instanceof AutomataFNDLambda) {
						Automata res=(Automata) respuesta;
						Algoritmo algAFNDlambda=new AFNDLambda_to_AFND(res);
						Automata a2=algAFNDlambda.ejecutar(false);
						//
						Algoritmo algAFN_AFD=new AFN_to_AFD(a2);
						respuesta=algAFN_AFD.ejecutar(false);
					}
					else {
						Algoritmo algAFN_AFD=new AFN_to_AFD((Automata)respuesta);
						respuesta=algAFN_AFD.ejecutar(false);
					}
				
				}
			
	/*		if(dePila){
				
				
			}
			if(deTuring){
				
				
			}*/
			//if(!dePila && !deTuring){
			Automatas_equivalentes algEquivalencia=new Automatas_equivalentes();
			algEquivalencia.registraAutomata1((Automata)respuesta);
			if (a==null) 
				algEquivalencia.registraAutomata2((Automata) resultado);
			else 
				algEquivalencia.registraAutomata2(a);
			algEquivalencia.ejecutar(false);
			return algEquivalencia.getResultado();//devuelve el resultado de la correcciñn
			//}
		}
		return false;
		}//llave else arriba por si no pila o turing
		

		
	}
	
	public boolean corregirEquivalencia(boolean resultado) {
		return false;
		
	}

	
	public Alfabeto getAlfabeto() {
		// TODO Auto-generated method stub
		return alf;
	}
	
	
	public String toString() {
		if (entrada!=null)
			return enunciado.toString()+"\n"+entrada.toString()+"\n"+resultado.toString()+"\n";
		else return enunciado.toString()+"\n"+resultado.toString()+"\n";
	}


	
	public String getTipo() {
		// TODO Auto-generated method stub
		return tipo;
	}

	
	public String getRuta() {
		return ruta;
	}

	
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

}
