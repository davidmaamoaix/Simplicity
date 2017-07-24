//DavidM's Simplicity
package simplicity.UI;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import simplicity.io.IO;
import simplicity.objects.BtnElement;
import simplicity.objects.Element;
import simplicity.objects.InputElement;
import simplicity.objects.RectElement;
import simplicity.objects.TextElement;

public class mainInterface extends Application{
	
	public static ArrayList<Element> elements=new ArrayList<Element>();
	public static ArrayList<Element> deprecated=new ArrayList<Element>();
	public static String savePath=null;
	public static int focus=-1;
	public static Group g;
	public static int toolFocus=0;
	static Rectangle workspace;
	static Stage stageMain;
	static TextField[] config=new TextField[4];
	static TextField[] propText=new TextField[6];
	static TextField[] propRect=new TextField[7];
	static TextField[] propBtn=new TextField[9];
	static TextField[] propInput=new TextField[4];
	static GridPane gridpane;
	static GridPane right;
	static GridPane propPan;
	static boolean dragging=false;static boolean draggingBtn=false;
	static boolean draggingElement=false;
	static int x1=0,y1=0,x2=0,y2=0;
	static int x1Btn=0,y1Btn=0,x2Btn=0,y2Btn=0;
	static int x1Drag=0,y1Drag=0,x2Drag=0,y2Drag=0;
	
	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage stage){
		
		stageMain=stage;
		Tooltip tooltip=new Tooltip();
		
		//Setup
		gridpane=new GridPane();
		gridpane.setStyle("-fx-background-color: #F4F4F4;");
		g=new Group();
		g.getChildren().add(gridpane);
		Scene scene=new Scene(g,1280,760);
		scene.getStylesheets().add("style.css");
		stage.setScene(scene);
		stage.setTitle("Simplicity");
		stage.setResizable(false);
		stage.setMinWidth(1280);
		stage.setMinHeight(760);
		stage.show();
		
		//Workspace
		workspace=new Rectangle();
		workspace.setOnMouseClicked(new EventHandler<MouseEvent>()
        {	@Override
			public void handle(MouseEvent e){
        			if(toolFocus==2) addElement((int)e.getX(),(int)e.getY());
        			if(toolFocus==4) addElement((int)e.getX(),(int)(e.getY()));
        	}});
		workspace.setOnMousePressed(new EventHandler<MouseEvent>()
        {	@Override
			public void handle(MouseEvent e){
        			if(toolFocus==1){
        				if(!dragging){
        					x1=(int)e.getX();
        					y1=(int)e.getY();
        				}
        				dragging=true;
        			}
        			if(toolFocus==3){
        				if(!draggingBtn){
        					x1Btn=(int)e.getX();
        					y1Btn=(int)(e.getY());
        				}
        				draggingBtn=true;
        			}
        	}});
		workspace.setOnMouseReleased(new EventHandler<MouseEvent>()
        {	@Override
			public void handle(MouseEvent e){
        			if(toolFocus==0){
        				if(draggingElement){
        					x2Drag=(int)e.getX();
        					y2Drag=(int)e.getY();
        				}
        				draggingElement=false;
        				moveElement();
        			}
        			if(toolFocus==1){
        				if(dragging){
        					x2=(int)e.getX();
        					y2=(int)e.getY();
        				}
        				dragging=false;
        				addElement(x1,y1,x2,y2);
        			}
        			if(toolFocus==3){
        				if(draggingBtn){
        					x2Btn=(int)e.getX();
        					y2Btn=(int)e.getY();
        				}
        				draggingBtn=false;
        				addElement(x1Btn+300,y1Btn+30,x2Btn+300,y2Btn+30);
        			}
        	}});
		Rectangle placeholder=new Rectangle(680,600);
		placeholder.setFill(Color.web("0xF4F4F4"));
		GridPane center=new GridPane();
		center.add(placeholder,0,0);
		center.add(workspace,0,0);
		gridpane.add(center,1,1);
		resizeWorkspace(680,600,"0xFFFFFF");
		
		//Property Panel
		GridPane left=new GridPane();
		Rectangle property=new Rectangle(300,350);
		gridpane.setVgap(0);
		property.setFill(Color.web("0xE7E7E7"));
		left.add(property,0,0);
		gridpane.add(left,0,1);
		GridPane propertyText=new GridPane();
		propertyText.setHgap(-30);
		propertyText.setVgap(10);
		left.add(propertyText,0,0);
		Text sceneProperty=new Text("\n\tScene Properties\n");
		sceneProperty.setFont(new Font(20));
		propertyText.add(sceneProperty,0,0);
		String[] properties=new String[] {"Title:","Scene Width:","Scene Height:","Background Color:"};
		String[] comment=new String[] {"Simplicity","680","600","FFFFFF"};
		for(int i=0;i<properties.length;i++){
			Text text=new Text("\t"+properties[i]);
			text.setFont(new Font(15));
			propertyText.add(text,0,i+1);
			TextField textField=new TextField(comment[i]);
			textField.setMaxWidth(100);
			propertyText.add(textField,1,i+1);
			config[i]=textField;
		}
		Button refresh=new Button("Refresh");
		propertyText.add(refresh,1,6,2,1);
		refresh.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        refreshWorkspace();
		        updateWorkspace();
		    }
		});
		
		//Tool Panel
		Rectangle tool=new Rectangle(300,740);
		tool.setFill(Color.web("0xE7E7E7"));
		left.add(tool,0,2);
		GridPane toolText=new GridPane();
		toolText.setHgap(0);
		toolText.setVgap(10);
		left.add(toolText,0,2);
		Text toolMenu=new Text("\t\tTools\n");
		toolMenu.setFont(new Font(20));
		toolText.add(toolMenu,0,0,3,1);
		String[] imgs=new String[] {"Cursor","Rect","Text","Button","Input"};
		Image btnImg;Button btn;
		for(int i=0;i<imgs.length;i++){
			final int count=i;
			btnImg=new Image(imgs[i]+".png");
			btn=new Button();
			btn.setGraphic(new ImageView(btnImg));
			btn.getStyleClass().add("toolBtn");
			tooltip=new Tooltip();
			tooltip.setText(imgs[i]);
			btn.setTooltip(tooltip);
			btn.setMinWidth(60);
			btn.setMinHeight(60);
			btn.setMaxWidth(60);
			btn.setMaxHeight(60);
			btn.setOnAction(new EventHandler<ActionEvent>() {
	            @Override public void handle(ActionEvent e) {
	                toolFocus=count;
	                //System.out.println(toolFocus);
	            }
	        });
			toolText.add(btn,i,1);
		}
		
		//Properties
		Rectangle prop=new Rectangle(300,740);
		prop.setFill(Color.web("0xE7E7E7"));
		right=new GridPane();
		propPan=new GridPane();
		right.add(prop,0,0);
		right.add(propPan,0,0);
		right.setLayoutX(980);
		right.setLayoutY(29);
		Text propertyPanelTitle=new Text("\n\tElement Properties\n");
		propertyPanelTitle.setFont(new Font(20));
		propPan.add(propertyPanelTitle,0,0);
		g.getChildren().add(right);
		updateProp();
		
		//Menu Bar
		MenuBar menuBar = new MenuBar();
		menuBar.prefWidthProperty().bind(stage.widthProperty());
		Menu menuFile = new Menu("File");
		Menu menuEdit = new Menu("Edit");
        menuBar.getMenus().addAll(menuFile, menuEdit);
        gridpane.add(menuBar,0,0,2,1);
        
        //Menu Item
        String[] actions=new String[] {"New","Open","Save","Save As...","Export","Close"};
        for(int i=0;i<actions.length;i++){
        	final int temp=i;
        		MenuItem fileItem=new MenuItem(actions[i]);
        		fileItem.setOnAction(new EventHandler<ActionEvent>(){
        			public void handle(ActionEvent t){
        				fileAction(temp);
        			}
        		});
        		menuFile.getItems().addAll(fileItem);
        }
        
        actions=new String[] {"Duplicate Selected","Delete Selected","Bring to Front"};
        for(int i=0;i<actions.length;i++){
        	final int temp=i;
        		MenuItem editItem=new MenuItem(actions[i]);
        		editItem.setOnAction(new EventHandler<ActionEvent>(){
        			public void handle(ActionEvent t){
        				editAction(temp);
        			}
        		});
        		menuEdit.getItems().addAll(editItem);
        }
	}

	public static void refreshWorkspace(){
		try{
			resizeWorkspace(Integer.parseInt(config[1].getText()),Integer.parseInt(config[2].getText()),"0x"+config[3].getText());
		}catch(Exception e){
			resizeWorkspace(680,600,"0xFFFFFF");
			//System.out.println("Error: "+e);
		}
	}
	
	public static void resizeWorkspace(int w,int h,String color){
		workspace.setWidth(w);
		workspace.setHeight(h);
		workspace.setFill(Color.web(color));
	}
	
	static void editAction(int x){
		switch(x){
		case 0:duplicate();return;
		case 1:delete();return;
		case 2:front();return;
		}
	}
	
	static void fileAction(int x){
		switch(x){
		case 0:fileNew();return;
		case 1:fileOpen();return;
		case 2:fileSave();return;
		case 3:fileSaveAs();return;
		case 4:fileExport();return;
		case 5:fileClose();return;
		}
	}
	
	static void fileNew(){
		elements=new ArrayList<Element>();
		config[0].setText("Simplicity");
		config[1].setText("680");
		config[2].setText("600");
		config[3].setText("FFFFFF");
		savePath=null;
		focus=-1;
		refreshWorkspace();
		updateWorkspace();
		updateProp();
	}
	
	static void fileOpen(){
		fileNew();
		DirectoryChooser x=new DirectoryChooser();
		File folder=x.showDialog(stageMain);
		if(folder==null) return;
		String path=folder.getPath();
		config[0].setText(IO.readFrom(path+"/Stage/Title.txt"));
		config[1].setText(IO.readFrom(path+"/Stage/Width.txt"));
		config[2].setText(IO.readFrom(path+"/Stage/Height.txt"));
		config[3].setText(IO.readFrom(path+"/Stage/BG.txt"));
		savePath=IO.readFrom(path+"/path.txt");
		for(File obj:new File(path+"/Elements/TextElements").listFiles()){
			if(!obj.getName().equals(".DS_Store")&&!obj.getName().split("#")[0].equals("_Deprecated")){
				int _x=Integer.parseInt(IO.readFrom(path+"/Elements/TextElements/"+obj.getName()+"/X.txt"));
				int _y=Integer.parseInt(IO.readFrom(path+"/Elements/TextElements/"+obj.getName()+"/Y.txt"));
				String content=IO.readFrom(path+"/Elements/TextElements/"+obj.getName()+"/Content.txt");
				String color=IO.readFrom(path+"/Elements/TextElements/"+obj.getName()+"/Color.txt");
				String font=IO.readFrom(path+"/Elements/TextElements/"+obj.getName()+"/Font.txt");
				int size=Integer.parseInt(IO.readFrom(path+"/Elements/TextElements/"+obj.getName()+"/Size.txt"));
				TextElement te=new TextElement(_x,_y);
				te.size=size;
				te.font=font;
				te.name=obj.getName();
				te.content=content;
				te.color=color;
				elements.add(te);
			}
		}
		for(File obj:new File(path+"/Elements/RectElements").listFiles()){
			if(!obj.getName().equals(".DS_Store")&&!obj.getName().split("#")[0].equals("_Deprecated")){
				//System.out.println(IO.readFrom(path+"/Elements/RectElements/"+obj.getName()+"/X.txt"));
				int _x=Integer.parseInt(IO.readFrom(path+"/Elements/RectElements/"+obj.getName()+"/X.txt"));
				int _y=Integer.parseInt(IO.readFrom(path+"/Elements/RectElements/"+obj.getName()+"/Y.txt"));
				int w=Integer.parseInt(IO.readFrom(path+"/Elements/RectElements/"+obj.getName()+"/Width.txt"));
				int h=Integer.parseInt(IO.readFrom(path+"/Elements/RectElements/"+obj.getName()+"/Height.txt"));
				RectElement te=new RectElement(_x,_y,w,h);
				te.arcWidth=Integer.parseInt(IO.readFrom(path+"/Elements/RectElements/"+obj.getName()+"/ArcWidth.txt"));
				te.arcHeight=Integer.parseInt(IO.readFrom(path+"/Elements/RectElements/"+obj.getName()+"/ArcHeight.txt"));
				te.name=obj.getName();
				te.color=IO.readFrom(path+"/Elements/RectElements/"+obj.getName()+"/Color.txt");
				elements.add(te);
			}
		}
		//System.out.println(new File(path+"/Elements/BtnElements").listFiles().length);
		for(File obj:new File(path+"/Elements/BtnElements").listFiles()){
			if(!obj.getName().equals(".DS_Store")&&!obj.getName().split("#")[0].equals("_Deprecated")){
				int _x=Integer.parseInt(IO.readFrom(path+"/Elements/BtnElements/"+obj.getName()+"/X.txt"));
				int _y=Integer.parseInt(IO.readFrom(path+"/Elements/BtnElements/"+obj.getName()+"/Y.txt"));
				int w=Integer.parseInt(IO.readFrom(path+"/Elements/BtnElements/"+obj.getName()+"/Width.txt"));
				int h=Integer.parseInt(IO.readFrom(path+"/Elements/BtnElements/"+obj.getName()+"/Height.txt"));
				BtnElement te=new BtnElement(_x,_y,w,h);
				te.name=obj.getName();
				te.color=IO.readFrom(path+"/Elements/BtnElements/"+obj.getName()+"/Color.txt");
				te.text=IO.readFrom(path+"/Elements/BtnElements/"+obj.getName()+"/Text.txt");
				te.arc=Integer.parseInt(IO.readFrom(path+"/Elements/BtnElements/"+obj.getName()+"/Arc.txt"));
				te.borderWidth=Integer.parseInt(IO.readFrom(path+"/Elements/BtnElements/"+obj.getName()+"/BorderWidth.txt"));
				te.borderColor=IO.readFrom(path+"/Elements/BtnElements/"+obj.getName()+"/BorderColor.txt");
				elements.add(te);
			}
		}
		for(File obj:new File(path+"/Elements/InputElements").listFiles()){
			if(!obj.getName().equals(".DS_Store")&&!obj.getName().split("#")[0].equals("_Deprecated")){
				int _x=Integer.parseInt(IO.readFrom(path+"/Elements/InputElements/"+obj.getName()+"/X.txt"));
				int _y=Integer.parseInt(IO.readFrom(path+"/Elements/InputElements/"+obj.getName()+"/Y.txt"));
				int w=Integer.parseInt(IO.readFrom(path+"/Elements/InputElements/"+obj.getName()+"/Width.txt"));
				int h=Integer.parseInt(IO.readFrom(path+"/Elements/InputElements/"+obj.getName()+"/Height.txt"));
				InputElement te=new InputElement(_x,_y,w,h,obj.getName());
				te.name=obj.getName();
				te.color=IO.readFrom(path+"/Elements/InputElements/"+obj.getName()+"/Color.txt");
				elements.add(te);
			}
		}
		focus=-1;
		refreshWorkspace();
		updateWorkspace();
		updateProp();
	}
	
	static void fileSave(){
		if(savePath==null){
			DirectoryChooser x=new DirectoryChooser();
			File folder=x.showDialog(stageMain);
			if(folder==null) return;
			savePath=folder.getPath()+"/"+config[0].getText();
		}else{
			
		}
		File dir=new File(savePath);
		dir.mkdir();
		IO.writeTo(savePath,savePath+"/path.txt");
		File stageDir=new File(savePath+"/Stage");
		stageDir.mkdir();
		String[] fileName=new String[] {"Title","Width","Height","BG"};
		for(int i=0;i<fileName.length;i++){
			IO.writeTo(config[i].getText(),savePath+"/Stage/"+fileName[i]+".txt");
		}
		dir=new File(savePath+"/Elements");
		dir.mkdir();
		dir=new File(savePath+"/Elements/TextElements");
		dir.mkdir();
		dir=new File(savePath+"/Elements/RectElements");
		dir.mkdir();
		dir=new File(savePath+"/Elements/BtnElements");
		dir.mkdir();
		dir=new File(savePath+"/Elements/InputElements");
		dir.mkdir();
		for(Element i:elements){
			if(i.getClass()==TextElement.class){
				TextElement data=(TextElement)i;
				dir=new File(savePath+"/Elements/TextElements/"+data.name);
				dir.mkdir();
				//IO.writeTo(data.name,savePath+"/Elements/TextElements/"+data.name+"/Name.txt"); //The folder's name==the element's name
				IO.writeTo(Integer.toString(data.x),savePath+"/Elements/TextElements/"+data.name+"/X.txt");
				IO.writeTo(Integer.toString(data.y),savePath+"/Elements/TextElements/"+data.name+"/Y.txt");
				IO.writeTo(data.content,savePath+"/Elements/TextElements/"+data.name+"/Content.txt");
				IO.writeTo(data.color,savePath+"/Elements/TextElements/"+data.name+"/Color.txt");
				IO.writeTo(Integer.toString(data.size),savePath+"/Elements/TextElements/"+data.name+"/Size.txt");
				IO.writeTo(data.font,savePath+"/Elements/TextElements/"+data.name+"/Font.txt");
			}
			if(i.getClass()==RectElement.class){
				RectElement data=(RectElement)i;
				dir=new File(savePath+"/Elements/RectElements/"+data.name);
				dir.mkdir();
				IO.writeTo(Integer.toString(data.x),savePath+"/Elements/RectElements/"+data.name+"/X.txt");
				IO.writeTo(Integer.toString(data.y),savePath+"/Elements/RectElements/"+data.name+"/Y.txt");
				IO.writeTo(Integer.toString(data.width),savePath+"/Elements/RectElements/"+data.name+"/Width.txt");
				IO.writeTo(Integer.toString(data.height),savePath+"/Elements/RectElements/"+data.name+"/Height.txt");
				IO.writeTo(Integer.toString(data.arcWidth),savePath+"/Elements/RectElements/"+data.name+"/ArcWidth.txt");
				IO.writeTo(Integer.toString(data.arcHeight),savePath+"/Elements/RectElements/"+data.name+"/ArcHeight.txt");
				IO.writeTo(data.color,savePath+"/Elements/RectElements/"+data.name+"/Color.txt");
			}
			if(i.getClass()==BtnElement.class){
				BtnElement data=(BtnElement)i;
				dir=new File(savePath+"/Elements/BtnElements/"+data.name);
				dir.mkdir();
				IO.writeTo(Integer.toString(data.x),savePath+"/Elements/BtnElements/"+data.name+"/X.txt");
				IO.writeTo(Integer.toString(data.y),savePath+"/Elements/BtnElements/"+data.name+"/Y.txt");
				IO.writeTo(Integer.toString(data.width),savePath+"/Elements/BtnElements/"+data.name+"/Width.txt");
				IO.writeTo(Integer.toString(data.height),savePath+"/Elements/BtnElements/"+data.name+"/Height.txt");
				IO.writeTo(data.color,savePath+"/Elements/BtnElements/"+data.name+"/Color.txt");
				IO.writeTo(data.text,savePath+"/Elements/BtnElements/"+data.name+"/Text.txt");
				IO.writeTo(Integer.toString(data.arc),savePath+"/Elements/BtnElements/"+data.name+"/Arc.txt");
				IO.writeTo(Integer.toString(data.borderWidth),savePath+"/Elements/BtnElements/"+data.name+"/BorderWidth.txt");
				IO.writeTo(data.borderColor,savePath+"/Elements/BtnElements/"+data.name+"/BorderColor.txt");
			}
			if(i.getClass()==InputElement.class){
				InputElement data=(InputElement)i;
				dir=new File(savePath+"/Elements/InputElements/"+data.name);
				dir.mkdir();
				IO.writeTo(Integer.toString(data.x),savePath+"/Elements/InputElements/"+data.name+"/X.txt");
				IO.writeTo(Integer.toString(data.y),savePath+"/Elements/InputElements/"+data.name+"/Y.txt");
				IO.writeTo(Integer.toString(data.width),savePath+"/Elements/InputElements/"+data.name+"/Width.txt");
				IO.writeTo(Integer.toString(data.height),savePath+"/Elements/InputElements/"+data.name+"/Height.txt");
				IO.writeTo(data.color,savePath+"/Elements/InputElements/"+data.name+"/Color.txt");
			}
		}
		for(Element i:deprecated){
			if(i.getClass()==TextElement.class){
				TextElement data=(TextElement)i;
				dir=new File(savePath+"/Elements/TextElements/"+data.name);
				if(dir.isDirectory()){
					File newDir=new File(savePath+"/Elements/TextElements/_Deprecated#"+data.name);
					dir.renameTo(newDir);
				}
			}
			if(i.getClass()==RectElement.class){
				RectElement data=(RectElement)i;
				dir=new File(savePath+"/Elements/RectElements/"+data.name);
				if(dir.isDirectory()){
					File newDir=new File(savePath+"/Elements/RectElements/_Deprecated#"+data.name);
					dir.renameTo(newDir);
				}
			}
			if(i.getClass()==BtnElement.class){
				BtnElement data=(BtnElement)i;
				dir=new File(savePath+"/Elements/BtnElements/"+data.name);
				if(dir.isDirectory()){
					File newDir=new File(savePath+"/Elements/BtnElements/_Deprecated#"+data.name);
					dir.renameTo(newDir);
				}
			}
			if(i.getClass()==InputElement.class){
				InputElement data=(InputElement)i;
				dir=new File(savePath+"/Elements/InputElements/"+data.name);
				if(dir.isDirectory()){
					File newDir=new File(savePath+"/Elements/InputElements/_Deprecated#"+data.name);
					dir.renameTo(newDir);
				}
			}
		}
	}
	
	static void fileSaveAs(){
		DirectoryChooser x=new DirectoryChooser();
		File folder=x.showDialog(stageMain);
		if(folder==null) return;
		String saveAsPath=folder.getPath()+"/"+config[0].getText();
		File dir=new File(saveAsPath);
		dir.mkdir();
		IO.writeTo(saveAsPath,saveAsPath+"/path.txt");
		File stageDir=new File(saveAsPath+"/Stage");
		stageDir.mkdir();
		String[] fileName=new String[] {"Title","Width","Height","BG"};
		for(int i=0;i<fileName.length;i++){
			IO.writeTo(config[i].getText(),saveAsPath+"/Stage/"+fileName[i]+".txt");
		}
		dir=new File(saveAsPath+"/Elements");
		dir.mkdir();
		dir=new File(saveAsPath+"/Elements/TextElements");
		dir.mkdir();
		dir=new File(saveAsPath+"/Elements/RectElements");
		dir.mkdir();
		dir=new File(saveAsPath+"/Elements/BtnElements");
		dir.mkdir();
		dir=new File(saveAsPath+"/Elements/InputElements");
		dir.mkdir();
		for(Element i:elements){
			if(i.getClass()==TextElement.class){
				TextElement data=(TextElement)i;
				dir=new File(saveAsPath+"/Elements/TextElements/"+data.name);
				dir.mkdir();
				//IO.writeTo(data.name,saveAsPath+"/Elements/TextElements/"+data.name+"/Name.txt"); //The folder's name==the element's name
				IO.writeTo(Integer.toString(data.x),saveAsPath+"/Elements/TextElements/"+data.name+"/X.txt");
				IO.writeTo(Integer.toString(data.y),saveAsPath+"/Elements/TextElements/"+data.name+"/Y.txt");
				IO.writeTo(data.content,saveAsPath+"/Elements/TextElements/"+data.name+"/Content.txt");
				IO.writeTo(data.color,saveAsPath+"/Elements/TextElements/"+data.name+"/Color.txt");
				IO.writeTo(Integer.toString(data.size),saveAsPath+"/Elements/TextElements/"+data.name+"/Size.txt");
				IO.writeTo(data.font,saveAsPath+"/Elements/TextElements/"+data.name+"/Font.txt");
			}
			if(i.getClass()==RectElement.class){
				RectElement data=(RectElement)i;
				dir=new File(saveAsPath+"/Elements/RectElements/"+data.name);
				dir.mkdir();
				IO.writeTo(Integer.toString(data.x),saveAsPath+"/Elements/RectElements/"+data.name+"/X.txt");
				IO.writeTo(Integer.toString(data.y),saveAsPath+"/Elements/RectElements/"+data.name+"/Y.txt");
				IO.writeTo(Integer.toString(data.width),saveAsPath+"/Elements/RectElements/"+data.name+"/Width.txt");
				IO.writeTo(Integer.toString(data.height),saveAsPath+"/Elements/RectElements/"+data.name+"/Height.txt");
				IO.writeTo(Integer.toString(data.arcWidth),saveAsPath+"/Elements/RectElements/"+data.name+"/ArcWidth.txt");
				IO.writeTo(Integer.toString(data.arcHeight),saveAsPath+"/Elements/RectElements/"+data.name+"/ArcHeight.txt");
				IO.writeTo(data.color,saveAsPath+"/Elements/RectElements/"+data.name+"/Color.txt");
			}
			if(i.getClass()==BtnElement.class){
				BtnElement data=(BtnElement)i;
				dir=new File(saveAsPath+"/Elements/BtnElements/"+data.name);
				dir.mkdir();
				IO.writeTo(Integer.toString(data.x),saveAsPath+"/Elements/BtnElements/"+data.name+"/X.txt");
				IO.writeTo(Integer.toString(data.y),saveAsPath+"/Elements/BtnElements/"+data.name+"/Y.txt");
				IO.writeTo(Integer.toString(data.width),saveAsPath+"/Elements/BtnElements/"+data.name+"/Width.txt");
				IO.writeTo(Integer.toString(data.height),saveAsPath+"/Elements/BtnElements/"+data.name+"/Height.txt");
				IO.writeTo(data.color,saveAsPath+"/Elements/BtnElements/"+data.name+"/Color.txt");
				IO.writeTo(data.text,saveAsPath+"/Elements/BtnElements/"+data.name+"/Text.txt");
				IO.writeTo(Integer.toString(data.arc),saveAsPath+"/Elements/BtnElements/"+data.name+"/Arc.txt");
				IO.writeTo(Integer.toString(data.borderWidth),saveAsPath+"/Elements/BtnElements/"+data.name+"/BorderWidth.txt");
				IO.writeTo(data.borderColor,saveAsPath+"/Elements/BtnElements/"+data.name+"/BorderColor.txt");
			}
			if(i.getClass()==InputElement.class){
				InputElement data=(InputElement)i;
				dir=new File(savePath+"/Elements/InputElements/"+data.name);
				dir.mkdir();
				IO.writeTo(Integer.toString(data.x),savePath+"/Elements/InputElements/"+data.name+"/X.txt");
				IO.writeTo(Integer.toString(data.y),savePath+"/Elements/InputElements/"+data.name+"/Y.txt");
				IO.writeTo(Integer.toString(data.width),savePath+"/Elements/InputElements/"+data.name+"/Width.txt");
				IO.writeTo(Integer.toString(data.height),savePath+"/Elements/InputElements/"+data.name+"/Height.txt");
				IO.writeTo(data.color,savePath+"/Elements/InputElements/"+data.name+"/Color.txt");
			}
		}
		for(Element i:deprecated){
			if(i.getClass()==TextElement.class){
				TextElement data=(TextElement)i;
				dir=new File(saveAsPath+"/Elements/TextElements/"+data.name);
				if(dir.isDirectory()){
					File newDir=new File(saveAsPath+"/Elements/TextElements/_Deprecated#"+data.name);
					dir.renameTo(newDir);
				}
			}
			if(i.getClass()==RectElement.class){
				RectElement data=(RectElement)i;
				dir=new File(saveAsPath+"/Elements/RectElements/"+data.name);
				if(dir.isDirectory()){
					File newDir=new File(saveAsPath+"/Elements/RectElements/_Deprecated#"+data.name);
					dir.renameTo(newDir);
				}
			}
			if(i.getClass()==BtnElement.class){
				BtnElement data=(BtnElement)i;
				dir=new File(saveAsPath+"/Elements/BtnElements/"+data.name);
				if(dir.isDirectory()){
					File newDir=new File(saveAsPath+"/Elements/BtnElements/_Deprecated#"+data.name);
					dir.renameTo(newDir);
				}
			}
			if(i.getClass()==InputElement.class){
				InputElement data=(InputElement)i;
				dir=new File(savePath+"/Elements/InputElements/"+data.name);
				if(dir.isDirectory()){
					File newDir=new File(savePath+"/Elements/InputElements/_Deprecated#"+data.name);
					dir.renameTo(newDir);
				}
			}
		}
	}
	
	static void fileExport(){
		DirectoryChooser x=new DirectoryChooser();
		File folder=x.showDialog(stageMain);
		if(folder==null) return;
		String exportPath=folder.getPath();
		String file=exportPath+"/"+config[0].getText()+".java";
		IO.writeTo("import javafx.scene.*;",file);
		IO.writeTo("import javafx.stage.*;",file,true);
		IO.writeTo("import javafx.application.*;",file,true);
		IO.writeTo("import javafx.scene.paint.Color;",file,true);
		IO.writeTo("import javafx.scene.shape.*;",file,true);
		IO.writeTo("import javafx.scene.control.*;",file,true);
		IO.writeTo("import javafx.scene.text.*;",file,true);
		IO.writeTo("",file,true);
		IO.writeTo("public class "+config[0].getText()+" extends Application{",file,true);
		IO.writeTo("\tpublic static void main(String[] args){",file,true);
		IO.writeTo("\t\tlaunch(args);",file,true);
		IO.writeTo("\t}",file,true);
		IO.writeTo("",file,true);
		IO.writeTo("\t@Override",file,true);
		IO.writeTo("\tpublic void start(Stage stage){",file,true);
		IO.writeTo("\t\tGroup group=new Group();",file,true);
		IO.writeTo("\t\tScene scene=new Scene(group,"+config[1].getText()+","+config[2].getText()+",Color.web(\"0x"+config[3].getText()+"\"));",file,true);
		IO.writeTo("\t\tstage.setScene(scene);",file,true);
		IO.writeTo("\t\tstage.setResizable(false);",file,true);
		IO.writeTo("\t\tstage.setTitle(\""+config[0].getText()+"\");",file,true);
		IO.writeTo("\t\tstage.setMinWidth("+config[1].getText()+");",file,true);
		IO.writeTo("\t\tstage.setMinHeight("+Integer.toString(Integer.parseInt(config[2].getText())+22)+");",file,true);
		IO.writeTo("\t\tstage.setMaxWidth("+config[1].getText()+");",file,true);
		IO.writeTo("\t\tstage.setMaxHeight("+Integer.toString(Integer.parseInt(config[2].getText())+22)+");",file,true);
		IO.writeTo("\t\tstage.show();",file,true);
		
			//Compiling Rectangles
		for(Element i:elements){
			if(i.getClass()==RectElement.class){
				RectElement rect=(RectElement)i;
				String name=(rect.name).replaceAll(" ","_");
				IO.writeTo("\t\tRectangle "+name+"=new Rectangle("+rect.x+","+rect.y+","+rect.width+","+rect.height+");",file,true);
				IO.writeTo("\t\t"+name+".setFill(Color.web(\"0x"+rect.color+"\"));",file,true);
				IO.writeTo("\t\t"+name+".setArcWidth("+rect.arcWidth+");",file,true);
				IO.writeTo("\t\t"+name+".setArcHeight("+rect.arcHeight+");",file,true);
				IO.writeTo("\t\t"+"group.getChildren().add("+name+");",file,true);
			}
		
			//Compiling Buttons
			if(i.getClass()==BtnElement.class){
				BtnElement btn=(BtnElement)i;
				String name=(btn.name).replaceAll(" ","_");
				IO.writeTo("\t\t"+"Button "+name+"=new Button(\""+btn.text+"\");",file,true);
				IO.writeTo("\t\t"+name+".setMinWidth("+btn.width+");",file,true);
				IO.writeTo("\t\t"+name+".setMaxWidth("+btn.width+");",file,true);
				IO.writeTo("\t\t"+name+".setMinHeight("+btn.height+");",file,true);
				IO.writeTo("\t\t"+name+".setMaxHeight("+btn.height+");",file,true);
				IO.writeTo("\t\t"+name+".setStyle(\"-fx-base:#"+btn.color+";\"+\"-fx-background-radius:"+Integer.toString(btn.arc)+";\"+\"-fx-border-width:"+Integer.toString(btn.borderWidth)+";\"+\"-fx-border-color:#"+btn.borderColor+";\");",file,true);
				IO.writeTo("\t\t"+name+".setLayoutX("+(btn.x-300)+");",file,true);
				IO.writeTo("\t\t"+name+".setLayoutY("+(btn.y-30)+");",file,true);
				IO.writeTo("\t\t"+"group.getChildren().add("+name+");",file,true);
			}
		
			//Compiling Texts
			if(i.getClass()==TextElement.class){
				TextElement text=(TextElement)i;
				String name=(text.name).replaceAll(" ","_");
				IO.writeTo("\t\t"+"Text "+name+"=new Text("+text.x+","+text.y+",\""+text.content+"\");",file,true);
				IO.writeTo("\t\t"+name+".setFont(new Font(\""+text.font+"\","+text.size+"));",file,true);
				IO.writeTo("\t\t"+name+".setFill(Color.web(\"0x"+text.color+"\"));",file,true);
				IO.writeTo("\t\t"+"group.getChildren().add("+name+");",file,true);
			}
		
			//Compiling TextFields
			if(i.getClass()==InputElement.class){
				InputElement text=(InputElement)i;
				String name=(text.name).replaceAll(" ","_");
				IO.writeTo("\t\t"+"TextField "+name+"=new TextField();",file,true);						IO.writeTo("\t\t"+name+".setMinWidth("+Integer.toString(text.width)+");",file,true);
				IO.writeTo("\t\t"+name+".setMaxWidth("+Integer.toString(text.width)+");",file,true);
				IO.writeTo("\t\t"+name+".setMinHeight("+Integer.toString(text.height)+");",file,true);
				IO.writeTo("\t\t"+name+".setMaxHeight("+Integer.toString(text.height)+");",file,true);
				IO.writeTo("\t\t"+name+".setLayoutX("+Integer.toString(text.x-300)+");",file,true);
				IO.writeTo("\t\t"+name+".setLayoutY("+Integer.toString(text.y-30)+");",file,true);
				IO.writeTo("\t\t"+"group.getChildren().add("+name+");",file,true);
			}
		}
		IO.writeTo("\t}",file,true);
		IO.writeTo("}",file,true);
	}
	
	static void addElement(int x,int y){
		if(toolFocus==2) elements.add(new TextElement(x,y));
		if(toolFocus==4) elements.add(new InputElement(x+300,y+30,100,20));
		focus=elements.size()-1;
		updateWorkspace();
		updateProp();
	}
	
	static void addElement(int x,int y,int xx,int yy){
		if(toolFocus==1){
			if(xx<x){
				int a=xx;xx=x;x=a;
			}
			if(yy<y){
				int a=yy;yy=y;y=a;
			}
			elements.add(new RectElement(x,y,xx-x,yy-y));
		}
		if(toolFocus==3){
			if(xx<x){
				int a=xx;xx=x;x=a;
			}
			if(yy<y){
				int a=yy;yy=y;y=a;
			}
			elements.add(new BtnElement(x,y,xx-x,yy-y));
		}
		focus=elements.size()-1;
		updateWorkspace();
		updateProp();
	}
	
	static void updateShape(){
		for(Element i:elements){
			if(i.getClass()==RectElement.class){
				RectElement re=(RectElement)i;
				if(re.x+re.width>workspace.getWidth()) re.width=(int)(workspace.getWidth()-re.x);
				if(re.y+re.height>workspace.getHeight()) re.height=(int)(workspace.getHeight()-re.y);
				if(re.x<0) re.x=0;
				if(re.y<0) re.y=0;
				if(re.x>workspace.getWidth()||re.y>workspace.getHeight()){
					focus=findFocus(re);
					delete();
					return;
				}
			}
			if(i.getClass()==BtnElement.class){
				BtnElement btn=(BtnElement)i;
				if(btn.x+btn.width-300>workspace.getWidth()) btn.width=(int)(workspace.getWidth()-btn.x+300);
				if(btn.y+btn.height-30>workspace.getHeight()) btn.height=(int)(workspace.getHeight()-btn.y+30);
				if(btn.x<300) btn.x=300;
				if(btn.y<30) btn.y=(int)30;
				if(btn.x>workspace.getWidth()+300||btn.y>workspace.getHeight()+30){
					focus=findFocus(btn);
					delete();
					return;
				}
			}
			if(i.getClass()==TextElement.class){
				TextElement text=(TextElement)i;
				if(text.x<0) text.x=0;
				if(text.y<0) text.y=0;
				if(text.x>workspace.getWidth()||text.y>workspace.getHeight()){
					focus=findFocus(text);
					delete();
					return;
				}
			}
			if(i.getClass()==InputElement.class){
				InputElement text=(InputElement)i;
				if(text.x<300) text.x=300;
				if(text.y<30) text.y=30;
				if(text.x>workspace.getWidth()+300||text.y>workspace.getHeight()+30){
					focus=findFocus(text);
					delete();
					return;
				}
			}
		}
	}
	
	static void fileClose(){
		Platform.exit();
	}
	
	static void front(){
		duplicate();
		delete();
	}
	
	static void duplicate(){
		if(focus>=0){
			if(elements.get(focus).getClass()==TextElement.class){
				TextElement x=(TextElement)elements.get(focus);
				TextElement text=new TextElement(x.x,x.y,x.name);
				text.content=x.content;
				text.font=x.font;
				text.size=x.size;
				text.color=x.color;
				elements.add(text);	
			}
			if(elements.get(focus).getClass()==RectElement.class){
				RectElement x=(RectElement)elements.get(focus);
				RectElement text=new RectElement(x.x,x.y,x.width,x.height,x.name);
				text.arcWidth=x.arcWidth;
				text.arcHeight=x.arcHeight;
				text.color=x.color;
				elements.add(text);	
			}
			if(elements.get(focus).getClass()==BtnElement.class){
				BtnElement x=(BtnElement)elements.get(focus);
				BtnElement btn=new BtnElement(x.x,x.y,x.width,x.height,x.name);
				btn.text=x.text;
				btn.arc=x.arc;
				btn.color=x.color;
				elements.add(btn);	
			}
			if(elements.get(focus).getClass()==InputElement.class){
				InputElement x=(InputElement)elements.get(focus);
				InputElement input=new InputElement(x.x,x.y,x.width,x.height,x.name);
				input.color=x.color;
				elements.add(input);	
			}
			updateWorkspace();
			updateProp();
		}
	}
	
	static void updateWorkspace(){
		//System.out.println("Update");
		updateShape();
		g.getChildren().clear();
		g.getChildren().add(gridpane);
		g.getChildren().add(right);
		int xOffset=300;int yOffset=(600-Integer.parseInt(config[2].getText()))/2+30;
		for(Element i:elements){
			if(i.getClass()==RectElement.class){
				RectElement re=(RectElement)i;
				Rectangle rect=new Rectangle(re.x+xOffset,re.y+yOffset,re.width,re.height);
				rect.setFill(Color.web("0x"+re.color));
				rect.setArcWidth(re.arcWidth);
				rect.setArcHeight(re.arcHeight);
				rect.setOnMouseClicked(new EventHandler<MouseEvent>()
		        {	@Override
					public void handle(MouseEvent e){
		        			if(toolFocus==2) addElement((int)e.getSceneX()-300,(int)e.getSceneY()-30);
		        			if(toolFocus==4) addElement((int)e.getSceneX()-300,(int)e.getSceneY()-30);
		        			if(toolFocus==0){
		        				focus=findFocus(re);
		        				updateProp();
		        			}
		        	}});
				rect.setOnMousePressed(new EventHandler<MouseEvent>()
		        {	@Override
					public void handle(MouseEvent e){
				        	if(toolFocus==0){
		        				if(!draggingElement){
		        					focus=findFocus(re);
		        					updateProp();
		        					x1Drag=(int)e.getSceneX()-300;
		        					y1Drag=(int)e.getSceneY()-30;
		        				}
		        				draggingElement=true;
		        			}
		        			if(toolFocus==1){
		        				if(!dragging){
		        					x1=(int)e.getSceneX()-300;
		        					y1=(int)e.getSceneY()-30;
		        				}
		        				dragging=true;
		        			}
		        			if(toolFocus==3){
		        				if(!draggingBtn){
		        					x1Btn=(int)e.getSceneX();
		        					y1Btn=(int)e.getSceneY();
		        				}
		        				draggingBtn=true;
		        			}
		        	}});
				rect.setOnMouseReleased(new EventHandler<MouseEvent>()
		        {	@Override
					public void handle(MouseEvent e){
				        	if(toolFocus==0){
		        				if(draggingElement){
		        					focus=findFocus(re);
		        					x2Drag=(int)e.getSceneX()-300;
		        					y2Drag=(int)e.getSceneY()-30;
		        				}
		        				draggingElement=false;
		        				moveElement();
		        				updateProp();
		        			}
		        			if(toolFocus==1){
		        				if(dragging){
		        					x2=(int)e.getSceneX()-300;
		        					y2=(int)e.getSceneY()-30;
		        				}
		        				dragging=false;
		        				addElement(x1,y1,x2,y2);
		        			}
		        			if(toolFocus==3){
		        				if(draggingBtn){
		        					x2Btn=(int)e.getX();
		        					y2Btn=(int)e.getY();
		        				}
		        				draggingBtn=false;
		        				addElement(x1Btn,y1Btn,x2Btn,y2Btn);
		        			}
		        	}});
				g.getChildren().add(rect);
			}
			if(i.getClass()==TextElement.class){
				TextElement text=(TextElement)i;
				Text t=new Text(text.x+xOffset,text.y+yOffset,text.content);
				t.setFill(Color.web("0x"+text.color));
				t.setFont(Font.font (text.font,text.size));
				t.setOnMouseClicked(new EventHandler<MouseEvent>()
		        {	@Override
					public void handle(MouseEvent e){
		        			if(toolFocus==0){
		        				focus=findFocus(text);
		        				updateProp();
		        			}
		        	}});
				t.setOnMousePressed(new EventHandler<MouseEvent>()
		        {	@Override
					public void handle(MouseEvent e){
				        	if(toolFocus==0){
		        				if(!draggingElement){
		        					focus=findFocus(text);
		        					updateProp();
		        					x1Drag=(int)e.getSceneX()-300;
		        					y1Drag=(int)e.getSceneY()-30;
		        				}
		        				draggingElement=true;
		        			}
		        	}});
				t.setOnMouseReleased(new EventHandler<MouseEvent>()
		        {	@Override
					public void handle(MouseEvent e){
				        	if(toolFocus==0){
		        				if(draggingElement){
		        					focus=findFocus(text);
		        					x2Drag=(int)e.getSceneX()-300;
		        					y2Drag=(int)e.getSceneY()-30;
		        				}
		        				draggingElement=false;
		        				moveElement();
		        				updateProp();
		        			}
		        	}});
				g.getChildren().add(t);
			}
			if(i.getClass()==BtnElement.class){
				BtnElement btn=(BtnElement)i;
				Button button=new Button(btn.text);
				button.setLayoutX(btn.x);button.setLayoutY(btn.y+(600-workspace.getHeight())/2);
				button.setMinWidth(btn.width);button.setMinHeight(btn.height);
				button.setMaxWidth(btn.width);button.setMaxHeight(btn.height);
				button.setStyle("-fx-base:#"+btn.color+";"+"-fx-background-radius:"+Integer.toString(btn.arc)+";"+"-fx-border-width:"+Integer.toString(btn.borderWidth)+";"+"-fx-border-color:#"+btn.borderColor+";");
				button.setOnMouseClicked(new EventHandler<MouseEvent>()
		        {	@Override
					public void handle(MouseEvent e){
		        			if(toolFocus==0){
		        				focus=findFocus(btn);
		        				updateProp();
		        			}
		        	}});
				button.setOnMousePressed(new EventHandler<MouseEvent>()
		        {	@Override
					public void handle(MouseEvent e){
				        	if(toolFocus==0){
				        		focus=findFocus(btn);
				        		updateProp();
		        				if(!draggingElement){
		        					x1Drag=(int)e.getSceneX()-300;
		        					y1Drag=(int)e.getSceneY()-30;
		        				}
		        				draggingElement=true;
		        			}
		        	}});
				button.setOnMouseReleased(new EventHandler<MouseEvent>()
		        {	@Override
					public void handle(MouseEvent e){
				        	if(toolFocus==0){
		        				if(draggingElement){
		        					x2Drag=(int)e.getSceneX()-300;
		        					y2Drag=(int)e.getSceneY()-30;
		        				}
		        				draggingElement=false;
		        				moveElement();
		        				updateProp();
		        			}
		        	}});
				g.getChildren().add(button);
			}
			if(i.getClass()==InputElement.class){
				InputElement text=(InputElement)i;
				TextField t=new TextField();
				t.setMinWidth(text.width);t.setMaxWidth(text.width);
				t.setMinHeight(text.height);t.setMaxHeight(text.height);
				t.setLayoutX(text.x);t.setLayoutY(text.y+(600-workspace.getHeight())/2);
				t.setOnMouseClicked(new EventHandler<MouseEvent>()
		        {	@Override
					public void handle(MouseEvent e){
		        			if(toolFocus==0){
		        				focus=findFocus(text);
		        				updateProp();
		        			}
		        	}});
				t.setOnMousePressed(new EventHandler<MouseEvent>()
		        {	@Override
					public void handle(MouseEvent e){
				        	if(toolFocus==0){
		        				if(!draggingElement){
		        					focus=findFocus(text);
		        					updateProp();
		        					x1Drag=(int)e.getSceneX()-300;
		        					y1Drag=(int)e.getSceneY()-30;
		        				}
		        				draggingElement=true;
		        			}
		        	}});
				t.setOnMouseReleased(new EventHandler<MouseEvent>()
		        {	@Override
					public void handle(MouseEvent e){
				        	if(toolFocus==0){
		        				if(draggingElement){
		        					focus=findFocus(text);
		        					x2Drag=(int)e.getSceneX()-300;
		        					y2Drag=(int)e.getSceneY()-30;
		        				}
		        				draggingElement=false;
		        				moveElement();
		        				updateProp();
		        			}
		        	}});
				g.getChildren().add(t);
			}
		}
	}
	
	static boolean inCanvas(MouseEvent e){
		if(e.getX()>workspace.getX()&&e.getX()<workspace.getX()+workspace.getWidth()){
			if(e.getY()>workspace.getY()&&e.getY()<workspace.getY()+workspace.getHeight()) return true;
		}
		return false;
	}
	
	static void delete(){
		if(focus>=0){
			deprecated.add(elements.get(focus));
			elements.remove(focus);
			focus=-1;
			updateProp();
			updateWorkspace();
		}
	}
	
	static void moveElement(){
		if(focus>=0){
			elements.get(focus).x+=x2Drag-x1Drag;
			elements.get(focus).y+=y2Drag-y1Drag;
			updateWorkspace();
		}
	}
	
	static int findFocus(Element e){
		int out=0;
		for(Element i:elements){
			if(Objects.equals(i.name,e.name)){
				return out;
			}
			out++;
		}
		return -1;
	}
	
	static void updateProp(){
		propPan.getChildren().clear();
		Text propertyPanelTitle=new Text("\n\tElement Properties\n");
		propertyPanelTitle.setFont(new Font(20));
		propPan.add(propertyPanelTitle,0,0);
		if(focus>=0){
			propPan.setHgap(-50);
			propPan.setVgap(10);
			if(elements.get(focus).getClass()==TextElement.class){
				TextElement data=(TextElement)elements.get(focus);
				String[] props=new String[] {"Position.X: ","Position.Y: ","Color: ","Content of Text: ","Font: ","Size: "};
				String[] info=new String[] {Integer.toString(data.x),Integer.toString(data.y),data.color,data.content,data.font,Integer.toString(data.size)};
				for(int i=0;i<props.length;i++){
					Text prop=new Text("\t"+props[i]);
					prop.setFont(new Font(15));
					propPan.add(prop,0,i+1);
					TextField textField=new TextField(info[i]);
					textField.setMaxWidth(100);
					propPan.add(textField,1,i+1);
					propText[i]=textField;
				}
				Button refresh=new Button("Refresh");
				propPan.add(refresh,1,9,2,1);
				refresh.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    		try{
					        data.x=Integer.parseInt(propText[0].getText());
					        data.y=Integer.parseInt(propText[1].getText());
					        data.color=propText[2].getText();
					        data.content=propText[3].getText();
					        data.font=propText[4].getText();
					        data.size=Integer.parseInt(propText[5].getText());
					        updateWorkspace();
					        updateShape();
				    		}catch(Exception error){
				    			for(int i=0;i<props.length;i++){
				    				propText[i]=new TextField(info[i]);
							}
				    		}
				    }
				});
			}
			if(elements.get(focus).getClass()==RectElement.class){
				RectElement data=(RectElement)elements.get(focus);
				String[] props=new String[] {"Position.X: ","Position.Y: ","Color: ","Width: ","Height: ","ArcWidth: ","ArcHeight: "};
				String[] info=new String[] {Integer.toString(data.x),Integer.toString(data.y),data.color,Integer.toString(data.width),Integer.toString(data.height),Integer.toString(data.arcWidth),Integer.toString(data.arcHeight)};
				for(int i=0;i<props.length;i++){
					Text prop=new Text("\t"+props[i]);
					prop.setFont(new Font(15));
					propPan.add(prop,0,i+1);
					TextField textField=new TextField(info[i]);
					textField.setMaxWidth(100);
					propPan.add(textField,1,i+1);
					propRect[i]=textField;
				}
				Button refresh=new Button("Refresh");
				propPan.add(refresh,1,10,2,1);
				refresh.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    		try{
					        data.x=Integer.parseInt(propRect[0].getText());
					        data.y=Integer.parseInt(propRect[1].getText());
					        data.color=propRect[2].getText();
					        data.width=Integer.parseInt(propRect[3].getText());
					        data.height=Integer.parseInt(propRect[4].getText());
					        data.arcWidth=Integer.parseInt(propRect[5].getText());
					        data.arcHeight=Integer.parseInt(propRect[6].getText());
					        updateWorkspace();
					        updateShape();
				    		}catch(Exception error){
				    			for(int i=0;i<props.length;i++){
				    				propRect[i]=new TextField(info[i]);
							}
				    		}
				    }
				});
			}
			if(elements.get(focus).getClass()==BtnElement.class){
				BtnElement data=(BtnElement)elements.get(focus);
				String[] props=new String[] {"Position.X: ","Position.Y: ","Color: ","Width: ","Height: ","Text: ","Arc: ","Border Width: ","Border Color: "};
				String[] info=new String[] {Integer.toString(data.x-300),Integer.toString(data.y-30),data.color,Integer.toString(data.width),Integer.toString(data.height),data.text,Integer.toString(data.arc),Integer.toString(data.borderWidth),data.borderColor};
				for(int i=0;i<props.length;i++){
					Text prop=new Text("\t"+props[i]);
					prop.setFont(new Font(15));
					propPan.add(prop,0,i+1);
					TextField textField=new TextField(info[i]);
					textField.setMaxWidth(100);
					propPan.add(textField,1,i+1);
					propBtn[i]=textField;
				}
				Button refresh=new Button("Refresh");
				propPan.add(refresh,1,10,2,1);
				refresh.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    		try{
					        data.x=Integer.parseInt(propBtn[0].getText())+300;
					        data.y=Integer.parseInt(propBtn[1].getText())+30;
					        data.color=propBtn[2].getText();
					        data.width=Integer.parseInt(propBtn[3].getText());
					        data.height=Integer.parseInt(propBtn[4].getText());
					        data.text=propBtn[5].getText();
					        data.arc=Integer.parseInt(propBtn[6].getText());
					        data.borderWidth=Integer.parseInt(propBtn[7].getText());
					        data.borderColor=propBtn[8].getText();
					        updateWorkspace();
					        updateShape();
				    		}catch(Exception error){
				    			for(int i=0;i<props.length;i++){
				    				propBtn[i]=new TextField(info[i]);
							}
				    		}
				    }
				});
			}
			if(elements.get(focus).getClass()==InputElement.class){
				InputElement data=(InputElement)elements.get(focus);
				String[] props=new String[] {"Position.X: ","Position.Y: ","Width: ","Height: "};
				String[] info=new String[] {Integer.toString(data.x-300),Integer.toString(data.y-30),Integer.toString(data.width),Integer.toString(data.height)};
				for(int i=0;i<props.length;i++){
					Text prop=new Text("\t"+props[i]);
					prop.setFont(new Font(15));
					propPan.add(prop,0,i+1);
					TextField textField=new TextField(info[i]);
					textField.setMaxWidth(100);
					propPan.add(textField,1,i+1);
					propInput[i]=textField;
				}
				Button refresh=new Button("Refresh");
				propPan.add(refresh,1,10,2,1);
				refresh.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    		try{
					        data.x=Integer.parseInt(propInput[0].getText())+300;
					        data.y=(int)(Integer.parseInt(propInput[1].getText())+30);
					        data.width=Integer.parseInt(propInput[2].getText());
					        data.height=Integer.parseInt(propInput[3].getText());
					        updateWorkspace();
					        updateShape();
				    		}catch(Exception error){
				    			for(int i=0;i<props.length;i++){
				    				propBtn[i]=new TextField(info[i]);
							}
				    		}
				    }
				});
			}
		}
	}
}
