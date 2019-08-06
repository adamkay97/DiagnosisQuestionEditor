package Controllers;

import Classes.QuestionSet;
import Classes.ScoringAlgorithm;
import Enums.ButtonTypeEnum;
import Managers.DatabaseManager;
import Managers.QuestionSetManager;
import Managers.StageManager;
import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ScoringContentController implements Initializable 
{
    @FXML private Label lblLowLow;
    @FXML private JFXComboBox<Integer> cmbLowUp;
    
    @FXML private Label lblMedLow;
    @FXML private JFXComboBox<Integer> cmbMedUp;
    
    @FXML private Label lblHighLow;
    @FXML private Label lblHighUp;
    
    @FXML private Label lblHeader;
    @FXML private Button btnNext;
    
    private int qCount;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        QuestionSet questionSet;
        
        if(QuestionSetManager.getInEdit())
        {
            lblHeader.setText("Edit Scoring Algorithm");
            btnNext.setText("Save");
            questionSet = QuestionSetManager.getQuestionSet(QuestionSetManager.getCurrentEditSet());
        }
        else
            questionSet = QuestionSetManager.getCurrentCreateSet();
        
        setupScoringContent(questionSet);
    }    
    
    @FXML public void btnNext_Action(ActionEvent event) 
    {
        if(validateInput())
        {
            boolean inEdit = QuestionSetManager.getInEdit();
            
            saveScoringAlgorithm(inEdit);
            
            if(inEdit)
                StageManager.loadContentScene(StageManager.EDITQUESTIONS);
            else
                StageManager.loadContentScene(StageManager.CREATEQUESTIONS);
        }
        else
        {
            String msg = "Please ensure all bounds are set and valid.";
            StageManager.loadPopupMessage("Warning", msg, ButtonTypeEnum.OK);
        }
    }

    @FXML public void btnBack_Action(ActionEvent event) 
    {
        if(QuestionSetManager.getInEdit())
            StageManager.loadContentScene(StageManager.EDITQUESTIONS);
        else
            StageManager.loadContentScene(StageManager.INFO);
    }
    
    private void setupScoringContent(QuestionSet questionSet)
    {
        qCount = questionSet.getNumberOfQuestions();
        
        lblLowLow.setText("0");
        lblHighUp.setText(Integer.toString(qCount));
        
        addNumberList(cmbLowUp, 1, qCount);
        cmbLowUp.setStyle("-fx-font: 24px \"Berlin Sans FB\";");
        cmbMedUp.setStyle("-fx-font: 24px \"Berlin Sans FB\";");
        
        if(QuestionSetManager.getInEdit())
        {
            //Set label and cmbBox values from the scoring algorithm on question set
            ScoringAlgorithm scoring = questionSet.getScoringAlgorithm();
            cmbLowUp.setValue(scoring.getLowRiskUpBound());
            lblMedLow.setText(Integer.toString(scoring.getMedRiskLowBound()));
            cmbMedUp.getItems().add(scoring.getMedRiskUpBound());
            cmbMedUp.setValue(scoring.getMedRiskUpBound());
            lblHighLow.setText(Integer.toString(scoring.getHighRiskLowBound()));
        }
            
        createComboListeners();
    }
    
    private void saveScoringAlgorithm(boolean inEdit)
    {
        int ll = 0; 
        int lu = cmbLowUp.getValue();
        int ml = Integer.parseInt(lblMedLow.getText());
        int mu = cmbMedUp.getValue();
        int hl = Integer.parseInt(lblHighLow.getText());
        int hu = qCount;
        
        ScoringAlgorithm scoringAlgorithm = new ScoringAlgorithm(ll, lu, ml, mu, hl, hu);
        
        if(inEdit)
        {
            String setName = QuestionSetManager.getCurrentEditSet();
            QuestionSetManager.getQuestionSet(setName).setScoringAlgorithm(scoringAlgorithm);
            
            DatabaseManager dbManager = new DatabaseManager();
            if(dbManager.connect())
            {
                //If the scoring algorithm is being edited just update the data in the database
                dbManager.updateScoringAlgorithm(setName, scoringAlgorithm);
                dbManager.disconnect();
            }
        }
        else
            QuestionSetManager.getCurrentCreateSet().setScoringAlgorithm(scoringAlgorithm);
    }
    
    private void createComboListeners()
    {
        //When upper bound of low risk changed, enable medium risk with s
        cmbLowUp.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override public void changed(ObservableValue ov, Integer oldVal, Integer newVal) {
                if(newVal != null && !Objects.equals(oldVal, newVal)) {
                    lblMedLow.setText(Integer.toString(newVal+1));
                    lblHighLow.setText(Integer.toString(newVal+2));
                    cmbMedUp.setDisable(false);
                    addNumberList(cmbMedUp, newVal+1, qCount);
                }
            }    
        });
        
        //If combobox value changed and user ensures changes are saved reset the grid pane for new set
        cmbMedUp.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override public void changed(ObservableValue ov, Integer oldVal, Integer newVal) {
                if(newVal != null && !Objects.equals(oldVal, newVal)) {
                    lblHighLow.setText(Integer.toString(newVal+1));
                }
            }    
        });
    }
    
    private boolean validateInput()
    {
        if(cmbLowUp.getValue() == null)
            return false;
        if(cmbMedUp.getValue() == null)
            return false;
        
        if(Integer.parseInt(lblMedLow.getText()) > cmbMedUp.getValue())
            return false;
        
        return true;
    }
    
    private void addNumberList(JFXComboBox cmbBox, int start, int end)
    {
        ArrayList<Integer> numList = new ArrayList<>();
        for(int i = start; i < end; i++)
            numList.add(i);
        cmbBox.getItems().setAll(numList);
    }

}
