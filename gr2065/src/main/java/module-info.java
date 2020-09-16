module swapp {
	requires transitive com.fasterxml.jackson.core;
	requires transitive com.fasterxml.jackson.databind;

	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires javafx.controls;
	
	exports swapp.core;
	exports swapp.json;
	exports swapp.ui;

	opens swapp.ui to javafx.fxml;
}
