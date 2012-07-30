package de.master.core.graph.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * A basic graph implementation. this graph is not directed, cyclic and
 * weighted.
 * 
 * @author m.kandora
 * 
 * @param <T>
 *            the containing generic data type
 */
public class Graph<T>
{

	/**
	 * A set of all nodes in this graph
	 */
	protected List<Node<T>> nodes;

	/**
	 * A map of all weights between all nodes
	 */
	protected Map<Node<T>, Map<Node<T>, Double>> weights;

	/**
	 * A mapping of predecessors to a given node
	 */
	protected Map<Node<T>, Node<T>> predecessors;

	/**
	 * the first node in the graph.
	 */
	protected Node<T> first;

	/**
	 * this is the node pointer, where successive nodes will be inserted as
	 * children
	 */
	protected Node<T> previous;

	/**
	 * Creates a new empty graph
	 */
	public Graph()
	{
		nodes 			= new LinkedList<Node<T>>();
		weights			= new HashMap<Node<T>, Map<Node<T>, Double>>();
		predecessors 	= new HashMap<Node<T>, Node<T>>();
	}

	/**
	 * Inserts a specific node into the graph. Please note that the inserted
	 * node will be the child of an existing predecessor. if no predecessor is
	 * available the child must be the first node. all successive nodes will be
	 * appended to this node.
	 * 
	 * @param weight
	 *            the weight of the inserted child
	 * @param node
	 *            the node to be inserted
	 */
	public void insert(final double weight, final Node<T> node)
	{
		if (previous == null)
		{
			previous = node;
			first = previous;
			nodes.add(previous);
		} 
		else
		{
			
			updateChildren(node);
			
			if(!nodes.contains(node))
			{
				nodes.add(node);
			}
			predecessors.put(node, previous);
			insertWeight(previous, node, weight);
		}
	}
	
	/**
	 * inserts node into previous node and vice versa
	 * @param node
	 */
	private void updateChildren(final Node<T> node)
	{
		List<Node<T>> children = previous.getNeighbours();
		if (!children.contains(node) && !node.equals(previous))
		{
			children.add(node);
			
			List<Node<T>> other = node.getNeighbours();
			if(!other.contains(previous))
			{
				other.add(previous);
			}
			
		}
	}

	/**
	 * Inserts a specific child after the node at. Inserts an edge
	 * 
	 * @param weight
	 *            the weight for this child
	 * @param node
	 *            the node to be inserted
	 * @param at
	 *            the node after the child is to be inserted
	 */
	public void insertAt(final double weight, final Node<T> node, final Node<T> at)
	{
		previous = at;
		insert(weight, node);
	}

	/**
	 * Removes a node from graph
	 * 
	 * @param node
	 *            the node to remove
	 */
	public void remove(final Node<T> node)
	{
		if (node.equals(previous))
		{
			Node<T> tmpPre = predecessors.get(previous);
			if (tmpPre != null)
			{
				previous = tmpPre;
			}
		}
		if (node.equals(first))
		{
			if (nodes.size() == 1)
			{
				nodes.remove(first);
			} else
			{
				if (nodes.contains(first))
				{
					// find maximum weighted child
					Node<T> n = maxWeight(first.getNeighbours(), first);
					for (Node<T> child : first.getNeighbours())
					{
						if (!child.equals(n))
						{
							n.addNode(child);
							predecessors.put(child, n);
							double weight = weights.get(first).get(child);
							removeWeight(first, child);
							insertWeight(n, child, weight);
						}
					}
				} else
				{
					System.err.println("this should not happy.");
				}
			}
		}

		nodes.remove(node);
		Node<T> pre = null;
		if (predecessors.containsKey(node))
		{
			predecessors.remove(node);
		}
		if (predecessors.containsValue(node))
		{
			for (Node<T> n : predecessors.keySet())
			{
				if (predecessors.get(n) != null
						&& predecessors.get(n).equals(node))
				{
					pre = n;
				}
			}
			predecessors.remove(pre);
		}
		removeWeight(pre, node);
		
		// remove also from neighbours
		Stack<Node> buffer = new Stack<Node>();
		for(Node n : node.getNeighbours())
		{
			buffer.push(n);
		}
		while(!buffer.isEmpty()) {
			buffer.pop().removeNode(node);
		}
	}

