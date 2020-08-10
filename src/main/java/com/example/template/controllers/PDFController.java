package com.example.template.controllers;

import com.example.template.Repostiories.UserRepostiory;

import java.io.File;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.platform.operation.ExecutionContext;
import com.adobe.platform.operation.auth.Credentials;
import com.adobe.platform.operation.exception.SdkException;
import com.adobe.platform.operation.exception.ServiceApiException;
import com.adobe.platform.operation.exception.ServiceUsageException;
import com.adobe.platform.operation.io.FileRef;
import com.adobe.platform.operation.pdfops.CreatePDFOperation;
import com.adobe.platform.operation.pdfops.options.createpdf.CreatePDFOptions;
import com.adobe.platform.operation.pdfops.options.createpdf.PageLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
public class PDFController {



    @Autowired
    UserRepostiory UserRepostiory;


    @RequestMapping(value = "/generatorPDF")
    public static void generatorPDF(HttpServletResponse response) throws Exception {

        ByteArrayOutputStream baos = null;
        OutputStream out = null;
        try {
            Map<String,Object> data = new HashMap<>();
            data.put("name", "nikhil");


            baos = PDFServices.createPDF(data, "template1.ftl");;
            response.setContentType( "application/x-msdownload");
            String fileName = URLEncoder.encode("dowload.pdf", "UTF-8");

            response.setHeader( "Content-Disposition", "attachment;filename=" + fileName);
            out = response.getOutputStream();
            baos.writeTo(out);
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Export failed:" + e.getMessage());
        } finally{
            if(baos != null){
                baos.close();
            }
            if(out != null){
                out.close();
            }
        }
    }

    @RequestMapping(value = "/genPDF")
    public void genPDF(){

        try {

            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("pdftools-api-credentials.json")
                    .build();

            //Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials);
            CreatePDFOperation htmlToPDFOperation = CreatePDFOperation.createNew();

            // Set operation input from a source file.
            FileRef source = FileRef.createFromLocalFile("src/main/resources/templates/helloworld.ftl",
                    CreatePDFOperation.SupportedSourceFormat.DOC.getMediaType());
            htmlToPDFOperation.setInput(source);
            // Provide any custom configuration options for the operation.
            setCustomOptions(htmlToPDFOperation);

            // Execute the operation.
            FileRef result = htmlToPDFOperation.execute(executionContext);

            // Save the result to the specified location.
            result.saveAs("output/createPDFFromDynamicHtmlOutput.pdf");

        } catch (ServiceApiException | IOException | SdkException | ServiceUsageException | JSONException ex) {
            System.out.println("Exception encountered while executing operation"+ex);
        }

    }
    private static void setCustomOptions(CreatePDFOperation htmlToPDFOperation) throws JSONException {
        // Define the page layout, in this case an 8 x 11.5 inch page (effectively portrait orientation).
        PageLayout pageLayout = new PageLayout();
        pageLayout.setPageSize(8, 11.5);

        //Set the dataToMerge field that needs to be populated in the HTML before its conversion
        JSONObject dataToMerge = new JSONObject();
        dataToMerge.put("name","nikhil");
//        dataToMerge.put("sub_title","Easily integrate PDF actions within your document workflows.");

        // Set the desired HTML-to-PDF conversion options.
        CreatePDFOptions htmlToPdfOptions = CreatePDFOptions.htmlOptionsBuilder()
                .includeHeaderFooter(true)
                .withPageLayout(pageLayout)
                .withDataToMerge(dataToMerge)
                .build();
        htmlToPDFOperation.setOptions(htmlToPdfOptions);
    }


}

