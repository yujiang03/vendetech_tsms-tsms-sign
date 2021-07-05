package com.vendetech.hr.service.impl.kit;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class PDFUtil {

    public static Map<String, AcroFields.Item> readAcrobatFields(String pdfPath) {
        PdfReader reader = null;
        ByteArrayOutputStream bos = null;
        try {
            reader = new PdfReader(pdfPath);
            bos = new ByteArrayOutputStream();
            PdfStamper ps = new PdfStamper(reader, bos);
            AcroFields s = ps.getAcroFields();
            bos.close();
            return s.getFields();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    Objects.requireNonNull(bos).close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
