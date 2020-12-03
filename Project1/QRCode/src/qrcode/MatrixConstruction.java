package qrcode;

public class MatrixConstruction {

	/*
	 * Constants defining the color in ARGB format
	 * 
	 * W = White integer for ARGB
	 * 
	 * B = Black integer for ARGB
	 * 
	 * both needs to have their alpha component to 255
	 */
	final static int W = 0xFF_FF_FF_FF;
	final static int B = 0xFF_00_00_00;
	

	// ...  MYDEBUGCOLOR = ...;
	// feel free to add your own colors for debugging purposes
	final static int RED = 0xFF_FF_00_00;
	final static int GREEN = 0xFF_00_FF_00;
	final static int BLUE = 0xFF_00_00_FF;
	final static int PINK = 0xFF_FF_81_7A;

	/**
	 * Create the matrix of a QR code with the given data.
	 * 
	 * @param version
	 *            The version of the QR code
	 * @param data
	 *            The data to be written on the QR code
	 * @param mask
	 *            The mask used on the data. If not valid (e.g: -1), then no mask is
	 *            used.
	 * @return The matrix of the QR code
	 */
	public static int[][] renderQRCodeMatrix(int version, boolean[] data, int mask) {

		/*
		 * PART 2
		 */
		int[][] matrix = constructMatrix(version, mask);
		/*
		 * PART 3
		 */
		addDataInformation(matrix, data, mask);

		return matrix;
	}

	/*
	 * =======================================================================
	 * 
	 * ****************************** PART 2 *********************************
	 * 
	 * =======================================================================
	 */

	/**
	 * Create a matrix (2D array) ready to accept data for a given version and mask
	 * 
	 * @param version
	 *            the version number of QR code (has to be between 1 and 4 included)
	 * @param mask
	 *            the mask id to use to mask the data modules. Has to be between 0
	 *            and 7 included to have a valid matrix. If the mask id is not
	 *            valid, the modules would not be not masked later on, hence the
	 *            QRcode would not be valid
	 * @return the qrcode with the patterns and format information modules
	 *         initialized. The modules where the data should be remain empty.
	 */
	public static int[][] constructMatrix(int version, int mask) { // All structure components added to the empty matrix
		int[][] matrix = initializeMatrix(version);
		addFinderPatterns(matrix);
		addAlignmentPatterns(matrix, version);
		addTimingPatterns(matrix);
		addDarkModule(matrix);
		addFormatInformation(matrix, mask);
		
		
		return matrix;
	}

	/**
	 * Create an empty 2d array of integers of the size needed for a QR code of the
	 * given version
	 * 
	 * @param version
	 *            the version number of the qr code (has to be between 1 and 4
	 *            included
	 * @return an empty matrix
	 */
	public static int[][] initializeMatrix(int version) { // Creation of an array of a certain size depending of the version.
		int matrixSize = QRCodeInfos.getMatrixSize(version);
		return new int[matrixSize][matrixSize];
	}

