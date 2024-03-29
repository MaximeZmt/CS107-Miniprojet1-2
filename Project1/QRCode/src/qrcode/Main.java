package qrcode;


public class Main {

	public static final String INPUT="Hello World !!!"; 

	/*
	 * Parameters
	 */
	public static final int VERSION = 4;
	public static final int MASK = 2;
	public static final int SCALING = 10;

	public static void main(String[] args) {
		
		/*
		 * Encoding
		 */
		boolean[] encodedData = DataEncoding.byteModeEncoding(INPUT, VERSION);
		
		/*
		 * image
		 */
		int[][] qrCode = MatrixConstruction.renderQRCodeMatrix(VERSION, encodedData); // have to add mask number if want to force a mask. Otherwise use bonus part to select it.

		/*
		 * Visualization
		 */
		Helpers.show(qrCode, SCALING);
	}

}
