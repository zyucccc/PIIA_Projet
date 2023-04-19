package main;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//
import entite.Food;
import control.List_control;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;




public class AllMenuUI extends Application {
	//id
    private static int currentIndex = 0;
    //菜单列表
    private static NodeList recipeList;
    private static VBox recipeBox;
    private static List<Food> fridgeFoods;
    private static VBox changeBox;
    
    //没啥用,方便测试
    public static void main(String[] args) {
        launch(args);
    }
    public void start(Stage primaryStage) {
        Scene scene = createScene(1);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //


    //主要场景
    public static Scene createScene(int nb) {
    	currentIndex = nb-1;

        // 创建界面
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        HBox buttonsBox = new HBox();
        buttonsBox.setAlignment(Pos.TOP_RIGHT);
        buttonsBox.setSpacing(10);

        // 创建一个新的 VBox 用于存储菜谱信息
        recipeBox = new VBox();
        recipeBox.setAlignment(Pos.CENTER);
        changeBox = new VBox();
        changeBox.setAlignment(Pos.TOP_RIGHT);
        
     // 在 createScene 方法中调用 loadFoods 方法并将结果存储在类成员变量中
        fridgeFoods = List_control.loadFoods("src/main/Foods.xml");
        
        // 添加左右箭头按钮
        Button prevButton = new Button("<");
        prevButton.setOnAction(event -> {
        	System.out.println("Prev button clicked...");
            if (currentIndex > 0) {

                currentIndex--;
                
            }else {
            	currentIndex=recipeList.getLength() - 1;
            }
            showRecipe(currentIndex);
        });

        Button nextButton = new Button(">");
        nextButton.setOnAction(event -> {
        	System.out.println("Next button clicked...");
            if (currentIndex < recipeList.getLength() - 1) {
                currentIndex++;
                
            }else {
            	currentIndex=0;
            }
            showRecipe(currentIndex);
        });

        // 将左右箭头按钮和 recipeBox 添加到 vbox hbox
        buttonsBox.getChildren().addAll(changeBox,prevButton, nextButton);
        vbox.getChildren().addAll(recipeBox);
        

        // 读取XML文件
        try {
            File inputFile = new File("src/main/Menu.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // 获取所有菜谱
            recipeList = doc.getElementsByTagName("recipe");
            System.out.println(currentIndex);
            showRecipe(currentIndex);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 返回按钮
        Button backButton = new Button("Retour");
        backButton.setOnAction(event -> {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
        });

        HBox hbox = new HBox();
        hbox.getChildren().add(backButton);
        hbox.setAlignment(Pos.TOP_RIGHT);
        buttonsBox.getChildren().add(backButton);

        // 添加组件
        StackPane root = new StackPane();
        root.getChildren().addAll(vbox, buttonsBox);

        Scene scene = new Scene(root, 800, 600);
        return scene;
    }
    
    
    

    
    
    //显示某一个菜单
    private static void showRecipe(int index) {
        Node nNode = recipeList.item(index);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) nNode;
            String name = eElement.getElementsByTagName("name").item(0).getTextContent();
            Label nameLabel = new Label(name+":");
            nameLabel.setFont(Font.font("System", FontWeight.BOLD, 24)); // 设置字体大小和样式
            changeBox.getChildren().clear();
            recipeBox.getChildren().clear(); // 先清空整个 recipeBox
            recipeBox.getChildren().add(nameLabel);
         // 在 nameLabel 和 ingredientLabel 之间添加垂直间距
            Separator separator = new Separator(Orientation.HORIZONTAL);
            recipeBox.getChildren().add(separator);
            boolean allEnough = true;
         // 创建一个都缺了啥食物的ListView            
            List<Food> FoodsList = new ArrayList<>();
            List<Food> missingFoodsList1 = new ArrayList<>();
            ListView<String> missingFoodsList = new ListView<>();
            // 获取该菜谱的所有食材
            NodeList ingredientsList = eElement.getElementsByTagName("ingredient");
            for (int i = 0; i < ingredientsList.getLength(); i++) {
                Node ingredientNode = ingredientsList.item(i);
                if (ingredientNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element ingredientElement = (Element) ingredientNode;
                    String ingredientName = ingredientElement.getAttribute("name");
                    //调用下面转换的方法
                    float ingredientQuantity = List_control.parseQuantity(ingredientElement.getAttribute("quantity"));
                    String ingredientType = ingredientElement.getAttribute("type");
                    String ingredientUnit = ingredientElement.getAttribute("unit");
                    String ingredientSize = ingredientElement.getAttribute("size");

                    Food food_ingredient = new Food(ingredientName, ingredientQuantity, ingredientUnit, ingredientType,ingredientSize);
                     FoodsList.add(food_ingredient);
                    Label ingredientLabel = new Label("- " + ingredientName + " : " + ingredientQuantity + " " + ingredientUnit);

                    // 检查食材是否足够
                    Food foundFood = null;

                 // 检查食材是否足够
                 boolean isEnough = false;
                 for (Food food : fridgeFoods) {
                     if (food.getName().equalsIgnoreCase(ingredientName)
                             && food.getUnit().equalsIgnoreCase(ingredientUnit)) {

                         // 记录找到的食物
                         foundFood = food;
                         
                         if (food.getQuantity() >= ingredientQuantity) {
                             isEnough = true;
                             break;
                         }
                     }
                 }

                 if (!isEnough) {
                	 allEnough=false;
                     ingredientLabel.setTextFill(Color.RED);

                     
                  // 将缺失的食物添加到 missingFoodsList
                     float missingQuantity = (foundFood != null) ? (ingredientQuantity - foundFood.getQuantity()) : ingredientQuantity;

                     // 如果 ingredientUnit 为空且 ingredientQuantity 不是整数，则对 missingQuantity 向上取整
                     if (ingredientUnit.isEmpty() && ingredientQuantity % 1 != 0) {
                         missingQuantity = (float) Math.ceil(missingQuantity);
                     }
                     Food missing_food = new Food(ingredientName, ingredientQuantity, ingredientUnit, ingredientType,ingredientSize);
                     missingFoodsList1.add(missing_food);
                     missingFoodsList.getItems().add(ingredientName + " : " + missingQuantity + " " + ingredientUnit);
                 }
                    ingredientLabel.setFont(Font.font("System", 15)); 

                    recipeBox.getChildren().add(ingredientLabel);
                }
                
            }
            Button changeButton;
            if(allEnough){
            	changeButton = new Button("Cook");
                changeButton.setOnAction(event -> {
                    System.out.println("Cook button clicked...");
                    // Update Food.xml with used ingredients
                    List_control.updateFoodXml(FoodsList);
                    // 做饭
                    Stage stage = (Stage) changeButton.getScene().getWindow();
                    stage.close();
                });
                
            }else {
            	changeButton = new Button("generer la list");
                changeButton.setOnAction(event -> {
                    System.out.println("generer button clicked..."+missingFoodsList);
                    // 跳转到另一个页面
                    Stage stage = new Stage();
    	            stage.setHeight(600);
    	            stage.setWidth(810);
    	            
    	            FoodsList.forEach(System.out::println);
    	            stage.setScene(Refrigerateur_withListUI.createScene(FoodsList,missingFoodsList1));
//    	            stage.setScene(Refrigerateur_withList.createScene(missingFoodsList));
//    	            stage.setScene(RefrigerateurUI.createScene());
    			    stage.show();	
                });
            }
            changeBox.getChildren().add(changeButton);
            System.out.println("Current Index: " + index);
            System.out.println("Recipe Name: " + name);
        }
    }
    

}