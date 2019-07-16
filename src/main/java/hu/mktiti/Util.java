package hu.mktiti;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

final class Util {

    private static final List<String> JAVA_KEYWORDS = Arrays.asList(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch",
            "char", "class", "continue", "default", "do", "double", "else", "enum",
            "exports", "extends", "final", "finally", "float", "for", "if", "implements",
            "import", "instanceof", "int", "interface", "long", "module", "native",
            "new", "package", "private", "protected", "public", "requires", "return",
            "short", "static", "strictfp", "super", "switch", "synchronized", "this",
            "throw", "throws", "transient", "try", "void", "volatile", "while",
            "true", "null", "false", "var", "const", "goto"
    );

    private static final List<String> KOTLIN_KEYWORDS = Arrays.asList(
            // Hard keywords only
            "as", "break", "class", "continue", "do", "else",
            "false", "for", "fun", "if", "in", "interface",
            "is", "null", "object", "package", "return", "super",
            "this", "throw", "true", "try", "typealias", "typeof",
            "val", "var", "when", "while"
    );
    
    private Util() {
        throw new AssertionError("Util class cannot be instantiated");
    }

    static String escapeString(final String value) {
        final StringBuilder builder = new StringBuilder(value.length() + 10);
        builder.append('"');
        for (char c : value.toCharArray()) {
            switch (c) {
                case '\t':
                    builder.append("\\t");
                    break;
                case '\b':
                    builder.append("\\b");
                    break;
                case '\n':
                    builder.append("\\n");
                    break;
                case '\r':
                    builder.append("\\r");
                    break;
                case '\f':
                    builder.append("\\f");
                    break;
                case '\'':
                    builder.append("\\\'");
                    break;
                case '\"':
                    builder.append("\\\"");
                    break;
                case '\\':
                    builder.append("\\\\");
                    break;
                default:
                    builder.append(c);
                    break;
            }
        }
        builder.append('"');
        return builder.toString();
    }

    static boolean isValidJavaName(final String name) {
        return isValidName(name, JAVA_KEYWORDS,
                first -> Character.isLetter(first) || first == '_' || first == '$',
                rest -> Character.isLetterOrDigit(rest) || rest == '_' || rest == '$'
        );
    }

    static boolean isValidKotlinName(final String name) {
        return isValidName(name, KOTLIN_KEYWORDS,
                first -> Character.isLetter(first) || first == '_',
                rest -> Character.isLetterOrDigit(rest) || rest == '_'
        );
    }

    private static boolean isValidName(
            final String name,
            final List<String> keywords,
            final Predicate<Character> firstPred,
            final Predicate<Character> restPred
    ) {
        if (name == null || name.length() == 0 || keywords.contains(name)) {
            return false;
        }

        boolean first = true;
        for (char c : name.toCharArray()) {
            if (first) {
                if (firstPred.test(c)) {
                    first = false;
                } else {
                    return false;
                }
            } else if (!restPred.test(c)) {
                return false;
            }
        }

        return true;
    }

}