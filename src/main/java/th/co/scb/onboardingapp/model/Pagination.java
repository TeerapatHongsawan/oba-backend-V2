package th.co.scb.onboardingapp.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Pagination {
    private String pagingLimit;
    private String pagingOffset;
    private String previousPage;
    private String firstPage;
    private String lastPage;
    private String nextPage;
}
