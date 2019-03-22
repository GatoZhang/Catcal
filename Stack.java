package main;
import java.util.ArrayList;

public class Stack {
	static ArrayList<String> Stack = new ArrayList<String>();
	
	public void input(String item) {
		Stack.add(item);
	}
	
	public String output() {
		String item = new String();
		
		if (Stack.size() > 0){
			item = Stack.get(Stack.size() - 1);
			Stack.remove(Stack.size() - 1);
		}
		
		return item;
	}
	
	public String top() {
		if ( Stack.size() > 0 ){
			return Stack.get(Stack.size() - 1);
		} else {
			return "Error";
		}
		
	}
	
	public int size(){
		int size;
		
		size = Stack.size();
		
		return size;
	}
}
