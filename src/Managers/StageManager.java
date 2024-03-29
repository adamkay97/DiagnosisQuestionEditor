package Managers;

import Enums.ButtonTypeEnum;
import Controllers.*;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;

public class StageManager 
{
    //Constants for FXML form paths
    public static final String MAIN = "/Forms/MainForm.fxml";
    public static final String START = "/Forms/StartContent.fxml";
    public static final String POPUP = "/Forms/PopUpMessage.fxml";
    public static final String EDITQUESTIONS = "/Forms/EditQuestionsContent.fxml";
    public static final String ADDLANGUAGE = "/Forms/EditAddLanguageForm.fxml";
    public static final String CREATESET = "/Forms/CreateSetContent.fxml"; 
    public static final String CREATEQUESTIONS = "/Forms/CreateQuestionsContent.fxml"; 
    public static final String INFO = "/Forms/InformationContent.fxml";
    public static final String SCORING = "/Forms/ScoringContent.fxml";
    
    public static final String MANAGELANGS = "/Forms/ManageLanguagesForm.fxml"; 
    
    private static MainFormController mainFormController;
    //private static AnchorPane rootPane;
    
    private static boolean popupAnswer;
    
    //Offsets used for calculating where the form should be once its been dragged
    private static double offsetX = 0;
    private static double offsetY = 0;
    
    //Used for when Main Form is first loaded
    private static boolean onLoad = true;
    
    private static Scene rootScene;
    
    private static final LogManager logManager = new LogManager();
    
    /**
     * Loads the scene passed as a parameter into
     * the main content stack pane on the main form 
     * @param fxmlPath path of the FXML scene
     */
    public static void loadContentScene(String fxmlPath)
    {
        try
        {
            mainFormController.setAllScene(
                FXMLLoader.load(StageManager.class.getResource(fxmlPath)
            ));
            
            //if(!onLoad)
                //LanguageManager.setFormText(getFormName(fxmlPath), rootScene);
        }
        catch(IOException ex) 
        {
            logManager.ErrorLog("Failed loading scene = " + ex.getMessage());
        }
    }
    
    /**
     * Loads the scene passed as a parameter into
     * the main content stack pane on the main form
     * Used when scene needs variables to be passed to the controller
     * @param root actual scene to be loaded into main form
     */
    public static void loadContentSceneParent(Parent root)
    {
        try
        {
            mainFormController.setAllScene(root);
        }
        catch(Exception ex) 
        {
            logManager.ErrorLog("Failed loading scene = " + ex.getMessage());
        }
    }
    
    /**
     * Loads a new stage for a pop up message, 
     * this could be used for errors or warning messages
     * that require a simple yes no input from the user
     * 
     * @param headerText - Text for header on pop up
     * @param messageText - Text for actual message
     * @param buttonType - enum for button type that is wanted to be shown
     * @return boolean for when a Yes/No pop up is required
     */
    public static boolean loadPopupMessage(String headerText, String messageText, ButtonTypeEnum buttonType)
    {
        try
        {
            //Initialise bool for 'OK' pop up types
            popupAnswer = false;
            
            //Load popup form, pass variables to Popup controller for setting the text on the popup
            FXMLLoader loader = new FXMLLoader(StageManager.class.getResource(POPUP));
            Parent root = (Parent)loader.load();
            Scene popup = new Scene(root);
            PopUpMessageController popupController = loader.<PopUpMessageController>getController();
            popupController.setPopupContent(headerText, messageText, buttonType);
            
            Stage popupStage = new Stage();
            setFormMoveHandlers(root, popupStage);
            
            //Add event handlers for YesNo popup buttons
            if(buttonType == ButtonTypeEnum.YESNO)
            {
                popupController.btnYes.setOnAction(e -> 
                {
                    popupAnswer = true;
                    popupStage.close();
                });
                
                popupController.btnNo.setOnAction(e -> 
                {
                    popupAnswer = false;
                    popupStage.close();
                });
            }
            
            //Set pop up style to Undecorated, set Modality to freeze rest of application until popup is closed
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(popup);
            popupStage.showAndWait();
        }
        catch(IOException ex)
        {
            logManager.ErrorLog("Failed loading pop up message - " + ex.getMessage());
        }
        return popupAnswer;
    }       
    
    /**
     * Loads the main stages i.e login page, registration
     * and the main form. 
     * @param formPath Path for the form to be opened
     * @param stage The current stage to be used, either stage
     * given from application.start or new Stage() for other stages
     */
    public static void loadForm(String formPath, Stage stage)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(StageManager.class.getResource(formPath));

            Parent root = (Parent)loader.load();
            Scene newScene = new Scene(root);
            
            setFormMoveHandlers(root, stage);
            
            
            stage.initStyle(StageStyle.UNDECORATED);
            
            //If not primary stage don't allow any action until current form has been closed
            if(!formPath.equals(StageManager.MAIN))
                stage.initModality(Modality.APPLICATION_MODAL);
            
            stage.setScene(newScene);
            stage.show();
        }
        catch(Exception ex)
        {
            logManager.ErrorLog("Failed loading other form - " + ex.getMessage());
        }
    }
    
    /**
     * Loads new form using Parent object passed to function
     * Used when forms require data to be passed to it before loading
     * @param root Parent object containing the content of the form
     * @param stage New stage for the form to be loaded to
     */
    public static void loadFormParent(Parent root, Stage stage)
    {
         try
        {
            Scene newScene = new Scene(root);
            
            setFormMoveHandlers(root, stage);
            
            //rootScene = newScene;
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(newScene);
            stage.showAndWait();
        }
        catch(Exception ex)
        {
            logManager.ErrorLog("Failed loading other form - " + ex.getMessage());
        }
    }
    
    /**
     * Using lambda expressions to set the event listener for when
     * the user drags the form so that its position is changed to where is required
     * @param root Parent of the handlers
     * @param stage Stage that is to be moved
     */
    private static void setFormMoveHandlers(Parent root, Stage stage)
    {
        root.setOnMousePressed((MouseEvent event) -> 
        {
            offsetX = event.getSceneX();
            offsetY = event.getSceneY();
        });
            
        root.setOnMouseDragged((MouseEvent event) -> 
        {
            stage.setX(event.getScreenX() - offsetX);
            stage.setY(event.getScreenY() - offsetY);
        });
    }
    
    private static String getFormName(String fxmlPath)
    {
        String formName = "";
        
        switch (fxmlPath)
        {
            default:
                break;
        }
        
        return formName;
    }
        
    /**
     * Sets the mainFormController variable on the StageManager
     * for use when setting the content pane nested in the main form
     * @param controller 
     */
    public static void setMainFormController(MainFormController controller)
    {
        StageManager.mainFormController = controller;
    }
    
    //Used for getting the main form controller for setting values/designs on the main form 
    //from other content controllers
    public static MainFormController getMainFormController()
    {
        return StageManager.mainFormController;
    }
    
    public static void setOnLoad(boolean loaded) { onLoad = loaded; }
    public static boolean getOnLoad() { return onLoad; }
    
    public static void setRootScene(Scene scene) { rootScene = scene; }
    public static Scene getRootScene() { return rootScene; }        
}
