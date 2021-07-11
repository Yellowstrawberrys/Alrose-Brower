package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

public class Controller implements Initializable{


    @FXML
    WebView webView;
    private WebEngine webEngine;

    @FXML
    private Button btnGo;

    @FXML
    private TextField tvUrl;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button reload;

    @FXML
    private Button del;

    @FXML
    private Button add;

    @FXML
    private javafx.scene.control.CheckBox isjs;

    @FXML
    private Tab maintab;

    @FXML
    private TabPane tabp;

    public static String urls;
    public static Worker.State statess;
    public static TextField tt;
    Tab cta =maintab;
    public Tab ta;
    int st = 0;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ta = maintab;
        cta =maintab;
        urls=tvUrl.getText();
        btnGo.setDisable(true);
        webEngine = webView.getEngine();
        // Removing right clicks
        webView.setContextMenuEnabled(false);
        //Setting button action
        btnGo.setOnAction((event -> back()));

        reload.setOnAction((event -> reloadweb()));

        add.setOnAction((event -> {
            try {
                newtab();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        del.setOnAction((event -> deltab()));

        tvUrl.setText("about:blank");
        loadWebsite();
        tvUrl.setText("");
        // Updating progress bar using binding property
        progressBar.progressProperty().bind(webEngine.getLoadWorker().progressProperty());
        tabp.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        ta = sl();
                    }
                }
        );
        webEngine.getLoadWorker().stateProperty().addListener(
                (ov, oldState, newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        statess = newState;
                        // Hide progress bar when page is ready
                        try {
                            String t = webEngine.getTitle();
                            if (t.equals(null) || t.isEmpty() || t==null) {
                                Main.ch(webEngine.getLocation());
                                ta.setText(webEngine.getLocation());
                            } else{
                                Main.ch(webEngine.getTitle());
                                ta.setText(t);
                            }
                        }catch (NullPointerException e)
                        {
                            Main.ch(webEngine.getLocation());
                            ta.setText(webEngine.getLocation());
                        }
                        progressBar.setVisible(false);
                        btnGo.setDisable(webEngine.getHistory().getCurrentIndex() - 1 < 0);
                        if(st==0)
                            tvUrl.setText(webEngine.getLocation());
                        else if(st==1)
                            tvUrl.setText(urls);
                        isjs.setSelected(webEngine.isJavaScriptEnabled());
                        st=0;
                    }else if(newState == Worker.State.FAILED){
                        if(newState == Worker.State.RUNNING)
                            return;
                        else {
                            try {
                                URL urls = new URL("http://www.google.com");
                                URLConnection connection = urls.openConnection();
                                connection.connect();
                                loadcantfind();
                            } catch (MalformedURLException e) {
                                loadnoin();
                            } catch (IOException e) {
                                loadnoin();
                            }
                        }
                    } else {
                        //Showing progress bar
                        tvUrl.setText(webEngine.getLocation());
                        progressBar.setVisible(true);
                    }
                });
    }
    public void key(javafx.scene.input.KeyEvent event){
        if(event.getCode().equals(KeyCode.ENTER))
            loadWebsite();
    }
    private void loadWebsite() {
        if(tvUrl.getText().contains("://")){
            try {
                webEngine.load(tvUrl.getText());
                urls=tvUrl.getText();
            } catch (Exception e2) {
                System.out.println("Url Problem:" + e2.getCause().getMessage());
                e2.printStackTrace();
            }
        }else {
            webEngine.load("http://" + tvUrl.getText());
            urls=tvUrl.getText();
        }
    }
    private void loadcantfind(){
        st=1;
        webEngine.load(getClass().getResource("cantfind.html").toString());
    }
    private void loadnoin(){
        st=1;
        webEngine.load(getClass().getResource("nointernet.html").toString());
    }
    private void reloadweb(){
        try {
            webEngine.reload();
        }catch (Exception e){
            System.out.println("Url Problem:" + e.getCause().getMessage());
        }
    }

    public void isjse(ActionEvent actionEvent) {
        webEngine.setJavaScriptEnabled(isjs.isSelected());
    }
    private void back(){
        if(webEngine.getHistory().getCurrentIndex()-1 < 0)
            btnGo.setDisable(true);
        else{
            btnGo.setDisable(false);
            tvUrl.setText(webEngine.getHistory().getEntries().get(webEngine.getHistory().getCurrentIndex()-1).getUrl());
            loadWebsite();
        }
    }
    public void newtab() throws IOException {
        final Tab t= new Tab("New Tab");

            tabp.getTabs().add(t);
    }
    public void deltab(){
        Tab selectedTab = tabp.getSelectionModel().getSelectedItem();
        tabp.getTabs().removeAll(selectedTab);
    }
    public Tab sl(){
        Tab selectedTab = tabp.getSelectionModel().getSelectedItem();
        return selectedTab;
    }

}