import Models.PayComponent
import Models.Payload

fun serializePayloads(rows: ArrayList<RawData>, fields: List<DynamicConfigurationField>): ArrayList<Payload> {

    var payloads = ArrayList<Payload>()
    for (row in rows) {

        val actionField = fields.first { it.fieldType == FieldType.ActionCode }
        var actionCode = row.getPropertyValue(actionField.sourceField!!) as String

        val employeeField = fields.first { it.fieldType == FieldType.EmployeeCode }
        var employeeCode = row.getPropertyValue(employeeField.sourceField!!) as String

        val dataMap = serializeDataMaps(row, fields)

        val components = serializePayComponents(row, fields)
        val payload = Payload(employeeCode, actionCode, dataMap, components)
        payloads.add(payload)
    }
    return payloads
}

fun serializeDataMaps(row: RawData, fields: List<DynamicConfigurationField>): HashMap<String, String> {
    val map = HashMap<String, String>()
    val dataFields = fields.filter { it.targetEntity != "salary_component" && it.targetEntity != null }
    for (dataField in dataFields) {
        val data = row.getPropertyValue(dataField.sourceField!!).toString()
        if (data != "")
            map["${dataField.targetEntity}.${dataField.targetField}"] = data
    }
    return map
}

fun serializePayComponents(row: RawData, fields: List<DynamicConfigurationField>): ArrayList<PayComponent> {
    val payFields = fields.filter { it.targetEntity == "salary_component" }
    var components = ArrayList<PayComponent>()



    for (i in payFields.indices step 4) {
        try {
            val amount = row.getPropertyValue(payFields[i].sourceField!!).toString()
            val currency = row.getPropertyValue(payFields[i + 1].sourceField!!).toString()
            val startDate = row.getPropertyValue(payFields[i + 2].sourceField!!).toString()
            val endDate = row.getPropertyValue(payFields[i + 3].sourceField!!).toString()
            if (listOf(amount, currency, startDate, endDate).all { it != "" }) {

                components.add(PayComponent(amount.toInt(), currency, startDate, endDate))
            }
        } catch (e: Exception) {
            continue
        }
    }
    return components
}

fun findByTargetField(row: RawData, fields: List<DynamicConfigurationField>, targetField: String): String {
    val field = fields.first { it.targetField == targetField }
    return row.getPropertyValue(field.sourceField!!).toString()
}
