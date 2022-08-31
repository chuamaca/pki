package com.udemy.pki.core;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.udemy.pki.bean.CertificadoUdemy;

public class CertificateStore {
	
	
	//Metodos que nos permita leer nuestro archivo PFX
	
	public static CertificadoUdemy getCertificateFromFile(String patch, String key) {
		
		CertificadoUdemy certificado= new CertificadoUdemy();
		
		
		try {
			
			//CREAR NUESTRO JKS
			KeyStore jks = KeyStore.getInstance("PKCS12");
			
			InputStream in = new FileInputStream(patch);
			jks.load(in, key.toCharArray());
			in.close();
			
			
			//COntener el alisa que genera
			
			String aliasJks = jks.aliases().nextElement();
			PrivateKey pk = (PrivateKey) jks.getKey(aliasJks, key.toCharArray());
			
			//PARA GUARDAR NUESTRA CADENA DE CERTIFICACION
			Certificate [] chain= jks.getCertificateChain(aliasJks);
			
			//Tener el certicado principal
			X509Certificate oPublicCertificate = (X509Certificate) chain[0];
			
			certificado.setAlias(oPublicCertificate.getSubjectDN().getName());
			certificado.setPublicCertificate(oPublicCertificate);
			
			certificado.setPrivateKey(pk);
			certificado.setCertificateChain(chain);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return certificado;
	}
	
	//Metodo para listar todo nuestro certificados.
	public static List<CertificadoUdemy> ListCertificateFromStore() {
		
		List<CertificadoUdemy> listCertificadoUdemy = new ArrayList();
		
		
		try {
			
			KeyStore jks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
			jks.load(null, null);
			
			//Recorrer los certicados o ALIAS
			Enumeration<String> en = jks.aliases();
			
			while (en.hasMoreElements()) {
				CertificadoUdemy certificado= new CertificadoUdemy();
				String aliasKey = (String) en.nextElement();
				
				PrivateKey pk = (PrivateKey) jks.getKey(aliasKey, null);
				
				Certificate [] chain= jks.getCertificateChain(aliasKey);
				X509Certificate oPublicCertificate = (X509Certificate) chain[0];
				
				certificado.setAlias(oPublicCertificate.getSubjectDN().getName());
				certificado.setPublicCertificate(oPublicCertificate);
				certificado.setPrivateKey(pk);
				certificado.setCertificateChain(chain);
				
				listCertificadoUdemy.add(certificado);
				
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return listCertificadoUdemy;
	}

}
