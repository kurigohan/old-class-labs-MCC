
public class TreeBag 
{
	private BTNode root;
	
	
	public void add(Comparable x)
	{
		root = addNode(x, root);
	}
	
	private BTNode addNode(Comparable x, BTNode p)
	{
		if(p == null)
			//create the node - base case
			p = new BTNode(x, null, null);
		else if(x.compareTo(p.getData()) < 0)
			p.setLeft(addNode(x, p.getLeft()));
		else if(x.compareTo(p.getData()) > 0)
			p.setRight(addNode(x, p.getRight()));
		else //keys are equal - replace with new data
			p.setData(x);
		return p;
	}
	
	public boolean remove(Comparable target)
	{
        boolean success = false;
        
        if (retrieve (target) != null) {
            root = removeNode(target, root);
            success = true;
        }
        
        return success;
	}
	
	
	private BTNode removeNode(Comparable target, BTNode<Employee> p )
	{
		if(p == null) //tree is empty, target not found
			return p;
		if(target.compareTo(p.getData()) < 0)
			p.setLeft(removeNode(target, p.getLeft()));
		else if(target.compareTo(p.getData()) > 0)
			p.setRight(removeNode(target, p.getRight()));
		//found node to be removed
		
		//if node has 2 children
		else if((p.getLeft() != null) && (p.getRight() != null))
		{
			//get data in the right most node in the left subtree
			p.setData(p.getRight().getLeftmostData());
			//delete right most data in the left subtree
			p.setRight(p.getRight().removeLeftmost());
		}
		
		else if(p.getLeft() == null) //only right child
			p = p.getRight();
		else //only left child
			p = p.getLeft();
		
		return p;
	}
	
	
	public BTNode retrieve(Comparable element)
	{
		BTNode p = retrieveNode(element, root);
		
		if(p != null)
			return p;
		else
			return null;
		
	}
	
	private BTNode retrieveNode(Comparable target, BTNode<Employee> p)
	{
		if(p != null)
		{
			if(target.compareTo(p.getData()) == 0)
				return p;
			else if(target.compareTo(p.getData()) < 0)
				return retrieveNode(target, p.getLeft());
			else if(target.compareTo(p.getData()) > 0)
				return retrieveNode(target, p.getRight());
		}
		return p;
	}
	
	
	public void display()
	{
		traverse(root);
	}
	
	private void traverse(BTNode p)
	{
		if(p != null) //inorder
		{
			traverse(p.getLeft());
			System.out.println(p.getData());
			traverse(p.getRight());
			
		}
	}
	
	public String toString ()
    {
        return traverseStr (root);
    }
    
    private String traverseStr(BTNode p)
	{
		if(p != null) //inorder
		{
            if (p.getLeft() == null && p.getRight() != null)
                return p.getData ().toString () + "\r\n" + traverseStr(p.getRight());
            else if (p.getLeft() != null && p.getRight() == null)
                return p.getData ().toString () + "\r\n" + traverseStr(p.getLeft());
            else if (p.getLeft() != null && p.getRight() != null)
                return p.getData ().toString () + "\r\n" + traverseStr(p.getLeft()) + "\r\n" + traverseStr(p.getRight());
            else
                return p.getData ().toString ();
		}
        
        return "";
	}
}
