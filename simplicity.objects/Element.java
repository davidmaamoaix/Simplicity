//DavidM's Simplicity
package simplicity.objects;

import java.util.Objects;

import simplicity.UI.mainInterface;

public class Element{
	
	public String color;
	public String name;
	public int x;
	public int y;
	
	public void validName(){
		for(Element i:mainInterface.elements){
			if(Objects.equals(splitName(i.name),splitName(name))){
				name=splitName(name);
				name+="_"+(numName(name));
				return;
			}
		}
	}
	
	public int numName(String x){
		int out=1;
		for(Element i:mainInterface.elements){
			if(Objects.equals(splitName(i.name),splitName(name))){
				out++;
			}
		}
		return out;
	}
	
	public String splitName(String x){
		String[] list=x.split("_");
		String out=list[0];
		for(int i=1;i<list.length-1;i++){
			out+="_"+list[i];
		}
		return out;
	}
}
