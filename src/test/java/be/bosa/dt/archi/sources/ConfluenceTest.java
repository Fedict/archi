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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;

/**
 *
 * @author Bart.Hanssens
 */
public class ConfluenceTest {
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(options().port(8080));
	
	private String getInput(String file) throws IOException {
		try(InputStream is = getClass().getClassLoader().getResourceAsStream(file)) {
			byte[] b = is.readAllBytes();
			return new String(b);
		}
	}

	@Test
	public void noResults() throws IOException {
		stubFor(get(urlEqualTo("/wiki/rest/api/content/search?cql=label=none"))
				.willReturn(aResponse()
					.withStatus(200)
					.withHeader("Content-Type", "application/json")
					.withBody(getInput("notfound.json"))));
		
		Source src = new Confluence("http://localhost:8080");
		List<DaoContent> res = src.getContent("none");
		
		assertTrue(res.isEmpty());
	}

	@Test
	public void nineResults() throws IOException {
		stubFor(get(urlEqualTo("/wiki/rest/api/content/search?cql=label=architect"))
				.willReturn(aResponse()
					.withStatus(200)
					.withHeader("Content-Type", "application/json")
					.withBody(getInput("found.json"))));
		
		Source src = new Confluence("http://localhost:8080");
		List<DaoContent> res = src.getContent("architect");
		
		assertTrue(res.size() == 9);
	}
	
	@Test
	public void checkID() throws IOException {
		stubFor(get(urlEqualTo("/wiki/rest/api/content/search?cql=label=architect"))
				.willReturn(aResponse()
					.withStatus(200)
					.withHeader("Content-Type", "application/json")
					.withBody(getInput("found.json"))));
		
		Source src = new Confluence("http://localhost:8080");
		List<DaoContent> res = src.getContent("architect");

		assertTrue(res.stream().anyMatch(r -> r.getId().equals("id-" + "227210374")));
	}
}
