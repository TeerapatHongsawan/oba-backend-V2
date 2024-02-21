package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public abstract class AppFormModel {
    protected String date;
    protected String time;
    protected String branchName;
    protected String applicationNo;
    protected int pageNumber;
    protected int totalPage;
}
