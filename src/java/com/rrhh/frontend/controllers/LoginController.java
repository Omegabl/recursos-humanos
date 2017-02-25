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

import com.rrhh.backend.entities.Usuario;
import com.rrhh.backend.model.UsuarioFacadeLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author APRENDIZ
 */
@Named
@ViewScoped
public class LoginController implements Serializable {

	@EJB
	private UsuarioFacadeLocal EJBUsuario;
	private Usuario usuario;

	@PostConstruct
	public void init() {
		usuario = new Usuario();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void iniciarSesion() {
		Usuario us;

		try {
			us = EJBUsuario.iniciarSesion(usuario);
			if (us != null) {
				//Almacenar la sesión de JSF
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", us);
				FacesContext contex = FacesContext.getCurrentInstance();
				contex.getExternalContext().redirect("/rrhh/index.xhtml");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Usuario o contraseña incorrectos"));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Aviso", "Error!"));
		}

	}

	public void verificarSesion() {

		try {
			FacesContext context = FacesContext.getCurrentInstance();
			Usuario us = (Usuario) context.getExternalContext().getSessionMap().get("usuario");

			if (us == null) {
				context.getExternalContext().redirect("/rrhh/login.xhtml");
			}
		} catch (Exception e) {
		}
	}

	public void verificarSesionLogin() {

		try {
			FacesContext context = FacesContext.getCurrentInstance();
			Usuario us = (Usuario) context.getExternalContext().getSessionMap().get("usuario");
			if (us != null) {
				context.getExternalContext().redirect("/rrhh/index.xhtml");
			}
		} catch (Exception e) {
			// log para guardar un registro de error
		}

	}

	public String obtenerRolUsuario() {
		HttpSession sesion = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		Usuario u = (Usuario) sesion.getAttribute("usuario");
		return u.getRol();
	}

	public String mostrarNombreUsuario() {
		HttpSession sesion = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		Usuario u = (Usuario) sesion.getAttribute("usuario");
		return u.getNombre() + " " + u.getApellido();
	}

	public void cerrarSesion() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		context.getExternalContext().redirect("/rrhh/login.xhtml");
	}
}