	/**
	 * Add all finder patterns to the given matrix with a border of White modules.
	 * 
	 * @param matrix
	 *            the 2D array to modify: where to add the patterns
	 */
	public static void addFinderPatterns(int[][] matrix) { // Creation of the finder pattern
		int matrixSize = matrix[0].length;
		for (int i=0; i<3;++i) {
			if (i==0) {
				initialisePattern(matrix, 0, 0, 8, 0, 0);
			}
			else if (i==1) {
				initialisePattern(matrix, matrixSize-8, 0, 8, 1, 0);
			}
			else if (i==2) {
				initialisePattern(matrix, 0, matrixSize-8, 8, 0, 1);
			}
		}
	}


	
	private static void initialisePattern(int[][] matrix, int x, int y, int size, int deltaX, int deltaY) {
		addWhiteSquare(matrix, x, y, size); // Addition of the white square in the background
		addSquarePattern(matrix, x+deltaX, y+deltaY, size-1); // Addition of the finder pattern above the white square
	}
	
	
	private static void addWhiteSquare(int[][] matrix, int x, int y, int size) { // Creation of a white square
		for (int i = 0; i<size; ++i) {
			for (int j=0; j<size; ++j) {
				matrix[x+i][y+j]=W;
			}
		}
	}
	
	
	private static void addSquarePattern(int[][] matrix, int x, int y, int size) { // Placement of the finder pattern and the alignement pattern
		for (int i=0; i<size;++i) { // row
			for (int j=0; j<size;++j) { // line
				if ( (j==1 && i>0 && i<size-1) || (j==size-2 && i>0 && i<size-1) || (i==1 && j>0 && j<size-1) || (i==size-2 && j>0 && j<size-1)) {
					matrix[x+i][y+j] = W;
				}
				else {
					matrix[x+i][y+j] = B;
				}
			}
		}
		
	}
	
	
	/**
	 * Add the alignment pattern if needed, does nothing for version 1
	 * 
	 * @param matrix
	 *            The 2D array to modify
	 * @param version
	 *            the version number of the QR code needs to be between 1 and 4
	 *            included
	 */
	public static void addAlignmentPatterns(int[][] matrix, int version) { // Creation of the alignement pattern
		if (version>1) {
			int matrixLength = matrix[0].length;
			addSquarePattern(matrix, matrixLength-9, matrixLength-9, 5);
		}
	}

	/**
	 * Add the timings patterns
	 * 
	 * @param matrix
	 *            The 2D array to modify
	 */
	public static void addTimingPatterns(int[][] matrix) { // Creation of the timing pattern
		int matrixLength = matrix[0].length;
		int indexer = 0;
		for (int i=0;i<matrixLength;++i) { // Horizontal Line
			if(i>7&&i<matrixLength-8) {
				if (indexer%2==0) {
					matrix[i][6]=B;
				}else {
					matrix[i][6]=W;
				}
				++indexer;
			}
		}
		for (int i=0;i<matrixLength;++i) { // Vertical Line
			if(i>7&&i<matrixLength-8) { 
				if (indexer%2==0) { // Switch between black and white thanks to the modulo and the inverser variable
					matrix[6][i]=W;
				}else {
					matrix[6][i]=B;
				}
				++indexer;
			}
		}
	}

	/**
	 * Add the dark module to the matrix
	 * 
	 * @param matrix
	 *            the 2-dimensional array representing the QR code
	 */
	public static void addDarkModule(int[][] matrix) { // Creation of the dark module
		int matrixLength = matrix[0].length;
		matrix[8][matrixLength-8] = B;
	}

	/**
	 * Add the format information to the matrix
	 * 
	 * @param matrix
	 *            the 2-dimensional array representing the QR code to modify
	 * @param mask
	 *            the mask id
	 */
	public static void addFormatInformation(int[][] matrix, int mask) { // Addition of all modules that specify the QrCode type
		int matrixLength = matrix[0].length;
		boolean[] correctorCode = QRCodeInfos.getFormatSequence(mask);
		for (int i = 0; i<15;++i) {
			int fill;
			if (correctorCode[i]==false) {
				fill = W;
			}
			else {
				fill = B;
			}
			if(i<6) {
				matrix[i][8]=fill;
				matrix[8][matrixLength-1-i]=fill;
			}else if (i==6){
				matrix[i+1][8]=fill;
				matrix[8][matrixLength-1-i]=fill;
			}
			else if (i==7) {
				matrix[i+1][8]=fill;
				matrix[matrixLength-8][8]=fill;
			}
			else if (i==8) {
				matrix[i][7]=fill;
				matrix[matrixLength-7][8]=fill;
			}
			else if (i>8) {
				matrix[8][5-i+9]=fill;
				matrix[matrixLength-8-7+i][8]=fill;
			}
		}
	}

	/*
	 * =======================================================================
	 * ****************************** PART 3 *********************************
	 * =======================================================================
	 */

