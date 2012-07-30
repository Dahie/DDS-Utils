package de.master.core.graph.base;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a Node in a graph. The edges are weighted and stored
 * in a hashmap.
 * 
 * @author mkandora
 *
 * @param <T> the containing Data
 */
public class Node<T>  {// extends Comparable<T>> implements Comparable<Node<T>>{

	private T data;
	private List<Node<T>> edges;
	private static int currentID = 0;
	private int id;
	
	
	/**
	 * Constructs a new Node with no data
	 */
	public Node() {
		this(null);
	}
	
	/**
	 * Constructs a node with data
	 * @param data
	 */
	public Node(final T data)
	{
		this.data = data;
		this.edges = new LinkedList<Node<T>>();
		this.id = ++currentID;
	}
	
	/**
	 * Inserts a node into the set of children
	 * @param node
	 */
	protected void addNode(final Node<T> node) {
		this.edges.add((Node<T>) node);
	}
	
	/**
	 * Removes a node from set of children
	 * @param node
	 */
	protected void removeNode(final Node<T> node) {
		this.edges.remove(node);
	}
	
	/**
	 * returns the children of this node
	 * 
	 * @return
	 * 
	 */
	public final List<Node<T>> getNeighbours() {
		return this.edges;
	}
	
	/**
	 * Returns the number of child nodes
	 * @return
	 */
	public final int getNeighboursCount() {
		return this.edges.size();
	}
	
	/**
	 * Returns true if this node has no children
	 * @return
	 */
	public boolean isLeaf() {
		return this.edges.isEmpty();  
	}


	/**
	 * returns the associated data for this node object
	 * @return
	 */
	public T getData() {
		return data;
	}

	/**
	 * Sets data for this node
	 * 
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}
	
		
	public int getId() {
		return id;
	}
	
	public String toString() {
		return  "Node [data = " + getData() + "]";
	}


//	@Override
//	public int compareTo(Node<T> o)
//	{
//		if(this.data.compareTo(o.data) > 0) return 1;
//		if(this.data.compareTo(data) < 0) return -1;
//		return 0;
//	}
	
	
}
