package large.file.reading.challenge;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class CityServiceTest {

    private @Autowired CityService cityService;

    @Test
    @SneakyThrows
    void test(){
        final var responses =
                cityService.getAvgOfTempParallel("Warszawa");

        assertThat(responses).isNotNull().hasSize(6);
        assertThat(responses.getFirst().getYear()).isEqualTo(2018);
        assertThat(responses.getFirst().getAverageTemperature()).isEqualTo(13.52);

        assertThat(responses.get(1).getYear()).isEqualTo(2019);
        assertThat(responses.get(1).getAverageTemperature()).isEqualTo(13.81);

        assertThat(responses.get(2).getYear()).isEqualTo(2020);
        assertThat(responses.get(2).getAverageTemperature()).isEqualTo(16.12);

        assertThat(responses.get(3).getYear()).isEqualTo(2021);
        assertThat(responses.get(3).getAverageTemperature()).isEqualTo(15.61);

        assertThat(responses.get(4).getYear()).isEqualTo(2022);
        assertThat(responses.get(4).getAverageTemperature()).isEqualTo(14.68);

        assertThat(responses.get(5).getYear()).isEqualTo(2023);
        assertThat(responses.get(5).getAverageTemperature()).isEqualTo(15.46);
    }
}
