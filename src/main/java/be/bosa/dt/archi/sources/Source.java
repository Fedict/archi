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

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data source
 *
 * @author Bart Hanssens
 */
public abstract class Source {
	private final static Logger LOG = Logger.getLogger(Source.class.getName());

	private final HttpClient client;
	private final String server;
	private final String user;
	private final String pass;
	

	protected String getServer() {
		return server;
	}

	/**
	 * Get all content for a specific type of info
	 * 
	 * @param param type of information
	 * @return list of content
	 * @throws IOException
	 */
	public abstract List<DaoContent> getContent(String param) throws IOException;

	/**
	 * Make HTTP GET request and return the response body as string.
	 * Basic Authentication headers are sent if user name and password are not null
	 * 
	 * @param url url to GET
	 * @return string of response body
	 * @throws URISyntaxException
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	protected String makeHttpGET(String url) throws URISyntaxException, IOException, InterruptedException {
		URI uri = new URI(url);
		LOG.log(Level.INFO, "GET data from {0}", uri);
	
		HttpRequest.Builder builder = HttpRequest.newBuilder().GET().uri(uri);

		// Adding Basic Authenticator to the basic JDK 11 HttpClient does not seem to work:
		// header is only sent when promted for by the server, not proactively.
		if (user != null && pass != null) {
			String encoded = Base64.getEncoder().encodeToString((user + ":" + pass).getBytes());
			builder.setHeader("Authorization", "Basic " + encoded);
		}
	
		HttpResponse<String> resp = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
		if (resp.statusCode() != 200) {
			LOG.log(Level.SEVERE, "GET returned status {0}", resp.statusCode());
		}
		return resp.body();
	}

	/**
	 * Constructor
	 * @param server
	 * @param user
	 */
	public Source(String server, String user, String pass) {
		this.server = server;
		this.user = user;
		this.pass = pass;
		this.client = HttpClient.newBuilder()
							.cookieHandler(new CookieManager())
							.followRedirects(Redirect.NORMAL)
							.version(HttpClient.Version.HTTP_1_1)
							.build();
	}
}
