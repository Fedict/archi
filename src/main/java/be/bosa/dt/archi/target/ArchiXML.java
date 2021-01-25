/*
 * Copyright (c) 2021, FPS BOSA DG DT
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package be.bosa.dt.archi.target;

import be.bosa.dt.archi.dao.DaoContent;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.opengroup.xsd.archimate._3.ApplicationComponent;
import org.opengroup.xsd.archimate._3.ElementType;
import org.opengroup.xsd.archimate._3.ElementsType;
import org.opengroup.xsd.archimate._3.LangStringType;
import org.opengroup.xsd.archimate._3.ModelType;
import org.opengroup.xsd.archimate._3.PreservedLangStringType;

/**
 * Writes the content to an Archi XML file
 * 
 * @author Bart Hanssens
 */
public class ArchiXML {
	private final OutputStream os;
	
	/**
	 * Create an 'element' node from content
	 * 
	 * @param content content item
	 * @return 
	 */
	private ElementType createComponent(DaoContent content) {
		ApplicationComponent component = new ApplicationComponent();
		component.setIdentifier(content.getId());
		
		LangStringType lt = new LangStringType();
		lt.setValue(content.getTitle());
		lt.setValue("en");
		component.getNameGroup().add(lt);

		PreservedLangStringType plt = new PreservedLangStringType();
		plt.setValue(content.getDescription());
		plt.setValue("en");	
		component.getDocumentation().add(plt);
		
		return component;
	}

	
	/**
	 * Write a list of content items to XML
	 * 
	 * @param contents list of content items
	 * @throws IOException 
	 * @throws JAXBException 
	 */
	public void write(List<DaoContent> contents) throws IOException, JAXBException {
		ModelType model = new ModelType();
		
		LangStringType lt = new LangStringType();
		lt.setValue("Demo");
		lt.setValue("en");
		model.getNameGroup().add(lt);

		ElementsType els = new ElementsType();
		for(DaoContent c: contents) {
			els.getElement().add(createComponent(c));
		}
		model.setElements(els);
		
		JAXBContext context = JAXBContext.newInstance(ModelType.class);
		Marshaller mar = context.createMarshaller();
		mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		
		QName qName = new QName("org.opengroup.xsd.archimate._3.ModelType", "model");
		JAXBElement<ModelType> root = new JAXBElement<>(qName, ModelType.class, model);

		mar.marshal(root, os);
	}
	
	/**
	 * Constructor
	 * 
	 * @param os 
	 */
	public ArchiXML(OutputStream os) {
		this.os = os;
	}
}
