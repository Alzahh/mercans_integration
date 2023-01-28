import Models.ResultJson
import Models.generateRawDataClass
import com.google.gson.Gson
import java.io.File

const val FILE_NAME = "input_01.csv"
const val CSV_PATH = "src/main/resources/input_01.csv"
const val CONFIGURATION_PATH = "src/main/resources/dynamic_config.json"


fun main(args: Array<String>) {
    // read configuration JSON and map it into DynamicConfiguration object
    val configuration = buildConfiguration(CONFIGURATION_PATH)

    // this is a hack. It probably should be moved to Maven task or somehow set wih compiler order options
    if (args.isNotEmpty() && args[0] == "generate") {
        generateRawDataClass(configuration)
    } else {

        // read CSV and map to the rawData instance
        val csv = readCSV(CSV_PATH)
        // apply validators, mappings, business logic
        val (rows, errors) = applyBusinessLogic(csv, configuration)

        // serialize data to data object to convert to the json
        val payloads = serializePayloads(rows, configuration.fields)
        val result = ResultJson(FILE_NAME, payloads, errors)
        val gson = Gson()
        println()
        File("src/main/resources/result.json").writeText(gson.toJson(result))
    }
}