	/**
	 * Choose the color to use with the given coordinate using the masking 0
	 * 
	 * @param col
	 *            x-coordinate
	 * @param row
	 *            y-coordinate
	 * @param color
	 *            : initial color without masking
	 * @return the color with the masking
	 */
	public static int maskColor(int col, int row, boolean dataBit, int masking) { // Formatting of the data before setting it in the matrix. Flip of the data depending on the mask
		
		
		boolean inverser=false; // Variable used to know if the data needs to be flipped
		switch (masking) { // Formulas of the 8 masks
			case 0:
				if ((col+row)%2==0) {
					inverser = true;
				}else {
					inverser = false;
				}
				break;
			case 1:
				if ((row % 2) == 0) {
					inverser = true;
				}else {
					inverser = false;
				}
				break;
			case 2:
				if (col % 3 == 0) {
					inverser = true;
				}else {
					inverser = false;
				}
				break;
			case 3:
				if ((col+row)%3 == 0) {
					inverser = true;
				}else {
					inverser = false;
				}
				break;
			case 4:
				if ((Math.floor(row/2)+Math.floor(col/3))%2 == 0) {
					inverser = true;
				}else {
					inverser = false;
				}
				break;
			case 5:
				if (((col*row)%2)+((col*row)%3) == 0) {
					inverser = true;
				}else {
					inverser = false;
				}
				break;
			case 6:
				if (   (( ((col*row)%2) + ((col*row)%3) )%2)    == 0) {
					inverser = true;
				}else {
					inverser = false;
				}
				break;
			case 7:
				if (  ( ( ((col+row)%2) + ((col*row)%3) )%2 )  == 0) {
					inverser = true;
				}else {
					inverser = false;
				}
				break;
			default:
				inverser=false;
				break;
				}
		if (inverser) { // Returns black or white depending on the mask
			if (dataBit) {
				return W;
			}
			else {
				return B;
			}
		}else{
			if (dataBit) {
				return B;
			}
			else {
				return W;
			}
			
		}		
	}

	/**
	 * Add the data bits into the QR code matrix
	 * 
	 * @param matrix
	 *            a 2-dimensionnal array where the bits needs to be added
	 * @param data
	 *            the data to add
	 */
	public static void addDataInformation(int[][] matrix, boolean[] data, int mask) { // Filling of the matrix
		int matrixLength = matrix[0].length;
		int dataL = data.length;
		int counter = 0;
		for (int i=matrixLength-1; i>2;i=i-4) {
			for(int j=matrixLength-1; j>=0;--j) { // Upwards
					if (matrix[i][j]==0x00) {
						
						
						boolean dataFill = false;
						if (counter<dataL) { 
							dataFill = data[counter];
						}else {
							dataFill = false;
						}
						
						
						matrix[i][j]=maskColor(i, j, dataFill , mask);
						++counter;
					}
					if (matrix[i-1][j]==0x00) {
						
						boolean dataFill = false;
						if (counter<dataL) { 
							dataFill = data[counter];
						}else {
							dataFill = false;
						}
						
						
						matrix[i-1][j]=maskColor(i-1, j, dataFill, mask);
						++counter;
					}
					
				}
			
			if(i==8) { // Skip of the timing pattern
				i=i-1;
			}
			
			for(int j=0; j<matrixLength;++j) { // Downwards
				if (matrix[i-2][j]==0x00) {
					boolean dataFill = false;
					if (counter<dataL) { 
						dataFill = data[counter];
					}else {
						dataFill = false;
					}
					
					matrix[i-2][j]=maskColor(i-2, j, dataFill, mask);
					++counter;
				}
				if (matrix[i-3][j]==0x00) {
					boolean dataFill = false;
					if (counter<dataL) { 
						dataFill = data[counter];
					}else {
						dataFill = false;
					}
					
					
					matrix[i-3][j]=maskColor(i-3, j, dataFill, mask);
					++counter;
				}
			}
		}
	}

	/*
	 * =======================================================================
	 * 
	 * ****************************** BONUS **********************************
	 * 
	 * =======================================================================
	 */

	/**
	 * Create the matrix of a QR code with the given data.
	 * 
	 * The mask is computed automatically so that it provides the least penalty
	 * 
	 * @param version
	 *            The version of the QR code
	 * @param data
	 *            The data to be written on the QR code
	 * @return The matrix of the QR code
	 */
	public static int[][] renderQRCodeMatrix(int version, boolean[] data) {

		int mask = findBestMasking(version, data);

		return renderQRCodeMatrix(version, data, mask);
	}

