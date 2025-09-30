package moviefyPackge.moviefy.services.Util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import moviefyPackge.moviefy.domain.Entities.XmlEntity;
import moviefyPackge.moviefy.enums.Xml.XmlImportType;
import moviefyPackge.moviefy.enums.Xml.XmlStatus;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Stream;


@Component
@RequiredArgsConstructor
public class XmlUtil {
    private static final Path STORAGE_ROOT = Paths.get("storage", "xml").toAbsolutePath();
    private final ResourceLoader resourceLoader;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public void validateXmlWithXsd(MultipartFile xmlFile, String xsdFileName) {
        try {

            Resource xsdResource = resourceLoader.getResource("classpath:" + xsdFileName);
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdResource.getFile());

            Validator validator = schema.newValidator();


            try (InputStream xmlInput = xmlFile.getInputStream()) { validator.validate(new StreamSource(xmlInput)); }

        }
        catch (IOException e) {
            throw new RuntimeException("I/O error during XML validation: " + e.getMessage(), e);
        }
        catch (Exception e) {
            throw new RuntimeException("XML validation failed: " + e.getMessage(), e);
        }
    }

    public <T> T unmarshal(MultipartFile xmlFile, Class<T> wrapperClass) {
        try (InputStream in = xmlFile.getInputStream()) {
            JAXBContext context = JAXBContext.newInstance(wrapperClass);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            Object obj = unmarshaller.unmarshal(in);
            return wrapperClass.cast(obj);
        } catch (Exception e) {
            throw new RuntimeException("Unmarshal failed: " + e.getMessage(), e);
        }
    }

    public <T> String marshal(T jaxbRoot, Class<?>... itemTypes) {
        try {
            JAXBContext context = JAXBContext.newInstance(
                    Stream.concat(Stream.of(jaxbRoot.getClass()), Arrays.stream(itemTypes))
                            .toArray(Class<?>[]::new)
            );
            StringWriter writer = new StringWriter();
            context.createMarshaller().marshal(jaxbRoot, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("XML Marshal failed: " + e.getMessage(), e);
        }
    }


    public <T> String marshal(T jaxbRoot,File output ,Class<?>... itemTypes) {
        try {
            JAXBContext context = JAXBContext.newInstance(
                    Stream.concat(Stream.of(jaxbRoot.getClass()), Arrays.stream(itemTypes))
                            .toArray(Class<?>[]::new)
            );
            StringWriter writer = new StringWriter();
            context.createMarshaller().marshal(jaxbRoot, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("Marshal failed: " + e.getMessage(), e);
        }
    }

    public void validateXmlString(String xml, String xsdClasspath) {
        try (InputStream xsdIn = resourceLoader.getResource("classpath:" + xsdClasspath).getInputStream()) {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new StreamSource(xsdIn));
            Validator v = schema.newValidator();
            v.validate(new StreamSource(new StringReader(xml)));
        } catch (Exception e) {
            throw new RuntimeException("XML string validation failed: " + e.getMessage(), e);
        }
    }

    public String transformXmlToHtml(String xml, String xslClasspath) {
        try (InputStream xslIn = resourceLoader.getResource("classpath:" + xslClasspath).getInputStream()) {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer(new StreamSource(xslIn));

            Source xmlSrc = new StreamSource(new StringReader(xml));
            StringWriter out = new StringWriter();
            transformer.transform(xmlSrc, new StreamResult(out));
            return out.toString();
        } catch (Exception e) {
            throw new RuntimeException("XSLT transform failed: " + e.getMessage(), e);
        }
    }


    public String saveUploadedFile(MultipartFile xmlFile, XmlImportType type) throws IOException {
        String relativePath = buildRelativeImportPath(type, xmlFile.getOriginalFilename());
        Path targetAbs = STORAGE_ROOT.resolve(relativePath).normalize();
        Files.createDirectories(targetAbs.getParent());
        try (InputStream in = xmlFile.getInputStream()) {
            Files.copy(in, targetAbs, StandardCopyOption.REPLACE_EXISTING);
        }
        return relativePath;
    }

    private void validatedOutputDirExists(File outFile) throws IOException{
        File parentDir = outFile.getParentFile();
        if (!parentDir.exists()){
            boolean isCreated = parentDir.mkdirs();
            if (!isCreated)
                throw new IOException("Critical Server Error: Failed to create output directory " + parentDir.getAbsolutePath());
        }
    }
    private String buildRelativeImportPath(XmlImportType type, String original) {
        String dateFolder = java.time.LocalDate.now().toString();
        String safe = safeName(original);
        String fileName = java.util.UUID.randomUUID() + "-" + safe;
        return Paths.get("imports", type.name().toLowerCase(), dateFolder, fileName).toString();
    }

    private String safeName(String original) {
        String base = (original == null || original.isBlank()) ? "upload.xml" : original;
        base = base.replace("\\", "/");
        base = base.substring(base.lastIndexOf('/') + 1);
        base = base.replaceAll("[^a-zA-Z0-9._-]", "_");
        return base.isBlank() ? "upload.xml" : base;
    }

    public XmlEntity saveReportInStorage(String bookingType, String report) throws IOException {
        String safeType = (bookingType == null ? "UNKNOWN" : bookingType.replaceAll("[^a-zA-Z0-9._-]", "_"));
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = safeType + "_report_" + timestamp + ".xml";
        Path filePath = STORAGE_ROOT.resolve(fileName).normalize();
        validatedOutputDirExists(filePath.toFile());
        Files.writeString(filePath, report, java.nio.charset.StandardCharsets.UTF_8);
        return XmlEntity.builder()
                .fileName(fileName)
                .opType(bookingType)
                .storagePath(filePath.toString())
                .status(XmlStatus.SUCCESS)
                .build();
    }
}