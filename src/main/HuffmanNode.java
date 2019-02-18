package main;

import java.util.Map;

// HuffmanNode
//
// Interface implemented by InternalHuffmanNode and LeafHuffmanNode
// Used for both creating Huffman tree from frequency counts and
// creating canonical Huffman tree from code lengths.

public interface HuffmanNode extends Comparable<HuffmanNode> {

	// count() returns the frequency count associated with this
	// node. For internal nodes, this is the sum of the count
	// associated with its children. For leaf nodes this is the
	// count associated with the symbol represented by the leaf.
	// Used by the encoder when creating initial Huffman tree.
	// Not used when creating canonical tree after code lengths
	// are already known.
	int count();	
	
	// isLeaf() returns true if leaf node, false if internal node.
	boolean isLeaf();
	
	// symbol() returns symbol associated with leaf node. Not applicable for
	// internal node. Calling on internal node should throw an exception.
	int symbol();
	
	// height() returns distance between this node and farthest leaf underneath it.
	// By definition, 0 for leaf nodes.
	int height();
	
	// isFull() indicates if all paths underneath this node
	// through the tree end at leaf nodes. 
	// True by definition for leaf nodes. 
	// True for internal nodes only if both children
	// are not null and both children are also full.
	boolean isFull();
	
	// insertSymbol() attempts to insert leaf node for given
	// symbol under this node that is length distance
	// away and follows rules for canonical tree construction:
	//  * First try to go left
	//  * If not possible or fails, then try to go right
	//  * If not possible or fails, return false to indicate failure.
	//
	// Returns true if new leaf node was successfully inserted, 
	// false otherwise. Should be able to implement this recursively,
	// reducing length by 1 for each recursion until length is 1 at which
	// point new leaf node with symbol should be created and attached
	// if possible. 
	//
	// This method is only applicable for internal nodes. Calling on
	// a leaf node should be an exception.
	boolean insertSymbol(int length, int symbol);
	
	// left() and right() return the left or right child of an
	// internal node. Return value of null indicates that the
	// internal node does not yet have a left or right child.
	// Only applicable for internal nodes, calling these on a
	// leaf node should result in an exception.
	HuffmanNode left();
	HuffmanNode right();
	
	
	void makeParent(InternalHuffmanNode par);
	
	void removeLeft();
	void removeRight();


	// compareTo implements the Comparable interface for 
	// two nodes. First compares by count and then by height.
	default int compareTo(HuffmanNode other) {
		if (count() != other.count()) {
			return count() - other.count();
		} else {
			return height() - other.height();
		}
	}
}
