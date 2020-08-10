package com.example.template.controllers;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;


import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import org.xhtmlrenderer.pdf.ITextRenderer;
import com.lowagie.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class PDFServices {

    /**
     * Export pdf files via template
     * @param data data
     * @param templateFileName template file name
     * @throws Exception
     */
    public static ByteArrayOutputStream createPDF(Map<String,Object> data, String templateFileName) throws Exception {
        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        cfg.setClassForTemplateLoading(PDFServices.class,"/templates");
        ITextRenderer renderer = new ITextRenderer();
        OutputStream out = new ByteArrayOutputStream();
        try {
//            renderer.getFontResolver().addFont("/templates/font/SIMSUN.TTC", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Template template = cfg.getTemplate(templateFileName, "UTF-8");
            StringWriter writer = new StringWriter();


            Rectangle a4 = PageSize.A4;
//            renderer.getWriter().setPageSize(a4);
            template.process(data, writer);
            writer.flush();

            String html = writer.toString();
            renderer.setDocumentFromString(html);
//            renderer.getWriter().setPageSize(a4);
            renderer.getSharedContext().setBaseURL("images/");
            renderer.layout();
            renderer.createPDF(out, false);
            renderer.finishPDF();
            out.flush();
            return (ByteArrayOutputStream)out;
        } finally {
            if(out != null){
                out.close();
            }
        }
    }
}