/******************************************************************************
* This class is a homework assignment;
* A DoubleLinkedSeq</CODE> is a collection of double</CODE> numbers.
* The sequence can have a special "current element," which is specified and 
* accessed through four methods that are not available in the sequence class 
* (start, getCurrent, advance and isCurrent).
*
* <dl><dt><b>Limitations:</b>
*   Beyond Int.MAX_VALUE</CODE> elements, the size</CODE> method
*   does not work.
*
* <dt><b>Note:</b><dd>
*   This file contains only blank implementations ("stubs")
*   because this is a Programming Project for my students.
*
* <dt><b>Outline of Java Source Code for this class:</b><dd>
*   <A HREF="../../../../edu/colorado/collections/DoubleLinkedSeq.java">
*   http://www.cs.colorado.edu/~main/edu/colorado/collections/DoubleLinkedSeq.java
*   </A>
*   </dl>
*
* @version
*   Jan 24, 1999
******************************************************************************/
public class DoubleLinkedSeq implements Cloneable {
    Node head;
    Node tail;
    Node cursor;
    
    int manyNodes;

    /**
    * Initialize an empty sequence.
    * @param - none
    * <dt><b>Postcondition:</b><dd>
    *   This sequence is empty.
    **/   
    public DoubleLinkedSeq () {
        head = null;
        tail = null;
        cursor = null;
        manyNodes = 0;
    }


    /**
    * Add a new element to this sequence, after the current element. 
    * @param element</CODE>
    *   the new element that is being added
    * <dt><b>Postcondition:</b><dd>
    *   A new copy of the element has been added to this sequence. If there was
    *   a current element, then the new element is placed after the current
    *   element. If there was no current element, then the new element is placed
    *   at the end of the sequence. In all cases, the new element becomes the
    *   new current element of this sequence. 
    * @exception OutOfMemoryError
    *   Indicates insufficient memory for a new node.
    **/
    public void addAfter (double element) {
        Node n = new Node (element, null);
        
        if (cursor != null) {
            n.setLink (cursor.getLink () );
            cursor.setLink (n);
            
            if (n.getLink () == null)
                tail = n;
        }
        else {
            if (manyNodes == 0) {
                head = n;
                tail = n;
            }
            else {
                tail.setLink (n);
                tail = n;
            }
        }
        
        cursor = n;
        manyNodes++;
    }


    /**
    * Add a new element to this sequence, before the current element. 
    * @param element</CODE>
    *   the new element that is being added
    * <dt><b>Postcondition:</b><dd>
    *   A new copy of the element has been added to this sequence. If there was
    *   a current element, then the new element is placed before the current
    *   element. If there was no current element, then the new element is placed
    *   at the start of the sequence. In all cases, the new element becomes the
    *   new current element of this sequence. 
    * @exception OutOfMemoryError
    *   Indicates insufficient memory for a new node.
    **/
    public void addBefore (double element) {
        Node n = new Node (element, null);
        
        if (cursor != null) {
            n.setData (cursor.getData () );
            cursor.setData (element);
            
            n.setLink (cursor.getLink () );
            cursor.setLink (n);
        }
        else {
            if (manyNodes == 0) {
                head = n;
                tail = n;
            }
            else {
                n.setLink (head);
                head = n;
            }
            
            cursor = n;
        }
        
        manyNodes++;
    }


    /**
    * Place the contents of another sequence at the end of this sequence.
    * @param addend</CODE>
    *   a sequence whose contents will be placed at the end of this sequence
    * <dt><b>Precondition:</b><dd>
    *   The parameter, addend</CODE>, is not null. 
    * <dt><b>Postcondition:</b><dd>
    *   The elements from addend</CODE> have been placed at the end of 
    *   this sequence. The current element of this sequence remains where it 
    *   was, and the addend</CODE> is also unchanged.
    * @exception NullPointerException
    *   Indicates that addend</CODE> is null. 
    * @exception OutOfMemoryError
    *   Indicates insufficient memory to increase the size of this sequence.
    **/
    public void addAll (DoubleLinkedSeq addend) {
        if (addend == null)
            throw new NullPointerException ("Addend cannot be null.");
        
        if (addend != null) {
            DoubleLinkedSeq newAddend = (DoubleLinkedSeq)addend.clone ();
            
            if (tail != null) {
                tail.setLink (newAddend.head);
                manyNodes += newAddend.size ();
            }
            else {
                head = newAddend.head;
                tail = newAddend.tail;
            }
        }
        else
            throw new NullPointerException ("The sequence passed is null.");
    }   


