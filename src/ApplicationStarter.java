import java.io.File;

import ProjectAnalyser.ProjectStructureAnalyser;
import ProjectAnalyser.ProjectStructureContainer;
import ProjectStructureGraphBuilder.CellType;
import ProjectStructureGraphBuilder.Graph;
import ProjectStructureGraphBuilder.Layout;
import ProjectStructureGraphBuilder.Model;
import ProjectStructureGraphBuilder.RandomLayout;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ApplicationStarter extends Application {
	Graph graph = new Graph();
	Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage=primaryStage;
		Button btn = new Button();
		btn.setText("Select Project Root");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				DirectoryChooser fileChooser = new DirectoryChooser();
				fileChooser.setTitle("Select Project Root");
				File selectedDirectory = fileChooser.showDialog(primaryStage);
				if (selectedDirectory == null) {
					// labelSelectedDirectory.setText("No Directory selected");
				} else {
					ProjectStructureAnalyser psa = new ProjectStructureAnalyser(selectedDirectory);
					graph = new Graph();
					addGraphComponents(psa.getPsc());
					createGraph(graph);
				}

			}
		});

		StackPane root = new StackPane();
		root.getChildren().add(btn);

		Scene scene = new Scene(root, 300, 250);

		primaryStage.setTitle("Hello World!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		System.err.println("Start application");
		launch(args);
	}
	
	 private void addGraphComponents(ProjectStructureContainer psc) {
		 Model model = graph.getModel();

	        graph.beginUpdate();
        
	        for(String key:psc.getDepenciesMap().keySet()) {
	        	model.addCell(key, CellType.TITLEDPANE);
	        }
	        for(String key:psc.getReferencesMap().keySet()) {
	        	model.addCell(key, CellType.TITLEDPANE);
	        }
	        
	        for(String key:psc.getDepenciesMap().keySet()) {
	        	for(String dep: psc.getDepenciesMap().get(key)) {
	        		model.addEdge(key,dep);
	        		for(String root:psc.getReferencesMap().get(dep)) {
	        			model.addEdge(dep, root);
	        		}
	        	}
	        	
	        }

	        graph.endUpdate();

	 }
	
	public void createGraph(Graph graph) {
	       BorderPane root = new BorderPane();

	        root.setCenter(graph.getScrollPane());

	        Scene scene = new Scene(root, 1024, 768);
	      //  scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	        primaryStage.setScene(scene);
	        primaryStage.show();

	        Layout layout = new RandomLayout(graph);
	        layout.execute();
	}

}
