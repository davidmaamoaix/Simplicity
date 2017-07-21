//DavidM's Simplicity
package simplicity.objects;

public class InputElement extends Element{
	
	public int width;
	public int height;
	
	public InputElement(int _x,int _y,int w,int h){
		name="New Input";
		color="FFFFFF";
		x=_x;
		y=_y;
		width=w;
		height=h;
		validName();
	}
	
	public InputElement(int _x,int _y,int w,int h,String inName){
		name=inName;
		color="FFFFFF";
		x=_x;
		y=_y;
		width=w;
		height=h;
		validName();
	}
}