    /**
    * Move forward, so that the current element is now the next element in
    * this sequence.
    * @param - none
    * <dt><b>Precondition:</b><dd>
    *   isCurrent()</CODE> returns true. 
    * <dt><b>Postcondition:</b><dd>
    *   If the current element was already the end element of this sequence 
    *   (with nothing after it), then there is no longer any current element. 
    *   Otherwise, the new element is the element immediately after the 
    *   original current element.
    * @exception IllegalStateException
    *   Indicates that there is no current element, so 
    *   advance</CODE> may not be called.
    **/
    public void advance () {
        if (cursor != null)
            cursor = cursor.getLink ();
        else
            throw new IllegalStateException ("There is no current element.");
    }


    /**
    * Generate a copy of this sequence.
    * @param - none
    * @return
    *   The return value is a copy of this sequence. Subsequent changes to the
    *   copy will not affect the original, nor vice versa. Note that the return
    *   value must be type cast to a DoubleLinkedSeq</CODE> before it can be used.
    * @exception OutOfMemoryError
    *   Indicates insufficient memory for creating the clone.
    **/ 
    public Object clone () {
        DoubleLinkedSeq newSeq = null;
        
        try {
            newSeq = (DoubleLinkedSeq)super.clone ();
            newSeq.head = null;
            newSeq.tail = null;
            newSeq.cursor = null;
            newSeq.manyNodes = 0;
            
            Node copy = head;
            
            while (copy != null) {
                newSeq.addAfter (copy.getData () );
                copy = copy.getLink ();
            }
        }
        catch (CloneNotSupportedException e) {
            System.out.println (e);
        }
        
        return newSeq;
    }


    /**
    * Create a new sequence that contains all the elements from one sequence
    * followed by another.
    * @param s1</CODE>
    *   the first of two sequences
    * @param s2</CODE>
    *   the second of two sequences
    * <dt><b>Precondition:</b><dd>
    *   Neither s1 nor s2 is null.
    * @return
    *   a new sequence that has the elements of s1</CODE> followed by the
    *   elements of s2</CODE> (with no current element)
    * @exception NullPointerException.
    *   Indicates that one of the arguments is null.
    * @exception OutOfMemoryError
    *   Indicates insufficient memory for the new sequence.
    **/   
    public static DoubleLinkedSeq catenation (DoubleLinkedSeq s1, DoubleLinkedSeq s2) {
        if (s1 == null || s2 == null)
            throw new NullPointerException ("The arguments cannot be null.");
        
        DoubleLinkedSeq newS1 = (DoubleLinkedSeq)s1.clone ();
        newS1.addAll (s2);
        
        return newS1;
    }


    /**
    * Accessor method to get the current element of this sequence. 
    * @param - none
    * <dt><b>Precondition:</b><dd>
    *   isCurrent()</CODE> returns true.
    * @return
    *   the current capacity of this sequence
    * @exception IllegalStateException
    *   Indicates that there is no current element, so 
    *   getCurrent</CODE> may not be called.
    **/
    public double getCurrent () {
        if (cursor != null)
            return cursor.getData ();
        else
            throw new IllegalStateException ("There is no current element.");
    }

    
    /**
    * Accessor method to determine whether this sequence has a specified 
    * current element that can be retrieved with the 
    * getCurrent</CODE> method. 
    * @param - none
    * @return
    *   true (there is a current element) or false (there is no current element at the moment)
    **/
    public boolean isCurrent () {
        return (cursor != null);
    }

    
    /**
    * Remove the current element from this sequence.
    * @param - none
    * <dt><b>Precondition:</b><dd>
    *   isCurrent()</CODE> returns true.
    * <dt><b>Postcondition:</b><dd>
    *   The current element has been removed from this sequence, and the 
    *   following element (if there is one) is now the new current element. 
    *   If there was no following element, then there is now no current 
    *   element.
    * @exception IllegalStateException
    *   Indicates that there is no current element, so 
    *   removeCurrent</CODE> may not be called. 
    **/
    public void removeCurrent () {
        if (cursor != null) {
            if (manyNodes > 1) {
                if (cursor.getLink () != null) {
                    cursor.setData (cursor.getLink ().getData () );
                    cursor.setLink (cursor.getLink ().getLink () );
                }
                else {
                    Node newCursor = head;
                    while (newCursor.getLink () != cursor)
                        newCursor = newCursor.getLink ();
                    
                    newCursor.setLink (null);
                    cursor = null;
                }
            }
            else {
                tail = null;
                head = null;
                cursor = null;
            }
            
            manyNodes--;
        }
        else
            throw new IllegalStateException ("There is no current element.");
    }


