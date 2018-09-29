package pqsort;

/**
   The required methods for a priority queue.
*/
public interface PQInterface<E>
{
	/**
	   Inserts an item into the priority queue.
	   Preconditions: item is not null
	   Throws: PQException if the insertion cannot be accommodated
	*/
	public void pqInsert(E item) throws PQException;
	
	/**
	   Removes the smallest item from the priority queue.
	   Postconditions: there are items in the priority queue
	   Throws: PQException if there are no items remaining
	*/
	public E pqRemove() throws PQException;
	
	/**
	   Indicates whether there are any items in the priority queue.
	*/
	public boolean pqIsEmpty();
}
