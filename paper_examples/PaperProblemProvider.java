package paper;

import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Problem;
import org.moeaframework.core.spi.ProblemProvider;

public class PaperProblemProvider extends ProblemProvider {

	@Override
	public Problem getProblem(String name) {
		if ("paper".equalsIgnoreCase(name)) {
			return new PaperProblem(250);
		}
		return null;
	}

	@Override
	public NondominatedPopulation getReferenceSet(String name) {
		return null;
	}
}
