package pqsort;

import util.QueueLinked;

class BinaryTreeIterator<T>
{
   private TreeNode<T> root;  
   private QueueLinked<T> items;       

   public BinaryTreeIterator(TreeNode<T> root) 
   {
      this.root = root;
      items = new QueueLinked<T>();
      setInorder();
   } 

   public boolean hasNext() 
   {
      return !items.isEmpty();
   }  

   public T next()
   {
	   return items.dequeue();
   } 

   public void setPreorder() 
   {
      items.dequeueAll();
      preorder(root);
   } 

   public void setInorder() 
   {
      items.dequeueAll();
      inorder(root);
   }  

   public void setPostorder() 
   {
      items.dequeueAll();
      postorder(root);
   }   

   private void preorder(TreeNode<T> tNode) 
   {
      if (tNode != null) 
      {  
         items.enqueue(tNode.getItem());
         preorder(tNode.getLeft());
         preorder(tNode.getRight());
      } 
   } 

   private void inorder(TreeNode<T> tNode) 
   {
      if (tNode != null) 
      {  
         inorder(tNode.getLeft());
         items.enqueue(tNode.getItem());
         inorder(tNode.getRight());
      }
   }  

   private void postorder(TreeNode<T> tNode) 
   {
      if (tNode != null) 
      {  
         postorder(tNode.getLeft());
         postorder(tNode.getRight());
         items.enqueue(tNode.getItem());
      } 
   } 
}  