    /**
    * Return the number of elements in this sequence.
    * @param - none
    * @return
    *   the number of elements in this sequence
    **/ 
    public int size () {
        return manyNodes;
    }


    /**
    * Set the current element at the front of this sequence.
    * @param - none
    * <dt><b>Postcondition:</b><dd>
    *   The front element of this sequence is now the current element (but 
    *   if this sequence has no elements at all, then there is no current 
    *   element).
    **/ 
    public void start () {
        cursor = head;
    }
    
    /**
    * Set the current element at the tail of this sequence.
    * @param - none
    * <dt><b>Postcondition:</b><dd>
    *   The current element will point to the tail of the sequence.
    * @exception IllegalStateException
    *   Indicates that the sequence is empty.
    **/
    public void currentLast () {
        if (tail != null)
            cursor = tail;
        else
            throw new IllegalStateException ("The sequence is empty.");
    }
    
    /**
    * Add an element to the head of this sequence.
    * @param num The element to add to the sequence.
    * <dt><b>Postcondition:</b><dd>
    *   The element will be added to the head of the sequence.
    * @exception OutOfMemoryError
    *   Indicates insufficient memory for the new element.
    **/
    public void addFront (double num) {
        start ();
        addAfter (num);
    }
    
    /**
    * Remove the element at the front of this sequence.
    * @param - none
    * <dt><b>Postcondition:</b><dd>
    *   The element at the front will be removed the sequence.
    * @exception IllegalStateException
    *   Indicates that the sequence is empty.
    **/
    public void removeFront () {
        start ();
        removeCurrent ();
    }
    
    /**
    * Add an element to the tail of this sequence.
    * @param num The element to add to the sequence.
    * <dt><b>Postcondition:</b><dd>
    *   The element will be added to the tail of the sequence.
    * @exception OutOfMemoryError
    *   Indicates insufficient memory for the new element.
    * @exception IllegalStateException
    *   Indicates that the sequence is empty.
    **/
    public void addEnd (double num) {
        currentLast ();
        addAfter (num);
    }
    
    /**
    * Set the current element to the index passed.
    * @param i The index of the desired element.
    * <dt><b>Postcondition:</b><dd>
    *   The current element will point to the index passed.
    * @exception IllegalStateException
    *   Indicates that the sequence is empty.
    * @exception IllegalStateException
    *   Indicates that the index passed is out of bounds.
    **/
    public void setCurrent (int i) {
        if (manyNodes == 0)
            throw new IllegalStateException ("The sequence is empty.");
        
        if (i < manyNodes || i >= 0) {
            if (i == 0)
                cursor = head;
            else if (i == (manyNodes - 1) )
                cursor = tail;
            else {
                cursor = head;
                for (int n = 0; n < i; n++)
                    cursor = cursor.getLink ();
            }
        }
        else
            throw new IllegalStateException ("The index is out of bounds.");
    }
    
    /**
    * Get the value of the element at the passed index.
    * @param i The index of the desired element value.
    * <dt><b>Postcondition:</b><dd>
    *   The current element will point to the index passed.
    * @exception IllegalStateException
    *   Indicates that the sequence is empty.
    * @exception IllegalStateException
    *   Indicates that the index passed is out of bounds.
    **/
    public double retrieveElement (int i ) {
        setCurrent (i);
        
        return getCurrent ();
    }
    
    /**
    * Returns all the current values in the list as a String.
    * @param - none
    * <dt><b>Postcondition:</b><dd>
    *   The String containing all the elements will be returned.
    * @return
    *   The String containing all the elements.
    **/
    public String toString () {
        String items = new String ();
        Node newCursor = head;
        
        while (newCursor != null) {
            items += (newCursor.getData () + " ");
            newCursor = newCursor.getLink ();
        }
        
        return items;
    }
}
