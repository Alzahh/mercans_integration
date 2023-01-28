package Models

import java.util.*

class ResultJson(val fname: String, var payload: List<Payload>, var errors: ArrayList<String>) {
    val uuid: UUID = UUID.randomUUID()

}