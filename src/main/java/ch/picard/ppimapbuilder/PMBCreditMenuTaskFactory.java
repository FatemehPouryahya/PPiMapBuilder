package ch.picard.ppimapbuilder;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

import ch.picard.ppimapbuilder.ui.credits.CreditFrame;
import org.cytoscape.work.TaskMonitor;

import javax.swing.*;

/**
 * PPiMapBuilder app sub menu
 */
public class PMBCreditMenuTaskFactory extends AbstractTaskFactory {

	private final CreditFrame creditWindow;

	public PMBCreditMenuTaskFactory(CreditFrame creditWindow) {
		this.creditWindow = creditWindow;
	}

	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(
			new AbstractTask() {
				@Override
				public void run(TaskMonitor taskMonitor) throws Exception {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							creditWindow.setVisible(true);
						}
					});

				}
			}
		);
	}

}