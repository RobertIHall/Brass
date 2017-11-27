package pqsort;

class TreeNode<E>
{
   private E item;
   private TreeNode<E> left_child;
   private TreeNode<E> right_child;

  public TreeNode(E item) 
  {
     this.item = item;
     left_child  = null;
     right_child = null;
  } 

  public E getItem() 
  {
    return item;
  }  

  public void setItem(E item) 
  {
    this.item  = item;
  }  

  public TreeNode<E> getLeft() 
  {
    return left_child;
  } 

  public void setLeft(TreeNode<E> left) 
  {
    left_child  = left;
  }  

  public TreeNode<E> getRight() 
  {
    return right_child;
  }  

  public void setRight(TreeNode<E> right) 
  {
    right_child  = right;
  }  
}
