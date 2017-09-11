package it.unibz.inf.ontop.utils;

import java.util.*;


public class TreeNode<T> {
   private T data;
   private TreeNode<T> parent;
   private List<TreeNode<T>> children;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public TreeNode(T data){
       this.data = data;
       this.children = new ArrayList<>();
       parent = null;

   }

   public List<TreeNode<T>> getChildren() {
      return children;
   }

   public void addChild(T child) {
      TreeNode<T> newNode = new TreeNode<T>(child);
      newNode.setParent(this);
      children.add(newNode);
   }

   public void addChild(TreeNode<T> child) {
      child.parent = this;
      children.add(child);
   }

   public List<TreeNode<T>> preOrderTraversal(){
        List<TreeNode<T>> visited = new ArrayList<>(Arrays.asList(this));
        this.getChildren().forEach( t -> visited.addAll(t.preOrderTraversal()));
        return visited;

   }

   public List<TreeNode<T>> parentPath(){
      if(this.parent == null){
         return new ArrayList<>(Arrays.asList(this));
      }else {
         List<TreeNode<T>> newList = new ArrayList<>();
         newList.add(this);
         newList.addAll(parent.parentPath());
         return newList;
      }
   }

    /**
     * Out of two paths (from the node to the root) of the current node and a given node this tracks the same nodes
     * in the reverse order. The last identical node represents lowest common ancestor of two tree nodes
     */
   public Optional<T> lowestCommonAncestor(TreeNode<T> treeNode){
       List<TreeNode<T>> aParentPath = this.parentPath();
       List<TreeNode<T>> bParentPath = treeNode.parentPath();
       T lowestCommonAncestor = null;
       if (!aParentPath.isEmpty() && !bParentPath.isEmpty())
       {
           ListIterator<TreeNode<T>> aIterator = aParentPath.listIterator(aParentPath.size());
           ListIterator<TreeNode<T>> bIterator = bParentPath.listIterator(bParentPath.size());
           while(aIterator.hasPrevious() & bIterator.hasPrevious()){
               T aPathNode = aIterator.previous().getData();
               T bPathNode = bIterator.previous().getData();
               if (aPathNode.equals(bPathNode)) lowestCommonAncestor = aPathNode;
               else break;
           }
       }
       return Optional.ofNullable(lowestCommonAncestor);
   }

}
