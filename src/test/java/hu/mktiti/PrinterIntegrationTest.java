package hu.mktiti;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

abstract class PrinterIntegrationTest {

    protected Class<?> result(final PrinterConf conf) throws Exception {
        final Map<String, String> vars = new HashMap<>();
        vars.put("version", "1.0");
        return result(conf, vars);
    }

    protected Class<?> result(final PrinterConf conf, final Map<String, String> variables) throws Exception {
        final ConstPrinter printer = createPrinter(conf);
        final StringWriter writer = new StringWriter();

        final String filename = printer.getFilename();
        final String qualifiedName = conf.packageName + "." + conf.className;

        try (final PrintWriter target = new PrintWriter(writer)) {
            printer.print(variables, new PrintWriter(writer));
        }

        final String content = writer.toString();

        return compile(filename, qualifiedName, content);
    }

    abstract protected ConstPrinter createPrinter(final PrinterConf conf);

    abstract protected Class<?> compile(final String filename, final String qualifiedName, final String content) throws Exception;

}