import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Set;
import java.util.StringJoiner;

@SupportedAnnotationTypes("one.autremel.fearsome.network.Payload")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class PayloadAnnotationProcessor extends AbstractProcessor {
    private static final String NETWORK_PACKAGE = "one.autremel.fearsome.network";
    private static final String SERVER_PAYLOAD_CLASS_NAME = NETWORK_PACKAGE + ".IServerPayload";
    private static final String CLIENT_PAYLOAD_CLASS_NAME = NETWORK_PACKAGE + ".IClientPayload";
    private static final String CLASS_NAME = "Payloads";

    private static final String CLASS_TEMPLATE = """
package {package};

import java.util.List;

public class {class_name} {
    public static final List<ServerPayloadId<?>> SERVER_PAYLOAD_IDS = List.of(
{server_payload_ids}
    );
                
    public static final List<ClientPayloadId<?>> CLIENT_PAYLOAD_IDS = List.of(
{client_payload_ids}
    );
}
""";

    private final ArrayList<String>
            serverPayloads = new ArrayList<>(),
            clientPayloads = new ArrayList<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        var elementUtils = processingEnv.getElementUtils();
        var typeUtils = processingEnv.getTypeUtils();

        var serverPayloadType = typeUtils.erasure(elementUtils.getTypeElement(SERVER_PAYLOAD_CLASS_NAME).asType());
        var clientPayloadType = typeUtils.erasure(elementUtils.getTypeElement(CLIENT_PAYLOAD_CLASS_NAME).asType());

        for (var annotation : annotations) {
            var elements = roundEnv.getElementsAnnotatedWith(annotation);
            for (var element : elements) {
                if (!element.getKind().isClass()) continue;

                var name = ((TypeElement) element).getQualifiedName().toString();
                var found = false;

                for (var anInterface : ((TypeElement) element).getInterfaces()) {
                    var erased = typeUtils.erasure(anInterface);

                    if (processingEnv.getTypeUtils().isSameType(erased, serverPayloadType)) {
                        serverPayloads.add(name);
                        found = true;
                    } else if (processingEnv.getTypeUtils().isSameType(erased, clientPayloadType)){
                        clientPayloads.add(name);
                        found = true;
                    }

                    if (found) break;
                }

                if (!found) {
                    throw new RuntimeException(name + " is not inheriting IServerPayload or IClientPayload");
                }
            }
        }

        if (roundEnv.processingOver()) {
            writeCodeToFile();
        }

        return true;
    }

    private void writeCodeToFile() {
        try {
            var file = this.processingEnv.getFiler().createSourceFile(CLASS_NAME);

            var code = fillTemplate(serverPayloads, clientPayloads);
            try (var writer = new PrintWriter(file.openWriter())) {
                writer.println(code);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String toContent(ArrayList<String> payloads) {
        var joiner = new StringJoiner(",\n");
        for (var payload : payloads) {
            joiner.add("\t\t\t" + payload + ".ID");
        }

        return joiner.toString();
    }

    private String fillTemplate(ArrayList<String> serverPayloads, ArrayList<String> clientPayloads) {
        return CLASS_TEMPLATE
                .replace("{package}", NETWORK_PACKAGE)
                .replace("{class_name}", CLASS_NAME)
                .replace("{server_payload_ids}", toContent(serverPayloads))
                .replace("{client_payload_ids}", toContent(clientPayloads));
    }
}
