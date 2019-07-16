package hu.mktiti;

import java.io.PrintWriter;
import java.util.Map;
import java.util.function.Function;

interface ConstPrinter {

    @FunctionalInterface interface Factory extends Function<PrinterConf, ConstPrinter> {}

    String getDirPath();

    String getFilename();

    boolean isValid(final String variable);

    void print(final Map<String, String> variables, final PrintWriter target);

}