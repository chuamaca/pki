package com.udemy.test;

import java.util.List;

import com.udemy.pki.bean.CertificadoUdemy;
import com.udemy.pki.core.CertificateStore;
import com.udemy.pki.util.Constante;

public class ServiceTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			try {
				
				/*
				CertificadoUdemy certificado= CertificateStore.getCertificateFromFile(Constante.CERTIFICADO, Constante.CLAVE);
				System.out.println(certificado.getAlias());
				System.out.println("---------------------------------");
				System.out.println(certificado.getPrivateKey().getAlgorithm());
				System.out.println("---------------------------------");
				System.out.println(certificado.getPublicCertificate().toString());
				*/
				
				List<CertificadoUdemy> listCertificadoUdemy = CertificateStore.ListCertificateFromStore();
				
				for (CertificadoUdemy certificadoUdemy: listCertificadoUdemy) {
					System.out.println(certificadoUdemy.getAlias());
					System.out.println(certificadoUdemy.getPublicCertificate().getIssuerDN());
					System.out.println(certificadoUdemy.getPublicCertificate().getNotAfter());
					System.out.println("---------------------------------");
				}
				
			}catch(Exception e) {
				
			}
	}

}
