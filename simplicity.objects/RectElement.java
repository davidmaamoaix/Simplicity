//DavidM's Simplicity
package simplicity.objects;

public class RectElement extends Element{
	
	public int width;
	public int height;
	public int arcWidth=0;
	public int arcHeight=0;
	
	public RectElement(int _x,int _y,int w,int h){
		name="New Rect";
		color="000000";
		x=_x;
		y=_y;
		width=w;
		height=h;
		validName();
	}
	
	public RectElement(int _x,int _y,int w,int h,String inName){
		name=inName;
		color="000000";
		x=_x;
		y=_y;
		width=w;
		height=h;
		validName();
	}
}
