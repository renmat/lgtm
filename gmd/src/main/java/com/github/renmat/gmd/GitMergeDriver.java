package com.github.renmat.gmd;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class GitMergeDriver {

	/**
	 * %O base version (parent of source and target)
	 * %A current branch version (source)
	 * %B target branch version(target)
	 * %P file name for storing merge result
	 * @param args extension %O %A %B %P
	 */
	public static void main(String[] args) throws Exception {
		String extension = args[0];
		Path oursPath = Paths.get(args[2]);
		String ours = Files.readString(oursPath);
		String thiers = Files.readString(Paths.get(args[3]));
		if("json".equals(extension)) {
			String merged = jsonMerge(ours, thiers);
			Files.write(oursPath, merged.getBytes());
		} else if("java".equals(extension)) {
			//TODO JDT AST merge
		}
	}

	private static String jsonMerge(String sourceJson, String targetJson) throws JsonMappingException, JsonProcessingException {
		JsonMapper jsonMapper = JsonMapper.builder().build();
		Object base = jsonMapper.readValue(sourceJson, Object.class);
		Object merged = jsonMapper.readerForUpdating(base).readValue(targetJson);
		return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(merged);
	}

}
