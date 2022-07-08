package paper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Csv {

	private static final String DELIMITER = ",";
	private Map<String, Map<String, Double>> data = new HashMap<>();

	public Csv(String filename) {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(DELIMITER);
				Map<String, Double> metrics = new HashMap<>();
				metrics.put("accuracy", Double.parseDouble(values[1]));
				metrics.put("accuracy_sd", Double.parseDouble(values[2]));
				metrics.put("f1_score", Double.parseDouble(values[3]));
				metrics.put("f1_score_sd", Double.parseDouble(values[4]));
				data.put(values[0], metrics);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Map<String, Double> getMetrics(String phenotype) {
		if (this.data.containsKey(phenotype)) {
			return this.data.get(phenotype);
		}
		return null;
	}
}
