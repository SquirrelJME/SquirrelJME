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
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;
import java.util.function.BooleanSupplier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

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
	
	/** The class search paths. */
	protected final JList<Path> classSearchPaths;
	
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
		JCheckBox resumeOnConnect = PreferencesDialog.__settingsCheck(
			__preferences::isResumeOnConnect,
			__preferences::setResumeOnConnect);
		sequence.add(new KeyValuePanel(
			new JLabel("Resume on Connect"),
			resumeOnConnect));
		
		// Local classes only?
		// Resume on connect?
		JCheckBox localClassOnly = PreferencesDialog.__settingsCheck(
			__preferences::isLocalClassOnly,
			__preferences::setLocalClassOnly);
		sequence.add(new KeyValuePanel(
			new JLabel("Local Classes Only"),
			localClassOnly));
		
		// Search paths
		JPanel classSearchPanel = new JPanel();
		classSearchPanel.setLayout(new BorderLayout());
		
		// The list of values
		JList<Path> classSearchPaths = new JList<Path>();
		this.classSearchPaths = classSearchPaths;
		classSearchPaths.setMinimumSize(new Dimension(100, 200));
		classSearchPaths.setPreferredSize(new Dimension(100, 200));
		this.searchUpdate();
		
		// Scrolling view for the list
		JScrollPane classSearchScroll = new JScrollPane(
			classSearchPaths, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		classSearchScroll.setMinimumSize(
			new Dimension(100, 200));
		classSearchScroll.setPreferredSize(
			new Dimension(100, 200));
		
		// Buttons for actions
		JPanel classSearchButtons = new JPanel();
		classSearchButtons.setLayout(new GridLayout(1, 3));
		
		// Add directory to search
		JButton addDirSearch = new JButton("Add Directory");
		addDirSearch.addActionListener(
			(__event) -> this.searchAdd(true));
		
		// Add jar to search
		JButton addJarSearch = new JButton("Add Jar");
		addJarSearch.addActionListener(
			(__event) -> this.searchAdd(false));
			
		// Remove from search
		JButton removeSearch = new JButton("Remove");
		removeSearch.addActionListener(
			(__event) -> this.searchRemove());
		
		// Add everything
		classSearchButtons.add(addDirSearch);
		classSearchButtons.add(addJarSearch);
		classSearchButtons.add(removeSearch);
		classSearchPanel.add(classSearchScroll, BorderLayout.CENTER);
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
	
	/**
	 * Creates a checkbox which is used to change a boolean preference.
	 *
	 * @param __get The get method.
	 * @param __set The set method.
	 * @return The resultant checkbox.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/29
	 */
	private static JCheckBox __settingsCheck(BooleanSupplier __get,
		BooleanConsumer __set)
		throws NullPointerException
	{
		if (__get == null || __set == null)
			throw new NullPointerException("NARG");
		
		// Setup new box
		JCheckBox result = new JCheckBox();
		result.setSelected(__get.getAsBoolean());
		
		// Add update for value
		result.addActionListener((__event) -> {
			boolean set = result.isSelected();
			
			// Update value
			__set.accept(set);
			result.setSelected(set);
		});
		
		// Return it
		return result;
	}
	
	/**
	 * Removes an item from the search.
	 *
	 * @since 2024/01/29
	 */
	public void searchRemove()
	{
		// Remove the selected item from the list
		Path selected = this.classSearchPaths.getSelectedValue();
		if (selected != null)
			this.preferences.getClassSearchPath().remove(selected);
		
		// Update
		this.searchUpdate();
	}
	
	/**
	 * Adds a search entry.
	 *
	 * @param __dir If {@code true} then a directory will be added.
	 * @since 2024/01/29
	 */
	public void searchAdd(boolean __dir)
	{
		// Are we adding a directory or a file?
		JFileChooser chooser = new JFileChooser();
		if (__dir)
		{
			chooser.setDialogTitle("Select Directory");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		else
		{
			chooser.setDialogTitle("Select File");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setFileFilter(
				new FileNameExtensionFilter("Jar Files",
					"zip", "jar", "kjx"));
			chooser.setAcceptAllFileFilterUsed(true);
		}
		
		// Shared between the two
		chooser.setCurrentDirectory(
			Paths.get(System.getProperty("user.dir")).toFile());
		
		// Show it and request the file
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			// Try to get the file
			File file = chooser.getSelectedFile();
			
			// Add to the preferences
			if (file != null)
			{
				// Add in
				this.preferences.getClassSearchPath()
					.add(file.toPath().toAbsolutePath());
				
				// Update
				this.searchUpdate();
			}
		}
	}
	
	/**
	 * Updates the class search list.
	 *
	 * @since 2024/01/29
	 */
	public void searchUpdate()
	{
		// Replace list with what is currently set
		this.classSearchPaths.setListData(
			new Vector<>(this.preferences.getClassSearchPath()));
		
		// Redraw
		Utils.revalidate(this.classSearchPaths);
	}
}
