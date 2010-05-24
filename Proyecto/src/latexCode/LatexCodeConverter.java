package latexCode;

import java.awt.Graphics2D;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import accesoBD.Mensajero;

import vista.vistaGrafica.AristaAP;
import vista.vistaGrafica.AristaTuring;
import vista.vistaGrafica.AutomataCanvas;
import vista.vistaGrafica.CurvedArrow;
import vista.vistaGrafica.PanelCentral;

public class LatexCodeConverter {

	private static LatexCodeConverter lc;
	private PanelCentral p;	 
	private int xIni1;
	private int xIni2;
	private int yIni;
	private String latex;
	private Mensajero mensajero;
	private char upDown;
	private int contador;
	private String blanco;
	
	public LatexCodeConverter(PanelCentral panel){
		
		p = panel;
		latex = "";
		upDown = '^';
		mensajero = Mensajero.getInstancia();
		blanco = mensajero.devuelveMensaje("simbolos.blanco",4);
		contador = 0;

	}
	
	/**
	 * lc es un Singleton por tanto contiene una instancia de si mismo
	 * y aqui la devuelve de forma estatica
	 * @return Mensajero instancia de Mensajero para la aplicaciñn
	 */
	public static LatexCodeConverter getInstancia(PanelCentral panel){ 
		if(lc==null) lc = new LatexCodeConverter(panel);
		return lc;
	}
	
	public void convertir(){
		generaCodigo();
	}
	
