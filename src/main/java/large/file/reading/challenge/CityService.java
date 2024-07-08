package large.file.reading.challenge;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Math.toIntExact;
import static java.util.stream.Collectors.averagingDouble;
import static java.util.stream.Collectors.groupingBy;

@Service
public class CityService {

    private static final int NUM_THREADS = 4;

    @Transactional(readOnly = true)
    public List<CityResponse> getAvgOfTempParallel(final String cityName) throws IOException {

        Collection<CityResponse> syncCollection = Collections.synchronizedCollection(new ArrayList<>());

        try(FileChannel channel = FileChannel.open(Path.of("src/main/resources/example_file.csv"), StandardOpenOption.READ);
            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()){

            long remainingSize = channel.size(); //get the total number of bytes in the file
            long partSize = remainingSize / NUM_THREADS; //file_size/threads

            long start = 0;//file pointer
            while (remainingSize >= partSize) {

                MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, start, partSize);

                while(buffer.get(toIntExact(partSize)-1) != (byte)'\n'){
                    partSize++;
                    buffer = channel.map(FileChannel.MapMode.READ_ONLY, start, partSize);
                }
                //launches a new thread
                executor.execute(new FileRead(buffer, syncCollection, cityName));
                remainingSize = remainingSize - partSize;
                start = start + partSize;
            }
            executor.shutdown();
            //Wait for all threads to finish
            while (!executor.isTerminated()) {}
        }
        Map<Integer, Double> map = syncCollection.stream()
                .collect(groupingBy(CityResponse::getYear, averagingDouble(CityResponse::getAverageTemperature)));
        Set<Integer> integers = map.keySet();
        return integers.stream().map(key -> new CityResponse(key, BigDecimal.valueOf(map.get(key))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue())).toList();
    }

}
