package moviefyPackge.moviefy.enums.Xml;

public enum XmlOpType {
    IMPORT_DATA("IMPORT_DATA_%s"),
    EXPORT_DATA("EXPORT_DATA_%s"),
    GENERATE_REPORT("GENERATE_REPORT_%s");

    private final String template;

    XmlOpType(String template) {
        this.template = template;
    }

    public String format(String input) {
        return String.format(template, input);
    }
}