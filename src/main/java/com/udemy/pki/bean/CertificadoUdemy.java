package com.udemy.pki.bean;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class CertificadoUdemy {
	
	private PrivateKey privateKey;
	private Certificate[] certificateChain;
	private String alias;
	private X509Certificate publicCertificate;
	
	
	
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	public Certificate[] getCertificateChain() {
		return certificateChain;
	}
	public void setCertificateChain(Certificate[] certificateChain) {
		this.certificateChain = certificateChain;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public X509Certificate getPublicCertificate() {
		return publicCertificate;
	}
	public void setPublicCertificate(X509Certificate publicCertificate) {
		this.publicCertificate = publicCertificate;
	}
	
	

}
