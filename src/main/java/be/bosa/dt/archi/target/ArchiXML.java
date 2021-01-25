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
import java.util.ArrayList;
import java.util.List;
import org.xmlbeam.XBProjector;

/**
 * Writes the content to an Archi XML file
 * 
 * @author Bart Hanssens
 */
public class ArchiXML {
	private final OutputStream os;
	private final XBProjector projector;

	public void write(List<DaoContent> contents) throws IOException {
		ArchiModel m = projector.io().fromURLAnnotation(ArchiModel.class);
		m.setName("Demo");
		m.setDescription("Generated");
		
		List<ArchiModelElement> els = new ArrayList<>();
		for(DaoContent c: contents) {
			ArchiModelElement el = projector.projectEmptyElement("element", ArchiModelElement.class);
			el.setId(c.getId());
			
			ArchiModelName n = projector.projectEmptyElement("name", ArchiModelName.class);
			n.setName(c.getTitle());
			n.setLang("en");

			el.setName(n);

			els.add(el);
		}
		m.setElements(els);
		projector.io().stream(os).write(m);
	}
	
	/**
	 * Constructor
	 * 
	 * @param os 
	 */
	public ArchiXML(OutputStream os) {
		this.os = os;
		projector = new XBProjector();
	}
}
