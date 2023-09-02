package layouts.controller;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import remember.Conecao;
import remember.RememberProgram;

public class SerieBoxController {

    RememberProgram remember;
    Conecao cn = new Conecao();
    LayoutController layoutPrincipal;
    int nTab;
    

    @FXML
    private Button atualizar;

    @FXML
    private Label dataSerie;

    @FXML
    private Button delete;

    @FXML
    private ImageView imgSerie;

    @FXML
    private Spinner<Integer> novoEp;

    @FXML
    private Spinner<Integer> novaNota;

    @FXML
    private TextArea novaAnalize;
    
    @FXML
    private Label serieName;

    @FXML
    private Label ultimoEp;

    @FXML
    private Label nota;

    @FXML
    private Label analize;
    

    public SerieBoxController(){}

    public SerieBoxController(RememberProgram rm, LayoutController lay) {
        this.remember = rm;
        layoutPrincipal = lay;
    } 

    public SerieBoxController(LayoutController lay) {
        layoutPrincipal = lay;
    } 
    
    public void setLayoutPrincipal(LayoutController layoutPrincipal) {
        this.layoutPrincipal = layoutPrincipal;
    }

    public void setRemember(RememberProgram remember) {
        this.remember = remember;
    }

    public void setnTab(int nTab) {
        this.nTab = nTab;
    }

    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000);
        valueFactory.setValue(remember.getEp());
        novoEp.setValueFactory(valueFactory);

        SpinnerValueFactory<Integer> valueFactoryNt = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
        valueFactoryNt.setValue(remember.getNota());
        novaNota.setValueFactory(valueFactoryNt);

        setSerieName(remember.getName());
        setDataSerie(remember.getDate());
        setUltimoEp(remember.getEp());
        setImageCapa(remember.getCapaImg());
        setNota(remember.getNota());
        setAnalize(remember.getAnalize());
    }

    private void setSerieName(String name) {
        this.serieName.setText(name.toUpperCase()); 
    }

    private void setUltimoEp(int ep){
        this.ultimoEp.setText(String.format("Ultimo episódio assistido: %d", ep));
    }

    private void setDataSerie(String date){
        this.dataSerie.setText(date);
    }

    private void setImageCapa(String img){
        File file = new File(img);
        Image imgCapa = new Image(file.toURI().toString());
        this.imgSerie.setImage(imgCapa);
    }

    private void setNota(int nota){
        this.nota.setText(String.format("  %d/10", nota));
    }

    private void setAnalize(String analize){
        this.analize.setText(analize);
    }
    
    @FXML
    void atualizarEp(ActionEvent event) throws ParseException, IOException {
        remember.setEp(novoEp.getValue());
        if (! novaAnalize.getText().isBlank()) remember.setAnalize(novaAnalize.getText());
        remember.setNota(novaNota.getValue());
        remember.setDate(new Date());

        cn.createConnection();
        cn.updateRemember(remember);
        cn.closeConnection();
        layoutPrincipal.attAllRemembers();
    }

    @FXML
    void deleteSerie(ActionEvent event) throws ParseException, IOException {
        cn.createConnection();
        cn.deleteRemember(remember.getIdDb());
        cn.closeConnection();
        layoutPrincipal.attAllRemembers();
        layoutPrincipal.deleteTab(nTab);
    }

}
