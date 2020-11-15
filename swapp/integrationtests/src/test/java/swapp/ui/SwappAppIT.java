package swapp.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class SwappAppIT extends ApplicationTest {

	private SwappAppController controller;

	@Override
	public void start(final Stage stage) throws Exception {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("SwappApp_it.fxml"));
		final Parent root = loader.load();
		this.controller = loader.getController();
		stage.setScene(new Scene(root));
		stage.show();
	}

	@BeforeEach
	public void setupItems() throws URISyntaxException {
		try (Reader reader = new InputStreamReader(getClass().getResourceAsStream("it-swappmodel.json"))) {
			String port = System.getProperty("swapp.port");
			assertNotNull(port, "No swapp.port system property set");
			URI baseUri = new URI("http://localhost:" + port + "/swapp/");
			System.out.println("Base RemoteSwappAcces URI: " + baseUri);
			this.controller.setSwappDataAccess(new RemoteSwappAccess(baseUri));
		} catch (IOException ioe) {
			fail(ioe.getMessage());
		}
	}

	@Test
	public void testController_initial() {
		assertNotNull(this.controller);
	}
}
