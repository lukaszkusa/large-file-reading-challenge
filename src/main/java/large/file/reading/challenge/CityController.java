package large.file.reading.challenge;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/city")
@AllArgsConstructor
public class CityController{

    CityService cityService;
    @GetMapping(
        path = "/{cityName}",
        produces = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE})
    public List<CityResponse> getAvgOfTempByCity(@PathVariable("cityName") final String cityName) throws IOException {
        return cityService.getAvgOfTempParallel(cityName);
    }


}
