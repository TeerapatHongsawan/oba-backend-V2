package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.util.List;

@Data
public class Suitability {
    private String answerQ1;
    private String answerQ2;
    private String answerQ3;
    private List<String> answerQ4;
    private String answerQ5;
    private String answerQ6;
    private String answerQ7;
    private String answerQ8;
    private String answerQ9;
    private String answerQ10;
    private String answerQ11;
    private String answerQ12;
    private String answerQ13;
    private String answerQ14;
    private String answerQ15;
    private String totalScore;
    private String expireDate;
    private String scoreDate;
    private boolean suitabilityChange;
    private boolean riskScoreUpdateSuccess;

}
