package com.udemy.pki.core;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.udemy.pki.bean.CertificadoUdemy;

public class PadesFirma {
	
	
	public static byte[] firmaBasico (byte[] data, CertificadoUdemy certificado) throws Exception {
		
		try {
			
			PdfReader reader = new PdfReader(data);
			
			ByteArrayOutputStream nuevodocumento= new ByteArrayOutputStream();
			PdfStamper stp= PdfStamper.createSignature(reader, nuevodocumento, '\000', null, true);
			PdfSignatureAppearance sap = stp.getSignatureAppearance();
			
			sap.setCrypto(certificado.getPrivateKey(), certificado.getCertificateChain(), null, PdfSignatureAppearance.WINCER_SIGNED);
			
			sap.setReason("Firma Digital");
			sap.setLocation("Lima");
			sap.setVisibleSignature(new Rectangle(100,100,350,200), 1, "sig");
			stp.close();
			
			return nuevodocumento.toByteArray();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	
	
	public static byte[] firmaPdfAvanzado ( byte[] datadoc, CertificadoUdemy certificado) throws Exception {
		
		try {
			
			
			PdfReader reader = new PdfReader(datadoc);
			ByteArrayOutputStream nuevoDocumento= new ByteArrayOutputStream();
			PdfStamper stamper = PdfStamper.createSignature(reader, nuevoDocumento, '\000', null, true);
			PdfSignatureAppearance sap = stamper.getSignatureAppearance();
			
			PdfSignature signature = new PdfSignature(PdfName.ADOBE_PPKLITE, new PdfName("adbe.pkcs7.detached"));
            signature.setReason("Firma Digital");
            signature.setLocation("Lima");
			
			
            Date fechaFirma=new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fechaFirma);
            signature.setDate(new PdfDate(calendar));
            
            sap.setSignDate(calendar);
            sap.setCryptoDictionary(signature);
            
            String firmado = "Firmado por " + PdfPKCS7.getSubjectFields(certificado.getPublicCertificate()).getField("CN");
            String razon = "Motivo: " + "Firma Digital";
            String lugar = "Lugar: " + "Lima";
            SimpleDateFormat dateformatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss Z");
            String fecha = "Fecha: " + dateformatter.format(fechaFirma);
            String firmaH = firmado + '\n' + razon + '\n' + lugar + '\n' + fecha;
            sap.setLayer2Text(firmaH);
            sap.setVisibleSignature(new Rectangle(100, 100, 350, 200), 1, null);
            
            int contentEstimated = 8192;
            System.out.println(reader.getFileLength());
            HashMap<PdfName, Integer> exc = new HashMap<>();
            exc.put(PdfName.CONTENTS, new Integer(contentEstimated * 2 + 2));
            sap.preClose(exc);
            
            InputStream data = sap.getRangeStream();
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] buf = new byte[contentEstimated];
            int n;
            while ((n = data.read(buf)) > 0) {
                messageDigest.update(buf, 0, n);
            }

            byte[] hash = messageDigest.digest();
            Calendar calendario = Calendar.getInstance();
            
            PdfPKCS7 sgn = new PdfPKCS7(certificado.getPrivateKey(), certificado.getCertificateChain(), null, "SHA-256", null, false);
            byte[] sh = sgn.getAuthenticatedAttributeBytes(hash, calendario, null);
            sgn.update(sh, 0, sh.length);
            byte[] encodedSig = sgn.getEncodedPKCS7(hash, calendario, null, null);
			
            byte[] paddedSig = new byte[contentEstimated];
            System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);
            PdfDictionary pdfDic = new PdfDictionary();
            pdfDic.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
            sap.close(pdfDic);
			
            reader.close();
            nuevoDocumento.flush();
            nuevoDocumento.close();
			
			return nuevoDocumento.toByteArray();
			
		} catch (Exception e) {
			// TODO: handle exception
			
			e.printStackTrace();
			return null;
		}
		
	} 
	
	
	
	

}
