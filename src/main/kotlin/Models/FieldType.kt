import com.fasterxml.jackson.annotation.JsonValue

enum class FieldType(@JsonValue val typeName: String) {
    Regular("Regular"),
    ActionCode("ActionCode"),
    EmployeeCode("EmployeeCode")
}