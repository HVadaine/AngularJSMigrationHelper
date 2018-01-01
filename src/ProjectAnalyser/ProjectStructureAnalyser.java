package ProjectAnalyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class ProjectStructureAnalyser {
	private ProjectStructureContainer psc;
	final static Charset ENCODING = StandardCharsets.UTF_8;

	public ProjectStructureAnalyser(File rootDirectory) {
		HashMap<String, List<String>> depenciesMap = new HashMap<>();
		HashMap<String, List<String>> referencesMap = new HashMap<>();
		int fileNumber = 0;
		psc = new ProjectStructureContainer(depenciesMap, referencesMap, fileNumber);
		mapProjectStructure(rootDirectory);
	}

	public void mapProjectStructure(File rootDirectory) {
		Iterator<File> fileIterator = FileUtils.iterateFiles(rootDirectory, new SuffixFileFilter(".js"),
				TrueFileFilter.TRUE);
		fileIterator.forEachRemaining(file -> {
			if (file.getName().endsWith(".js")) {
				extractStructureFromAngularJSFile(file);
			}
			psc.incrementFileNumbers();
		});
	}

	public void extractStructureFromAngularJSFile(File angularJSFile) {
		String parsedFile = parseJSFile(angularJSFile);
		extractDepenciesAndReferences(
				parsedFile.replace("'", "").replace("\"", "").replace("[", "").replace("])", "").replace(";", ""));
	}

	public void extractDepenciesAndReferences(String parsedFile) {
		String[] splittedFile = parsedFile.split(",");
		
		if (splittedFile.length > 1) {
			String root = splittedFile[1];
			if (!psc.getDepenciesMap().containsKey(root)) {
				psc.getDepenciesMap().put(root, new ArrayList<>());
			}
			for (int i = 1; i < splittedFile.length; i++) {
				String dependecy = splittedFile[i];
				psc.getDepenciesMap().get(root).add(dependecy);
				if (!psc.getReferencesMap().containsKey(dependecy)) {
					psc.getReferencesMap().put(dependecy, new ArrayList<>());
				}
				psc.getReferencesMap().get(dependecy).add(root);
			}
		}
	}

	public String parseJSFile(File angularJSFile) {
		try {
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(angularJSFile)));
			String line = buffReader.readLine();
			StringBuilder fileText = new StringBuilder();
			boolean isModuleDeclarationStarted = false;
			while (line != null) {

				isModuleDeclarationStarted = line.startsWith("angular.module") || isModuleDeclarationStarted;

				if (isModuleDeclarationStarted) {
					if (line.contains(").")) {
						fileText.append(line.split("\\)\\.")[0]);
					} else {
						fileText.append(line);
					}
				}
				if (line.contains(")")) {
					isModuleDeclarationStarted = false;
				}
				line = buffReader.readLine();
			}
			buffReader.close();
			return fileText.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return the psc
	 */
	public ProjectStructureContainer getPsc() {
		return psc;
	}
}
