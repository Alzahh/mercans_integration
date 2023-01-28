import com.fasterxml.jackson.annotation.JsonFormat

@Suppress("MemberVisibilityCanBePrivate", "unused")
class DynamicConfigurationField {
    var fieldType: FieldType = FieldType.Regular

    @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
    var isMandatory: Boolean = false

    var sourceField: String? = null

    var targetEntity: String? = null

    var targetField: String? = null

    var dataType: DataType = DataType.Text

    var mappingKey: String? = null

    var dateFormat: String? = null

    var validationPattern: String? = null

    var regexCaptureGroupNr: Int? = null

    override fun toString(): String = "$fieldType\t$dataType\t$sourceField" + when (fieldType) {
        FieldType.Regular -> "\t->\t${entityKey()}"
        else -> ""
    }
    
    fun entityKey(): String = when (targetEntity) {
        null -> ""
        else -> "$targetEntity.$targetField"
    }
}