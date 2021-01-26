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
package be.bosa.dt.archi.sources;

import be.bosa.dt.archi.dao.DaoContent;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Atlassion confluence
 *
 * @author Bart Hanssens
 */
public class Confluence extends Source {
	private final static Logger LOG = Logger.getLogger(Confluence.class.getName());

	private final static String API_PATH = "/wiki/rest/api";
	private final static String SEARCH = "/content/search";
	private final static String CONTENT = "/content";
	private final static String EXPAND = "expand=body.storage,metadata.labels,labels";
	private final static String CQL = "cql=label=";
	
	/**
	 * Get all content from label
	 * 
	 * @param label
	 * @return list of content
	 * @throws IOException
	 */
	@Override
	public List<DaoContent> getContent(String label) throws IOException {
		JsonReader reader;

		try {
			String resp = makeHttpGET(getServer() + API_PATH + SEARCH + "?" + CQL + label + "&" + EXPAND);
			reader = Json.createReader(new StringReader(resp));
		} catch (InterruptedException | URISyntaxException ex) {
			throw new IOException(ex);
		}
		
		JsonObject obj = reader.readObject();
		JsonArray items = obj.get("results").asJsonArray();
		LOG.log(Level.INFO, "Results {0}", items.size());
		
		List<DaoContent> lst = new ArrayList<>();

		for(JsonValue i: items) {
			DaoContent content = new DaoContent(); 
			JsonObject itemObj = i.asJsonObject();
			JsonValue id = itemObj.get("id");
			if (id != null) {
				content.setId("id-" + ((JsonString) id).getString());
			}
			JsonValue title = itemObj.get("title");
			if (title != null) {
				content.setTitle(((JsonString) title).getString());
			}
			JsonObject body = itemObj.getJsonObject("body");
			if (body != null) {
				JsonObject storage = body.get("storage").asJsonObject();
				if (storage != null) {
					JsonValue val = storage.get("value");
					if (val != null) {
						content.setDescription(((JsonString) val).getString());
					}
				}
			}

			lst.add(content);
		}

		return lst;
	}

	/**
	 * Constructor
	 * 
	 * @param server 
	 */
	public Confluence(String server, String user, String pass) {
		super(server, user, pass);
	}
}
