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