package main;
import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.scene.text.Text;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import entite.Food;


public class FridgeVisualization2 extends Application {

	 private static Color otherDefaultColor = Color.BLUE;
	 private static Color drinksDefaultColor = Color.YELLOW;
	 private static Color meatDefaultColor = Color.RED;
	 private static Color veggiesDefaultColor = Color.GREEN;
	private static boolean[][] fridge = new boolean[10][15];
	
	public static void main(String[] args) {
	    launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
        for (int i = 0; i < fridge.length; i++) {
            for (int j = 0; j < fridge[i].length; j++) {
                fridge[i][j] = false;
            }
        }
	    Scene scene = createScene();

	    primaryStage.setScene(scene);
	    primaryStage.show();
	}
    //根据食物类型创建一个函数来计算食物的初始位置：
    private static int[] getInitialPosition(String type) {
        int[] position = new int[4];


        switch (type) {
            case "FL":
                position[0] = 0;
                position[1] = 8;
                position[2]=9;
                position[3]=14;
                break;
            case "Bouteilles":
                position[0] = 8;
                position[1] = 0;
                position[2]=9;
                position[3]=7;
                break;
            case "Viande":
                position[0] = 0;
                position[1] = 5;
                position[2]=7;
                position[3]=7;
                break;
            case "Autre":
            default:
                position[0] = 0;
                position[1] = 0;
                position[2]=7;
                position[3]=4;
                break;
        }

        return position;
    }
    //创建一个函数来在冰箱中找到一个合适的位置以放置食物，这个位置必须是空的，同时保持在区域的边界内。这个函数还会将占用的单元格设置为已占用
    private static int[] findFreePosition(boolean[][] fridge, String type, int width, int height) {
        int[] initialPosition = getInitialPosition(type);
        int[] position = new int[2];

        outerLoop:
        for (int i = initialPosition[1]; i < 15; i++) {
            for (int j = initialPosition[0]; j < 10; j++) {
                boolean canFit = true;


                for (int y = 0; y < height && canFit; y++) {
                    for (int x = 0; x < width && canFit; x++) {
                    	
                        if (j + x >= 10 || i + y >= 15 || fridge[j + x][i + y]) {
                            canFit = false;
                        }
                    }
                }

                if (canFit) {
                    position[0] = j;
                    position[1] = i;

                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            fridge[j + x][i + y] = true;
                        }
                    }

                    break outerLoop;
                }
            }
        }

        return position;
    }
    
    public static Scene createScene() {
        for (int i = 0; i < fridge.length; i++) {
            for (int j = 0; j < fridge[i].length; j++) {
                fridge[i][j] = false;
            }
        }
    	HBox hbox = new HBox();
        GridPane grid = new GridPane();
     // 创建添加食物按钮
        //Button addButton = new Button("add food");
        //GridPane.setConstraints(addButton, 0, 15, 10, 1);
        hbox.getChildren().add(grid);

        // 设置按钮事件处理程序
//        addButton.setOnAction(e -> {
//        	handleButtonClick(addButton);
// 
//        });
//
   
    
        for (int i = 0; i < 10; i++) {
            ColumnConstraints column = new ColumnConstraints(50);
            grid.getColumnConstraints().add(column);
        }
//
        for (int i = 0; i < 15; i++) {
            RowConstraints row = new RowConstraints(50);
            grid.getRowConstraints().add(row);
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 15; j++) {
                Color color;
                if (i < 8 && j < 5) {
                    color = otherDefaultColor;
                } else if (i >= 8 && j < 8) {
                    color = drinksDefaultColor;
                } else if (i < 8 && j >= 5 && j < 8) {
                    color = meatDefaultColor;
                } else {
                    color = veggiesDefaultColor;
                }
                Rectangle rect = createRectangle(color, 0.5);
                grid.add(rect, i, j);
            }
        }
        List<Food> foods = parseFoodsXML("src/main/Foods.xml");



        for (Food food : foods) {
            String type = food.getType();
            String size = food.getSize();
            String[] sizeParts = size.split("\\*");
            int width = Integer.parseInt(sizeParts[0]);
            int height = Integer.parseInt(sizeParts[1]);

            Color color;
            double saturation = 1;

            switch (type) {
                case "FL":
                    color = veggiesDefaultColor;
                    break;
                case "Bouteilles":
                    color = drinksDefaultColor;
                    break;
                case "Viande":
                    color = meatDefaultColor;
                    break;
                case "Autre":
                default:
                    color = otherDefaultColor;
                    break;
            }

            int[] position = findFreePosition(fridge, type, width, height);
            int column = position[0];
            int row = position[1];

//            for (int i = 0; i < width; i++) {
//                for (int j = 0; j < height; j++) {
//
//                    Rectangle rect = createRectangle(color, saturation);
//                    grid.add(rect, column + i, row + j);
//                }
//            }
//            for (int i = 0; i < width; i++) {
//                for (int j = 0; j < height; j++) {
//                    Rectangle rect = createRectangle(color, saturation);
//                    if (i == 0 && j == 0) {
//                        Text foodName = new Text(food.getName());
//                        StackPane stack = new StackPane(); // 创建一个StackPane来包含矩形和文本
//                        GridPane.setConstraints(stack, 0, 0, width, height);
//                        stack.getChildren().addAll(rect, foodName); // 将矩形和文本添加到StackPane中
//                        grid.add(stack, column + i, row + j); // 将StackPane添加到网格中
//                        
//                    } else {
//                        grid.add(rect, column + i, row + j);
//                    }
//                }
//            }
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                	Rectangle rect = createRectangle(color, saturation);
                    rect.setStroke(Color.TRANSPARENT);
                    Text foodName = new Text(food.getName()); // 添加文本
                    StackPane stack = new StackPane();
                    stack.getChildren().addAll(rect, foodName); // 将矩形和文本添加到StackPane中
                    stack.setOnMouseClicked(e -> {
                        // 处理点击事件
                    	System.out.println(" clicked..."+food.getName());
                    });
                    grid.add(stack, column + i, row + j);
                }
            }
       }

        StackPane emptyPane = new StackPane();
        emptyPane.setStyle("-fx-background-color: transparent;");
        emptyPane.setOnMouseClicked(e -> {
            // 处理点击空白区域的事件
            System.out.println("Clicked empty area.");
            handleButtonClick(emptyPane);
        });
        grid.add(emptyPane, 0, 0, 10, 15);
        Scene scene = new Scene(hbox, 500, 750);
        return scene;
    }

    private static Rectangle createRectangle(Color color, double saturation) {
        color = color.deriveColor(0, saturation, 1, 1);
        Rectangle rect = new Rectangle();

        // 直接设置矩形的大小为 50
        rect.setWidth(50);
        rect.setHeight(50);

        rect.setFill(color);
        rect.setStroke(Color.WHITE);
        rect.setStrokeWidth(1);
        return rect;
    }
    
    private static List<Food> parseFoodsXML(String xmlPath) {
        List<Food> foods = new ArrayList<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(xmlPath));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("food");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String name = element.getAttribute("name");
                    float quantity = Float.parseFloat(element.getAttribute("quantity"));
                    String unit = element.getAttribute("unit");
                    String type = element.getAttribute("type");
                    String size = element.getAttribute("size");

                    Food food = new Food(name, quantity, unit, type, size);
                    foods.add(food);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return foods;
    }
    private static void handleButtonClick(StackPane bu) {
       	TextInputDialog dialog = new TextInputDialog();
    	dialog.setTitle("ajouter de la nourriture");
    	dialog.setHeaderText(null);
    	dialog.setContentText("Veuillez entrer les informations sur l'aliment：");

    	// 创建五个 TextField 组件
    	TextField nameField = new TextField();
    	nameField.setPromptText("name");
    	TextField quantityField = new TextField();
    	quantityField.setPromptText("quantity");
    	TextField sizeField = new TextField();
    	sizeField.setPromptText("size(n*n)");
    	TextField typeField = new TextField();
    	typeField.setPromptText("type(FL/Bouteilles/Autre/Viande)");
    	TextField unitField = new TextField();
    	unitField.setPromptText("unit(terminer par .)");

    	// 将五个 TextField 组件添加到一个 VBox 容器中
    	VBox container = new VBox();
    	container.getChildren().addAll(nameField, quantityField, sizeField, typeField, unitField);
    	dialog.getDialogPane().setContent(container);

    	// 设置对话框的返回结果
    	dialog.setResultConverter(buttonType -> {
    	    if (buttonType == ButtonType.OK) {
    	        String name = nameField.getText();
    	        float quantity = Float.parseFloat(quantityField.getText());
    	        String size = sizeField.getText();
    	        String type = typeField.getText();
    	        String unit = unitField.getText();
    	        
    	        if (unit.trim().isEmpty()) {
    	            unit = " "; // 如果用户没有输入，则将 unit 设为一个空字符串
    	        }
    	        return name + " " + quantity + " " + size + " " + type + " " + unit;
    	    }
    	    return null;
    	});
    	

    	Optional<String> result = dialog.showAndWait();
    	result.ifPresent(input -> {
    	    String[] parts = input.split("\\s");
    	    if (!parts[2].matches("\\d+\\*\\d+")) {
    	        // 输入格式不正确，弹出错误对话框
    	        Alert alert = new Alert(AlertType.ERROR);
    	        alert.setTitle("错误");
    	        alert.setHeaderText(null);
    	        alert.setContentText("size格式不正确，请重新输入！"+parts[3]);
    	        alert.showAndWait();
    	    } else if(!parts[3].equals("Autre") && !parts[3].equals("Viande") && !parts[3].equals("FL")&&!parts[3].equals("Bouteilles")){
    	    	Alert alert = new Alert(AlertType.ERROR);
    	        alert.setTitle("错误");
    	        alert.setHeaderText(null);
    	        alert.setContentText("type格式不正确，请重新输入！"+parts[3]);
    	        alert.showAndWait();
    	    }
    	else {
    	        String name = parts[0];
    	        float quantity = Float.parseFloat(parts[1]);
    	        String size = parts[2];
    	        String type = parts[3];
    	        String unit = parts[4];

                // 将新的食物信息添加到 Foods.xml 文件中
                try {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(new File("src/main/Foods.xml"));

                    Element root = doc.getDocumentElement();

                    Element food = doc.createElement("food");
                    food.setAttribute("name", name);
                    food.setAttribute("quantity", Float.toString(quantity));
                    food.setAttribute("size", size);
                    food.setAttribute("type", type);
                    food.setAttribute("unit", unit);

                    root.appendChild(food);

                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    DOMSource source = new DOMSource(doc);
                    StreamResult resultStream = new StreamResult(new File("src/main/Foods.xml"));
                    transformer.transform(source, resultStream);
                    
                    for (int i = 0; i < fridge.length; i++) {
                        for (int j = 0; j < fridge[i].length; j++) {
                            fridge[i][j] = false;
                        }
                    }
                    Scene currentScene = bu.getScene();
                    Stage window = (Stage) currentScene.getWindow();
                    window.hide();
                    window.setScene(createScene());
                    window.show();


                } catch (Exception ex) {
                    ex.printStackTrace();
                    // 弹出错误对话框
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("错误");
                    alert.setHeaderText(null);
                    alert.setContentText("无法添加食物，请重试！");
                    alert.showAndWait();
                }
            }
        });
    }
    //
//    private boolean checkBounds(int x, int y, int width, int height) {
//        for (int i = x - 1; i < x + width + 1; i++) {
//            for (int j = y - 1; j < y + height + 1; j++) {
//                if (i >= 0 && i < 10 && j >= 0 && j < 15 && fridge[i][j]) {
//                    // 这个单元格已经被占用了
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
  
}
