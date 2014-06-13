package ch.picard.ppimapbuilder.data.protein.ortholog;

import ch.picard.ppimapbuilder.data.protein.Protein;

/**
 * Protein model associated with a score.
 */
public class OrthologScoredProtein extends Protein {

	private static final long serialVersionUID = 1L;

	private final Double score;

	public OrthologScoredProtein(Protein protein, Double score) {
		super(protein.getUniProtId(), protein.getOrganism());
		this.score = score;
	}

	public Double getScore() {
		return score;
	}

}