	/**
	 * Find the best mask to apply to a QRcode so that the penalty score is
	 * minimized. Compute the penalty score with evaluate
	 * 
	 * @param data
	 * @return the mask number that minimize the penalty
	 */
	public static int findBestMasking(int version, boolean[] data) {
		
		int mask = 0 ;
		int penaltyMaskBest = 0 ;
		int penaltyMask = 0 ;
		int maskBest = 0 ;
	
		for(mask = 0; mask <= 7; ++mask) { // Creation of the matrix with each mask applied
			int[][] matrix = constructMatrix(version, mask) ;
			addDataInformation(matrix, data, mask);
			System.out.print("mask " + "" + mask + " : ");
			penaltyMask = evaluate(matrix) ; // Calculation of the penalty points 
			if(penaltyMask <= penaltyMaskBest || penaltyMaskBest==0) { // Mask with the smallest total of penalty points taken
				penaltyMaskBest = penaltyMask ;
				maskBest = mask ;
			}
		}
		
		System.out.println("The best mask is N." + maskBest + " with " + penaltyMaskBest + " penalty points.") ;
		
		return maskBest ;
	}

	/**
	 * Compute the penalty score of a matrix
	 * 
	 * @param matrix:
	 *            the QR code in matrix form
	 * @return the penalty score obtained by the QR code, lower the better
	 */
	public static int evaluate(int[][] matrix) {
		
		int sizeMatrix = matrix.length ;
		int penaltyPoint = 0 ;
		
// Penalty 1 : Comparison of each byte with the next one. While identical, ++counter. If not identical and if counter>= 5, penaltyPoints+=(counter-2)
		
		for(int col=0; col<sizeMatrix; ++col) { // Comparison for each row
			int compteur1 = 1 ;
			for(int lin=0; lin<sizeMatrix-1; ++lin) { // (sizeMatrix-1) guarantees that it won't be out of bounds
				if(matrix[col][lin]==matrix[col][lin+1]) { // Comparison for each byte with the next one
					++compteur1 ;
				}
				else {
					if(compteur1 >= 5) { // If the byte is not identical to the one next to it, check of the counter
						penaltyPoint += (compteur1 - 2) ;
					}
					compteur1 = 1 ;
				}
			}
			if(compteur1 >= 5) { // Guarantee that when we are at the end of the row, the counter is still verified before being reset
				penaltyPoint += (compteur1 - 2) ; 
			}
		}
		
		for(int lin=0; lin<sizeMatrix; ++lin) { // Comparison for each line
			int compteur2 = 1 ;
			for(int col=0; col<sizeMatrix-1; ++col) {
				if(matrix[col][lin]==matrix[col+1][lin]) {
					++compteur2 ;
				}
				else {
					if(compteur2 >= 5) {
						penaltyPoint += (compteur2 - 2) ; // Guarantee that when we are at the end of the line, the counter is still verified
					}
					compteur2 = 1 ;
				}
			}
			if(compteur2 >= 5) {
				penaltyPoint += (compteur2 - 2) ; 
			}
		}

// Penalty 2 : Comparison of each byte with the one next to it, the one under it, the one in diagonal. If they are all identical, penaltyPoints += 3
		
		for(int col=0; col<sizeMatrix-1; ++col) { // (sizeMatrix-1) guarantees that it won't be out of bounds
			for(int lin=0; lin<sizeMatrix-1; ++lin) {
				if((matrix[col][lin]==matrix[col+1][lin]) && (matrix[col][lin]==matrix[col][lin+1]) && (matrix[col][lin]==matrix[col+1][lin+1])) { // Comparison of each byte with the one next to it, the one under it, the one in diagonal
					penaltyPoint += 3 ;
				}
			}
		}
		
// Penalty 3 : Comparison of each byte with the beginning of each sequence. If the first byte is similar, we compare the rest. Else, we break and compare the next byte.
//				If the bytes are identical to the whole sequence, penaltyPoints+=40

		int[] sequence1 = {W, W, W, W, B, W, B, B, B, W, B};
		int[] sequence2 = {B, W, B, B, B, W, B, W, W, W, W};
		int sizeSequence1 = sequence1.length ;
		int sizeSequence2 = sequence2.length ;
		
		for (int i =0; i<sizeMatrix; ++i) {
			for (int j =0; j<sizeMatrix; ++j) {
				if (matrix[i][j]==W) { // Comparison to sequence 1 beginning with White
					for (int compteur1=0; compteur1<sizeSequence1; ++compteur1) {
						if(i+compteur1>=sizeMatrix) {break;} // Guarantee that it won't be out of bounds
						if (matrix[i+compteur1][j]!=sequence1[compteur1]) {break;} // Byte not identical to the sequence
						if (compteur1==(sizeSequence1-1)) { // Comparison of the size of the bytes similar to the size of the sequence
							penaltyPoint += 40 ;
						}
					}
					for (int compteur2=0; compteur2<sizeSequence1; ++compteur2) {
						if(j+compteur2>=sizeMatrix) {break;}
						if (matrix[i][j+compteur2]!=sequence1[compteur2]) {break;}
						if (compteur2==(sizeSequence1-1)) {
							penaltyPoint += 40 ;
						}
					}
				}
				else { // Comparison to sequence 2 beginning with Black
					for (int compteur3=0; compteur3<sizeSequence2; ++compteur3) {
						if(i+compteur3>=sizeMatrix) {break;}
						if (matrix[i+compteur3][j]!=sequence2[compteur3]) {break;}
						if (compteur3==(sizeSequence2-1)) {
							penaltyPoint += 40 ;
						}
					}
					for (int compteur4=0; compteur4<sizeSequence2; ++compteur4) {
						if(j+compteur4>=sizeMatrix) {break;}
						if (matrix[i][j+compteur4]!=sequence2[compteur4]) {break;}
						if (compteur4==(sizeSequence2-1)) {
							penaltyPoint += 40 ;
						}
					}
				}
			}
		}
		
// Penalite 4 : Counting the number of black modules and total modules. Percentage calculated. Inferior and superior percentages taken, both multiples of 5.
//				Both subtracted by 50. Absolute value taken. Both multiplied by 2. Smallest value taken and added to the penalty points (penaltyPoints+=smallestMultiple)
		
		double blackModules = 0.0 ;
		double nModules = 0.0 ;
		
		for(int col=0; col<sizeMatrix; ++col) {
			for(int lin=0; lin<sizeMatrix; ++lin) {
				++nModules ;
				if(matrix[col][lin] == B) {
					++blackModules ;
				}
			}
		}
		
		int pourcentModules = (int)((blackModules / nModules) * 100.0) ; // Calculation of the percentage of black modules
		
		int multipleModules = pourcentModules % 5 ;
		int multiplePrecedent = 0 ;
		int multipleSuivant = 0 ;
		if(multipleModules == 0) { // Inferior and superior percentages, both multiples of 5
			multiplePrecedent = pourcentModules ;
			multipleSuivant = pourcentModules + 5 ;
		}
		else {
			multiplePrecedent = pourcentModules - multipleModules ;
			multipleSuivant = pourcentModules + (5 - multipleModules) ;
		}
		
		multiplePrecedent -= 50 ;
		multipleSuivant -= 50 ;
		
		multiplePrecedent = valeurAbsolue(multiplePrecedent) ; // Absolute value taken 
		multipleSuivant = valeurAbsolue(multipleSuivant) ;
				
		if(multiplePrecedent <= multipleSuivant) { // Smallest value taken
			penaltyPoint += 2*multiplePrecedent ; // Multiplied by 2 before being added to the penalty points
		}
		else {
			penaltyPoint += 2*multipleSuivant ;
		}
		
//Penalty Total
		
		System.out.println(penaltyPoint) ;
		return penaltyPoint ;
	}
	
	
	public static int valeurAbsolue(int valeur) { // Method to calculate the absolute value of a value
		if(valeur<0) {
			valeur = (-1)*valeur ;
		}
		return valeur ;
	}
	
}