package th.co.scb.onboardingapp.model;

import lombok.*;
import th.co.scb.onboardingapp.model.Pagination;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ResDataWithPagination<T> {
    public Pagination pagination;
    public List<?> items;
}
