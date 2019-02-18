package main;

/* SymbolWithCodeLength
 * 
 * Class that encapsulates a symbol value along with the length of the code
 * associated with that symbol. Used to build the canonical huffman tree.
 * Implements Comparable in order to sort first by code length and then by symbol value.
 */

public class SymbolWithCodeLength implements Comparable<SymbolWithCodeLength> {
	
	// Instance fields should be declared here.
	private int _value;
	private int _code_length;
	// Constructor
	public SymbolWithCodeLength(int value, int code_length) {
		_value = value;
		_code_length = code_length;
	}

	// codeLength() should return the code length associated with this symbol
	public int codeLength() {
		return _code_length;
	}

	// value() returns the symbol value of the symbol
	public int value() {
		return _value;
	}

	// compareTo implements the Comparable interface
	// First compare by code length and then by symbol value.
	public int compareTo(SymbolWithCodeLength other) {
		if (codeLength() < other.codeLength()) {
			return -1;
		}
		if (codeLength() > other.codeLength()) {
			return 1;
		}
		if (value() < other.value()) {
			return -1;
		}
		if (value() > other.value()) {
			return 1;
		}
		return 0;
	}
}
