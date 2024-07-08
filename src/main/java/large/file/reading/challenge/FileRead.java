package large.file.reading.challenge;

import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class FileRead implements Runnable {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final String COMMA_DELIMITER = ";";
    private final Collection<CityResponse> syncCollection;
    private final MappedByteBuffer mappedByteBuffer;
    private final String cityName;
    private static final CharsetDecoder charsetDecoder = StandardCharsets.UTF_8.newDecoder();

    public FileRead(MappedByteBuffer mappedByteBuffer, Collection<CityResponse> syncCollection, String cityName) {
        this.syncCollection = syncCollection;
        this.cityName = cityName;
        this.mappedByteBuffer = mappedByteBuffer;
    }

    @Override
    public void run() {
        try {
            CharBuffer decode = charsetDecoder.decode(mappedByteBuffer);
            String stringChunk = decode.toString();
            String[] values;
            Year year;
            String[] split = stringChunk.split("\n");
            for(String line : split) {
                    if(line.contains(this.cityName)) {
                        values = line.split(COMMA_DELIMITER);
                        year = Year.parse(values[1], dateTimeFormatter);
                        syncCollection.add(new CityResponse(year.getValue(), Double.parseDouble(values[2])));
                    }
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}