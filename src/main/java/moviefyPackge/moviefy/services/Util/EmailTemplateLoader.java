package moviefyPackge.moviefy.services.Util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.core.io.ClassPathResource;

public final class EmailTemplateLoader {
    private EmailTemplateLoader() {}

    public static EmailTemplate load(String pathOnClasspath) throws Exception {
        var res = new ClassPathResource(pathOnClasspath);
        var dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        var doc = dbf.newDocumentBuilder().parse(res.getInputStream());
        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();
        String ns = root.getNamespaceURI();

        String from     = getText(root, ns, "from");
        String fromName = getText(root, ns, "fromName");
        String subject  = getText(root, ns, "subject");
        String html     = getText(root, ns, "html");

        return new EmailTemplate(from, fromName, subject, html);
    }

    private static String getText(Element parent, String ns, String tag) {
        var n = parent.getElementsByTagNameNS(ns, tag);
        return (n.getLength() > 0) ? n.item(0).getTextContent() : "";
    }

    public static final class EmailTemplate {
        public final String from;
        public final String fromName;
        public final String subject;
        public final String html;
        public EmailTemplate(String from, String fromName, String subject, String html) {
            this.from = from; this.fromName = fromName; this.subject = subject; this.html = html;
        }
    }
}
