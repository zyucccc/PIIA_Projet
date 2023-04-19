package control;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import entite.Food;

public class List_control {
	
	//food解析 Foods.xml 文件并将食物存储在一个 List<Food> 中
    public static List<Food> loadFoods(String filename) {
        List<Food> foods = new ArrayList<>();

        try {
            File inputFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList foodList = doc.getElementsByTagName("food");

            for (int i = 0; i < foodList.getLength(); i++) {
                Node foodNode = foodList.item(i);
                if (foodNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element foodElement = (Element) foodNode;
                    String name = foodElement.getAttribute("name");
                    float quantity = Float.parseFloat(foodElement.getAttribute("quantity"));
                    String unit = foodElement.getAttribute("unit");
                    String type = foodElement.getAttribute("type");
                    String size = foodElement.getAttribute("size");
                    //调food class
                    Food food = new Food(name, quantity, unit, type, size);
                    foods.add(food);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return foods;
    }
    
    public static boolean updateFoodList(List<Food> list1, List<Food> list2, boolean flag) {

        for (int i = 0; i < list2.size(); i++) {
            boolean isMatched = false;
            for (int j = 0; j < list1.size(); j++) {
                if (list1.get(j).getName().equals(list2.get(i).getName())) {
                    isMatched = true;
                    if (list1.get(j).getQuantity() > list2.get(i).getQuantity()) {
                        float newQuantity = list1.get(j).getQuantity() - list2.get(i).getQuantity();
                        list1.get(j).setQuantity(newQuantity);
                    } else if (list1.get(j).getQuantity() == list2.get(i).getQuantity()) {
                        list1.remove(j);
                    } else {
                        flag = false;
                        break;
                    }
                }
            }
            if (!isMatched) {
                flag = false;
                break;
            }
        }
        if (!flag) {
            System.out.println("Could not update food list");
            return false;
        }
        System.out.println("Updated food list successfully");
        return true;
    }
    
  //Fusionner toutes les données portant le même nom, la nouvelle quantité est la somme des quantités précédentes.
    public static void mergeFoodList(List<Food> foodList) {
        Map<String, Food> foodMap = new HashMap<>();
        for (Food food : foodList) {
            if (foodMap.containsKey(food.getName())) {
                // 如果已经有相同的name，则更新quantity
                Food existingFood = foodMap.get(food.getName());
                existingFood.setQuantity(existingFood.getQuantity() + food.getQuantity());
                foodMap.put(food.getName(), existingFood); // 将更新后的对象再次放回Map中
            } else {
                // 如果没有相同的name，则添加到map中
                foodMap.put(food.getName(), food);
            }
        }
        // 清空原来的foodList
        foodList.clear();
        // 将合并后的foodMap中的值添加回foodList中
        for (Food food : foodMap.values()) {
            foodList.add(food);
        }
    }


    
  //menu页做饭
    public static void updateFoodXml(List<Food> requiredFoods) {
        try {
            // 打开并解析 Foods.xml 文件
            File inputFile = new File("src/main/Foods.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // 获取 Foods.xml 中所有的食物节点
            NodeList foodsList = doc.getElementsByTagName("food");

            // 遍历所有需要更新数量的食物
            for (Food requiredFood : requiredFoods) {
                // 遍历 Foods.xml 中的食物节点
                for (int i = 0; i < foodsList.getLength(); i++) {
                    Node foodNode = foodsList.item(i);
                    if (foodNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element foodElement = (Element) foodNode;
                        String foodName = foodElement.getAttribute("name");
                        
                        // 如果找到匹配的食物名称
                        if (foodName.equalsIgnoreCase(requiredFood.getName())) {
                            // 更新食物数量
                        	float currentQuantity = Float.parseFloat(foodElement.getAttribute("quantity"));
                            float updatedQuantity = currentQuantity - requiredFood.getQuantity();

                            // 如果食物的数量与所需的数量相等，则删除该食物节点
                        	 if (currentQuantity == requiredFood.getQuantity()) {
                                 foodNode.getParentNode().removeChild(foodNode);
                             } else {
                                 // 否则，更新食物数量
                                 foodElement.setAttribute("quantity", Float.toString(updatedQuantity));
                             }
                            break;
                        }
                    }
                }
            }

            // 将更新后的 Foods.xml 写回文件
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src/main/Foods.xml"));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
  //转换将类似1/2化成0.5啥的
    public static float parseQuantity(String quantityString) {
        if (quantityString.contains("/")) {
            String[] parts = quantityString.split("/");
            float numerator = Float.parseFloat(parts[0]);
            float denominator = Float.parseFloat(parts[1]);
            return numerator / denominator;
        } else {
            return Float.parseFloat(quantityString);
        }
    }


}
