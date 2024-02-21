package th.co.scb.onboardingapp.model;


import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static th.co.scb.onboardingapp.constant.Constants.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PayloadResponseT<T>{

    public ResEntity res;
    public ResDataWithPagination<T> resData;

    public static <T> ResponseEntity<?> buildResponse(List<T> data) {

        PayloadResponseT<T> response = new PayloadResponseT<>();

        Pagination pagination = new Pagination();
        pagination.setPagingLimit(null);
        pagination.setPagingOffset(null);
        pagination.setPreviousPage(null);
        pagination.setFirstPage(null);
        pagination.setLastPage(null);
        pagination.setNextPage(null);

        ResDataWithPagination<T> resDataWithPagination = new ResDataWithPagination<>();
        resDataWithPagination.setPagination(pagination);
        resDataWithPagination.setItems(data);

        response.setResData(resDataWithPagination);

        ResEntity res = new ResEntity();
        res.setCode(SUCCESS);
        res.setStatus(HttpStatus.valueOf(200));
        res.setDescription(STATUS_DESCRIPTION);

        response.setRes(res);

        int http = 200;
        return ResponseEntity.status(http).body(response);

    }

}
