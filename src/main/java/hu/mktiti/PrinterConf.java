package hu.mktiti;

final class PrinterConf {

    enum Visibility {
        PUBLIC, PROTECTED, PACKAGE_PRIVATE, DEFAULT
    }

    final String packageName;
    final String className;
    final String comment;
    final Visibility visibility;
    final boolean useGetters;

    PrinterConf(String packageName, String className, String comment, Visibility visibility, boolean useGetters) {
        this.packageName = packageName;
        this.className = className;
        this.comment = comment;
        this.visibility = visibility;
        this.useGetters = useGetters;
    }

}