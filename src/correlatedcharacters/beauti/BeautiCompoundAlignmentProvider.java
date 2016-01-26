package correlatedcharacters.beauti;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import beast.app.beauti.Beauti;
import beast.app.beauti.BeautiAlignmentProvider;
import beast.app.beauti.BeautiDoc;
import beast.app.draw.ExtensionFileFilter;
import beast.core.BEASTInterface;
import beast.core.Description;
import beast.core.Input;
import beast.evolution.alignment.Alignment;
import beast.evolution.alignment.FilteredAlignment;
import beast.util.NexusParser;
import correlatedcharacters.polycharacter.CompoundAlignment;

@Description("Class for creating a Compound Alignment, which can be used to model correlated evolution of separate Aligment data.")
public class BeautiCompoundAlignmentProvider extends BeautiAlignmentProvider {
	@Override
	protected List<BEASTInterface> getAlignments(BeautiDoc doc) {
		// First, ask the user whether they want to create a new alignment from
		// file(s) or whether to clone an existing alignment.
		ArrayList<String> possibilities = new ArrayList<String>();
		possibilities.add("… from file(s)");
		final String cloning = "… by cloning existing Compound Alignment";
		for (String compoundAlignmentName : new String[] {}) {
			possibilities.add(cloning + compoundAlignmentName);
		}

		String s = possibilities.get(0);

		// Except, don't ask if there is only one option.
		if (possibilities.size() > 1) {
			s = (String) JOptionPane.showInputDialog(doc.getFrame(), "Create new Compound Alignment…",
					"Alignment Source", JOptionPane.QUESTION_MESSAGE, null, possibilities.toArray(), s);
		}

		if (s == null) {
			// The user did not want to do anything.
			return null;
		}
		if (s == possibilities.get(0)) {
			// The user wanted to load files.
			JFileChooser fileChooser = new JFileChooser(Beauti.g_sDir);
			String[] exts = { ".nex", ".nxs", ".nexus" };
			fileChooser.addChoosableFileFilter(new ExtensionFileFilter(exts, "Nexus file (*.nex)"));

			fileChooser.setDialogTitle("Load Alignments");
			fileChooser.setMultiSelectionEnabled(true);
			int rval = fileChooser.showOpenDialog(null);

			if (rval == JFileChooser.APPROVE_OPTION) {

				File[] files = fileChooser.getSelectedFiles();
				return getAlignments(doc, files);
			} else {
				return null;
			}
		} else {
			if (s.startsWith(cloning)) {
				// The user wanted to clone something.
				String cloneOriginal = s.substring(cloning.length());
				for (Alignment alignment : doc.alignments) {
					if (cloneOriginal == alignment.getID()) {
						List<BEASTInterface> newAlignments = new ArrayList<BEASTInterface>();
						newAlignments.add(alignment);
						return newAlignments;
					}
				}
				throw new RuntimeException();
			} else {
				// The user chose an option that does not exist.
				throw new RuntimeException();
			}
		}

	}

	@Override
	public List<BEASTInterface> getAlignments(BeautiDoc doc, File[] files) {
		List<BEASTInterface> partitions = new ArrayList<BEASTInterface>(1);
		// We shall read each file and get all individual sites from them.
		List<Alignment> componentAlignments = new ArrayList<Alignment>(files.length);
		for (File file : files) {
			String fileName = file.getName();
			if (fileName.toLowerCase().endsWith(".nex") || fileName.toLowerCase().endsWith(".nxs")
					|| fileName.toLowerCase().endsWith(".nexus")) {
				NexusParser parser = new NexusParser();
				try {
					parser.parseFile(file);
					if (parser.m_alignment.getSiteCount() < 1) {
						throw new IllegalArgumentException("Nexus file aligmnent was empty");
					}
					for (int i = parser.m_alignment.getSiteCount(); i > 0; --i) {
						FilteredAlignment site = new FilteredAlignment();
						site.initByName("data", parser.m_alignment, "filter", String.valueOf(i));
						componentAlignments.add(site);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Loading of " + fileName + " failed: " + ex.getMessage());
					return null;
				}
			}
			CompoundAlignment compoundAlignment = new CompoundAlignment(componentAlignments);
			partitions.add(compoundAlignment);
		}
		return partitions;
	}

}
