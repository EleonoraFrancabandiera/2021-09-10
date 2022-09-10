/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Adiacenza;
import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnDistante"
    private Button btnDistante; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcolaPercorso"
    private Button btnCalcolaPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB1"
    private ComboBox<Business> cmbB1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB2"
    private ComboBox<Business> cmbB2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	
    	String c = this.cmbCitta.getValue();
    	if(c==null) {
    		this.txtResult.appendText("Seleziona una città!");
    		return;
    	}
    	
    	this.model.creaGrafo(c);
    	this.txtResult.appendText("Grafo creato!\n");
    	this.txtResult.appendText("# Vertici : " + this.model.nVertici() + "\n");
    	this.txtResult.appendText("# Archi : " + this.model.nArchi() + "\n");
    	
    	this.cmbB1.getItems().clear();
    	this.cmbB1.getItems().addAll(this.model.getVertici());
    	
    	this.cmbB2.getItems().clear();
    	this.cmbB2.getItems().addAll(this.model.getVertici());
    }
    
    
    @FXML
    void doCalcolaLocaleDistante(ActionEvent event) {
    	
    	this.txtResult.clear();
    	
    	if(!this.model.grafoCreato()) {
    		this.txtResult.appendText("Crea prima il grafo!");
    		return;
    	}
    	
    	Business business = this.cmbB1.getValue();
    	if(business==null) {
    		this.txtResult.appendText("Seleziona un locale!");
    		return;
    	}
    	
    	this.txtResult.appendText("LOCALI PIU' DISTANTI: \n");    	
    	
    	for(Adiacenza a: this.model.getDistanzaMax(business)) {
    		this.txtResult.appendText(a.stampaLocaleDistante() + "\n");
    	}
    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	this.txtResult.clear();
    	
    	if(!this.model.grafoCreato()) {
    		this.txtResult.appendText("Crea prima il grafo!");
    		return;
    	}
    	
    	Business b1 = this.cmbB1.getValue();
    	if(b1==null) {
    		this.txtResult.appendText("Seleziona un locale da cui partire!");
    		return;
    	}
    	
    	Business b2= this.cmbB2.getValue();
    	if(b2==null) {
    		this.txtResult.appendText("Seleziona il locale in cui terminare il tour!");
    		return;
    	}else if(b1.equals(b2)) {
    		this.txtResult.appendText("Selezionare un locale di arrivo diverso da quello di partenza");
    		return;
    	}
    	
    	double x ;
    	try {
    		
    		x = Double.parseDouble(this.txtX2.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("Inserisci un valore numerico valido come voto minimo richiesto per le recensioni");
    		return;
    	}
    	
    	List<Business> listaLocali=this.model.cercaPercorso(b1, b2, x);
    	
    	this.txtResult.appendText("PERCORSO ENOGASTRONOMICO: \n");
    	for(Business b: listaLocali){
    		this.txtResult.appendText(b + "\n");
    	}
    	
    	this.txtResult.appendText("Kilometri totali percorsi: "+ this.model.contaKilometri(listaLocali));
    	
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDistante != null : "fx:id=\"btnDistante\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB1 != null : "fx:id=\"cmbB1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB2 != null : "fx:id=\"cmbB2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbCitta.getItems().addAll(this.model.getCity());
    }
}
