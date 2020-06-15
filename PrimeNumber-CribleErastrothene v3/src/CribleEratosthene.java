import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/*
 * ... 
 * Le crible d'�ratosth�ne est une m�thode, reposant sur cette id�e, 
 * qui fournit la liste des nombres premiers inf�rieurs � une valeur fix�e n 
 *
 *	(1) on forme la liste des entiers de 2 � n ;
 *	(2) on retient comme � nombre premier � le premier nombre de la liste non encore barr� (le premier dans ce cas est 2) ;
 *	(3) on barre tous les entiers multiples du nombre retenu � l'�tape pr�c�dente,
 *		en commen�ant par son carr� (puisque 2 � i, 3 � i, � , (i � 1) � i ont d�j� �t� barr�s en tant que multiples de 2, 3, ...) ;
 *	(4) on r�p�te les deux derni�res op�rations (c'est-�-dire : on retient le prochain nombre non barr� et on barre ses multiples) ;
 *	(5) d�s qu'on en est � chercher les multiples des nombres exc�dant la racine carr�e de n, on termine l'algorithme.
 *
 *  Hypoth�ses de base : on connait � l'avance le centi�me premier nombre premier = 541 
 *                       on connait � l'avance le milli�me premier nombre premier = 7919 
 *  - soit en allant consulter : https://fr.wikipedia.org/wiki/Liste_de_nombres_premiers
 *  - soit en allant consuler le fichier '1000PremiersNombresPremiers.txt', r�sultat du premier exercice r�solu TestPrimalite
 *
 */

public class CribleEratosthene {
	
	private static final String FILENAME = "PremiersNombresPremiers.txt";
	private static final int [] xthPrime = {100,1000};
	private static final int [] xthN = {542,7920};
	
	public static void main(String[] args) {
		
		CribleEratosthene ce = new CribleEratosthene();
		
		System.out.println("Recherche de nombres premiers (via Le crible d'�ratosth�ne)");
		System.out.println("Ecriture de ces nombres dans un fichier .txt");
		System.out.println();
		
	
		// X premiers nombres premiers, X = {100, 1000}
		for (int i = 0; i < xthPrime.length; i++) {
			long startTime = System.nanoTime();
			
			// cr�ation / initialisation de la table primeNumber 
			int n = xthN[i];
			int[] primeNumber = new int[n];	
			ce.createListOfInteger(primeNumber);
			
			// lancement des threads, marquage des multiples
			int limit = 1 + (int) Math.sqrt(n);			// (5)=> fin de l'algorithme
			for (int t=2; t <= limit; t++) {
				RemoveMultiple rm = new RemoveMultiple(t, primeNumber);
				Thread tx = new Thread(rm);
//				tx.setName("thread -> suppression multiple de " + t );
//				System.out.println(tx.getName());
				tx.start();
			}
			long endCalculTime = System.nanoTime();
			
			// �criture de la table des X premiers nombres premiers dans un fichier .txt
			long endPrintTime = ce.printPrimeToFile(String.valueOf(xthPrime[i]), primeNumber);	
			
			// affichage des temps de calcul, �criture, total en nanosecondes et secondes
			ce.displayTimes(String.valueOf(xthPrime[i]), startTime, endCalculTime, endPrintTime); 	// affichage temps recherche et �criture dans
		}
	}

	private void createListOfInteger(int[] primeNumber) {	
		// (1) on forme la liste des entiers de 2 � n (n = longeur du tableau d'entiers primeNumber);
		for (int i = 2; i < primeNumber.length; i++) {
			primeNumber[i] = i;
		}
	}
		
	private long printPrimeToFile(String x, int[] primeNumber) {
		// en entr�e : tableau de nombres premiers � imprimer
		// en sortie : temps d'impression

		String fileName = x + FILENAME;
		PrintStream ps;
		try {
			ps = new PrintStream(new File(fileName));
			PrintStream console = System.out;
			System.setOut(ps); 			// redirection Sysout vers le fichier .txt
			for (int value : primeNumber) {
				if (value != 0) System.out.println(value);
			}
			System.setOut(console); 	// redirection Sysout vers la console
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return System.nanoTime();
	}
	
	private void displayTimes(String x, long startTime, long endPrimeTime, long endPrintTime) {
		System.out.println(" > " + x + " premiers (fichier '" + x + FILENAME
				+ "' cr�� � la racine du projet");
		System.out.println(" --- Temps de calcul  en NANOSECONDES: " + (endPrimeTime - startTime) + " - en SECONDES: "
				+ getSeconds(endPrimeTime - startTime)); //
		System.out.println(" --- Temps d'�criture en NANOSECONDES: " + (endPrintTime - endPrimeTime)
				+ " - en SECONDES: " + getSeconds(endPrintTime - endPrimeTime));
		System.out.println(" --- Temps total (calcul + �criture), en NANOSECONDES: " + (endPrintTime - startTime)
				+ " - en SECONDES: " + getSeconds(endPrintTime - startTime));
		System.out.println();
	}
	
	private String getSeconds(long l) {
	// conversion nanoseconde => seconde (utilis� ! pour faciliter l'�criture de displayTime(,,)
		return String.format("%.8f", (float) (l / Math.pow(10, 9)));
	}
}
