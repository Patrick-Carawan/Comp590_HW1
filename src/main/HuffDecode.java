package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.InputStreamBitSource;
import io.InsufficientBitsLeftException;

public class HuffDecode {

	public static void main(String[] args) throws InsufficientBitsLeftException, IOException {
		String input_file_name = "data/compressed.dat";
		String output_file_name = "data/uncompressed.txt";

		
		FileInputStream fis = new FileInputStream(input_file_name);

		InputStreamBitSource bit_source = new InputStreamBitSource(fis);

		List<SymbolWithCodeLength> symbols_with_length = new ArrayList<SymbolWithCodeLength>();
		SymbolWithCodeLength pair;
		// Read in huffman code lengths from input file and associate them with each symbol as a 
		// SymbolWithCodeLength object and add to the list symbols_with_length.
		// Then sort they symbols.
		for (int i = 0; i < 256; i++) {
			pair = new SymbolWithCodeLength(i,bit_source.next(8));
			symbols_with_length.add(pair);
			
		}
		
		Collections.sort(symbols_with_length);
		
		// Now construct the canonical huffman tree

		HuffmanDecodeTree huff_tree = new HuffmanDecodeTree(symbols_with_length);

		int num_symbols = 0;

		// Read in the next 32 bits from the input file  the number of symbols

		num_symbols = bit_source.next(32);
		
		try {

			// Open up output file.
			
			FileOutputStream fos = new FileOutputStream(output_file_name);
			byte[] text = new byte[num_symbols];
			for (int i=0; i<num_symbols; i++) {
				// Decode next symbol using huff_tree and write out to file.
				Integer temp = huff_tree.decode(bit_source);
				text[i] = temp.byteValue();
			}
			
			fos.write(text);

			// Flush output and close files.
			
			fos.flush();
			fos.close();
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}


