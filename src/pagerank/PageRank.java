/*
    # PSEUDOCODIGO #

    Algoritmo PageRank

    teleportacion = 0.85;
    iteraciones = 4;
    matrizP(nodos,nodos) = matriz binaria leida de fichero;
    nodos = longitud matrizP;
    matrizA(nodos,nodos) = matriz de ceros;
    numeroAristas(nodos) = arreglo de suma de las columnas;

    // Link rank y Random Walker en una sola iteracion. complejidad O(NxN)
    For i = 0 to nodos
            For j = 0 to nodos
                    matrizA(i,j) = teleportacion * ( matrizP(i,j) / numeroAristas(i)) + ((1-teleportacion)/nodos);
            End For
    End For

    arregloR(nodos) = 1/nodos  en cada posicion;
    arregloRanking(nodos) = ceros en cada posicion;

    //Iterando Power method A*R = yR. Complejidad O(NxN)
    For i = 0 to iteraciones
    print "Iteracion " + i
            For j = 0 to nodos
                    For k = 0 to nodos
                            arregloRanking(j) = arregloRanking(j) + (matrizA(k,j) * arregloR(j));
                    End For
            End For
    End For

    Print arregloRanking;
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Vector;
import java.util.BitSet;
import java.util.Collections;

class pair < X, Y > {
	X _first;
	Y _second;
	
	public pair(X f, Y s) { _first = f; _second = s; }
	
	X first() { return _first; }
	Y second() { return _second; }
	
	void setFirst(X f) { _first = f; }
	void setSecond(Y s) { _second = s; }
}

public class PageRank {

    private static final String NOMBRE_ARCHIVO = "dataset2.txt";
    private static final double PROBABILIDAD_TELEPORTACION = 0.85;
    private static final int MAX_NODOS = 100000;
    private static int NODO_MAXIMO = 0;
    private static final int ITERACIONES_METODO_POTENCIA = 4;
    private static int NUMERO_DE_NODOS = 875713;
    

    public static void main(String[] args) throws Exception {
        Vector< Vector<Integer> > vectorAristasNodo = new Vector< Vector<Integer> >(MAX_NODOS);
        Vector< Vector< pair< Integer , Double > > > vectorMatrizA = new Vector<Vector< pair< Integer,Double >>>(MAX_NODOS);
        BitSet registroNumeros = new BitSet(MAX_NODOS);
        registroNumeros.clear();
        
        for (int i = 0; i < MAX_NODOS; i++){
        	Vector< pair< Integer , Double > > NodoMatrizA = new Vector< pair< Integer , Double > >();
        	Vector<Integer> NodoMatrizQ = new Vector<Integer>();
        	vectorAristasNodo.add(NodoMatrizQ);
        	vectorMatrizA.add(NodoMatrizA);
        }

        FileReader in = new FileReader(NOMBRE_ARCHIVO);
        BufferedReader br = new BufferedReader(in);

        String line = null;
        while ((line = br.readLine()) != null) {
            String[] splits = line.split(" ");

            int x = Integer.parseInt(splits[0]);
            int y = Integer.parseInt(splits[1]);
            vectorAristasNodo.get(x).add(y);
            NODO_MAXIMO = max(NODO_MAXIMO,max(x,y));
            registroNumeros.set(x, true);
            registroNumeros.set(y, true);
        }
        br.close();

        NUMERO_DE_NODOS = 0;
        for (int i = 0; i < MAX_NODOS; i++)
        	if (registroNumeros.get(i) == true)
        		NUMERO_DE_NODOS++;
        registroNumeros = null;
        System.out.println("Nodos: " + NUMERO_DE_NODOS);
        System.out.println("NODO MAYOR: " + NODO_MAXIMO);
        double[][] matrizQ = new double[NODO_MAXIMO][NODO_MAXIMO];
        double[][] matrizA = new double[NODO_MAXIMO][NODO_MAXIMO];
        for (int i = 0; i < NODO_MAXIMO; i++) {
            for (int j = 0; j < NODO_MAXIMO; j++) {
                matrizQ[i][j] = 0;
                matrizA[i][j] = 0;
            }
        }
        
        for (int i = 0; i < MAX_NODOS; i++){
        	if(vectorAristasNodo.get(i).size() < 1) continue;
            Vector<Integer> lista = vectorAristasNodo.get(i);
            int numeroAristas = lista.size();
            Iterator<Integer> iterador = lista.iterator();
            while(iterador.hasNext()) {
            	int elemento = iterador.next();
                matrizQ[elemento - 1][i - 1] = 1;
                pair <Integer,Double> parejaAdiccionar = new pair< Integer , Double >(i,(PROBABILIDAD_TELEPORTACION / numeroAristas) + ((1 - PROBABILIDAD_TELEPORTACION) / NUMERO_DE_NODOS));
                vectorMatrizA.get(elemento).add(parejaAdiccionar);
            }
        }

        System.out.println("Matrix Q: ");

        for (int i = 0; i < matrizQ.length; i++) {
            for (int j = 0; j < matrizQ[i].length; j++) {
                //System.out.print(matrizQ[i][j] + "  ");
            }
            //System.out.println();
        }

        System.out.println("Mapeo Matrix A: ");
        for (int i = 0; i < MAX_NODOS; i++){
        	if(vectorMatrizA.get(i).size() < 1) continue;
        	Vector<pair<Integer, Double>> lista = new Vector<pair<Integer, Double>>();
        	lista = vectorMatrizA.get(i);
        	Iterator<pair<Integer, Double>> iterador = lista.iterator();
        	System.out.printf("Nodo: %d(", i);
        	while(iterador.hasNext()){
        		pair<Integer,Double> pareja = iterador.next();
        		System.out.printf(" %d %.4f ", pareja.first(), pareja.second());
        	}
        	System.out.printf(")\n");
        }
        Vector<Double> arrayR = new Vector<Double>();
        arrayR.setSize(NODO_MAXIMO);
        Collections.fill(arrayR, (1.0 / NUMERO_DE_NODOS));
        //NORMALIZANDO EL VECTOR R

        Vector<Double> vectorRanking = new Vector<Double>(NODO_MAXIMO);
        vectorRanking.setSize(NODO_MAXIMO);

        for (int i = 0; i < ITERACIONES_METODO_POTENCIA; i++) {
            // MULTIPLICANDO A x R (METODO POTENCIA)
            System.out.println("_________________________________");
            System.out.println("Iteracion Metodo Potencia: " + (i + 1));
            //System.out.println("Iteracion Metodo Potencia: " + (i + 1));
            for (int k = 0; k < arrayR.size(); k++) {
                Vector< pair<Integer,Double> > m = new Vector< pair<Integer,Double> >();
                if (vectorMatrizA.get(k+1).size() > 0)
                    m = vectorMatrizA.get(k+1);
                
                double rank = 0;
                for (int j = 0; j < arrayR.size(); j++) {
                    double elementoMatriz = 0;
                    if (!contiene(m,j)) {
                        elementoMatriz =  (1 - PROBABILIDAD_TELEPORTACION) / NUMERO_DE_NODOS; // 0 + (1-B)/N
                        //System.out.println("ELEMENTO MATRIZ " + elementoMatriz);
                    } else {
                        elementoMatriz = m.get(encontrarIndex(m,j)).second();
                        matrizA[k][j - 1] = m.get(encontrarIndex(m,j)).second();
                    }
                    rank += elementoMatriz * arrayR.get(j);
                }
                vectorRanking.set(k, rank);
                rank = 0;
                //System.out.println(vectorRanking.get(k));
            }
            arrayR = vectorRanking;
        }

        System.out.println("Matrix A: ");

        for (int i = 0; i < matrizQ.length; i++) {
            for (int j = 0; j < matrizA[i].length; j++) {
                //System.out.print(String.format( "%.2f", matrizA[i][j] ) + "  ");
            }
            //System.out.println();
        }

        System.out.println("Vector Ranking: ");

        for (int i = 0; i < NODO_MAXIMO; i++) {
            System.out.println("Pagina " + (i + 1) + " :" + vectorRanking.get(i));
        } 
    }
    
    static int encontrarIndex(Vector< pair<Integer,Double> > vectorABuscar, int elemento){
    	for(int i = 0; i < vectorABuscar.size(); i++){
    		if (vectorABuscar.get(i).first() == elemento)
    			return i;
    	}
    	return -1;
    }

    static boolean contiene(Vector< pair<Integer,Double> > vectorABuscar, int elemento){
    	for (int i = 0; i < vectorABuscar.size(); i++){
    		if (vectorABuscar.get(i).first() == elemento)
    			return true;
    	}
    	return false;
    }
    
	static int max(int a, int b) {
		return a > b ? a : b;
	}
}