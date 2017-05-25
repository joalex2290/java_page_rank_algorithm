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
import java.util.Collections;
import java.util.Comparator;

// Clase que nos prestará apoyo
// Tiene una variable X y otra Y las cuales representan el elemento de la izquierda (X) y el elemento
// de la derecha (Y), el constructor requiere darle valores iniciales, el método first retorna el elemento de la
// izquierda y el método second retorna el elemento de la derecha además temos los sets.
class pair < X, Y > {
	X _first;
	Y _second;
	protected Object first;
	
	public pair(X f, Y s) { _first = f; _second = s; }
	
	X first() { return _first; }
	Y second() { return _second; }
	
	void setFirst(X f) { _first = f; }
	void setSecond(Y s) { _second = s; }
}//fin clase pair

//Clase PageRank que se encarga de rankear una serie de páginas
public class PageRank {

    private static final String NOMBRE_ARCHIVO = "dataset.txt";//Archivos de carga 1
    //private static final String NOMBRE_ARCHIVO = "dataset2.txt";//Archivos de carga 2
    private static final double PROBABILIDAD_TELEPORTACION = 0.85;//Valor seleccionado arbitratiamente
    private static final int MAX_NODOS = 100000;//Cantidad máxima de nodos
    private static final int ITERACIONES_METODO_POTENCIA = 4;//Iteracciones del método de la potencia
    private static int NUMERO_DE_NODOS = 0;//Numero total de nodos a procesar
    

