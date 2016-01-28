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
import beast.app.beauti.BeautiSubTemplate;
import beast.app.draw.ExtensionFileFilter;
import beast.core.BEASTInterface;
import beast.core.Description;
import beast.core.Input;
import beast.core.Input.Validate;
import beast.evolution.alignment.Alignment;
import beast.evolution.alignment.FilteredAlignment;
import beast.evolution.datatype.DataType;
import beast.evolution.datatype.StandardData;
import beast.evolution.datatype.UserDataType;
import beast.util.NexusParser;
import correlatedcharacters.polycharacter.CompoundAlignment;

@Description("Class for creating a Compound Alignment, which can be used to model correlated evolution of separate Aligment data.")
public class BeautiCompoundAlignmentProvider extends BeautiAlignmentProvider {
	public Input<BeautiSubTemplate> sitetemplate = new Input<BeautiSubTemplate>("sitetemplate",
			"template to be used for creating a new component site.", Validate.REQUIRED);

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
						throw new RuntimeException("this is not implemented yet.");
						// newAlignments.add(alignment); //FIXME: CLONE! DON'T
						// RE-USE!
						// return newAlignments;
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
					// Add each site as a separate alignment.
					for (int i = parser.m_alignment.getSiteCount(); i > 0; --i) {
						FilteredAlignment site = new FilteredAlignment();
						site.initByName("id", parser.m_alignment.getID() + "_" + String.valueOf(i), "data",
								parser.m_alignment, "filter", String.valueOf(i), "dataType",
								parser.m_alignment.dataTypeInput.get(), "userDataType",
								parser.m_alignment.userDataTypeInput.get());
						site.setID(parser.m_alignment.getID() + "_" + String.valueOf(i));
						System.out.printf("%s\n", site.toString());
						doc.addPlugin(site);
						if (site.getDataType() instanceof BEASTInterface) {
							doc.addPlugin((BEASTInterface) site.getDataType());
						}
						componentAlignments.add(site);
						doc.addAlignmentWithSubnet(site, sitetemplate.get());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Loading of " + fileName + " failed: " + ex.getMessage());
					return null;
				}
			}
		}
		// FIXME: Here, we would like a dialogue showing the possible
		// components, for selection.

		// Build a compound alignment from the selected components, and return
		// it.
		CompoundAlignment compoundAlignment = new CompoundAlignment(componentAlignments.get(0));
		// compound alignment needs an ID that does not exist yet.
		int i = 0;
		for (int j = 0; j < doc.alignments.size(); ++j) {
			Alignment alignment = doc.alignments.get(j);
			if (alignment.getID() == ("compound" + String.valueOf(i))) {
				++i;
				j = 0;
			}
		}
		compoundAlignment.setID("compound" + String.valueOf(i));
		doc.addPlugin(compoundAlignment);
		partitions.add(compoundAlignment);
		doc.addAlignmentWithSubnet(compoundAlignment, template.get());
		System.out.printf("%s", partitions.get(0).getID());
		return partitions;
	}

}
