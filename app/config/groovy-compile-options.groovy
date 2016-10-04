import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper

import static org.codehaus.groovy.control.CompilePhase.CONVERSION

/**
 * Adds custom compilation configuration to the groovy compiler.
 */
withConfig(configuration) {
    /** All classes will be statically compiled even if they do not have the `@CompileStatic` annotation. */
    ast(CompileStatic)
    /**
     * For annotation processors like ButterKnife and Dagger the annotated members need to be
     * package protected or greater access, but groovy will take these varaibles make them private
     * and generate automatic getters and setters. This configuration runs through the AST in the
     * CONVERSION phase and adds the `@PackageScope` annotation so these automatic getters and
     * setters do not get generated, and so you don't have to remember to add the annotation every
     * where you use a annotation processing annotation.
     */
    inline(phase: CONVERSION) { source, context, classNode ->
        source.AST.classes*.fields.flatten().each { field ->
            def found = field.annotations.find { it.classNode.name == 'InjectView' || it.classNode.name == 'Inject' || it.classNode.name == 'BindView'}

            if (found) {
                field.addAnnotation(new AnnotationNode(ClassHelper.make(PackageScope)))
            }
        }
    }
}
