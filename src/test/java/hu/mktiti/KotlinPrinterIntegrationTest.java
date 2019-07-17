package hu.mktiti;

import org.jetbrains.kotlin.cli.common.ExitCode;
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments;
import org.jetbrains.kotlin.cli.common.messages.MessageCollector;
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer;
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector;
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler;
import org.jetbrains.kotlin.config.Services;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class KotlinPrinterIntegrationTest extends PrinterIntegrationTestBase {

    @Rule
    public final TemporaryFolder tempDir = new TemporaryFolder();

    @Override
    boolean isClassModifierValid(final PrinterConf.Visibility visibility, int modifiers) {
        return Modifier.isPublic(modifiers);
    }

    @Override
    boolean isMemberModifierValid(final PrinterConf.Visibility visibility, int modifiers) {
        return Modifier.isPublic(modifiers);
    }

    @Override
    protected ConstPrinter createPrinter(final PrinterConf conf) {
        return new KotlinPrinter(conf);
    }

    @Override
    protected Class<?> compile(final String filename, final String qualifiedName, final String content) throws Exception {
        final File baseDir = tempDir.newFolder();

        final File sourceFile = new File(baseDir, filename);
        try (final FileWriter writer = new FileWriter(sourceFile)) {
            writer.write(content);
        }

        final K2JVMCompiler compiler = new K2JVMCompiler();

        final K2JVMCompilerArguments args = compiler.createArguments();
        args.setIncludeRuntime(false);
        args.setNoStdlib(true);
        args.setNoReflect(true);
        args.setClasspath(System.getProperty("java.class.path"));
        args.setDestination(baseDir.getAbsolutePath());
        args.setFreeArgs(Collections.singletonList(sourceFile.getAbsolutePath()));

        MessageCollector collector = new PrintingMessageCollector(System.err, MessageRenderer.PLAIN_RELATIVE_PATHS, args.getVerbose());
        final ExitCode exitCode = compiler.exec(collector, Services.EMPTY, args);

        assertEquals(ExitCode.OK, exitCode);

        final ClassLoader classLoader = new URLClassLoader(new URL[] { baseDir.toURI().toURL() });

        return classLoader.loadClass(qualifiedName);
    }

}