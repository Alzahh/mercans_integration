/**
 * Usage:
 * import com.fasterxml.jackson.core.type.TypeReference
 * import com.fasterxml.jackson.databind.ObjectMapper
 * import com.fasterxml.jackson.module.kotlin.KotlinModule
 * import java.io.File
 *
 * val mapper = ObjectMapper().registerModule(KotlinModule())
 * val jsonString: String = File("...").readText(Charsets.UTF_8)
 * val config = mapper.readValue<DynamicConfiguration>(jsonString, object : TypeReference<DynamicConfiguration>() {})
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class DynamicConfiguration(
    val fileNamePattern: String,
    val fields: List<DynamicConfigurationField>,
    val mappings: Map<String, Map<String, String>>
) {
    fun validateFilename(input: String) = Regex(fileNamePattern).matches(input)

    fun tryMap(mappingKey: String, input: String): String? {
        if (!mappings.containsKey(mappingKey)) return null

        return mappings[mappingKey]!!.getOrDefault(input, null)
    }

    companion object {
        const val GLOBAL_DATE_FORMAT = "yyyy-MM-dd"
    }
}
