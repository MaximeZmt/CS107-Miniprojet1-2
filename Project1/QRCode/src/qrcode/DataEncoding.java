package qrcode;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import reedsolomon.ErrorCorrectionEncoding;

public final class DataEncoding {

	/**
	 * @param input
	 * @param version
	 * @return
	 */
	public static boolean[] byteModeEncoding(String input, int version) {
		int maxLength = QRCodeInfos.getMaxInputLength(version); // For ISO conversion
		int codeWordLength = QRCodeInfos.getCodeWordsLength(version);
		int corrLength = QRCodeInfos.getECCLength(version);
		System.out.println("ISOMAX "+maxLength+"- codeWordMax: "+codeWordLength);
		int[] encodedStr = encodeString(input,maxLength);
		int[] enrichEncodedStr = addInformations(encodedStr);
		int[] filledEncodedStr = fillSequence(enrichEncodedStr, codeWordLength);
		int[] correctionGenStr = addErrorCorrection(filledEncodedStr, corrLength);
		boolean[] finalEncoded = bytesToBinaryArray(correctionGenStr);
		System.out.println(Arrays.toString(finalEncoded));
		return finalEncoded;
	}

	/**
	 * @param input
	 *            The string to convert to ISO-8859-1
	 * @param maxLength
	 *          The maximal number of bytes to encode (will depend on the version of the QR code)
	 * @return A array that represents the input in ISO-8859-1. The output is
	 *         truncated to fit the version capacity
	 */
	/*
	 * This methode takes an input in String and return an int array of unsigned byte.
	 */
	public static int[] encodeString(String input, int maxLength) {
		byte[] tabByte = input.getBytes(StandardCharsets.ISO_8859_1);
		int tabByteL = tabByte.length;
		int[] converted;
		if (tabByteL>=maxLength) {
			converted = new int[maxLength]; 
		}else {
			converted = new int[tabByteL]; 
		}
		int convertedL = converted.length;
		for (int i = 0;i<convertedL;++i ) {
			if (i<tabByteL) {
				converted[i]=tabByte[i]& 0xFF;
			}
		}
		return converted;
	}

	/**
	 * Add the 12 bits information data and concatenate the bytes to it
	 *
	 * @param inputBytes
	 *            the data byte sequence
	 * @return The input bytes with an header giving the type and size of the data
	 */
	
	
	public static int[] addInformations(int[] inputBytes) {
		int inputBytesL = inputBytes.length;
		int[] reply = new int[inputBytesL+2];
		
		if (inputBytes.length != 0) {
			int octet0 = (0b0100_0000) | (inputBytesL >>4)&0xFF; //create fix byte 0 (indicator + first part of size)
			int octet1 = ((inputBytesL<<4)|(inputBytes[0]>>4))&0xFF; //create fix byte (second part of size + first part of input)
			int octetLast= ((inputBytes[inputBytesL-1]<<4)|(0b0000))&0xFF; // create fix byte (last part of input + indicator)
			
			for (int i=0; i<(inputBytesL+2);++i) { //put all the bytes together
				if (i==0) {
					reply[i]=octet0;
				}else if (i==1) {
					reply[i]=octet1;
				}else if (i==(inputBytesL+1)) {
					reply[i]=octetLast;
				}
				else {
					reply[i]= ((inputBytes[i-2]<<4)|(inputBytes[i-1]>>4))&0xFF;
				}
			}
		}else {
			reply[0]=0b0100_0000;
			reply[1]=0b0000_0000;
		}
		return reply; //return an array with 2 byte in addition compared to the input.
	}
	

	/**
	 * Add padding bytes to the data until the size of the given array matches the
	 * finalLength
	 *
	 * @param encodedData
	 *            the initial sequence of bytes
	 * @param finalLength
	 *            the minimum length of the returned array
	 * @return an array of length max(finalLength,encodedData.length) padded with
	 *         bytes 236,17
	 */
	public static int[] fillSequence(int[] encodedData, int finalLength) { //fill empty box of the array

			int[] filledSeq = new int[finalLength];
			int counter = 0;
			for (int i = 0; i<finalLength; ++i) {
				if (encodedData.length>i) {
					filledSeq[i]=encodedData[i];
				}
				else {
					++counter;
					if (counter%2==0) {
						filledSeq[i]=17;
					}
					else {
						filledSeq[i]=236;
					}
				}
			}
			return filledSeq;
	
	}

	/**
	 * Add the error correction to the encodedData
	 *
	 * @param encodedData
	 *            The byte array representing the data encoded
	 * @param eccLength
	 *            the version of the QR code
	 * @return the original data concatenated with the error correction
	 */
	public static int[] addErrorCorrection(int[] encodedData, int eccLength) { //generate and add the code that manage Error in the code
		int[] reply = ErrorCorrectionEncoding.encode(encodedData, eccLength);
		int replyL = reply.length;
		int encodedDataL = encodedData.length;
		int[] toBeReturn = new int[replyL+encodedDataL];
		for(int i = 0;i<(replyL+encodedDataL);++i) {
			if(i<encodedDataL) {
				toBeReturn[i]=encodedData[i];
			}else {
				toBeReturn[i]=reply[i-encodedDataL];
			}
		}
		return toBeReturn;
	}

	/**
	 * Encode the byte array into a binary array represented with boolean using the
	 * most significant bit first.
	 *
	 * @param data
	 *            an array of bytes
	 * @return a boolean array representing the data in binary
	 */
	public static boolean[] bytesToBinaryArray(int[] data) { //transform all byte array in an array of bit represented by true (1) and false (0).
		int dataL = data.length;
		boolean[] array = new boolean[(dataL*8)];
		for (int i = 0; i<dataL;++i) {
				int s;
				int counter=0;
				while (data[i]>=1) {
					s =(data[i]%2);
					data[i] = (data[i]-(data[i]%2))/2;
					++counter;
					if (s==1) {
						array[(i*8)+(8-counter)]=true;
					}
					else {
						array[(i*8)+(8-counter)]=false;
					}
				}
			
			
		}
		return array;
	}

}
