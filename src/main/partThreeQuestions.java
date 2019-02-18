package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import io.InputStreamBitSource;
import io.InsufficientBitsLeftException;

public class partThreeQuestions {
	public static void main(String args[]) throws IOException, InsufficientBitsLeftException {
		String input_file_name = "data/uncompressed.txt";
		String output_file_name = "data/recompressed.txt";
		String original_compressed_in = "data/compressed.dat";

		FileInputStream fis = new FileInputStream(input_file_name);
		FileInputStream o_fis = new FileInputStream(original_compressed_in);
		FileInputStream c_fis = new FileInputStream(output_file_name);
		PrintWriter probs = new PrintWriter("data/probabilities.txt");
		
		// Read in each symbol (i.e. byte) of input file and 
		// update appropriate count value in symbol_counts
		// Should end up with total number of symbols 
		// (i.e., length of file) as num_symbols
		
		int[] symbol_counts = new int[256];
		int num_symbols = fis.available();
		int o_num_symbols = o_fis.available() - 260;
		while(fis.available() > 0) {
			int sym = fis.read();
			symbol_counts[sym]++;
		}

		// Close input file
		fis.close();
		
		InputStreamBitSource bit_source = new InputStreamBitSource(o_fis);
		InputStreamBitSource compressed_bit_source = new InputStreamBitSource(c_fis);
		
		List<SymbolWithCodeLength> symbols_with_length = new ArrayList<SymbolWithCodeLength>();
		List<SymbolWithCodeLength> new_symbols_with_length = new ArrayList<SymbolWithCodeLength>();
		SymbolWithCodeLength pair;
		SymbolWithCodeLength new_pair;
		// Read in huffman code lengths from input file and associate them with each symbol as a 
		// SymbolWithCodeLength object and add to the list symbols_with_length.
		// Then sort they symbols.
		double o_compressed_entropy = 0;
		double new_compressed_entropy = 0;
		for (int i = 0; i < 256; i++) {
			pair = new SymbolWithCodeLength(i,bit_source.next(8));
			symbols_with_length.add(pair);
			
			new_pair = new SymbolWithCodeLength(i, compressed_bit_source.next(8));
			new_symbols_with_length.add(new_pair);
			
		}

		// Create array of symbol values
		
		double theoretical_entropy = 0;	
		for (int i=0; i<256; i++) {
			if(symbol_counts[i] != 0) {
				double p = (double) symbol_counts[i] / (double) num_symbols;
				probs.println("Ascii: " + i + ", Character: '" + (char) i + "' Probability is " + p);
				theoretical_entropy += p * (Math.log(p)/Math.log(2));
				o_compressed_entropy += p * symbols_with_length.get(i).codeLength();	
				new_compressed_entropy += p * new_symbols_with_length.get(i).codeLength();
			}
			
		}
		
		theoretical_entropy = 0 - theoretical_entropy;
		
		probs.println("Theoretical Entropy of source message in bits per symbol: " + theoretical_entropy);
		probs.println("Compressed Entropy of provided file in bits per symbol: " + o_compressed_entropy);
		probs.println("Compressed Entropy of recompressed file in bits per symbol: " 
				+ new_compressed_entropy);
		probs.close();
	}
}