    public static void main(String[] args) throws Exception {
    	//Vector encargado de almacenar los elementos a los cuales se conecta un nodo i, ej:
    	// Posicion 0 = 1,2,3,4 quiere decir que el nodo cero se conecta con 1, 2, 3 y 4.
        Vector< Vector<Integer> > vectorAristasNodo = new Vector< Vector<Integer> >(MAX_NODOS);
        // Pendiente de comentar, no sé de qué se encarga
        Vector< Vector< pair< Integer , Double > > > vectorMatrizA = new Vector<Vector< pair< Integer,Double >>>(MAX_NODOS);
        //Inicializamos los vectores anteriores.
        for (int i = 0; i < MAX_NODOS; i++){
        	Vector< pair< Integer , Double > > NodoMatrizA = new Vector< pair< Integer , Double > >();
        	Vector<Integer> NodoMatrizQ = new Vector<Integer>();
        	vectorAristasNodo.add(NodoMatrizQ);
        	vectorMatrizA.add(NodoMatrizA);
        }//Fin for
        
        //Lectura archivos
        FileReader in = new FileReader(NOMBRE_ARCHIVO);
        BufferedReader br = new BufferedReader(in);

        //Lectura archivos
        String line = null;
        while ((line = br.readLine()) != null) {
        	// Los datos vienen dados en el siguiente formato
        	// a b
        	// a c
        	// c a
        	// Donde los dos primeros numeros quiere decir que hay una arista del nodo a al nodo b
        	// así sucesivamente.
            String[] splits = line.split(" ");
            int x = Integer.parseInt(splits[0]);
            int y = Integer.parseInt(splits[1]);
            vectorAristasNodo.get(x).add(y); // Añadimos el primer nodo con el segundo nodo
            NUMERO_DE_NODOS = max(NUMERO_DE_NODOS,max(x,y));// La cantidad de nodos va estar acotada
            											// por el nodo mayor. 
        }//fin while
        br.close();// Cerramos lector
        System.out.println("Nodos: " + NUMERO_DE_NODOS);// Imprimimos cantidad de nodos
        double[][] matrizQ = new double[NUMERO_DE_NODOS][NUMERO_DE_NODOS];// Creamos la matriz Q que representa
        //																	las conexiones 
        double[][] matrizA = new double[NUMERO_DE_NODOS][NUMERO_DE_NODOS];// La matriz A va representar la matriz
     // 																	Q modificada
        for (int i = 0; i < NUMERO_DE_NODOS; i++) {
            for (int j = 0; j < NUMERO_DE_NODOS; j++) {
            	// Inicializamos las matrices
            	matrizQ[i][j] = 0;
                matrizA[i][j] = 0;
            }// fin for j
        }// fin for i
        
        for (int i = 0; i < MAX_NODOS; i++){
        	if(vectorAristasNodo.get(i).size() < 1) continue; // Si el nodo i no tiene conexiones lo ignoramos
            Vector<Integer> lista = vectorAristasNodo.get(i);// Obtenemos las conexiones
            int numeroAristas = lista.size();// Obtenemos los links de salida
            Iterator<Integer> iterador = lista.iterator();// Creamos iterador
         // Vamos a iterar a través de todas las conexiones
            while(iterador.hasNext()) {
            	int elemento = iterador.next();// Tomamos el siguiente elemento
                matrizQ[elemento - 1][i - 1] = 1;// Colocamos 1 si la el nodo i se conecta con el elemento actual
                // La formula del surfista aleatorio
                pair <Integer,Double> parejaAdiccionar = new pair< Integer , Double >(i,(PROBABILIDAD_TELEPORTACION / numeroAristas) + ((1 - PROBABILIDAD_TELEPORTACION) / NUMERO_DE_NODOS));
                vectorMatrizA.get(elemento).add(parejaAdiccionar);// Agregamos pareja
            }// fin while
        }// fin for i
        
     // Imprimimos matriz Q:
     // System.out.println("Matrix Q: ");
     // 
     // for (int i = 0; i < matrizQ.length; i++) {
     //     for (int j = 0; j < matrizQ[i].length; j++) {
     //         //System.out.print(matrizQ[i][j] + "  ");
     //     }// fin for j
     //     //System.out.println();
     // }// fin for i
     // Resultado matriz A
        System.out.println("Mapeo Matrix A: ");
        for (int i = 0; i < MAX_NODOS; i++){
        	if(vectorMatrizA.get(i).size() < 1) continue;// Si el nodo no tiene links de entrada se ignora
        	Vector<pair<Integer, Double>> lista = new Vector<pair<Integer, Double>>();// Vector
        	lista = vectorMatrizA.get(i);
        	Iterator<pair<Integer, Double>> iterador = lista.iterator();
        	System.out.printf("Nodo: %d(", i);
        	while(iterador.hasNext()){
        		pair<Integer,Double> pareja = iterador.next();
        		System.out.printf(" %d %.4f ", pareja.first(), pareja.second());
        	}// fin while
        	System.out.printf(")\n");
        }// fin for i
        
     // Pendiente de comentar
        Vector<Double> arrayR = new Vector<Double>();
        arrayR.setSize(NUMERO_DE_NODOS);
        Collections.fill(arrayR, (1.0 / NUMERO_DE_NODOS));
        //NORMALIZANDO EL VECTOR R
        
     // Rankeo total
        Vector<Double> vectorRanking = new Vector<Double>(NUMERO_DE_NODOS);
        vectorRanking.setSize(NUMERO_DE_NODOS);
     // Metodo de la potencia
        for (int i = 0; i < ITERACIONES_METODO_POTENCIA; i++) {
            // MULTIPLICANDO A x R (METODO POTENCIA)
            System.out.println("_________________________________");
            System.out.println("Iteracion Metodo Potencia: " + (i + 1));
            //System.out.println("Iteracion Metodo Potencia: " + (i + 1));
            for (int k = 0; k < arrayR.size(); k++) {// Vamos a recorrer todos los nodos
                Vector< pair<Integer,Double> > m = new Vector< pair<Integer,Double> >();//Pendiente de comentar
                if (vectorMatrizA.get(k+1).size() > 0)// Si un nodo tiene conexiones de entrada
                	// 									Remplazaamos el valor de m por esas conexiones
                    m = vectorMatrizA.get(k+1);
             // El rankeo hasta el momento
                double rank = 0;
                for (int j = 0; j < arrayR.size(); j++) {
                    double elementoMatriz = 0; // Elemento de la matriz
                    if (!contiene(m,j)) {// Si M no contiene un link de entrada entonces el elemento de la matriz
                    	// 					va ser igual a 0 + (1-B)/N
                        elementoMatriz =  (1 - PROBABILIDAD_TELEPORTACION) / NUMERO_DE_NODOS; // 0 + (1-B)/N
                        //System.out.println("ELEMENTO MATRIZ " + elementoMatriz);
                    }// fin if 
                    else {
                    	// Si existe una conexion a k desde j entonces el elementoMatriz va tomar el valor
                    	// de esa conexion
                        elementoMatriz = m.get(encontrarIndex(m,j)).second();
                        matrizA[k][j - 1] = m.get(encontrarIndex(m,j)).second();
                    }// fin else
                 // Sumamos al rankeo la multplicacion de el arrayR en la posicion J por el elemento matriz
                    rank += elementoMatriz * arrayR.get(j);
                }// fin for j
             // Asignamos el valor del rank a la posicion de la pagina k
                vectorRanking.set(k, rank);
                rank = 0;// reiniciamos rank
                //System.out.println(vectorRanking.get(k));
            }// fin for k
            arrayR = vectorRanking;// Volvemos a iterar ahora sobre el vector de rankeo de pagina
        }// fin for i
     // Matiz A:
     // System.out.println("Matrix A: ");
     // 
     // for (int i = 0; i < matrizQ.length; i++) {
     //     for (int j = 0; j < matrizA[i].length; j++) {
     //         //System.out.print(String.format( "%.2f", matrizA[i][j] ) + "  ");
     //     }// fin for j
     //     //System.out.println();
     // }// fin for i
        // Vector total
        System.out.println("Vector Ranking");
     // Imprimimos los rankeos en orden
        for (int i = 0; i < NUMERO_DE_NODOS; i++) {
            System.out.println("Pagina " + (i + 1) + " :" + vectorRanking.get(i));
        } 
    }
 // Encuentra el index de un elemento en un vector
    static int encontrarIndex(Vector< pair<Integer,Double> > vectorABuscar, int elemento){
    	for(int i = 0; i < vectorABuscar.size(); i++){
    		if (vectorABuscar.get(i).first() == elemento)
    			return i;
    	}// fin for i
    	return -1;
    }// fin metodo
 // Determina si un vector contiene un elemento
    static boolean contiene(Vector< pair<Integer,Double> > vectorABuscar, int elemento){
    	for (int i = 0; i < vectorABuscar.size(); i++){
    		if (vectorABuscar.get(i).first() == elemento)
    			return true;
    	}// fin for i
    	return false;
    }// fin metodo
    // Método que retorna si un elemento es mayor a otro
	static int max(int a, int b) {
		return a > b ? a : b;
	}//fin método
}// fin clase PageRank