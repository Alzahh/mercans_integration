package Models

import DynamicConfiguration
import com.fasterxml.jackson.annotation.JsonProperty
import com.squareup.kotlinpoet.*
import kotlin.io.path.Path


fun generateRawDataClass(configuration: DynamicConfiguration) {

    var params = ArrayList<ParameterSpec>()
    var props = ArrayList<PropertySpec>()
    var codeBlocks = ArrayList<CodeBlock>()

    val nullableStringClass = String::class.asTypeName().copy(true)

    configuration.fields.forEach {

        // looks like CSV mapper is case-insensitive, so headers in CSV should be explicitly defined as annotations
        val ann = AnnotationSpec
            .builder(JsonProperty::class.java)
            .addMember("\"${it.sourceField}\"")
            .useSiteTarget(AnnotationSpec.UseSiteTarget.FIELD).build()

        // It seems to be the only way to pass nullable string class. (String?)

        val param = ParameterSpec.builder(it.sourceField.toString(), nullableStringClass).build()
        params.add(param)

        val prop = PropertySpec.builder(it.sourceField.toString(), nullableStringClass)
            .mutable()
            .initializer(it.sourceField.toString())
            .addAnnotation(ann)
            .build()

        props.add(prop)

        // amount of default parameters in constructor should be defined by amount of fields in conf.
        val block = CodeBlock.builder().add("%L", "null").build()
        codeBlocks.add(block)
    }
    // prepare constructor function for dataclass
    val constructorFunction = FunSpec.constructorBuilder().callThisConstructor(codeBlocks).build()

    // builder function for dataclass. It uses KotlinPoet for code generation
    val file = FileSpec.builder("", "RawData")
        .addType(
            TypeSpec.classBuilder("RawData")
                .addModifiers(KModifier.DATA)
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameters(params)
                        .build()
                )
                .addFunction(constructorFunction)
                .addProperties(props)
                .build()
        )
        .build()

    val path = Path("src/main/kotlin/Models")
    file.writeTo(path)
}