	/**
	 * Inserts a weight between two nodes. the first node is getting checked
	 * against an existing entry map. if the entry map is not empty or null, the
	 * second parameter gets inserted with the current weight.
	 * 
	 * if both parameters have no links at all, a new map is being created and
	 * both paramters, the appropriate weights and the reference other node is
	 * being inserted into {@link Graph#weights}
	 * 
	 * 
	 * @param a
	 * @param b
	 * @param weight
	 */
	protected void insertWeight(Node<T> a, Node<T> b, double weight)
	{
		Map<Node<T>, Double> link = null;
		if (weights.get(a) != null)
		{
			link = weights.get(a);
			link.put(b, weight);
			weights.put(a, link);
		} 
		else {
			link = new HashMap<Node<T>, Double>();
			link.put(b, weight);
			weights.put(a, link);
		}
		
		if (weights.get(b) != null)
		{
			link = weights.get(b);
			link.put(a, weight);
			weights.put(b, link);
		} 
		else 
		{
			link= new HashMap<Node<T>, Double>();
			link.put(a, weight);
			weights.put(b, link);
		}

	}

	/**
	 * Removes a weight between two nodes
	 * 
	 * @param a
	 * @param b
	 */
	protected void removeWeight(final Node<T> a, final Node<T> b)
	{
		try
		{
			if (a != null)
				weights.get(a).remove(b);
			if (b != null)
				weights.get(b).remove(a);
		} catch (Exception e)
		{
			// no need to throw an exception, since the map does not
			// permits null values
		}
	}

	/**
	 * Returns the child of parent with a maximum weighted edge
	 * 
	 * @param children
	 *            the children to look for the maximum weighted child
	 * @param parent
	 *            the parent to create an edge with one selected child
	 * @return the maximum weighted child of parent
	 */
	protected Node<T> maxWeight(final List<Node<T>> children,
			final Node<T> parent)
	{
		Node<T> max = null;
		double v = 0;
		for (Node<T> node : children)
		{
			if (v < weight(parent, node))
			{
				v = weight(parent, node);
				max = node;
			}
		}
		return max;
	}

	/**
	 * Returns the associated weight between two nodes
	 * 
	 * @param a
	 *            the first node
	 * @param b
	 *            the second node
	 * @return the weight weight between a and b
	 */
	public Double weight(final Node<T> a, Node<T> b)
	{
		Double v = 0.0;
		return weights.get(a) != null ? weights.get(a).get(b) : 0;
	}

	/**
	 * Returns the number of all nodes inside the graph
	 * 
	 * @return the number of nodes
	 */
	public int getNodeCount()
	{
		return nodes.size();
	}

	/**
	 * Returns all nodes from graph
	 * 
	 * @return a set with all nodes insode this graph
	 */
	public List<Node<T>> getNodes()
	{
		return nodes;
	}
	
////<<<<<<< .mine
//	public Node<T> getNodeByID(int id)
//	{
//		for (Node node : nodes)
//		{
//			if (node.getId() == id)
//				return node;
//		}
//		
//		return null;
//	}
//	
//	protected List<Node<T>> getNeighbours(Node<T> node) {
//		List<Node<T>> list = new ArrayList<>();
//		if(weights.get(node) != null) {
//			list.addAll(weights.get(node).keySet());
//		}
//		return list;
//	}
////=======
////d//	protected List<Node<T>> getNeighbours(Node<T> node) {
//////		List<Node<T>> list = new ArrayList<>();
//////		if(weights.get(node) != null) {
//////			list.addAll(weights.get(node).keySet());
//////		}
//////		return list;
//////	}
////>>>>>>> .r583

	/**
	 * returns the first node in this graph
	 * 
	 * @return
	 */
	public Node<T> getFirst()
	{
		return first;
	}

	/**
	 * Returns the previous node, that is the current node pointer
	 * 
	 * @return the previous node
	 */
	public Node<T> getPrevious()
	{
		return previous;
	}

	/**
	 * Sets the previous node, that is any new inserted nodes will be set as her
	 * child
	 * 
	 * @param node
	 *            the node to set as new predecessor
	 */
	public void setPrevious(final Node<T> node)
	{
		if (!nodes.contains(node))
			throw new RuntimeException(
					"Node "
							+ node
							+ "cannot be set as previous, since its not part of the graph!");
		this.previous = node;
	}

	/**
	 * Clears this graph. eg. removes all children, weights etc..
	 */
	public void clear()
	{
		nodes.clear();
		predecessors.clear();
		weights.clear();
		first = null;
		previous = null;
	}

	/**
	 * Returns true if this graph contains no nodes
	 * 
	 */
	public boolean isEmpty()
	{
		return getNodeCount() == 0;
	}

	/**
	 * Returns the predecessor (aka the parent) of Node n.
	 * 
	 * @param node
	 *            the node to find the predeccessor
	 * @return the predeccessor
	 */
	public Node<T> getPredecessor(final Node<T> node)
	{
		return predecessors.get(node);
	}
}
