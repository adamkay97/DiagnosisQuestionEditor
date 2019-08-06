package Classes;

public class ScoringAlgorithm 
{
    private int lowRiskLowBound;
    private int lowRiskUpBound;
    private int medRiskLowBound;
    private int medRiskUpBound;
    private int highRiskLowBound;
    private int highRiskUpBound;
    
    public ScoringAlgorithm() {}
    
    public ScoringAlgorithm(int ll, int lu, int ml, int mu, int hl, int hu)
    {
        lowRiskLowBound = ll;
        lowRiskUpBound = lu;
        medRiskLowBound = ml;
        medRiskUpBound = mu;
        highRiskLowBound = hl;
        highRiskUpBound = hu;
    }
    
    public int getLowRiskLowBound() { return lowRiskLowBound; }
    public int getLowRiskUpBound() { return lowRiskUpBound; }
    public int getMedRiskLowBound() { return medRiskLowBound; }
    public int getMedRiskUpBound() { return medRiskUpBound; }
    public int getHighRiskLowBound() { return highRiskLowBound; }
    public int getHighRiskUpBound() { return highRiskUpBound; }
    
    public void setLowRiskLowBound(int ll) { lowRiskLowBound = ll; }
    public void setLowRiskUpBound(int lu) { lowRiskUpBound = lu; }
    public void setMedRiskLowBound(int ml) { medRiskLowBound = ml; }
    public void setMedRiskUpBound(int mu) { medRiskUpBound = mu; }
    public void setHighRiskLowBound(int hl) { highRiskLowBound = hl; }
    public void setHighRiskUpBound(int hu) { highRiskUpBound = hu; }
}
