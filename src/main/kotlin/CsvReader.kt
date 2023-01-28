import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import java.io.FileInputStream

fun readCSV(path : String) : ArrayList<RawData> {
    val mapper = CsvMapper.builder()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) //ignore unknown columns
        .build()


    return  mapper.readerFor(RawData::class.java)
        .with(CsvSchema.emptySchema().withHeader())
        .readValues<RawData>(FileInputStream("src/main/resources/input_01.csv"))
        .readAll() as ArrayList<RawData>
}
