package main;


import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.util.List;


import control.List_control;
import control.Refrigerateur_withListUI_control;
import entite.Food;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.TransformerException;


public class RefrigerateurUI  {

    private static ListView<String> autresList;
    private static ListView<String> viandesList;
    private static ListView<String> bouteillesList;
    private static ListView<String> fruitsLegumesList;
    private static List<Food> fridgeFoods;
    private static String filename = "src/main/Foods.xml";
   
    public static Scene createScene() {
    	


        // 为四个类别创建四个listview
        autresList = new ListView<>();
        viandesList = new ListView<>();
        bouteillesList = new ListView<>();
        fruitsLegumesList = new ListView<>();

        //将文件中的foods读取到fridgeFoods中
        fridgeFoods = List_control.loadFoods(filename);
        
        // 从本地文件读取数据
        Refrigerateur_withListUI_control.updateAllListViews(fridgeFoods, autresList, viandesList, bouteillesList, fruitsLegumesList);
//        Refrigerateur_withListUI_control.addFoodsToListViews(fridgeFoods,autresList,viandesList,bouteillesList,fruitsLegumesList);

        Button retournerButton = new Button("retourner");
        Button ajouterButton = new Button("ajouter");
        Button sauterButton = new Button("Visualisation de la capacité du réfrigérateur");
        sauterButton.setOnAction(event -> {
            Stage stage = new Stage();
//            stage.setHeight(900);
//            stage.setWidth(810);
            stage.setScene(FridgeVisualization2.createScene());
		    stage.show();	
        });
        

        // 为每个listview创建一个vbox，里面从上到下是 类别 然后是listview本身
        VBox autresBox = createListBox("Autres", autresList);
        VBox viandesBox = createListBox("Viandes", viandesList);
        VBox bouteillesBox = createListBox("Bouteilles", bouteillesList);
        VBox fruitsLegumesBox = createListBox("Fruits et Legumes", fruitsLegumesList);

        //管理第一行的两个list的box
        HBox topRow = new HBox(10, autresBox, viandesBox);

        //管理第二行的两个list的box
        HBox bottomRow = new HBox(10, bouteillesBox, fruitsLegumesBox);

        // 一个vbox，里面有四个listview，构成整体的冰箱
        VBox vbox = new VBox(10, topRow, bottomRow);
        vbox.setPadding(new Insets(10));
        //vbox四周的黑线
        vbox.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        

        //左上角的标题
        Text title = new Text("Refrigerateur");
        title.setFont(Font.font(20));
        
        
        //四个监听器
        //openQuantityWindow 打开一个窗口进行增加删除的操作
        viandesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        	String itemName = viandesList.getSelectionModel().getSelectedItem();
        	openQuantityWindow(itemName,viandesList);
        	
            System.out.println("Selected ListView: viandesList");
            System.out.println("Selected item: " + newValue);
            
        });
        autresList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        	String itemName = autresList.getSelectionModel().getSelectedItem();
        	openQuantityWindow(itemName,autresList);
        	
            System.out.println("Selected ListView: viandesList");
            System.out.println("Selected item: " + newValue);
            
        });
        bouteillesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        	String itemName = bouteillesList.getSelectionModel().getSelectedItem();
        	openQuantityWindow(itemName,bouteillesList);
        	
            System.out.println("Selected ListView: viandesList");
            System.out.println("Selected item: " + newValue);
            
        });
        fruitsLegumesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        	String itemName = fruitsLegumesList.getSelectionModel().getSelectedItem();
        	openQuantityWindow(itemName,fruitsLegumesList);
        	
            System.out.println("Selected ListView: viandesList");
            System.out.println("Selected item: " + newValue);
            
        });
        
        //返回按钮
        retournerButton.setOnAction(event -> {
            Stage stage = (Stage) retournerButton.getScene().getWindow();
            stage.close();
        });
        
        ajouterButton.setOnAction(event -> {
            Stage stageAdd = new Stage();
            stageAdd.setHeight(350);
            stageAdd.setWidth(400);
        	
        	Label label_nom = new Label("Saisir le nom");
        	TextField textField_nom = new TextField();
            textField_nom.setPromptText("Veuillez entrer le nom de food");
            Label label_quantity = new Label("Saisir quantity");
            TextField textField_quantity = new TextField();
            textField_quantity.setPromptText("quantity: ");
            
            Label label_type = new Label("Saisir le type");
            TextField textField_type = new TextField();
            textField_type.setPromptText("type:Autre,Viande,FL,Bouteilles");
            
            Label label_unit = new Label("Saisir le unit (g,l,unite)");
            TextField textField_unit = new TextField();
            textField_unit.setPromptText("(g,l,unite)");
            
            Label label_size = new Label("Saisir le size");
            TextField textField_size = new TextField();
            textField_size.setPromptText("size: (n*n)");
            
            VBox vbox_addwindow = new VBox();
            Button Button_save = new Button("save");
            
            vbox_addwindow.getChildren().addAll(label_nom,textField_nom,label_quantity,textField_quantity,label_type,textField_type,label_unit,textField_unit,label_size,textField_size,Button_save);
            
            Scene scene_add = new Scene(vbox_addwindow,800,600);
            
            Button_save.setOnAction(event2 -> {
	          
//            	String itemString = textField_nom.getText() + " - " + textField_quantity.getText() + " " + textField_unit.getText() + " - " + textField_size.getText();
            	
           
            	Food food_add = new Food(textField_nom.getText(), Float.parseFloat(textField_quantity.getText()), textField_unit.getText(), textField_type.getText(),textField_size.getText());
       
                fridgeFoods.add(food_add);
                Refrigerateur_withListUI_control.updateAllListViews(fridgeFoods, autresList, viandesList, bouteillesList, fruitsLegumesList);
                
                try {
    				Refrigerateur_withListUI_control.writeFoodsToXml(fridgeFoods, filename);
    			} catch (ParserConfigurationException | TransformerException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}           	
            	           	
			    stageAdd.close();	
	        });
            
            stageAdd.setScene(scene_add);
            stageAdd.show();            
         
        	
        });
        
        HBox box_button2 = new HBox(10,ajouterButton,retournerButton); 
        box_button2.setAlignment(Pos.CENTER);

        // Create a GridPane to hold the rectangle and VBox
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(title, 0, 0);
        gridPane.add(vbox, 0, 1);
        gridPane.add(box_button2, 0, 2);
        gridPane.add(sauterButton, 0, 3);
        GridPane.setHalignment(box_button2, HPos.CENTER);
        GridPane.setHalignment(sauterButton, HPos.CENTER);
     

        // Create a Scene and show it
        Scene scene = new Scene(gridPane);
        return scene;
    }
    
    
    
    private static VBox createListBox(String categoryName, ListView<String> listView) {
        Label label = new Label(categoryName);
        label.setFont(Font.font(16));
        VBox vbox = new VBox(label, listView);
        return vbox;
    }
    
  //打开一个显示加号 减号button的窗口，点击减号会删去选中的那一行
    // ouvre une fenêtre avec un bouton plus moins, un clic sur le bouton moins efface la ligne sélectionnée
      private static void openQuantityWindow(String itemName, ListView<String> listView) {
          Stage stage = new Stage();
          stage.initModality(Modality.APPLICATION_MODAL);
          stage.setTitle("Choose quantity");

          // create controls
          Label label = new Label(itemName);
          Button addButton = new Button("+");
          Button subButton = new Button("-");

          // 点击add按钮后跳出一个界面 可以添加新的东西
       // En cliquant sur le bouton d'ajout, vous accédez à un écran où vous pouvez ajouter de nouveaux éléments.
          addButton.setOnAction(e -> {
          	Stage stageAdd = new Stage();
          	stageAdd.setHeight(350);
            stageAdd.setWidth(400);
          	
          	Label label_nom = new Label("Saisir le nom");
          	TextField textField_nom = new TextField();
              textField_nom.setPromptText("Veuillez entrer le nom de food");
              Label label_quantity = new Label("Saisir quantity");
              TextField textField_quantity = new TextField();
              textField_quantity.setPromptText("quantity: ");
              
              Label label_unit = new Label("Saisir le unit (g,l,unite)");
              TextField textField_unit = new TextField();
              textField_unit.setPromptText("(g,l,unite)");
              
              Label label_size = new Label("Saisir le size");
              TextField textField_size = new TextField();
              textField_size.setPromptText("size: (n*n)");
              VBox vbox_addwindow = new VBox();
              Button Button_save = new Button("save");
              
              vbox_addwindow.getChildren().addAll(label_nom,textField_nom,label_quantity,textField_quantity,label_unit,textField_unit,label_size,textField_size,Button_save);
              
              Scene scene_add = new Scene(vbox_addwindow,800,600);
              
              Button_save.setOnAction(event -> {
  	          
              	String itemString = textField_nom.getText() + " - " + textField_quantity.getText() + " " + textField_unit.getText() + " - " + textField_size.getText();
              	
              	listView.getItems().add(itemString);
              	String item_type = obtenir_type(listView);
              	Food food_add = new Food(textField_nom.getText(), Float.parseFloat(textField_quantity.getText()), textField_unit.getText(), item_type,textField_size.getText());
         
                  fridgeFoods.add(food_add);
                  Refrigerateur_withListUI_control.updateAllListViews(fridgeFoods, autresList, viandesList, bouteillesList, fruitsLegumesList);
                  
                  try {
      				Refrigerateur_withListUI_control.writeFoodsToXml(fridgeFoods, filename);
      			} catch (ParserConfigurationException | TransformerException e1) {
      				// TODO Auto-generated catch block
      				e1.printStackTrace();
      			}
//                  missingFoodsList.getItems().add(ingredientName + " : " + missingQuantity + " " + ingredientUnit);
              	
              	
              	
  			    stageAdd.close();	
  	        });
              
              stageAdd.setScene(scene_add);
              stageAdd.show();            
              stage.close();
          });

          
          //点击减号后会删去选中的东西
       // En cliquant sur le signe moins, vous supprimez l'élément sélectionné.
          subButton.setOnAction(e -> {
        		listView.getItems().remove(itemName);
            	String[] itemArray = itemName.split(" - ");
            	String name = itemArray[0];
            	for (Food food : fridgeFoods) {
                    if (food.getName().equals(name)) {
                        fridgeFoods.remove(food);
                        break;
                    }
                }
            	
            	try {
    				Refrigerateur_withListUI_control.writeFoodsToXml(fridgeFoods, filename);
    			} catch (ParserConfigurationException | TransformerException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}    
            	
            	
                stage.close();
          });

          // create layout
          VBox vbox = new VBox(10, label, addButton, subButton);
          vbox.setAlignment(Pos.CENTER);
          Scene scene = new Scene(vbox, 200, 200);
          stage.setScene(scene);
//         stage.showAndWait();
          stage.show();
      }
      
      
      public static String obtenir_type (ListView<String> List) {
  		   if (List == autresList) {return "Autre";} else if (List == viandesList) {return "Viande";} else if (List == bouteillesList) {return "Bouteilles";}
  		   else {return "FL";}
  		   
  	   }

   
}
    