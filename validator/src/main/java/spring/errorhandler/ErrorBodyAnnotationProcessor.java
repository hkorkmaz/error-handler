package spring.errorhandler;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes("spring.errorhandler.ErrorBody")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class ErrorBodyAnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        var result =  true;
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(ErrorBody.class)) {
            ExecutableElement typed = (ExecutableElement) annotatedElement;

            if (typed.getParameters().size() > 0) {
                error(typed, " must not contain parameters");
                result = false;
            }

            if (typed.getReturnType().getKind() == TypeKind.VOID) {
                error(typed, " must return something");
                result = false;
            }
        }

        return result;
    }

    private void error(Element e, String msg, Object... args) {
        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }

}
