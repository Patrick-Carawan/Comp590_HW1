package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.ByteBuffer;

import io.OutputStreamBitSink;

public class HuffEncode {

	public static void main(String[] args) throws IOException {
		String input_file_name = "data/uncompressed.txt";
		String output_file_name = "data/recompressed.txt";

		FileInputStream fis = new FileInputStream(input_file_name);
		PrintWriter probs = new PrintWriter("data/probabilities.txt");
		
		// Read in each symbol (i.e. byte) of input file and 
		// update appropriate count value in symbol_counts
		// Should end up with total number of symbols 
		// (i.e., length of file) as num_symbols
		
		int[] symbol_counts = new int[256];
		int num_symbols = fis.available();
		while(fis.available() > 0) {
			int sym = fis.read();
			symbol_counts[sym]++;
		}

		// Close input file
		fis.close();

		// Create array of symbol values
		
		int[] symbols = new int[256];
		double entropy = 0;
		for (int i=0; i<256; i++) {
			symbols[i] = i;
			if(symbol_counts[i] != 0) {
				double p = (double) symbol_counts[i] / (double) num_symbols;
				probs.println("Ascii: " + i + ", Character: " + (char) i + ". Probability is " + p);
				entropy += p * (Math.log(p)/Math.log(2));
			}
			
		}
		
		probs.println("Entropy in bits per symbol: " + entropy);
		probs.close();
		
		// Create encoder using symbols and their associated counts from file.
		
		HuffmanEncoder encoder = new HuffmanEncoder(symbols, symbol_counts);
		
		// Open output stream.
		FileOutputStream fos = new FileOutputStream(output_file_name);
		OutputStreamBitSink bit_sink = new OutputStreamBitSink(fos);

		// Write out code lengths for each symbol as 8 bit value to output file.
		for(int i = 0; i < 256; i++) {
			bit_sink.write(encoder.getCode(i).length(), 8);
		}

		// Write out total number of symbols as 32 bit value.

		bit_sink.write(num_symbols, 32);
		// Reopen input file.
		fis = new FileInputStream(input_file_name);

		// Go through input file, read each symbol (i.e. byte),
		// look up code using encoder.getCode() and write code
        // out to output file.
		
		for(int i = 0; i < num_symbols; i++) {
			int uncompressed_char = fis.read();
			//Long x = Long.parseLong(encoder.getCode(uncompressed_char));
			//byte[] compressed_char = ByteBuffer.allocate((int) Math.ceil(encoder.getCode(uncompressed_char).length()/8)).putLong(x).array();
			//fos.write(compressed_char);
			bit_sink.write(encoder.getCode(uncompressed_char));
		}

		// Pad output to next word.
		bit_sink.padToWord();

		// Close files.
		fis.close();
		fos.close();
	}
}

