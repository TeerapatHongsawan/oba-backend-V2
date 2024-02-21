package th.co.scb.onboardingapp.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import th.co.scb.onboardingapp.model.TJLogActivityDesc;
import th.co.scb.onboardingapp.model.TJLogActivityDto;
import th.co.scb.onboardingapp.model.entity.ActivityEntity;
import java.util.List;

@Mapper
public interface ActivityMapper {

    ActivityMapper INSTANCE = Mappers.getMapper(ActivityMapper.class);

    @Mapping(target = "activityJson", source = "activityJson", qualifiedByName = "mapTJLogActivityDescToString")
    ActivityEntity map(TJLogActivityDto dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "activityJson", source = "activityJson", qualifiedByName = "mapActivityJson"),
            @Mapping(target = "recordType", constant = "TJ_LOG")
    })
    List<ActivityEntity> mapList(List<TJLogActivityDto> dtos);

    @Named("mapTJLogActivityDescToString")
    @SneakyThrows
    default String mapTJLogActivityDescToString(TJLogActivityDesc desc) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(desc);
    }
}
