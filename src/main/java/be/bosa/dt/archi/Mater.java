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
import java.io.File;
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
	@Option(names = {"-s", "--source"}, description = "confluence", defaultValue="confluence")
    private String src;

	@Option(names = {"-u", "--url"}, description = "link to the source", defaultValue="http://localhost")
    private String url;

	@Option(names = {"-t", "--type"}, description = "info type")
    private String type;

	@Option(names = {"-f", "--file"}, description = "write result to file")
    private File file;
	
	@Option(names = { "-h", "--help" }, usageHelp = true, description = "display help message")
    private boolean needHelp = false;

	@Override
	public Integer call() throws Exception {
		Source source = null;

		switch(src) {
			case "confluence": source = new Confluence(url);
		}
		List<DaoContent> content = source.getContent(type);

		if (file != null) {
			
		}
		return 0;
	}
	
	public static void main(String[] args) {
		int exitCode = new CommandLine(new Mater()).execute(args);
        System.exit(exitCode);
	} 

}
