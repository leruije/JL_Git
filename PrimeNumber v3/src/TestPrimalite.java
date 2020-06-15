import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

/*
 * Les premiers algorithmes pour d�cider si un nombre est premier (appel�s tests de primalit�) consistent 
 * � essayer de le diviser par tous les nombres qui n'exc�dent pas sa racine carr�e : 
 * s'il est divisible par l'un d'entre eux, il est compos�, et sinon, il est premier. 
 * Cependant, l'algorithme d�duit de cette formulation peut �tre rendu plus efficace : 
 * il sugg�re beaucoup de divisions inutiles, par exemple, si un nombre n'est pas divisible par 2, 
 * il est inutile de tester s'il est divisible par 4. 
 * En fait, il suffit de tester sa divisibilit� par tous les nombres premiers n'exc�dant pas sa racine carr�e.
 */

public class TestPrimalite {

	private static final String FILENAME = "PremiersNombresPremiers.txt";
	private static int [] xthPrime = {100,1000};
	
	public static void main(String[] args) throws FileNotFoundException {
		
		TestPrimalite tp = new TestPrimalite();

		System.out.println("Recherche de nombres premiers (via la m�thode utilisant la racine carr�e)");
		System.out.println("Ecriture de ces nombres dans un fichier .txt (en redirigeant Sysout vers ce fichier)");
		System.out.println();
		
		// X premiers nombres premiers, xthPrime = {100, 1000}
		for (int i = 0; i < xthPrime.length; i++) {
			long startTime = System.nanoTime();
			
			// cr�ation de la table primeNumber 
			ArrayList<Integer> primeNumber = new ArrayList<Integer>(); 

			// calcul des xth premiers nombres premiers demand�s
			primeNumber = tp.computePrimeNumber(xthPrime[i]); 					
			long endCalculTime = System.nanoTime();
			
			// �criture de la table des X premiers nombres premiers dans un fichier .txt
			long endPrintTime = tp.printPrimeToFile(primeNumber); 		

			// affichage des temps de calcul, �criture, total en nanosecondes et secondes
			tp.displayTimes(String.valueOf(xthPrime[i]), startTime, endCalculTime, endPrintTime); 	
		}
	}

	private ArrayList computePrimeNumber(int n) {
		// en entr�e : n = les n premiers nombres premiers � calculer
		// en sortie : le tableau avec ces n premiers nombres premiers calcul�s
		// rem: la table (ArrayList) prime contient un t�moin (true => le ni�me nombre est premier, sinon false)
		//      permet de tester la divisibilit� de n par tous les nombres premiers n'exc�dant pas sa racine carr�e.

		int count = 0;
		int i = 1;
		ArrayList<Boolean> prime = new ArrayList<Boolean>(); // {true si le ni�me nombre est premier, sinon false
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
		// algorithme de calcul bas� sur la division de n par tous les nombres premiers
		// qui n'exc�dent pas sa racine carr�e (lookup dans ArrayList<Boolean> prime)
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
		// en entr�e : tableau de nombres premiers � imprimer
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
				+ "' cr�� � la racine du projet");
		System.out.println(" --- Temps de calcul  en NANOSECONDES: " + (endPrimeTime - startTime) + " - en SECONDES: "
				+ getSeconds(endPrimeTime - startTime)); //
		System.out.println(" --- Temps d'�criture en NANOSECONDES: " + (endPrintTime - endPrimeTime)
				+ " - en SECONDES: " + getSeconds(endPrintTime - endPrimeTime));
		System.out.println(" --- Temps total (calcul + �criture), en NANOSECONDES: " + (endPrintTime - startTime)
				+ " - en SECONDES: " + getSeconds(endPrintTime - startTime));
		System.out.println();
	}

	private static String getSeconds(long l) {
	// conversion nanoseconde => seconde (utiliser ! pour faciliter l'�criture de displayTime(,,)
		return String.format("%.8f", (float) (l / Math.pow(10, 9)));
	}

}
