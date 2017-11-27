package pqsort;

class Node<E>
{
   private E item;
   private Node<E> next;  

   public Node(E item) 
   {
      this.item = item;
      next = null;
   }

   public void setItem(E item) 
   {
      this.item = item;
   } 

   public E getItem() 
   {
      return item;
   } 

   public void setNext(Node<E> next) 
   {
      this.next = next;
   } 

   public Node<E> getNext() 
   {
      return next;
   }
} 
