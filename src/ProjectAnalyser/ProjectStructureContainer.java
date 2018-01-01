package ProjectAnalyser;

import java.util.HashMap;
import java.util.List;

public class ProjectStructureContainer {
	private HashMap<String, List<String>> depenciesMap;
	private HashMap<String, List<String>> referencesMap;
	private int fileNumber;
	
	
	public ProjectStructureContainer(HashMap<String, List<String>> depenciesMap,
			HashMap<String, List<String>> referencesMap, int fileNumber) {
		super();
		this.depenciesMap = depenciesMap;
		this.referencesMap = referencesMap;
		this.fileNumber = fileNumber;
	}
	
	public void incrementFileNumbers() {
		fileNumber++;
	}
	
	/**
	 * @return the depenciesMap
	 */
	public HashMap<String, List<String>> getDepenciesMap() {
		return depenciesMap;
	}
	/**
	 * @return the referencesMap
	 */
	public HashMap<String, List<String>> getReferencesMap() {
		return referencesMap;
	}
	/**
	 * @return the fileNumber
	 */
	public int getFileNumber() {
		return fileNumber;
	}
	
	
}
