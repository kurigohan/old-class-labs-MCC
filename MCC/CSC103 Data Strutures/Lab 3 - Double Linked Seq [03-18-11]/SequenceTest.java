public class SequenceTest {
	static DoubleLinkedSeq sq1, sq2, sq3;
	static String[] params;
	public SequenceTest(){
		
		sq1=new DoubleLinkedSeq();
		sq2=new DoubleLinkedSeq();
		sq3=new DoubleLinkedSeq();
		params=null;
		
	}
	public void menu(String st) {
		params = st.split("[ \\-,]+");
		
		System.out.println("Input line: "+st);
		int choice = Integer.parseInt(params[0]);
		switch(choice){
		case 1: System.out.println("Create a sequence\n-----------------");
				if(params.length>1){
                    // if the first sequence has elements in it, create the second sequence
                    if (sq1.size () == 0) {
                        sq1 = createSeq(st);
                        printSeq(sq1);
                    }
                    else {
                        sq2 = createSeq(st);
                        printSeq(sq2);
                    }
				}
				else
					System.out.println("No data was created\n-----------------\n");
				break;
		case 2: System.out.println("Delete an element\n-----------------");
				if(find(Double.parseDouble(params[1]))!=-1){
					sq1.setCurrent(find(Double.parseDouble(params[1])));
					sq1.removeCurrent();
				}
				
				printSeq(sq1);
				break;
		case 3: System.out.println("Delete the first element from the sequence"
									+"\n-----------------");
				sq1.removeFront();
				System.out.println("Front element deleted.");
				printSeq(sq1);
				break;
		case 4: System.out.println("Add an element before another element"
									+"\n-----------------");
				if(find(Double.parseDouble(params[2]))!=-1){
					sq1.setCurrent(find(Double.parseDouble(params[2])));
					sq1.addBefore(Double.parseDouble(params[1]));
					System.out.println(params[1]+" added before the current element.");
					printSeq(sq1);
				}
				else
					System.out.println("Could not find element.\n-----------------\n");
				break;
		case 5: System.out.println("Add an element after an element"
									+"\n-----------------");
				if(find(Double.parseDouble(params[2]))!=-1){
					sq1.setCurrent(find(Double.parseDouble(params[2])));
					sq1.addAfter(Double.parseDouble(params[1]));
					System.out.println(params[1]+" added before the current element.");
					printSeq(sq1);
				}
				else
					System.out.println("Could not find element.\n-----------------");
				break;
		case 6: System.out.println("Add an element to the end of the sequence"
									+"\n-----------------");
				sq1.addEnd(Double.parseDouble(params[1]));
				System.out.println(params[1]+" added to the end.");
				printSeq(sq1);
				break;
		case 7: System.out.println("Display an element at a certain index"
									+"\n-----------------");
				System.out.println("The element at index "+params[1]+" is "+
									sq1.retrieveElement(Integer.parseInt(params[1])));
				printSeq(sq1);
				break;
		case 8: System.out.println("Display the last element in the sequence"
									+"\n-----------------");
				sq1.currentLast();
				System.out.println("The last element is "+ sq1.getCurrent());
				printSeq(sq1);
				break;
		case 9: System.out.println("Replace an element with another element"
									+"\n-----------------");
				if(find(Double.parseDouble(params[1]))!=-1){
					sq1.setCurrent(find(Double.parseDouble(params[1])));
					sq1.removeCurrent();
					sq1.addBefore(Double.parseDouble(params[2]));
					System.out.println(params[1]+" replaced with element "+params[2]);
					printSeq(sq1);
				}
				else
					System.out.println("Could not find element.\n-----------------\n");
				break;
		case 10: System.out.println("Append another sequence to the first sequence"
									+"\n-----------------");
				 sq1.addAll(sq2);
				 System.out.println("Second sequence appended to first sequence.");
				 printSeq(sq1);
				 break;
		case 11: System.out.println("Create a clone sequence\n-----------------");
				 sq3 = (DoubleLinkedSeq)sq1.clone();
				 System.out.println("Clone created.");
				 printSeq(sq3);
				 break;
		case 12: System.out.println("Print the sequence\n-----------------");
                if (params[1].equals("1") )
                    printSeq(sq1);
                else if (params[1].equals("2") )
                    printSeq(sq2);
				 break;
		case 13: System.out.println("Quit\n-----------------");
				 break;
		default: System.out.println("**Error invalid input**\n-----------------");
				 break;
		}
	}
	
	public static DoubleLinkedSeq createSeq(String st){
        DoubleLinkedSeq newSeq = null;
		newSeq = new DoubleLinkedSeq();
		for(int i=1;i<params.length;i++)
			newSeq.addAfter(Double.parseDouble(params[i]));
		return newSeq;
	}
	
	public static void printSeq(DoubleLinkedSeq seq){
		System.out.println("The sequence: "+seq);
		System.out.println("Number of elements: "+seq.size());
		System.out.println("The current element: "+seq.getCurrent());
		System.out.println("-----------------\n");
	}
	
	public static int find(double num){
		for(int i=0;i<sq1.size();i++){
			if(sq1.retrieveElement(i)==num){
				return i;
			}
		}
		return -1;
	}

}
