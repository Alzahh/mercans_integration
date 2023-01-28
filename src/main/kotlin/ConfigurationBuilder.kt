import org.json.JSONObject
import java.io.FileInputStream
import java.io.InputStream

fun buildConfiguration(path: String): DynamicConfiguration {
    val json = JSONObject(readJSONFromAsset(path)).toMap()

    val dynamicFields = ArrayList<DynamicConfigurationField>()
    val mappings = json["mappings"] as Map<String, Map<String, String>>
    val fields = json["fields"] as List<Map<String, String>>

    for (field in fields) {
        val df = serializeDynamicConfigurationField(field)
        dynamicFields.add(df)
    }

    return DynamicConfiguration(json["fileNamePattern"].toString(), dynamicFields, mappings)
}

// read json as a map
fun readJSONFromAsset(path: String): String? {
    val json: String?
    try {
        val inputStream: InputStream = FileInputStream(path)
        json = inputStream.bufferedReader().use { it.readText() }
    } catch (ex: Exception) {
        ex.printStackTrace()
        return null
    }
    return json
}

fun serializeDynamicConfigurationField(map: Map<String, Any>): DynamicConfigurationField {
    val dynamicField = DynamicConfigurationField()
    dynamicField.sourceField = map["sourceField"] as String?
    dynamicField.fieldType = FieldType.valueOf(map["fieldType"] as String)
    dynamicField.dataType = DataType.valueOf(map["dataType"] as String)
    dynamicField.isMandatory = map["isMandatory"] as Boolean
    dynamicField.targetEntity = map["targetEntity"] as String?
    dynamicField.targetField = map["targetField"] as String?
    dynamicField.mappingKey = map["mappingKey"] as String?
    dynamicField.dateFormat = map["dateFormat"] as String?
    dynamicField.validationPattern = map["validationPattern"] as String?
    dynamicField.regexCaptureGroupNr = map["regexCaptureGroupNr"] as Int?

    return dynamicField

}
