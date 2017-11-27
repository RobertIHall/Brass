package pqsort;

import java.util.Comparator;

class SortedListLinked<E>
{
   private Node<E> head;  
   private Comparator<E> comp;
   private int size;

   public SortedListLinked(Comparator<E> comp) 
   {
      removeAll();
	  this.comp = comp;
   } 

   public void removeAll() 
   {
      head = null;
      size = 0;
   }

   public boolean isEmpty() 
   {
      return (size == 0);
   }  

   public int size() 
   {
      return size;
   }  

   public E remove()
   {  
	  if (size == 0)
      {
         return null;
      }

      E temp = head.getItem();
      head = head.getNext();
      size--;
      return temp;
   }

   private Node<E> locateNodeAdd(E item) 
   {
      //DO THIS
      //find the insertion location (remember FIFO for duplicates)
	  Node<E> curr = head;
      Node<E> prev = null;

      //duplicates placed later in the list (FIFO for duplicates)
      while (curr != null && comp.compare(item, curr.getItem()) >= 0) 
      {
         prev = curr;            
         curr = curr.getNext();
      }
	  
      return prev;
	  
   }

   public void add(E item)
   {
      Node<E> prev = locateNodeAdd(item);

      if (prev == null)
      {
         //insert the new node containing the new item at the beginning of the list
         Node<E> node = new Node<E>(item);
         node.setNext(head);
         head = node;
      } 
      else 
      {
         //insert the new node containing the new item after the node that prev references
         Node<E> node = new Node<E>(item);
         node.setNext(prev.getNext());
         prev.setNext(node);
      } 

      size++;
   }  

   public ListLinkedIterator<E> iterator()
   {
      return new ListLinkedIterator<E>(head);
   }
} 