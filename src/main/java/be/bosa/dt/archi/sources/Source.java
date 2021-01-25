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
import java.net.Authenticator;
import java.net.CookieManager;
import java.net.PasswordAuthentication;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.util.List;

/**
 * Data source
 *
 * @author Bart Hanssens
 */
public abstract class Source {
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
	 * Get HTTP client, usinf basic authentication when user and pass are set
	 * 
	 * @return 
	 */
	protected HttpClient getHttpClient() {
		HttpClient.Builder builder = HttpClient.newBuilder()
			.cookieHandler(new CookieManager())
			.followRedirects(Redirect.NORMAL)
			.version(HttpClient.Version.HTTP_1_1);
		
		if (user != null && pass != null) {
			builder.authenticator(
				new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(user, pass.toCharArray());
					}
				});
			}
        return builder.build();
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
	}
}
