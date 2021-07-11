package sos;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class Myweb extends Application {

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        BorderPane borderPane = new BorderPane();
        TabPane tb=new TabPane();
        tb.setPrefSize(Screen.getPrimary().getBounds().getWidth(),Screen.getPrimary().getBounds().getHeight());
        tb.setSide(Side.TOP);
        Createfirsttab(tb);
        final Tab newtab = new Tab();
        newtab.setText(" + ");
        newtab.setClosable(false);
        tb.getTabs().addAll(newtab);

        ScrollPane sp = new ScrollPane();

        tb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            int as = 0;
            @Override
            public void changed(ObservableValue<? extends Tab> observable,
                                Tab oldSelectedTab, Tab newSelectedTab) {
                if (newSelectedTab == newtab) {
                    Tab tab = new Tab();
                    tab.setText("        ");
                    //WebView - to display, browse web pages.
                    WebView webView = new WebView();
                    webView.setPrefSize(Screen.getPrimary().getBounds().getWidth(),Screen.getPrimary().getBounds().getHeight());
                    final WebEngine webEngine = webView.getEngine();
                    //webEngine.load(DEFAULT_URL);
                    final TextField urlField = new TextField("http://");
                    Button goButton = new Button("<");
                    Button bButton = new Button(">");
                    webEngine.locationProperty().addListener(new ChangeListener<String>() {
                        @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            urlField.setText(newValue);
                        }
                    });
                    webEngine.getLoadWorker().stateProperty().addListener(
                            (ov, oldState, newState) -> {
                                if (newState == Worker.State.SUCCEEDED) {
                                    tab.setText(webEngine.getTitle());
                                    goButton.setDisable(webEngine.getHistory().getCurrentIndex() - 1 < 0);
                                    bButton.setDisable(as <= 0);
                                }else if(newState == Worker.State.FAILED){
                                    if(newState == Worker.State.RUNNING)
                                        return;
                                    else {
                                        try {
                                            URL urls = new URL("http://www.google.com");
                                            URLConnection connection = urls.openConnection();
                                            connection.connect();
                                            webEngine.load(getClass().getResource("cantfind.html").toString());
                                            tab.setText("Page Does Not Found");
                                            urlField.setText("Page Does Not Found");
                                        } catch (MalformedURLException e) {
                                            webEngine.load(getClass().getResource("nointernet.html").toString());
                                            tab.setText("No internet");
                                            urlField.setText("No internet");
                                        } catch (IOException e) {
                                            webEngine.load(getClass().getResource("nointernet.html").toString());
                                            tab.setText("No internet");
                                            urlField.setText("No internet");
                                        }
                                    }
                                }
                            });
                    //Action definition for the Button Go.
                    EventHandler<ActionEvent> goAction = new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent event) {
                            webEngine.load(urlField.getText().contains("://")
                                    ? urlField.getText()
                                    : "http://" + urlField.getText());
                        }
                    };
                    EventHandler<ActionEvent> his = new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent event) {
                            as++;
                            if(webEngine.getHistory().getCurrentIndex() -1 < 0){
                                goButton.setDisable(true);
                            }else {
                                webEngine.load(webEngine.getHistory().getEntries().get(webEngine.getHistory().getEntries().size()-1).getUrl());
                            }
                        }
                    };
                    EventHandler<ActionEvent> bhis = new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent event) {
                            if(as <= 0){
                                goButton.setDisable(true);
                            }else {
                                webEngine.load(webEngine.getHistory().getEntries().get(webEngine.getHistory().getEntries().size()+1).getUrl());
                                as--;
                            }
                        }
                    };
                    urlField.setOnAction(goAction);
                    goButton.setOnAction(his);
                    bButton.setOnAction(bhis);
                    HBox hBox = new HBox(5);
                    hBox.getChildren().setAll(goButton,bButton,urlField);
                    HBox.setHgrow(urlField, Priority.ALWAYS);
                    final VBox vBox = new VBox(5);
                    sp.setContent(webView);
                    vBox.getChildren().setAll(hBox, sp);
                    VBox.setVgrow(webView, Priority.ALWAYS);
                    tab.setContent(vBox);
                    final ObservableList<Tab> tabs = tb.getTabs();
                    tab.closableProperty().bind(Bindings.size(tabs).greaterThan(2));
                    tabs.add(tabs.size() - 1, tab);
                    //tb.getTabs().add(tab);
                    tb.getSelectionModel().select(tab);
                }else {
                    newSelectedTab.getContent().visibleProperty().set(true);
                    newSelectedTab.getContent().setVisible(true);
                }
            }

        });

        borderPane.setCenter(tb);
        root.getChildren().add(borderPane);
        Scene scene = new Scene(root, 1200, 600);

        primaryStage.setTitle("Alroseb");
        primaryStage.setScene(scene);
        primaryStage.show();
    }




    public static void main(String[] args) {
        launch(args);
    }




    private Tab Createfirsttab(TabPane tb) {
        Tab stab =new Tab("Welcome to Alroseb!");
        Label label = new Label();
        label.setText("\n\t\t\t For Start to Browsing internet please create new tab.");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        stab.setContent(label);
        tb.getTabs().add(stab);
        tb.getSelectionModel().select(stab);

        return stab;
    }
}