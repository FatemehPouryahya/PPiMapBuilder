package ch.picard.ppimapbuilder.ui.querywindow.panel;

import ch.picard.ppimapbuilder.data.organism.Organism;
import ch.picard.ppimapbuilder.ui.util.HelpIcon;
import ch.picard.ppimapbuilder.ui.util.PMBUIStyle;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class OtherOrganismSelectionPanel {

	private static final long serialVersionUID = 1L;
	private final LinkedHashMap<Organism, JCheckBox> organisms;
	private final JPanel panSourceOtherOrganisms;

	public OtherOrganismSelectionPanel(JPanel parent, Color darkForeground, CompoundBorder panelBorder, CompoundBorder fancyBorder) {

		//parent.setLayout(new MigLayout());
		
		// Other organisms label
		JLabel lblHomologOrganism = new JLabel("Other organisms:");
		parent.add(lblHomologOrganism, "cell 0 2");

		// Other organisms Help Icon
		JLabel lblHelpOtherOrganism = new HelpIcon("Select here the other organism in which you want to search homologous interactions");
		lblHelpOtherOrganism.setHorizontalAlignment(SwingConstants.RIGHT);
		parent.add(lblHelpOtherOrganism, "cell 1 2");

		// Other organisms scrollpane containing a panel that will contain checkbox at display
		JScrollPane scrollPaneOtherOrganisms = new JScrollPane();
		scrollPaneOtherOrganisms.setViewportBorder(PMBUIStyle.emptyBorder);
		scrollPaneOtherOrganisms.setBorder(fancyBorder);
		parent.add(scrollPaneOtherOrganisms, "cell 0 3 2 1,grow");

		// Other organisms panel that will contain checkbox at display
		panSourceOtherOrganisms = new JPanel();
		panSourceOtherOrganisms.setBorder(PMBUIStyle.emptyBorder);
		panSourceOtherOrganisms.setBackground(Color.WHITE);
		scrollPaneOtherOrganisms.setViewportView(panSourceOtherOrganisms);
		panSourceOtherOrganisms.setLayout(new BoxLayout(panSourceOtherOrganisms,BoxLayout.Y_AXIS));
		
		organisms = new LinkedHashMap<Organism, JCheckBox>();
		
		/*setLayout(new BorderLayout());
		setBorder(new EmptyBorder(5, 5, 5, 5));
		organisms = new LinkedHashMap<Organism, JCheckBox>();
		final JLabel lblSourceDatabases = new JLabel("Organisms for homology:");
		add(lblSourceDatabases, BorderLayout.NORTH);

		panSourceOtherOrganisms = new JPanel();
		panSourceOtherOrganisms.setBackground(Color.white);
		panSourceOtherOrganisms.setBorder(PMBUIStyle.emptyBorder);

		panSourceOtherOrganisms.setLayout(new BoxLayout(panSourceOtherOrganisms, BoxLayout.Y_AXIS));

		// Source databases scrollpane containing a panel that will contain checkbox at display
		final JScrollPane scrollPaneSourceDatabases = new JScrollPane(panSourceOtherOrganisms);
		scrollPaneSourceDatabases.setViewportBorder(PMBUIStyle.emptyBorder);
		add(scrollPaneSourceDatabases, BorderLayout.CENTER);*/
	}

	/**
	 * Updates the database list with an list of String
 	 * Updates the organism list with an list of organism
	 * @param ogs
	 */
	public void updateList(List<Organism> ogs) {
		// Creation of the database list
		organisms.clear();
		panSourceOtherOrganisms.removeAll();
		for (Organism og : ogs) {
			JCheckBox j = new JCheckBox(og.getScientificName(), true);
			j.setBackground(Color.white);
			organisms.put(og, j);

			panSourceOtherOrganisms.add(j);
		}
	}

	/**
	 * Get the list of selected databases
	 *
	 * @return list of database values
	 */
	public List<Organism> getSelectedOrganisms() {
		ArrayList<Organism> organismList = new ArrayList<Organism>();

		// For each entry of the database linkedHashmap
		for (Entry<Organism, JCheckBox> entry : organisms.entrySet()) {
			if (entry.getValue().isSelected()) // If the checkbox is selected
			{
				organismList.add(entry.getKey()); // The database name is add into the list to be returned
			}
		}
		return organismList;
	}
	
	public LinkedHashMap<Organism, JCheckBox> getOrganisms() {
		return organisms;
	}

}
