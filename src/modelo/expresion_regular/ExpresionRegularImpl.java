package modelo.expresion_regular;

import modelo.AutomatasException;
import modelo.automatas.Alfabeto;
import java.util.StringTokenizer;
import java.util.ArrayList;

import accesoBD.Mensajero;

/**
 * Clase que implementa la interfaz de expresión regular e implementa las funcines
 * que las mismas realizan en la aplicación.
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class ExpresionRegularImpl implements ExpresionRegular {

	private String expresion;
	private Alfabeto alfabeto;
	private ArbolER arbol;
	private ArrayList<String> tokens;
	private int i;
	private Mensajero mensajero;
	private boolean cerrado;
	private boolean mas;
	private int j;

	/**
	 * Constructor de expresion regular que inicializa la expresion y calcula el árbol
	 * sintáctico de la misma de manera recursiva y con una sola pasada.
	 * @param a alfaberto de la expresión
	 * @param exp cadena que representa la expresión regular
	 * @throws AutomatasException lanza la excepción si la expresión está mal construida
	 * o tiene un error sintáctico. 
	 */
	public ExpresionRegularImpl(Alfabeto a,String exp)throws AutomatasException{
		i=0;
		j=0;
		cerrado=false;
		mas=false;
		mensajero=new Mensajero();
		this.expresion=exp;
		alfabeto=a;
		tokens=rellenaTokens(expresion);
		arbol=analiza();
		arbol.setER(expresion);
	}

	private ArbolER analiza() {
		// TODO Auto-generated method stub
		return expresion(null);
	}
	
	private ArbolER expresion(ArbolER arb){
		if(cerrado)return arb;
		if(mas && j==1) return arb;
		if(i>=tokens.size()) return arb;
		else{
			if(tokens.get(i).equals("%")){
				return new ArbolER_imp(null,null,tokens.get(i));
			} else{
				if(tokens.get(i).equals("+")){
					String s=tokens.get(i);
					if(mas) return arb;
					i++;
					mas=true;
					j=0;
					ArbolER aux=expresion(null);
					cerrado=false;
					mas=false;
					return expresion(new ArbolER_imp(arb,aux,s));
				} else {
					return expresion(factor(arb));
				}
			}
		}
	}
	
	private ArbolER factor(ArbolER arb){
		if(i>=tokens.size()) return arb;
		else{
			if(tokens.get(i).equals(".")){
				String s=tokens.get(i);
				i++;
				ArbolER aux=termino(null);
				return new ArbolER_imp(arb,aux,s);	
			} else {
				return termino(arb);
			}
		}
	}
	
	private ArbolER termino(ArbolER arb){
		if(tokens.get(i).equals("(")){
			i++;
			return expresion(null);
		} else {
			if(tokens.get(i).equals(")")){
				if(mas){
					j++;
					return arb;
				}
				if(i+1<tokens.size()){
					if(tokens.get(i+1).equals("*")){
						i+=2;
						return new ArbolER_imp(arb,null,"*");
					} else {
						if(tokens.get(i+1).equals("^+")){
							i+=2;
							return new ArbolER_imp(arb,null,"^+");
						}	
					}
				}
				i++; 
				cerrado=true;
				return arb;
			} else {
				return letras(arb);
			}
		}
	}
	
	private ArbolER letras(ArbolER arb){
		ArbolER a=new ArbolER_imp(null,null,tokens.get(i));
		if(i+1<tokens.size()){
			if(tokens.get(i+1).equals("*")){
				i+=2;
				return new ArbolER_imp(a,null,"*");
			} else {
				if(tokens.get(i+1).equals("^+")){
					i+=2;
					return new ArbolER_imp(a,null,"^+");
				}	
			}
		}
		i++;
		return a;
	}

	 
	public ArbolER getArbolER(){
		// TODO Auto-generated method stub
		return arbol;
	}

	
	public String getExpresionRegular() {
		// TODO Auto-generated method stub
		return expresion;
	}
	
	private boolean estaAlfabeto(String s){
		return alfabeto.estaLetra(s);
	}
	
	private ArrayList<String> rellenaTokens(String s) throws AutomatasException{
		ArrayList<String> t= new ArrayList<String>();
		StringTokenizer tok=new StringTokenizer(s,"+*()^ ",true);
		while(tok.hasMoreTokens()){
			String ss=tok.nextToken();
			if(!ss.equals(" ")){
				if(esSignoReservado(ss)){
					if(ss.equals("^") && tok.nextToken().equals("+")) t.add("^+");
					else {
						if(ss.equals("^") && !tok.nextToken().equals("+")){
							throw new AutomatasException(mensajero.devuelveMensaje("expregular.operadores",2));
						}
						else
							if(t!=null && t.size()!=0){
								if((t.get(t.size()-1).equals(")")||esUnitario(t.get(t.size()-1))||estaAlfabeto(t.get(t.size()-1)))&&ss.equals("(")){
									t.add(".");
								}
								t.add(ss);
							}
							else{
								if(t.size()==s.length()-1 && ss.equals("+"))
									throw new AutomatasException(mensajero.devuelveMensaje("expregular.operadores",2));
								else
									t.add(ss);
							}
					}
				} else {
					if(t!=null && t.size()-1>0){
						if(t.get(t.size()-1).equals(")")||t.get(t.size()-1).equals("*")||t.get(t.size()-1).equals("^+")){
							t.add(".");
						}
					}
					ArrayList<String> a=tratarString(ss);
					int j=0;
					while(j<a.size()){
						t.add(a.get(j));
						j++;
					}
				}
			}
		}
		return t; 
	}
	
	private boolean esUnitario(String s){
		return s.equals("*")||s.equals("^+");
	}
	
	private ArrayList<String> tratarString(String s)throws AutomatasException{
		int pos=0;
		ArrayList<String> a= new ArrayList<String>();
		String ss=null;
		while(pos<s.length()){
			Character c= s.charAt(pos);
			if(ss!=null) ss+=c.toString();
			else ss=new String(c.toString());
			if(estaAlfabeto(ss)){
				a.add(ss);
				if(pos<s.length()-1){
					a.add(".");
				}
				ss=null;
			} else {
				if(pos>=s.length()+1) throw new AutomatasException(mensajero.devuelveMensaje("expregular.letras",2));
			}
			pos++;
		}
		if(ss!=null) throw new AutomatasException(mensajero.devuelveMensaje("expregular.letras",2));
		return a;
	}
	
	private boolean esSignoReservado(String s){
		return (s.equals("+")||s.equals("*")||s.equals("^")||
		s.equals("(")||s.equals(")"));
	}
	
	
	public Alfabeto getAlfabeto() {
		return alfabeto;
	}

	
	public void setAlfabeto(Alfabeto alfabeto) {
		this.alfabeto = alfabeto;
	}

}
