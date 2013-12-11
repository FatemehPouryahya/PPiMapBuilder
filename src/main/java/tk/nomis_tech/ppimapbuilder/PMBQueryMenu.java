package tk.nomis_tech.ppimapbuilder;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import tk.nomis_tech.ppimapbuilder.ui.QueryWindow;
import tk.nomis_tech.ppimapbuilder.util.PsicquicRegistry;

/**
 * The interaction query menu
 */
public class PMBQueryMenu extends AbstractTask {

	@Override
	public void run(TaskMonitor arg0) throws Exception {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					QueryWindow qw = new QueryWindow();

					PsicquicRegistry reg = new PsicquicRegistry();
					qw.updateLists(reg.getServices());

					qw.setVisible(true);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null,
							"Unable to get PSICQUIC databases");
				}
			}
		});
	}

}
