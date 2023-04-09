

package dissertacao.classes;

public class Metric {

    String grammar;
    String dataset;
    String phenotype;
    float accuracy;
    float accuracy_sd;
    float f1_score;
    float f1_score_sd;
    String status;

    public String getGrammar() {
        return grammar;
    }

    public void setGrammar(String grammar) {
        this.grammar = grammar;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getPhenotype() {
        return phenotype;
    }

    public void setPhenotype(String phenotype) {
        this.phenotype = phenotype;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public float getAccuracySD() {
        return accuracy_sd;
    }

    public void setAccuracySD(float accuracy_sd) {
        this.accuracy_sd = accuracy_sd;
    }

    public float getF1Score() {
        return f1_score;
    }

    public void setF1Score(float f1_score) {
        this.f1_score = f1_score;
    }

    public float getF1ScoreSD() {
        return f1_score_sd;
    }

    public void setF1ScoreSD(float f1_score_sd) {
        this.f1_score_sd = f1_score_sd;
    }  
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }  
    
}