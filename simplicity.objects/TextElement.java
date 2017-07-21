//DavidM's Simplicity
package simplicity.objects;

public class TextElement extends Element{
	
	public String content="New Text";
	public String font;
	public int size;
	
	public TextElement(int _x,int _y){
		name="New Text";
		color="000000";
		size=20;
		font="Verdana";
		x=_x;
		y=_y;
		validName();
	}
	
	public TextElement(int _x,int _y,String inName){
		name=inName;
		color="000000";
		size=20;
		font="Verdana";
		x=_x;
		y=_y;
		validName();
	}
}