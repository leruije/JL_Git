import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

/*
 * Les premiers algorithmes pour décider si un nombre est premier (appelés tests de primalité) consistent 
 * à essayer de le diviser par tous les nombres qui n'excèdent pas sa racine carrée : 
 * s'il est divisible par l'un d'entre eux, il est composé, et sinon, il est premier. 
 * Cependant, l'algorithme déduit de cette formulation peut être rendu plus efficace : 
 * il suggère beaucoup de divisions inutiles, par exemple, si un nombre n'est pas divisible par 2, 
 * il est inutile de tester s'il est divisible par 4. 
 * En fait, il suffit de tester sa divisibilité par tous les nombres premiers n'excédant pas sa racine carrée.
 */

public class TestPrimalite {

	private static final String FILENAME = "PremiersNombresPremiers.txt";
	private static int [] xthPrime = {100,1000};
	
	public static void main(String[] args) throws FileNotFoundException {
		
		TestPrimalite tp = new TestPrimalite();

		System.out.println("Recherche de nombres premiers (via la méthode utilisant la racine carrée)");
		System.out.println("Ecriture de ces nombres dans un fichier .txt (en redirigeant Sysout vers ce fichier)");
		System.out.println();
		
		// X premiers nombres premiers, xthPrime = {100, 1000}
		for (int i = 0; i < xthPrime.length; i++) {
			long startTime = System.nanoTime();
			
			// création de la table primeNumber 
			ArrayList<Integer> primeNumber = new ArrayList<Integer>(); 

			// calcul des xth premiers nombres premiers demandés
			primeNumber = tp.computePrimeNumber(xthPrime[i]); 					
			long endCalculTime = System.nanoTime();
			
			// écriture de la table des X premiers nombres premiers dans un fichier .txt
			long endPrintTime = tp.printPrimeToFile(primeNumber); 		

			// affichage des temps de calcul, écriture, total en nanosecondes et secondes
			tp.displayTimes(String.valueOf(xthPrime[i]), startTime, endCalculTime, endPrintTime); 	
		}
	}

	private ArrayList computePrimeNumber(int n) {
		// en entrée : n = les n premiers nombres premiers à calculer
		// en sortie : le tableau avec ces n premiers nombres premiers calculés
		// rem: la table (ArrayList) prime contient un témoin (true => le nième nombre est premier, sinon false)
		//      permet de tester la divisibilité de n par tous les nombres premiers n'excédant pas sa racine carrée.

		int count = 0;
		int i = 1;
		ArrayList<Boolean> prime = new ArrayList<Boolean>(); // {true si le nième nombre est premier, sinon false
		prime.add(0, false); // 0 n'est pas premier
		prime.add(1, false); // 1 n'est pas premier

		ArrayList<Integer> primeNumber = new ArrayList<Integer>(); // contient les n premiers nombres premiers

		while (count < n) {
			i++;
			prime.add(i, false);
			if (isPrimeSqrt(i, prime)) {
				primeNumber.add(i);
				prime.set(i, true);
				count++;
			}
		}
		return primeNumber;
	}

	private boolean isPrimeSqrt(int n, ArrayList prime) {
		// algorithme de calcul basé sur la division de n par tous les nombres premiers
		// qui n'excèdent pas sa racine carrée (lookup dans ArrayList<Boolean> prime)
		if (n == 1) {
			return false;
		}
		for (int i = 2; i <= (int) Math.sqrt(n); i++) {
			if ((boolean) prime.get(i)) {
				if (n % i == 0) {
					return false;
				}
			}
		}
		return true;
	}

	private long printPrimeToFile(ArrayList primeNumber) {
		// en entrée : tableau de nombres premiers à imprimer
		// en sortie : temps d'impression

		String fileName = primeNumber.size() + FILENAME;
		PrintStream ps;
		try {
			ps = new PrintStream(new File(fileName));
			PrintStream console = System.out;
			System.setOut(ps); 			// redirection Sysout vers le fichier .txt
			for (int x = 0; x < primeNumber.size(); x++) {
				System.out.println(primeNumber.get(x));
			}
			System.setOut(console); 	// redirection Sysout vers la console
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return System.nanoTime();
	}

	private void displayTimes(String x, long startTime, long endPrimeTime, long endPrintTime) {
		System.out.println(" > " + x + " premiers (fichier '" + x + FILENAME
				+ "' créé à la racine du projet");
		System.out.println(" --- Temps de calcul  en NANOSECONDES: " + (endPrimeTime - startTime) + " - en SECONDES: "
				+ getSeconds(endPrimeTime - startTime)); //
		System.out.println(" --- Temps d'écriture en NANOSECONDES: " + (endPrintTime - endPrimeTime)
				+ " - en SECONDES: " + getSeconds(endPrintTime - endPrimeTime));
		System.out.println(" --- Temps total (calcul + écriture), en NANOSECONDES: " + (endPrintTime - startTime)
				+ " - en SECONDES: " + getSeconds(endPrintTime - startTime));
		System.out.println();
	}

	private static String getSeconds(long l) {
	// conversion nanoseconde => seconde (utiliser ! pour faciliter l'écriture de displayTime(,,)
		return String.format("%.8f", (float) (l / Math.pow(10, 9)));
	}

}
