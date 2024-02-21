package th.co.scb.onboardingapp.helper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MappingHelper {

//    private MapperFactory mapperFactory;
    private  ObjectMapper objectMapper = new ObjectMapper();
    private  ModelMapper modelMapper;


//    public MappingHelper(MapperFactory mapperFactory) {
//        this.mapperFactory = mapperFactory;
//    }

    public MappingHelper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        objectMapper.registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public <A, B> B map(A src, Class<B> cls) {
        //return mapperFactory.getMapperFacade((Class<A>) src.getClass(), cls, false).map(src);
        return objectMapper.convertValue(src, cls);
    }

    public <A, B> List<B> mapAsList(Collection<A> src, Class<B> cls) {
//        Function<A, BoundMapperFacade<A, B>> facadeFn = (x) -> mapperFactory.getMapperFacade((Class<A>) x.getClass(), cls, false);
//        return src.stream()
//                .map(it -> facadeFn.apply(it).map(it))
//                .collect(Collectors.toList());

//        return src.stream()
//                .map(it -> objectMapper.convertValue(it, cls))
//                .collect(Collectors.toList());

        return src
                .stream()
                .map(user -> modelMapper.map(user, cls))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public <A, B> B copy(A src, B dest) {

        //argetObject targetObject = objectMapper.convertValue(sourceObject, TargetObject.class);
        return objectMapper.updateValue(dest, src);
    }
    @SneakyThrows
    public <A, B> B convertToList(A src, Class<B> cls) {
        //return mapperFactory.getMapperFacade((Class<A>) src.getClass(), cls, false).map(src);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(List.class, new StringToListDeSerializer());
        objectMapper.registerModule(module);
        return objectMapper.readValue((String) src, cls);
    }

    @SneakyThrows
    public <A, B> B convertToObject(A src, Class<B> cls) {
        //return mapperFactory.getMapperFacade((Class<A>) src.getClass(), cls, false).map(src);
        return objectMapper.readValue((String) src, cls);
    }

}