	private void generaCodigo(){
		
		
		contador++;
		AutomataCanvas c = p.getCanvas();
        int numEst = c.getListaEstados().size();
        int numAristas;
        String nombreEstado;
        String tipoAuto = null;
        int coorX;
        int coorY;
        int k = 1;
        int xOrigen;
        int yOrigen;
        int xDestino;
        int yDestino;
        String etiqueta;
        char com = '\"';
        /*String*/ latex = "% Generated by Rocio Barrig�ete, Mario Huete\n" +
        "\\documentclass[12pt]{article}\n" +
        "\\input xy\n" +
        "\\xyoption{all}\n" +
        "\\usepackage[all, knot]{xy}\n" +
        "\\xyoption{arc}\n" +
        "\\begin{document}\n\n" +
        "\\[\n" +
        "\\xy\n";
        for(int i = 0; i < numEst; i++){
            nombreEstado = c.getListaEstados().get(i).getEtiqueta();
            coorX = (Math.abs(c.getListaEstados().get(i).getX())*160)/842;
            coorY = (c.getListaEstados().get(i).getY()*160)/783;
            latex = latex + "("+coorX+","+coorY+")*{"+nombreEstado+"};\n";
        }
        for(int i = 0; i < numEst; i++){
            nombreEstado = c.getListaEstados().get(i).getEtiqueta();
            coorX = (Math.abs(c.getListaEstados().get(i).getX())*160)/842;
            coorY = (c.getListaEstados().get(i).getY()*160)/783;
            if(nombreEstado.equals(c.getEstadoInicial())){
                latex = latex + "("+coorX+","+coorY+")*\\xycircle(5.00,5.00){}="+com+nombreEstado+com+";\n";
                xIni1 = coorX-10;
                xIni2 = coorX-5;
                yIni = coorY;
            }
            else if(c.getListaFinales().contains(nombreEstado)){
                latex = latex + "("+coorX+","+coorY+")*\\xycircle(5.00,5.00){--}="+com+nombreEstado+com+";\n";
            }
            else {

                latex = latex + "("+coorX+","+coorY+")*\\xycircle(5.00,5.00){}="+com+nombreEstado+com+";\n";
            }
        }
        latex = latex + "\\ar@{=>}("+xIni1+","+yIni+")*{}; ("+xIni2+","+yIni+")*{}\n";
        /*\ar@/^1pc/^{a}(5,0)*{};(15,0)*{}
        \ar@/^2pc/^{a}(-3,4)*{};(4,3)*{}   %//XXX para loop ^o _ en 2pc*/
        tipoAuto  = p.getPanel().getTipoAutomata();
        if((tipoAuto.equals("AutomataFD"))||(tipoAuto.equals("AutomataFND"))||(tipoAuto.equals("AutomataFNDLambda"))){
        	numAristas = c.getListaAristas().size();
        	for(int j = 0; j < numAristas; j++){
                if(c.getListaAristas().get(j).getOrigen().equals(c.getListaAristas().get(j).getDestino())){
                    k = 2;
                    upDown = '_';
                }
                else {
                    k = 1;
                    upDown = '^';
                }
                xOrigen = (Math.abs(c.getListaAristas().get(j).getX())*160)/842;
                yOrigen = (c.getListaAristas().get(j).getY()*160)/783;
                xDestino = (Math.abs(c.getListaAristas().get(j).getFx())*160)/842;
                yDestino = (c.getListaAristas().get(j).getFy()*160)/783;
                etiqueta = c.getListaAristas().get(j).getEtiqueta();
                if(Math.abs(yOrigen-yDestino) > 30){
                	if(yOrigen > yDestino){
                		yOrigen = yOrigen - 5;
                		yDestino = yDestino + 5;
                	}
                	else {
                		yOrigen = yOrigen + 5;
                		yDestino = yDestino - 5;
                	}
                }
                else {
                	if(xOrigen < xDestino){
                		xOrigen = xOrigen + 5;
                		xDestino = xDestino - 5;
                	}
                	else {
                		xOrigen = xOrigen - 5;
                		xDestino = xDestino + 5;
                	}
                }
                latex = latex + "\\ar@/"+upDown+k+"pc/"+upDown+"{"+etiqueta+"}("+xOrigen+
                        ","+yOrigen+")*{};("+xDestino+","+yDestino+")*{}\n";
            }
        }
        if(tipoAuto.equals("AutomataPila")){
			numAristas = c.getListaAristasPila().size();
			int j = 0;
			int i = 0;
			String etiquetaAux = "";// String etiqueta = "";
			AristaAP aristaT = c.getListaAristasPila().get(i);
			while (i < numAristas){
				
				if ( !aristaT.getMarcada() ){ 
					//Mensajero m=Mensajero.getInstancia();	
					String separador = mensajero.devuelveMensaje("simbolos.separador",4);
					if (etiquetaAux.equals("")){ 
						
						/*String r = aristaT.getSimboloCinta();
						if (r.equals(blanco)) r = "\\"+r;*/
						
						etiquetaAux = dameEtiqueta(aristaT.getEntradaSimbolos(),0) + separador + aristaT.getCimaPila() + separador + dameEtiqueta(aristaT.getSalidaPila(),1);
						j = i+1;
					}
				
					if (j < numAristas){ // falta llave
				
						AristaAP aux = c.getListaAristasPila().get(j);
						boolean origen,destinos;		
						destinos = aristaT.getDestino().equals( aux.getDestino());
						origen = aristaT.getOrigen().equals( aux.getOrigen());

						if (destinos && origen && !aux.getMarcada() ) {
							
							/*String r = aux.getSimboloCinta();
							if (r.equals(blanco)) r = "\\"+r;*/
							etiquetaAux += " , " + dameEtiqueta(aux.getEntradaSimbolos(),0) + separador + aux.getCimaPila() + separador + dameEtiqueta(aux.getSalidaPila(),1);

							aux.setMarcada(true);
						//j++;
						}
					
					}
							
					if	(j >= numAristas){
		
						
						//if(c.getListaAristasTuring().get(j).getOrigen().equals(c.getListaAristasTuring().get(j).getDestino()))
							
							if(aristaT.getOrigen().equals(aristaT.getDestino())){
			                    k = 2;
			                    upDown = '_';
							}
							else {
								k = 1;
								upDown = '^';
							}
							
			                xOrigen = (Math.abs(c.getListaAristasPila().get(i).getX())*160)/842;
			                yOrigen = (c.getListaAristasPila().get(i).getY()*160)/783;
			                xDestino = (Math.abs(c.getListaAristasPila().get(i).getFx())*160)/842;
			                yDestino = (c.getListaAristasPila().get(i).getFy()*160)/783;
			               // etiqueta = construyeEtiqueta(c.getListaAristasTuring());

						
			                if(Math.abs(yOrigen-yDestino) > 30){
			                	if(yOrigen > yDestino){
			                		yOrigen = yOrigen - 5;
			                		yDestino = yDestino + 5;
			                	}
			                	else {
			                		yOrigen = yOrigen + 5;
			                		yDestino = yDestino - 5;
			                	}
			                }
			                else {
			                	if(xOrigen < xDestino){
			                		xOrigen = xOrigen + 5;
			                		xDestino = xDestino - 5;
			                	}
			                	else {
			                		xOrigen = xOrigen - 5;
			                		xDestino = xDestino + 5;
			                	}
			                }
			                
			                latex = latex + "\\ar@/"+upDown+k+"pc/"+upDown+"{"+etiquetaAux+"}("+xOrigen+
	                        ","+yOrigen+")*{};("+xDestino+","+yDestino+")*{}\n";
					}
				}
				
				if (j >= numAristas){ 
					i++; 
					j = i+1;  
					etiquetaAux = "";
					if (i < numAristas) 	aristaT = c.getListaAristasPila().get(i);
				}
				else j++; 
			}
        }
        if(tipoAuto.equals("MaquinaTuring")){
        	
        	
			numAristas = c.getListaAristasTuring().size();
			int j = 0;
			int i = 0;
			String etiquetaAux = "";// String etiqueta = "";
			AristaTuring aristaT = c.getListaAristasTuring().get(i);
			while (i < numAristas){
				
				if ( !aristaT.getMarcada() ){ 
					//Mensajero m=Mensajero.getInstancia();	
					String separador = mensajero.devuelveMensaje("simbolos.separador",4);
					if (etiquetaAux.equals("")){ 
						
						String r = aristaT.getSimboloCinta();
						if (r.equals(blanco)) r = "\\"+r;
						
						etiquetaAux = dameEtiqueta(aristaT.getEntradaCinta(),0) + separador + r + separador + /*dameEtiqueta(*/aristaT.getDireccion()/*)*/;
						j = i+1;
					}
				
					if (j < numAristas){ // falta llave
				
						AristaTuring aux = c.getListaAristasTuring().get(j);
						boolean origen,destinos;		
						destinos = aristaT.getDestino().equals( aux.getDestino());
						origen = aristaT.getOrigen().equals( aux.getOrigen());

						if (destinos && origen && !aux.getMarcada() ) {
							
							String r = aux.getSimboloCinta();
							if (r.equals(blanco)) r = "\\"+r;
							
							etiquetaAux += " , " + dameEtiqueta(aux.getEntradaCinta(),0) + separador + r + separador + /*dameEtiqueta(*/aux.getDireccion()/*SalidaPila())*/;
							aux.setMarcada(true);
						//j++;
						}
					
					}
							
					if	(j >= numAristas){
		
						
						//if(c.getListaAristasTuring().get(j).getOrigen().equals(c.getListaAristasTuring().get(j).getDestino()))
							
							if(aristaT.getOrigen().equals(aristaT.getDestino())){
			                    k = 2;
			                    upDown = '_';
							}
							else {
								k = 1;
								upDown = '^';
							}
							
			                xOrigen = (Math.abs(c.getListaAristasTuring().get(i).getX())*160)/842;
			                yOrigen = (c.getListaAristasTuring().get(i).getY()*160)/783;
			                xDestino = (Math.abs(c.getListaAristasTuring().get(i).getFx())*160)/842;
			                yDestino = (c.getListaAristasTuring().get(i).getFy()*160)/783;
			               // etiqueta = construyeEtiqueta(c.getListaAristasTuring());

						
			                if(Math.abs(yOrigen-yDestino) > 30){
			                	if(yOrigen > yDestino){
			                		yOrigen = yOrigen - 5;
			                		yDestino = yDestino + 5;
			                	}
			                	else {
			                		yOrigen = yOrigen + 5;
			                		yDestino = yDestino - 5;
			                	}
			                }
			                else {
			                	if(xOrigen < xDestino){
			                		xOrigen = xOrigen + 5;
			                		xDestino = xDestino - 5;
			                	}
			                	else {
			                		xOrigen = xOrigen - 5;
			                		xDestino = xDestino + 5;
			                	}
			                }
			                
			                latex = latex + "\\ar@/"+upDown+k+"pc/"+upDown+"{"+etiquetaAux+"}("+xOrigen+
	                        ","+yOrigen+")*{};("+xDestino+","+yDestino+")*{}\n";
					}
				}
				
				if (j >= numAristas){ 
					i++; 
					j = i+1;  
					etiquetaAux = "";
					if (i < numAristas) 	aristaT = c.getListaAristasTuring().get(i);
				}
				else j++; 
			}

        }
        
        latex = latex + "\n"+"\\endxy\n" +
                "\\]\n\n" +
                "\\end{document}";
       System.out.println(latex);
	   creaArchivo();	
	}
	
	
	// 0 para simbolos, 1 para transicion de pila
	private String dameEtiqueta(ArrayList<String> a, int tipo){
	
		String s = "";
		Iterator<String> it = a.iterator();
		
		
		while (it.hasNext()){
			String r = it.next();
			if (r.equals(blanco)) r = "\\"+r;
			if (tipo == 0 && it.hasNext())
				r+=",";
			//else r+=
			s += r;
		}

		return s;
	}
	
	
	
