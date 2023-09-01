package layouts.controller;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import remember.Conecao;
import remember.RememberProgram;

public class LayoutController {

    private ArrayList<RememberProgram> allRemembers;
    private Conecao cn = new Conecao();
    private String caminhoImage = null;
    private FileChooser filechooser = new FileChooser();

    @FXML
    private Button add;

    @FXML
    private Spinner<Integer> episodio;

    @FXML
    private TextField nome;

    @FXML
    private Button sArq;

    @FXML
    private FlowPane seriesBox;
    
    @FXML
    private ImageView imgFundo;

    @FXML
    private TabPane tabArea;

    @FXML
    private TextArea analize;

    @FXML
    private Spinner<Integer> nota;

    @FXML
    public void initialize() throws ParseException, IOException {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000);
        valueFactory.setValue(1);
        episodio.setValueFactory(valueFactory);

        SpinnerValueFactory<Integer> valueFactoryNt = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
        valueFactoryNt.setValue(1);
        nota.setValueFactory(valueFactoryNt);

        tabArea.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

        attAllRemembers();
    }

    @FXML
    void cadastrar(ActionEvent event) throws ParseException, IOException {
        cn.createConnection();
        
        if (caminhoImage!=null) {
            cn.insertRemember(nome.getText(), episodio.getValue(), caminhoImage, analize.getText(), nota.getValue());
        } else {
            cn.insertRemember(nome.getText(), episodio.getValue());
        } 

        cn.closeConnection();

        attAllRemembers();
    }

    @FXML
    void selecionarArquivo(ActionEvent event) {
        File imgSrc = filechooser.showOpenDialog(sArq.getScene().getWindow());
        
        if (imgSrc != null) {
            caminhoImage =  imgSrc.toString();
            Image image = new Image(imgSrc.toURI().toString());
            imgFundo.setImage(image);
        } else {
            caminhoImage = null;
        }
    }


    private void createNewTabRemember(RememberProgram remember) throws IOException{
        
        SerieBoxController controllerRemember = new SerieBoxController(remember);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../serieBox.fxml"));
        fxmlLoader.setController(controllerRemember);
        Node serieBox = (Node) fxmlLoader.load();

        Tab tab = new Tab(remember.getName(), serieBox);
        tabArea.getTabs().add(tab);
    }


    private void attAllRemembers() throws ParseException, IOException{
        seriesBox.getChildren().clear();
        
        cn.createConnection();
        allRemembers = cn.selectAllRemembers();

        for (RememberProgram remember : allRemembers) {
            addSerieBox(remember);
        }
        cn.closeConnection();
    }

    private void addSerieBox(RememberProgram rm) throws IOException{
        File file = new File(rm.getCapaImg());
        Image img = new Image(file.toURI().toString());
        ImageView imgView = new ImageView(img);
        VBox serieBox = new VBox();
        Label name = new Label(rm.getName());

        serieBox.setAlignment(Pos.CENTER);
        serieBox.setCursor(Cursor.HAND);
        serieBox.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                try {
                    createNewTabRemember(rm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }    
        });

        imgView.setFitHeight(200);
        imgView.setFitWidth(150);

        serieBox.getChildren().addAll(imgView, name);
        seriesBox.getChildren().add(serieBox);
    }
}
