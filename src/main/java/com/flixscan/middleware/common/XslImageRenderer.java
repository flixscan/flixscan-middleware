package com.flixscan.middleware.common;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;
import org.jboss.logging.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;

import javax.xml.transform.stream.StreamSource;

import java.util.Base64;


@ApplicationScoped
public class XslImageRenderer {
    private static final Logger LOGGER = Logger.getLogger(XslImageRenderer.class.getName());
    public void xmlToPdf() {
        try {
            // Setup input and output files
            File xmlfile = new File("D:\\flixscan\\flixscan-middleware\\src\\main\\java\\com\\flixscan\\middleware\\common\\template\\required.xml");
            File xsltfile = new File("D:\\flixscan\\flixscan-middleware\\src\\main\\java\\com\\flixscan\\middleware\\common\\template\\296x128_Template.xsl");
            // configure fopFactory as desired
            final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                synchronized (fopFactory) {
                    Fop fop = fopFactory.newFop(MimeConstants.MIME_PNG, foUserAgent, byteArrayOutputStream);
                    TransformerFactory factory = TransformerFactory.newInstance();
                    Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));
                    Source src = new StreamSource(xmlfile);
                    Result res = new SAXResult(fop.getDefaultHandler());
                    transformer.transform(src, res);
                    byteArrayOutputStream.flush(); // Flush the output stream
                    String base64String = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
                    LOGGER.info(base64String);
                }
            } finally {
                byteArrayOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(-1);
        }
    }
}
