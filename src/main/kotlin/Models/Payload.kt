package Models

class Payload(
    var employeeCode: String,
    var action: String,
    var data: HashMap<String, String>,
    var payComponents: List<PayComponent>
) {

}