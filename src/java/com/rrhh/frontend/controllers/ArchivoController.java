/*
 * The MIT License
 *
 * Copyright 2017 APRENDIZ.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.rrhh.frontend.controllers;

import com.rrhh.backend.entities.Archivo;
import com.rrhh.backend.entities.Usuario;
import com.rrhh.backend.model.ArchivoFacadeLocal;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author APRENDIZ
 */
@Named
@ViewScoped
public class ArchivoController implements Serializable {

	@EJB
	private ArchivoFacadeLocal archivoEJB;
	private Archivo archivo;

	@PostConstruct
	public void init() {
		archivo = new Archivo();
	}
	private Part fileCedula;
	private Part fileEstudios;
	private Part fileExperiencia;
	private String nombre;
	private String pathReal;

	public String getPath() {
		return pathReal;
	}

	public void setPath(String path) {
		this.pathReal = path;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Part getFileCedula() {
		return fileCedula;
	}

	public void setFileCedula(Part fileCedula) {
		this.fileCedula = fileCedula;
	}

	public Part getFileEstudios() {
		return fileEstudios;
	}

	public void setFileEstudios(Part fileEstudios) {
		this.fileEstudios = fileEstudios;
	}

	public Part getFileExperiencia() {
		return fileExperiencia;
	}

	public void setFileExperiencia(Part fileExperiencia) {
		this.fileExperiencia = fileExperiencia;
	}

	public void upload() {
		//Cargue de cédula
		try {
			String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("archivos");
			path = path.substring(0, path.indexOf("\\build"));
			path = path + "\\archivos\\";
			this.nombre = fileCedula.getSubmittedFileName();
			System.out.println("Cédula: " + fileCedula.getSubmittedFileName());
			pathReal = "/UploadFile/archivos/" + this.nombre;
			path = path + this.nombre;
			InputStream in = fileCedula.getInputStream();

			byte[] data = new byte[in.available()];
			in.read(data);
			FileOutputStream out = new FileOutputStream(new File(path));
			out.write(data);
			in.close();
			out.close();
			archivo.setIdArchivo(1);
			archivo.setUrl(pathReal);
			archivo.setIdUsuario(obtenerIdUsuario());
			archivoEJB.create(archivo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Cargue de estudios
		try {
			String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("archivos");
			path = path.substring(0, path.indexOf("\\build"));
			path = path + "\\archivos\\";
			this.nombre = fileEstudios.getSubmittedFileName();
			System.out.println("Estudios: " + fileEstudios.getSubmittedFileName());
			pathReal = "/UploadFile/archivos/" + this.nombre;
			path = path + this.nombre;
			InputStream in = fileEstudios.getInputStream();

			byte[] data = new byte[in.available()];
			in.read(data);
			FileOutputStream out = new FileOutputStream(new File(path));
			out.write(data);
			in.close();
			out.close();
			archivo.setIdArchivo(2);
			archivo.setUrl(pathReal);
			archivo.setIdUsuario(obtenerIdUsuario());
			archivoEJB.create(archivo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Cargue de experiencia
		try {
			String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("archivos");
			path = path.substring(0, path.indexOf("\\build"));
			path = path + "\\archivos\\";
			this.nombre = fileExperiencia.getSubmittedFileName();
			System.out.println("Experiencia: " + fileExperiencia.getSubmittedFileName());
			pathReal = "/UploadFile/archivos/" + this.nombre;
			path = path + this.nombre;
			InputStream in = fileExperiencia.getInputStream();

			byte[] data = new byte[in.available()];
			in.read(data);
			FileOutputStream out = new FileOutputStream(new File(path));
			out.write(data);
			in.close();
			out.close();
			archivo.setIdArchivo(3);
			archivo.setUrl(pathReal);
			archivo.setIdUsuario(obtenerIdUsuario());
			archivoEJB.create(archivo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Usuario obtenerIdUsuario() {
		HttpSession sesion = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		Usuario u = (Usuario) sesion.getAttribute("usuario");
		return u/*.getIdUsuario()*/;
	}

	public Archivo getArchivo() {
		return archivo;
	}

	public void setArchivo(Archivo archivo) {
		this.archivo = archivo;
	}

	public static void send(String sujeto, String mensaje) throws UnsupportedEncodingException {

		final String user = "contactoseasena@gmail.com";//cambiará en consecuencia al servidor utilizado
		final String pass = "1068286sena";

//1st paso) Obtener el objeto de sesión
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", "smtp.gmail.com"); // envia 
		props.setProperty("mail.smtp.starttls.enable", "true");
		props.setProperty("mail.smtp.port", "25");
		props.setProperty("mail.smtp.starttls.required", "false");
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, pass);
			}
		});

//2nd paso)compose message
		try {
			BodyPart texto = new MimeBodyPart();
			texto.setText("Se informa que los archivos han sido recibidos");
			MimeMultipart multiparte = new MimeMultipart();
			multiparte.addBodyPart(texto);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user, "Juan David Moyano"));
			InternetAddress[] destinatarios = {
				new InternetAddress("jdmoyano-gutierrez@misena.edu.co")
			};

			message.setRecipients(Message.RecipientType.TO, destinatarios);
			message.setSubject(sujeto);
			message.setContent(multiparte, "text/html; charset=utf-8");

			//3rd paso)send message
			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}
}
