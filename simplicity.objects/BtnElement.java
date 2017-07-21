//DavidM's Simplicity
package simplicity.objects;

public class BtnElement extends Element{

	public String text;
	public int width;
	public int height;
	public int arc;
	public int borderWidth;
	public String borderColor;
	
	public BtnElement(int _x,int _y,int w,int h){
		name="New Btn";
		text="Button";
		color="000000";
		x=_x;
		y=_y;
		width=w;
		height=h;
		arc=0;
		borderWidth=0;
		borderColor="FFFFFF";
		validName();
	}
	
	public BtnElement(int _x,int _y,int w,int h,String inName){
		name=inName;
		text="Button";
		color="000000";
		x=_x;
		y=_y;
		width=w;
		height=h;
		arc=0;
		borderWidth=0;
		borderColor="FFFFFF";
		validName();
	}
}
