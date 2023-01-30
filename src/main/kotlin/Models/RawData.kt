import com.fasterxml.jackson.`annotation`.JsonProperty
import kotlin.String

public data class RawData(
  @field:JsonProperty("ACTION")
  public var ACTION: String?,
  @field:JsonProperty("contract_workerId")
  public var contract_workerId: String?,
  @field:JsonProperty("worker_name")
  public var worker_name: String?,
  @field:JsonProperty("worker_personalCode")
  public var worker_personalCode: String?,
  @field:JsonProperty("worker_gender")
  public var worker_gender: String?,
  @field:JsonProperty("contract_workStartDate")
  public var contract_workStartDate: String?,
  @field:JsonProperty("contract_endDate")
  public var contract_endDate: String?,
  @field:JsonProperty("pay_amount")
  public var pay_amount: String?,
  @field:JsonProperty("pay_currency")
  public var pay_currency: String?,
  @field:JsonProperty("pay_effectiveFrom")
  public var pay_effectiveFrom: String?,
  @field:JsonProperty("pay_effectiveTo")
  public var pay_effectiveTo: String?,
  @field:JsonProperty("compensation_amount")
  public var compensation_amount: String?,
  @field:JsonProperty("compensation_currency")
  public var compensation_currency: String?,
  @field:JsonProperty("compensation_effectiveFrom")
  public var compensation_effectiveFrom: String?,
  @field:JsonProperty("compensation_effectiveTo")
  public var compensation_effectiveTo: String?,
) {
  public constructor() : this(null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null)
}
