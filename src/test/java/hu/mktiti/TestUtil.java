package hu.mktiti;

import java.io.IOException;
import java.io.Writer;

final class TestUtil {

    private TestUtil() {
        throw new AssertionError("Static util class");
    }

    static Writer nopWriter() {
        return new Writer() {
            @Override
            public void write(char[] chars, int i, int i1) throws IOException {}

            @Override
            public void flush() throws IOException {}

            @Override
            public void close() throws IOException {}
        };
    }

}