	private void creaArchivo(){
		
	    try {
	    	// Apertura del fichero y creacion de BufferedReader para poder
	        // hacer una lectura comoda (disponer del metodo readLine()).
    		
    		PrintWriter pw = null;
    		String ruta = "LaTeX/"+"CLaTeX"+contador+".tex";
    		FileWriter fichero = new FileWriter(ruta);
			pw = new PrintWriter(fichero);

            pw.println(latex);
            muestraTex(ruta);
            pw.close();
	    }

    	catch(Exception e){
    		e.printStackTrace();
    	}
	}
	
	public void muestraTex(String ruta){
		
			Mensajero m= mensajero;
			String url=ruta;
			String osName = System.getProperty("os.name");
		      try {
		         if (osName.startsWith("Mac OS")) {
		            Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
		            Method openURL = fileMgr.getDeclaredMethod("openURL",
		               new Class[] {String.class});
		            openURL.invoke(null, new Object[] {url});
		            }
		         else if (osName.startsWith("Windows"))
		            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
		         else { //assume Unix or Linux
		            String[] browsers = {"TeXnicsCenter", "TeXworks", "Winedt","Kile"};
		            String browser = null;
		            for (int count = 0; count < browsers.length && browser == null; count++)
		               if (Runtime.getRuntime().exec(
		                     new String[] {"which", browsers[count]}).waitFor() == 0)
		                  browser = browsers[count];
		            if (browser == null) {
		            	JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.navegador", 2),"Error",JOptionPane.ERROR_MESSAGE);
		            } else {
		               Runtime.getRuntime().exec(new String[] {browser, url});
		            }
		         }
		      }
		      catch (Exception e) {
		    	  JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.ejecucion", 2),"Error",JOptionPane.ERROR_MESSAGE);
		      }
		
	}
	
}
