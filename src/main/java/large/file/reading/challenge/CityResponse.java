package large.file.reading.challenge;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
@Data
@AllArgsConstructor
public class CityResponse implements Serializable {

    @JsonView
    private int year;
    @JsonView
    private double averageTemperature;

}
