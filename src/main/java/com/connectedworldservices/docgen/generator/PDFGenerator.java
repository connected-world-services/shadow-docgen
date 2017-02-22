package com.connectedworldservices.docgen.generator;

import com.connectedworldservices.docgen.exception.InternalServerErrorException;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

@Component
public class PDFGenerator {

    @Autowired
    TemplateImageProvider templateImageProvider;
    private CSSResolver cssResolver;
    private HtmlPipelineContext htmlContext;

    public InputStream generatePdfFromHtmlWithImages(String html) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();
            // Pipelines
            PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
            HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, pdf);
            CssResolverPipeline css = new CssResolverPipeline(cssResolver, htmlPipeline);

            // XML Worker
            XMLWorker worker = new XMLWorker(css, true);
            XMLParser p = new XMLParser(worker, Charset.forName("UTF-8"));
            p.parse(new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8"))));

            document.close();
        } catch (Exception ex) {
            throw new InternalServerErrorException(ex);
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    @PostConstruct
    private void initPdfGenerator() {
        cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
        htmlContext = new HtmlPipelineContext(null);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        htmlContext.setImageProvider(templateImageProvider);
    }

}
