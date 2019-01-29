package ch.fhnw.swc.mrs.gui;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import ch.fhnw.swc.mrs.MovieRentalSystem;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

public class STUserTab extends ApplicationTest {

	private FxRobot robot;
	private TextField surnameField;
	private TextField firstnameField;
	private DatePicker birthDatePicker;
	private Button saveButton;
	private Button newButton;
	private Button editButton;
	private Button deleteButton;
	private Button cancelButton;
	@Override
	public void start(Stage stage) throws Exception {
		
		new MovieRentalSystem().start(stage);
	}

	@Override
	public void stop() {
	}

	@BeforeEach
	public void setUp() throws Exception {
		robot = new FxRobot();
		surnameField = (TextField) robot.lookup("#UserSurnameF").query();
		firstnameField = (TextField) robot.lookup("#UserFirstnameF").query();
		birthDatePicker = (DatePicker) robot.lookup("#UserBirthdateP").query();
		
		saveButton = (Button) robot.lookup("#UserSaveB").query();
		editButton = (Button) robot.lookup("#UserEditB").query();
		newButton = (Button) robot.lookup("#UserNewB").query();
		deleteButton = (Button) robot.lookup("#UserDeleteB").query();
		cancelButton = (Button) robot.lookup("#UserCancelB").query();
	}

	@AfterEach
	public void tearDown() throws Exception {
		FxToolkit.hideStage();
		release(new KeyCode[] {});
		release(new MouseButton[] {});
	}
	
	
	@DisplayName("test initial state - nothing selected")
	@Test
	void nothingSelected() throws Exception {
		assertAll("verify ui states for nothing selected", 
				() -> assertEquals(true, surnameField.isDisabled(), "surnameField disabling"),
				() -> assertEquals(true, firstnameField.isDisabled(), "firstnameField disabling"),
				() -> assertEquals(true, birthDatePicker.isDisabled(), "birthdatePicker disabling"),
				
				() -> assertEquals(true, cancelButton.isDisabled(), "newUser button not disbling"),
				() -> assertEquals(true, editButton.isDisabled(), "newUser button not disbling"),
				() -> assertEquals(true, deleteButton.isDisabled(), "newUser button not disbling"),
				() -> assertEquals(true, saveButton.isDisabled(), "saveButton disabling"),
				() -> assertEquals(false, newButton.isDisabled(), "newButton disabling")
			);
	}

	@DisplayName("test entering a new user.")
	@Test
	void enterNewUser() throws Exception {
		clickOn("#UserNewB");
		clickOn("#UserSurnameF");
		write("Locher");
		clickOn("#UserFirstnameF");
		write("James");
		clickOn("#UserBirthdateP");
		write("15.11.2018");
		clickOn("#UserSaveB");
		assertAll("verify ui states for nothing selected", 
				() -> assertEquals(true, surnameField.isDisabled(), "surnameField disabling"),
				() -> assertEquals(true, firstnameField.isDisabled(), "firstnameField disabling"),
				() -> assertEquals(true, birthDatePicker.isDisabled(), "birthdatePicker disabling"),
				
				() -> assertEquals(true, cancelButton.isDisabled(), "newUser button not disbling"),
				() -> assertEquals(true, editButton.isDisabled(), "newUser button not disbling"),
				() -> assertEquals(true, deleteButton.isDisabled(), "newUser button not disbling"),
				() -> assertEquals(true, saveButton.isDisabled(), "saveButton disabling"),
				() -> assertEquals(false, newButton.isDisabled(), "newButton disabling")
			);
		
	}
	

	/**
	 * Initialize the price categories for the MRS application. Do this before the
	 * application is started for the first time.
	 * 
	 * @throws Exception
	 *             whenever something goes unexpected, fail this test case.
	 */
	@BeforeAll
	private static void loadPriceCategories() throws Exception {
		URI uri = MovieRentalSystem.class.getClassLoader().getResource("data/pricecategories.config").toURI();
		try (Stream<String> stream = Files.lines(Paths.get(uri))) {
			stream.forEach(x -> {
				try {
					Class.forName(x);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			});
		}
	}

}
