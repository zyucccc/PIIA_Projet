package control;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


import entite.Food;

import javafx.scene.control.ListView;



public class Refrigerateur_withListUI_control {
	
	
	
	 public static void addFoodsToListView_missing(List<Food> foods, ListView<String> listView) {
	        for (Food food : foods) {
	            String itemString = food.getName() + " - " + food.getQuantity() + " " + food.getUnit() + " - " + food.getSize();
	            listView.getItems().add(itemString);
	        }
	    }
	 
	 public static void addFoodsToListViews(List<Food> foods, ListView<String> autresList, ListView<String> viandesList, ListView<String> bouteillesList, ListView<String> fruitsLegumesList) {
		    for (Food food : foods) {
		        String itemString = food.getName() + " - " + food.getQuantity() + " " + food.getUnit() + " - " + food.getSize();
		        switch (food.getType()) {
		            case "Autre":
		                autresList.getItems().add(itemString);
		                break;
		            case "Viande":
		                viandesList.getItems().add(itemString);
		                break;
		            case "Bouteilles":
		                bouteillesList.getItems().add(itemString);
		                break;
		            case "FL":
		                fruitsLegumesList.getItems().add(itemString);
		                break;
		            default:
		                // do nothing or handle unrecognized food types
		                break;
		        }
		    }
		}
	 
	 public static void clearAllListViews(ListView<String> autresList, ListView<String> viandesList, ListView<String> bouteillesList, ListView<String> fruitsLegumesList) {
		 autresList.getItems().clear();
		 viandesList.getItems().clear();
		 bouteillesList.getItems().clear();
		 fruitsLegumesList.getItems().clear();
	 }
	 
	 
	 public static void updateAllListViews(List<Food> foods, ListView<String> autresList, ListView<String> viandesList, ListView<String> bouteillesList, ListView<String> fruitsLegumesList) {
		 clearAllListViews(autresList,viandesList,bouteillesList,fruitsLegumesList);
		 List_control.mergeFoodList(foods);
		 addFoodsToListViews(foods,autresList,viandesList,bouteillesList,fruitsLegumesList);
	 }
	 
	 
	 
	 public static void writeFoodsToXml(List<Food> foods, String filePath) throws ParserConfigurationException, TransformerException {
		    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		    Document doc = docBuilder.newDocument();

		    // 创建root元素
		    Element rootElement = doc.createElement("foods");
		    rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		    rootElement.setAttribute("xsi:noNamespaceSchemaLocation", "foods.xsd");
		    doc.appendChild(rootElement);

		    // 添加food元素
		    for (Food food : foods) {
		        Element foodElement = doc.createElement("food");
		        foodElement.setAttribute("name", food.getName());
		        foodElement.setAttribute("quantity", Float.toString(food.getQuantity()));
		        foodElement.setAttribute("unit", food.getUnit());
		        foodElement.setAttribute("type", food.getType());
		        foodElement.setAttribute("size", food.getSize());
		        rootElement.appendChild(foodElement);
		    }

		    // 保存XML文件
		    TransformerFactory transformerFactory = TransformerFactory.newInstance();
		    Transformer transformer = transformerFactory.newTransformer();
		    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		    DOMSource source = new DOMSource(doc);
		    StreamResult result = new StreamResult(new File(filePath));
		    transformer.transform(source, result);
		}
       
	  
	 

	 
	 

	 
	 
	 
	 
	 
}
