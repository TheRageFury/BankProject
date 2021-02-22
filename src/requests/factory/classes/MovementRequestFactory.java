package requests.factory.classes;

import domain.Tag;
import requests.applicable.RequestBuilder;
import requests.classes.MovementRequest;
import requests.specifiers.MovementRequestParameters;
import utilities.time.Time;

import java.util.Collection;
import java.util.Map;

/**
 * This ADT represents a mechanism to assemble requests for movements,
 * hiding the mechanisms strictly coupled to that request type
 */
public class MovementRequestFactory extends AbstractRequestFactory<MovementRequestParameters> {
    private MovementRequest.MovementRequestBuilder builder;

    @Override
    protected RequestBuilder setupBuilder(Map<MovementRequestParameters, Object> args) {
        this.builder = new MovementRequest.MovementRequestBuilder();
        for(Map.Entry<MovementRequestParameters, Object> entry : args.entrySet()) {
            MovementRequestParameters param = entry.getKey();
            Object value = entry.getValue();

            assignParameter(param, value);
        }
        return builder;
    }

    private void assignParameter(MovementRequestParameters param, Object value){
        switch(param){
            case RANGE_QUANTITY -> withQuantity((double[]) value);
            case RANGE_TIME -> withTime((Time[]) value);
            case GROUP_WORDS -> withWords((Collection<String>) value);
            case GROUP_TAGS -> withTags((Collection<Tag>) value);
        }
    }

    private void withQuantity(double[] value){
        builder.withQuantity(value[0], value[1]);
    }
    private void withTime(Time[] times) {
        builder.withTime(times[0], times[1]);
    }
    private void withWords(Collection<String> words) {
        builder.withWordsInDescription(words);
    }
    private void withTags(Collection<Tag> tags) {
        builder.withTags(tags);
    }
}
