package hu.mktiti;

import java.io.PrintWriter;
import java.util.Map;

final class KotlinPrinter implements ConstPrinter {

    private final PrinterConf conf;

    KotlinPrinter(final PrinterConf conf) {
        this.conf = conf;
    }

    @Override
    public String getDirPath() {
        return "kotlin/generated";
    }

    @Override
    public String getFilename() {
        return conf.className + ".kt";
    }

    @Override
    public boolean isValid(final String variable) {
        return Util.isValidKotlinName(variable);
    }

    @Override
    public void print(final Map<String, String> variables, final PrintWriter target) {
        target.print("@file:JvmName(\"");
        target.print(conf.className);
        target.println("\")\n");

        target.print("package ");
        target.print(conf.packageName);
        target.println("\n");

        if (conf.comment != null) {
            target.print("// ");
            target.println(conf.comment);
            target.println();
        }

        target.print(visibilityString(conf.visibility));
        target.print("object ");
        target.print(conf.className);
        target.println(" {\n");

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            target.println(propertyString(entry.getKey(), entry.getValue()));
        }

        target.println("}");

    }

    private static String visibilityString(final PrinterConf.Visibility visibility) {
        return (visibility == PrinterConf.Visibility.PACKAGE_PRIVATE) ? "internal " : "public ";
    }

    private String propertyString(final String name, final String value) {
        if (conf.useGetters) {
            return propertyGetterString(name, value);
        } else {
            return propertyFieldString(name, value);
        }
    }

    private String propertyFieldString(final String name, final String value) {
        return "\t@JvmField " + visibilityString(conf.visibility)  + "val " + name + ": String = " + Util.escapeString(value) + "\n";
    }

    private String propertyGetterString(final String name, final String value) {
        return "\t@JvmStatic @get:JvmName(\"" + Util.getGetterName(name) + "\") "
                + visibilityString(conf.visibility)  + "val " + name + ": String = " + Util.escapeString(value) + "\n";
    }
}