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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import remember.Conecao;
import remember.RememberProgram;

public class LayoutController {

    private ArrayList<RememberProgram> allRemembers;
    private Conecao cn = new Conecao();
    private String caminhoImage = null;
    private FileChooser filechooser = new FileChooser();
    private FlowPane seriesBox = new FlowPane();
    private SerieBoxController controllerRemember = new SerieBoxController();

    @FXML
    private Button add;

    @FXML
    private Spinner<Integer> episodio;

    @FXML
    private TextField nome;

    @FXML
    private Button sArq;

    @FXML
    private AnchorPane areaPrincipal;
    
    @FXML
    private ImageView imgFundo;

    @FXML
    public void initialize() throws ParseException, IOException {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000);
        valueFactory.setValue(1);
        episodio.setValueFactory(valueFactory);

        seriesBox.setHgap(15);
        seriesBox.setVgap(15);
        seriesBox.setPrefSize(550, 500);
        setAreaPrincipalSeriesBox();
 
    }

    @FXML
    void cadastrar(ActionEvent event) throws ParseException, IOException {
        cn.createConnection();
        
        if (caminhoImage!=null) {
            cn.insertRemember(nome.getText(), episodio.getValue(), caminhoImage);
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


    private void setAreaPrincipalSeriesBox() throws ParseException, IOException{
        areaPrincipal.getChildren().clear();
        
        ScrollPane sp = new ScrollPane();
        sp.setContent(seriesBox);
        sp.fitToWidthProperty().set(false);
        sp.setPrefSize(570, 500);
        areaPrincipal.getChildren().add(sp);
        
        attAllRemembers();
    }


    private void setAreaPrincipalSerieBox(RememberProgram remember) throws IOException{
        areaPrincipal.getChildren().clear();
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../serieBox.fxml"));
        fxmlLoader.setController(controllerRemember);
        Node serieBox = (Node) fxmlLoader.load();
        areaPrincipal.getChildren().add(serieBox);
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
                    setAreaPrincipalSerieBox(rm);
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
