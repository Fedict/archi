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
package be.bosa.dt.archi;

import be.bosa.dt.archi.dao.DaoContent;
import be.bosa.dt.archi.sources.Confluence;
import be.bosa.dt.archi.sources.Source;
import be.bosa.dt.archi.target.ArchiXML;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 *
 * @author Bart.Hanssens
 */
@Command(name = "checksum", mixinStandardHelpOptions = true, version = "1.0",
         description = "Extracts (meta)data from wiki etc and convert it to Archimate.")
public class Mater implements Callable<Integer> {
	/*
	@Option(names = {"-s", "--source"}, description = "source type", defaultValue="confluence")
    private String src;
	*/
	@Option(names = {"-l", "--location"}, description = "Location of the data source", defaultValue="http://localhost")
    private String url;

	@Option(names = {"-u", "--user"}, description = "User name for the data source")
    private String user;

	@Option(names = {"-p", "--password"}, description = "Password for the data source")
    private String pass;

	@Option(names = {"-t", "--type"}, description = "Tag / info type", required = true)
    private String type;

	@Option(names = {"-f", "--file"}, description = "Write result to file")
    private Path path;

	@Override
	public Integer call() throws Exception {
		Source source = new Confluence(url, user, pass);

		List<DaoContent> content = source.getContent(type);
		if (content.isEmpty()) {
			return -1;
		}

		OutputStream os = (path != null) 
			? Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE, 
																	StandardOpenOption.TRUNCATE_EXISTING) 
			: System.out;

		ArchiXML xml = new ArchiXML(os);
		xml.write(content);

		return 0;
	}
	
	public static void main(String[] args) {
		int exitCode = new CommandLine(new Mater()).execute(args);
        System.exit(exitCode);
	} 
}
