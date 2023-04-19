package main;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Bienvenue extends Application {

    public void start(Stage primaryStage) {


        // 创建两个按钮
        Label label = new Label("Bienvenue!Veuillez choisir votre type de refrigerateur");
        Label label2 = new Label("Bienvenue!Veuillez entrer le nombre");
        Button etudiantButton = new Button("etudiant");
        Button familialButton = new Button("familial");
        Button confirmerButton = new Button("valide");
        
        TextField textField = new TextField();
        textField.setPromptText("Veuillez entrer le nombre de utilisateur");
        
        
        HBox hbox = new HBox();
		hbox.getChildren().addAll(etudiantButton,familialButton);
		
		VBox vbox = new VBox();
		vbox.getChildren().addAll(label,hbox);
		VBox vbox_nbpersonne = new VBox();
		vbox_nbpersonne.getChildren().addAll(label2,textField,confirmerButton);

		//布局
		hbox.setAlignment(Pos.CENTER); 
		vbox.setAlignment(Pos.CENTER); 
		vbox_nbpersonne.setAlignment(Pos.CENTER); 
	    hbox.setSpacing(20);
	    vbox.setSpacing(20);
	    vbox_nbpersonne.setSpacing(20);
		
	    
	    Scene scene = new Scene(vbox,800,600);
	    Scene nbpersonne = new Scene(vbox_nbpersonne,800,600);
	    
	    
	    etudiantButton.setOnAction(event -> {
	    	primaryStage.setScene(nbpersonne);

        });
	    
	 // 将按钮的事件处理程序设置为在单击时显示新场景
	    familialButton.setOnAction(event -> {
            primaryStage.setScene(nbpersonne);
        });
		confirmerButton.setOnAction(event -> {
			int nbUtilisateurs = Integer.parseInt(textField.getText());

            // 创建场景3并传递用户数量
            Scene scene3 = RecetteUI.createScene(nbUtilisateurs);

            // 切换到场景3
            primaryStage.setScene(scene3);
        });
	    
	    primaryStage.setScene(scene);
	    primaryStage.show();
	    
    }

    public static void main(String[] args) {
        launch(args);
    }
}
