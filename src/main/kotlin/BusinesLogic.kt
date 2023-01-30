import java.text.SimpleDateFormat
import kotlin.reflect.full.declaredMemberProperties

const val SUCCESS_STATUS = "ok"
const val HIRE_ACTION = "hire"
const val DATE_TARGET_FORMAT = "yyyy-MM-dd"
const val DATE_BASE_FORMAT = "yymmdd"
const val SALARY_COMPONENT = "salary_component"


fun applyBusinessLogic(
    rows: ArrayList<RawData>,
    configuration: DynamicConfiguration
): Pair<ArrayList<RawData>, ArrayList<String>> {
    val newRows = ArrayList<RawData>()
    val errors = ArrayList<String>()
    // actually will work only for 1 file
    var counter = 0
    parent@ for (row in rows) {
        val fields = configuration.fields
        var newRow: RawData = row.copy()
        // this loop is iterating through properties of RawData class
        child@ for (prop in RawData::class.declaredMemberProperties) {

            val field = fields.first { it.sourceField == prop.name }

            //  applying mappings if necessary
            if (field.mappingKey != null) {
                val map = configuration.mappings[field.mappingKey!!]

                try {
                    newRow = applyMapping(newRow, prop.name, map)
                } catch (e: Exception) {
                    if (field.isMandatory) {
                        errors.add("Failed to map property ${prop.name} with value: ${newRow.getPropertyValue(prop.name)}")
                        continue@parent

                    }
                    newRow.setPropertyValue(prop.name, "")
                }

            }


            //  applying validation if necessary
            var oldValue = newRow.getPropertyValue(prop.name).toString().lowercase()

            if (field.validationPattern != null && oldValue != "") {
                val match = findRegexMatch(newRow, oldValue, field.validationPattern!!, field.regexCaptureGroupNr)
                if (match == "") {
                    errors.add("Failed to apply regex validation. Pattern: ${field.validationPattern}, Value: $oldValue")
                }
                newRow.setPropertyValue(prop.name, match)
            }

            // reloading this variable in case it was modified in previous code block
            oldValue = newRow.getPropertyValue(prop.name).toString().lowercase()
            // parsing dates to proper format
            if (field.dataType == DataType.Date && field.dateFormat != null && oldValue != "") {
                try {
                    val formatter = SimpleDateFormat(field.dateFormat!!)
                    val date = formatter.parse(oldValue)
                    formatter.applyPattern(DATE_TARGET_FORMAT)
                    val newDate = formatter.format(date)
                    newRow.setPropertyValue(prop.name, newDate)
                } catch (e: Exception) {
                    if (field.isMandatory) errors.add("failed to parse date: $oldValue from field: ${prop.name}")
                    newRow.setPropertyValue(prop.name, "")


                }
            }
        }

        val actionField = fields.first { it.fieldType == FieldType.ActionCode }
        val employeeField = fields.first { it.fieldType == FieldType.EmployeeCode }
        if (newRow.getPropertyValue(actionField.sourceField!!) == HIRE_ACTION && newRow.getPropertyValue(employeeField.sourceField!!) == "") {
            val employeeCode = generateEmployeeCode(newRow, fields, counter)
            counter += 1
            newRow.setPropertyValue(employeeField.sourceField!!, employeeCode)
        }
        val checkStatus = checkMandatoryFields(newRow, configuration.fields)
        if (checkStatus == SUCCESS_STATUS) {
            newRows.add(newRow)
        } else {
            errors.add(checkStatus)
        }

    }
    return Pair(newRows, errors)
}


fun applyMapping(newRow: RawData, propName: String, map: Map<String, String>?): RawData {
    val oldValue = newRow.getPropertyValue(propName).toString().lowercase()

    val newValue = map?.get(oldValue) ?: throw Exception()
    newRow.setPropertyValue(propName, newValue)
    return newRow
}

fun findRegexMatch(newRow: RawData, oldValue: String, pattern: String, groupNr: Int?): String {
    return if (groupNr is Int) {
        pattern.toRegex().findAll(oldValue).map { it.groups[groupNr]!!.value ?: "" }.joinToString()
    } else {
        val isMatching = pattern.toRegex().matches(oldValue)
        if (isMatching) oldValue else ""
    }
}

fun generateEmployeeCode(newRow: RawData, fields: List<DynamicConfigurationField>, counter: Int): String {
    val hireDateField = fields.first { it.targetField == "hire_date" }
    val hireDate = newRow.getPropertyValue(hireDateField.sourceField!!)

    // this part is looking not really DRY, and it makes sense to do this part before dates were converted
    // but date formats are specified in JSON and may be different, So I cannot relly on them.

    return try {
        val formatter = SimpleDateFormat(DATE_TARGET_FORMAT)
        val date = formatter.parse(hireDate.toString())
        formatter.applyPattern(DATE_BASE_FORMAT)
        val newDate = formatter.format(date)
        var hexString = Integer.toHexString(counter)
        if (hexString.length < 2) {
            hexString = "0".plus(hexString)
        }
        newDate.plus(hexString)
    } catch (e: java.lang.Exception) {
        ""
    }

}

fun checkMandatoryFields(newRow: RawData, fields: List<DynamicConfigurationField>): String {
//    I cannot guarantee order of execution in main loop + it might affect some fields and leave them empty after validation
    for (prop in RawData::class.declaredMemberProperties) {
        val field = fields.first { it.sourceField == prop.name }
        val propValue = newRow.getPropertyValue(prop.name)
        if (field.isMandatory && field.targetEntity != SALARY_COMPONENT && (propValue == "" || propValue == null)) {
            return "One or more mandatory params are absent. Prop: ${prop.name}, Value: $propValue"
        }
    }
    return SUCCESS_STATUS
}
