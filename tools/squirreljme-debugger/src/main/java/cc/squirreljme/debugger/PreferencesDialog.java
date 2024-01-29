// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.nio.file.Path;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * Allows {@link Preferences} to be changed.
 *
 * @since 2024/01/27
 */
public class PreferencesDialog
	extends JDialog
{
	/** The preferences to edit. */
	protected final Preferences preferences;
	
	/**
	 * Initializes the dialog.
	 *
	 * @param __owner The owning window.
	 * @param __preferences The preferences to modify.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/29
	 */
	public PreferencesDialog(Window __owner, Preferences __preferences)
		throws NullPointerException
	{
		super(__owner);
		
		if (__preferences == null)
			throw new NullPointerException("NARG");
		
		this.preferences = __preferences;
		
		// Set title and icon
		this.setTitle("Preferences");
		Utils.setIcon(this);
		
		// Border layout is simple
		this.setLayout(new BorderLayout());
		
		// Setup sequence panel
		SequentialPanel sequence = new SequentialPanel(false);
		sequence.panel().setMinimumSize(
			new Dimension(400, 400));
		sequence.panel().setPreferredSize(
			new Dimension(400, 400));
		this.add(sequence.panel(), BorderLayout.CENTER);
		
		// Resume on connect?
		JCheckBox resumeOnConnect = new JCheckBox("");
		resumeOnConnect.setSelected(
			__preferences.isResumeOnConnect());
		resumeOnConnect.addActionListener((__event) -> {
			__preferences.setResumeOnConnect(
				resumeOnConnect.isSelected());
		});
		sequence.add(new KeyValuePanel(
			new JLabel("Resume on Connect"),
			resumeOnConnect));
		
		// Search paths
		JPanel classSearchPanel = new JPanel();
		classSearchPanel.setLayout(new BorderLayout());
		
		// The list of values
		JList<Path> classSearchPaths = new JList<Path>();
		classSearchPaths.setMinimumSize(new Dimension(100, 200));
		classSearchPaths.setPreferredSize(new Dimension(100, 200));
		classSearchPaths.setListData(
			new Vector<>(__preferences.getClassSearchPath()));
		
		// Buttons for actions
		JPanel classSearchButtons = new JPanel();
		classSearchButtons.setLayout(new GridLayout(1, 3));
		
		// Add directory to search
		JButton addDirSearch = new JButton("Add Directory");
		
		// Add jar to search
		JButton addJarSearch = new JButton("Add Jar");
			
		// Remove from search
		JButton removeSearch = new JButton("Remove");
		
		// Add everything
		classSearchButtons.add(addDirSearch);
		classSearchButtons.add(addJarSearch);
		classSearchButtons.add(removeSearch);
		classSearchPanel.add(classSearchPaths, BorderLayout.CENTER);
		classSearchPanel.add(classSearchButtons, BorderLayout.PAGE_END);
		sequence.add(new KeyValuePanel(
			new JLabel("Class Search Paths"),
			classSearchPanel));
		
		// Done button which closes the dialog
		JButton done = new JButton("Done");
		done.addActionListener((__event) -> {
			this.setVisible(false);
			this.dispose();
		});
		this.add(done, BorderLayout.PAGE_END);
		
		// Pack
		this.pack();
	}
}
