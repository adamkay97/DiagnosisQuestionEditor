package Controllers;

import Managers.StageManager;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainFormController implements Initializable
{
    @FXML private AnchorPane mainAnchorPane;
    @FXML private AnchorPane pnlMainContentAnchor;
    @FXML private StackPane pnlMainContent;
    
    @FXML private JFXButton btnQuit;
    @FXML private JFXButton btnLogout;
    @FXML private JFXButton btnMaxRes;
    @FXML private JFXButton btnMinimize;
    
    @FXML private ImageView maxResIcon;
    @FXML private Label lblHeader;
    
    private boolean maximized;
    
    private double DEFAULTX;
    private double DEFAULTY;
    private double DEFAULTHEIGHT;
    private double DEFAULTWIDTH;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        StageManager.setMainFormController(this);
        StageManager.loadContentScene(StageManager.START);
        
        //Run in seperate thread after form has been loaded in order to get access
        //to elements that are not available within 'intitialize'.
        Platform.runLater(() ->{            
            Stage mainStage = (Stage)mainAnchorPane.getScene().getWindow();
            
            DEFAULTX = mainStage.getX();
            DEFAULTY = mainStage.getY();
            DEFAULTHEIGHT = mainStage.getHeight();
            DEFAULTWIDTH = mainStage.getWidth();
        });
    }    
    
    @FXML public void btnMaxRes_Action(ActionEvent event) { maximizeMainForm(); }
    
    @FXML public void btnMinimize_Click(ActionEvent event) { minimizeMainForm(); }
    
    @FXML public void btnQuit_Click(ActionEvent event) { quitMainForm(); }
    
    @FXML public void btnLogout_Action(ActionEvent event) {}
    
    /**
     * Replaces all of the current scenes in the Main
     * Content stack pane.
     * 
     * @param node the scene that is to replace the current scene
     */
    public void setAllScene(Node node)
    {
        pnlMainContent.getChildren().setAll(node);
    }
    
    private void minimizeMainForm()
    {
        Stage currentStage = (Stage)btnMinimize.getScene().getWindow();
        currentStage.setIconified(true);
    }
    
    private void maximizeMainForm()
    {
        Stage mainStage = (Stage)mainAnchorPane.getScene().getWindow();
        
        if(!maximized)
        {
            maxResIcon.getStyleClass().clear();
            maxResIcon.getStyleClass().add("restore");
            
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            mainStage.setX(bounds.getMinX());
            mainStage.setY(bounds.getMinY());
            mainStage.setWidth(bounds.getWidth());
            mainStage.setHeight(bounds.getHeight());
            
            //double size = bounds.getWidth();
            //pnlMainContentAnchor.setPrefWidth(size);
            
            maximized = true;
        }
        else
        {
            maxResIcon.getStyleClass().clear();
            maxResIcon.getStyleClass().add("maximize");
            
            mainStage.setX(DEFAULTX);
            mainStage.setY(DEFAULTY);
            mainStage.setWidth(DEFAULTWIDTH);
            mainStage.setHeight(DEFAULTHEIGHT);
            
            //double size = DEFAULTWIDTH - pnlMenuContent.getWidth();
            //pnlMainContentAnchor.setPrefWidth(size);
            maximized = false;
        }
    }
   
    private void quitMainForm() 
    {
        Stage currentStage = (Stage)mainAnchorPane.getScene().getWindow();
        currentStage.close();
    }
    
}
