package main;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ListView;


import java.io.File;
//import view.MainUI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import entite.MenuEntry;

public class RecetteUI {

	private static ListView<MenuEntry> menuListView;
//	private static ListView<String> list;
  

	public static Scene createScene(int nbUtilisateurs) {


        // 创建两个按钮
        Label label = new Label("Voici les recettes");
        Button espaceButton = new Button("espace refrigerateur");

        //菜单 ListView
        menuListView = new ListView<>();
        menuListView.setPrefSize(400, 300);
        loadData("src/main/Menu.xml");

        // 跳转
        menuListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int selectedRecipeId = newValue.getId();
                Stage newStage = new Stage();
                newStage.setScene(AllMenuUI.createScene(selectedRecipeId));
                newStage.show();
            }
        });
        HBox hbox = new HBox();
		hbox.getChildren().addAll(espaceButton);
		
		VBox vbox = new VBox();
		vbox.getChildren().addAll(label,menuListView,hbox);
//		list = new ListView<>();
		
		
		 espaceButton.setOnAction(event -> {
	            Stage stage = new Stage();
	            stage.setHeight(600);
//	            stage.setWidth(810);
//	            stage.setScene(Refrigerateur_withList.createScene(list));
	            stage.setScene(RefrigerateurUI.createScene());
			    stage.show();	
	        });
		


		//布局
		hbox.setAlignment(Pos.CENTER); 
		vbox.setAlignment(Pos.CENTER); 
	    hbox.setSpacing(20);
	    vbox.setSpacing(20);
		
	    
	    Scene scene = new Scene(vbox,800,600);
	    
	    return scene;
	
	    
    }
	
	
	private static void loadData(String filename) {
    	try{
    		File file = new File(filename);
    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList recipeList = doc.getElementsByTagName("recipe");
            for (int i = 0; i < recipeList.getLength(); i++) {
                Node recipeNode = recipeList.item(i);
                if (recipeNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element recipeElement = (Element) recipeNode;
                    int id = Integer.parseInt(recipeElement.getAttribute("id"));
                    String name = recipeElement.getElementsByTagName("name").item(0).getTextContent();
                    menuListView.getItems().add(new MenuEntry(id, name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	
	
}
