package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import io.OutputStreamBitSink;

public class HuffmanEncoder {
		
	private Map<Integer, String> _code_map;
	private int num_symbols = 0;
	
	public HuffmanEncoder(int[] symbols, int[] symbol_counts) {
		assert symbols.length == symbol_counts.length;
		for(int elt : symbol_counts) {
			num_symbols += elt;
		}
		// Given symbols and their associated counts, create initial
		// Huffman tree. Use that tree to get code lengths associated
		// with each symbol. Create canonical tree using code lengths.
		// Use canonical tree to form codes as strings of 0 and 1
		// characters that are inserted into _code_map.

		// Start with an empty list of nodes
		
		List<HuffmanNode> node_list = new ArrayList<HuffmanNode>();
		
		// Create a leaf node for each symbol, encapsulating the
		// frequency count information into each leaf.
		for(int i = 0; i < symbols.length; i++) {
			node_list.add(new LeafHuffmanNode(symbol_counts[i], i));
		}

		// Sort the leaf nodes
		node_list.sort(null);
		//Collections.sort(node_list);

		// While you still have more than one node in your list...
		while(node_list.size() > 1) {
			// Remove the two nodes associated with the smallest counts
			InternalHuffmanNode root = 
					new InternalHuffmanNode(node_list.get(0).count() + node_list.get(1).count());
			root.makeLeft(node_list.get(1));
			root.left().makeParent(root);
			root.makeRight(node_list.get(0));
			root.right().makeParent(root);
			node_list.remove(1);
			node_list.remove(0);
			// Create a new internal node with those two nodes as children.
			
			// Add the new internal node back into the list
			node_list.add(root);
			// Resort
			node_list.sort(null);
		}

		// Create a temporary empty mapping between symbol values and their code strings
		Map<Integer, String> cmap = new HashMap<Integer, String>();

		// Start at root and walk down to each leaf, forming code string along the
		// way (0 means left, 1 means right). Insert mapping between symbol value and
		// code string into cmap when each leaf is reached.
		
		for(int i = 0; i < symbols.length; i++) {
			String code = "";
			HuffmanNode tracker = node_list.get(0);
			HuffmanNode prev = tracker;
			while(!tracker.isLeaf()) {
				if(tracker.left() != null) {
					code += '0';
					prev = tracker;
					tracker = tracker.left();
				} else if(tracker.right() != null) {
					code += '1';
					prev = tracker;
					tracker = tracker.right();
				} else {
					if(prev.left() == tracker) {
						prev.removeLeft();
					} else {
						prev.removeRight();
					}
					tracker = node_list.get(0);
					prev = tracker;
					code = "";
				}
			}
			cmap.put(tracker.symbol(), code);
			if(prev.left() == tracker) {
				prev.removeLeft();
			} else {
				prev.removeRight();
			}
		}
		// Create empty list of SymbolWithCodeLength objects
		List<SymbolWithCodeLength> sym_with_length = new ArrayList<SymbolWithCodeLength>();
		

		// For each symbol value, find code string in cmap and create new SymbolWithCodeLength
		// object as appropriate (i.e., using the length of the code string you found in cmap).
		for(int i = 0; i < symbols.length; i++) {
			sym_with_length.add(new SymbolWithCodeLength(i, cmap.get(i).length()));
		}
		// Sort sym_with_lenght
		Collections.sort(sym_with_length);

		// Now construct the canonical tree as you did in HuffmanDecodeTree constructor
		
		InternalHuffmanNode canonical_root = new InternalHuffmanNode();
		
		for(SymbolWithCodeLength s : sym_with_length) {
			canonical_root.insertSymbol(s.codeLength(), s.value());
		}

		// If all went well, tree should be full.
		assert canonical_root.isFull();
		
		// Create code map that encoder will use for encoding
		
		_code_map = new HashMap<Integer, String>();
		
		// Walk down canonical tree forming code strings as you did before and
		// insert into map.
		for(int i = 0; i < symbols.length; i++) {
			HuffmanNode tracker = canonical_root;
			HuffmanNode prev = tracker;
			String code = "";
			while(!tracker.isLeaf()) {
				if(tracker.left() != null) {
					code += '0';
					prev = tracker;
					tracker = tracker.left();
				} else if(tracker.right() != null) {
					code += '1';
					prev = tracker;
					tracker = tracker.right();
				} else {
					if(prev.left() == tracker) {
						prev.removeLeft();
					} else {
						prev.removeRight();
					}
					tracker = canonical_root;
					prev = tracker;
					code = "";
				}
			}
			_code_map.put(tracker.symbol(), code);
			if(prev.left() == tracker) {
				prev.removeLeft();
			} else {
				prev.removeRight();
			}
			if(prev.left() == null && prev.right() == null) {
				
			}
		}
	}

	public String getCode(int symbol) {
		return _code_map.get(symbol);
	}

	public void encode(int symbol, OutputStreamBitSink bit_sink) throws IOException {
		bit_sink.write(_code_map.get(symbol));
	}

}
