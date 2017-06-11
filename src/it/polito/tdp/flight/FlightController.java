package it.polito.tdp.flight;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.flight.model.Airport;
import it.polito.tdp.flight.model.Model;
import it.polito.tdp.flight.model.Stat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FlightController {
	Model model;
	
	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private TextField txtDistanzaInput;
	@FXML
	private TextField txtPasseggeriInput;
	@FXML
	private TextArea txtResult;
	@FXML
	void doCreaGrafo(ActionEvent event) {
		int num;
		try{
			num=Integer.parseInt(txtDistanzaInput.getText());
		}catch(NumberFormatException e){
			txtResult.appendText("Inserire un valore valido per la distanza.\n");
			return;
		}
		model.getPorti();
		String s=model.creaGrafo(num);
		txtResult.appendText(""+s);
		Airport a=model.getFiumicino();
		if(a!=null){
			txtResult.appendText("Aeroporto più lontano da Fiumicino direttamente raggiungibile: "+a+"\n");
		}	
	}

	@FXML
	void doSimula(ActionEvent event) {
		txtResult.clear();
		int num;
		try{
			num=Integer.parseInt(txtPasseggeriInput.getText());
		}catch(NumberFormatException e){
			txtResult.appendText("Inserire un valore valido per i passeggeri.\n");
			return;
		}
		
		List<Stat> lista=model.simula(num);
		txtResult.appendText("Risultati simulazione:\n");
		for(Stat s: lista){
			txtResult.appendText(s.getA()+" --> "+s.getPasseggeri()+"\n");
		}
	}

	@FXML
	void initialize() {
		assert txtDistanzaInput != null : "fx:id=\"txtDistanzaInput\" was not injected: check your FXML file 'Untitled'.";
		assert txtPasseggeriInput != null : "fx:id=\"txtPasseggeriInput\" was not injected: check your FXML file 'Untitled'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Untitled'.";

	}
	public void setModel(Model model) {
		this.model = model;
	}
}
