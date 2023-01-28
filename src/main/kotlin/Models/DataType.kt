import com.fasterxml.jackson.annotation.JsonValue

enum class DataType(@JsonValue val typeName: String) {
    Text("Text"),
    Integer("Integer"),
    Decimal("Decimal"),
    Bool("Bool"),
    Date("Date")
}