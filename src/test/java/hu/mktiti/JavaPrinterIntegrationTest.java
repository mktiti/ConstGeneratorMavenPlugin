package hu.mktiti;

import com.itranswarp.compiler.JavaStringCompiler;

import java.lang.reflect.Modifier;
import java.util.Map;

public class JavaPrinterIntegrationTest extends PrinterIntegrationTestBase {

    @Override
    boolean isClassModifierValid(final PrinterConf.Visibility visibility, int modifiers) {
        return isMemberModifierValid(visibility, modifiers);
    }

    @Override
    boolean isMemberModifierValid(final PrinterConf.Visibility visibility, int modifiers) {
        if (visibility == PrinterConf.Visibility.PUBLIC) {
            return Modifier.isPublic(modifiers);
        } else {
            return !Modifier.isPrivate(modifiers) && !Modifier.isProtected(modifiers) && !Modifier.isPublic(modifiers);
        }
    }

    @Override
    protected ConstPrinter createPrinter(final PrinterConf conf) {
        return new JavaPrinter(conf);
    }

    @Override
    protected Class<?> compile(final String filename, final String qualifiedName, final String content) throws Exception {
        final JavaStringCompiler compiler = new JavaStringCompiler();
        final Map<String, byte[]> results = compiler.compile(filename, content);

        return compiler.loadClass(qualifiedName, results);
    }

}