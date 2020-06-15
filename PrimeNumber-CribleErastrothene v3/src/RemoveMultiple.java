
public class RemoveMultiple implements Runnable{
	
//	private final int indice;
//	private final int[] primeNumber;
	private int indice;
	private int[] primeNumber;
	
	public RemoveMultiple(int indice, int[] primeNumber) {
		this.indice = indice;
		this.primeNumber = primeNumber;
	}

	@Override
	public void run() {
		// (3) on 'barre' (= remise à 0) tous les multiples de 'indice' dans 'primeNumber'
		for (int i = indice; (i * indice) < primeNumber.length; i++) {
			try {
				primeNumber[i * indice] = 0;	// remise à 0 <=> barrer
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
