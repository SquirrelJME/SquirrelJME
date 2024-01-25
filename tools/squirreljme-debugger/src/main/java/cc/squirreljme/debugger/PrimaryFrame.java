// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPCommandSet;
import cc.squirreljme.jdwp.JDWPCommandSetVirtualMachine;
import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.jdwp.JDWPIdKind;
import cc.squirreljme.jdwp.JDWPPacket;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;

/**
 * Primary display window.
 *
 * @since 2024/01/19
 */
public class PrimaryFrame
	extends JFrame
{
	/** The debugger state. */
	protected final DebuggerState state;
	
	/** The status panel. */
	protected final StatusPanel statusPanel;
	
	/** The toolbar for common actions. */
	protected final JToolBar toolBar;
	
	/** Thread view. */
	protected final ShownThreads shownThreads;
	
	/**
	 * Initializes the primary frame.
	 *
	 * @param __state The main debugging state.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public PrimaryFrame(DebuggerState __state)
		throws NullPointerException
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		// Store state for later usage
		this.state = __state;
		
		// To keep it more easily workable
		this.setMinimumSize(new Dimension(640, 480));
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		// Set window icon
		Utils.setIcon(this);
		
		// Insignia
		this.setTitle("SquirrelJME Debugger");
		
		// Default shortcut key mask, like Apple's Command button
		int metaMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		
		// About the debugger
		JMenuItem aboutItem = new JMenuItem("About...");
		aboutItem.setMnemonic('A');
		
		// Exit
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic('x');
		
		// File menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		fileMenu.add(aboutItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		
		// Capabilities view
		JMenuItem capsItem = new JMenuItem("Capabilities");
		capsItem.setMnemonic('C');
		capsItem.addActionListener(this::__inspectCapabilities);
		
		// Object view
		JMenuItem objectItem = new JMenuItem("Object");
		objectItem.setMnemonic('O');
		objectItem.setAccelerator(
			KeyStroke.getKeyStroke(Character.valueOf('o'), metaMask));
		
		// Type view
		JMenuItem typeItem = new JMenuItem("Type");
		typeItem.setMnemonic('y');
		typeItem.setAccelerator(
			KeyStroke.getKeyStroke(Character.valueOf('y'), metaMask));
		typeItem.addActionListener(this::__inspectType);
		
		// Thread group
		JMenuItem threadGroupItem = new JMenuItem("Thread Group");
		threadGroupItem.setMnemonic('G');
		threadGroupItem.setAccelerator(
			KeyStroke.getKeyStroke(Character.valueOf('g'), metaMask));
		
		// Thread
		JMenuItem threadItem = new JMenuItem("Thread");
		threadItem.setMnemonic('T');
		threadItem.setAccelerator(
			KeyStroke.getKeyStroke(Character.valueOf('t'), metaMask));
		threadItem.addActionListener(this::__inspectThread);
		
		// Frame
		JMenuItem frameItem = new JMenuItem("Frame");
		frameItem.setMnemonic('F');
		frameItem.setAccelerator(
			KeyStroke.getKeyStroke(Character.valueOf('f'), metaMask));
		
		// View menu
		JMenu viewMenu = new JMenu("View");
		viewMenu.setMnemonic('V');
		viewMenu.add(capsItem);
		viewMenu.add(objectItem);
		viewMenu.add(typeItem);
		viewMenu.add(threadGroupItem);
		viewMenu.add(threadItem);
		viewMenu.add(frameItem);
		
		// Menu bar
		JMenuBar mainMenu = new JMenuBar();
		mainMenu.add(fileMenu);
		mainMenu.add(viewMenu);
		
		// Use the menu finally
		this.setJMenuBar(mainMenu);
		
		// Setup toolbar
		JToolBar toolBar = new JToolBar();
		this.toolBar = toolBar;
		
		JButton viewClassDisk = PrimaryFrame.__barButton(toolBar,
			"View Class From Disk", "document-open");
		viewClassDisk.addActionListener(this::__viewClassDisk);
		
		JButton viewClassNet = PrimaryFrame.__barButton(toolBar,
			"View Class From Remote", "network-receive");
		viewClassNet.addActionListener(this::__viewClassNetwork);
		
		PrimaryFrame.__barButton(toolBar,
			"Copy Method to Clipboard", "edit-copy");
		
		toolBar.addSeparator();
		
		PrimaryFrame.__barButton(toolBar,
			"Resume Single Thread", "media-playback-start");
		PrimaryFrame.__barButton(toolBar,
			"Pause Single Thread", "media-playback-pause");
		
		toolBar.addSeparator();
		
		PrimaryFrame.__barButton(toolBar,
			"Resume All Threads", "weather-clear");
		PrimaryFrame.__barButton(toolBar,
			"Pause All Threads", "weather-snow");
		
		toolBar.addSeparator();
		
		PrimaryFrame.__barButton(toolBar,
			"Single Step", "go-down");
		PrimaryFrame.__barButton(toolBar,
			"Single Step in Method Only", "go-bottom");
		PrimaryFrame.__barButton(toolBar,
			"Step Over", "go-jump");
		PrimaryFrame.__barButton(toolBar,
			"Step Out To Parent Frame", "go-top");
		PrimaryFrame.__barButton(toolBar,
			"Continuously Single Step", "media-seek-forward");
		PrimaryFrame.__barButton(toolBar,
			"Perform X Single Steps", "media-skip-forward");
		PrimaryFrame.__barButton(toolBar,
			"Run To Caret", "input-mouse");
		
		toolBar.addSeparator();
		
		PrimaryFrame.__barButton(toolBar,
			"Run Until Exception Thrown", "weather-storm");
		
		// Add to the top
		this.add(toolBar, BorderLayout.PAGE_START);
		
		// Setup status panel
		StatusPanel statusPanel = new StatusPanel(__state);
		this.statusPanel = statusPanel;
		
		// Add that to the bottom
		this.add(statusPanel, BorderLayout.PAGE_END);
		
		// Show thread view on the left
		ShownThreads shownThreads = new ShownThreads(__state);
		this.add(shownThreads, BorderLayout.LINE_START);
		this.shownThreads = shownThreads;
		
		// For now just show some random method
		try (InputStream in = PrimaryFrame.class.getResourceAsStream(
			"test"))
		{
			if (in != null)
				this.showMethod(new JavaMethodViewer(
					ClassFile.decode(in).methods()[2]));
		}
		catch (IOException __ignored)
		{
		}
	}
	
	/**
	 * Shows the given method.
	 *
	 * @param __method The method to view.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	public void showMethod(MethodViewer __method)
		throws NullPointerException
	{
		if (__method == null)
			throw new NullPointerException("NARG");
		
		ShownMethod show = new ShownMethod(this.state, __method);
		this.add(show, BorderLayout.CENTER);
		
		// Pack
		this.pack();
	}
	
	/**
	 * Opens the inspector selection.
	 *
	 * @param __stored The stored items to look at.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	private void __inspect(StoredInfo<?> __stored)
		throws NullPointerException
	{
		Utils.inspect(this, this.state, __stored);
	}
	
	/**
	 * Shows the capabilities of the connected virtual machine.
	 *
	 * @param __event Not used.
	 * @since 2024/01/19
	 */
	private void __inspectCapabilities(ActionEvent __event)
	{
		InspectCapabilities inspect = new InspectCapabilities(this,
			this.state.capabilities);
		
		// Show it
		inspect.setLocationRelativeTo(null);
		inspect.setVisible(true);
	}
	
	/**
	 * Opens the thread inspector.
	 *
	 * @param __event Not used.
	 * @since 2024/01/20
	 */
	private void __inspectThread(ActionEvent __event)
	{
		this.__inspect(this.state.storedInfo.getThreads());
	}
	
	/**
	 * Inspects the given type.
	 *
	 * @param __action Not used.
	 * @since 2024/01/23
	 */
	private void __inspectType(ActionEvent __action)
	{
		// Get every single loaded class because this is really expected
		DebuggerState state = this.state;
		try (JDWPPacket out = state.request(JDWPCommandSet.VIRTUAL_MACHINE,
			JDWPCommandSetVirtualMachine.ALL_CLASSES))
		{
			// Wait for response
			state.sendThenWait(out, Utils.TIMEOUT,
				(__state, __reply) -> {
					// We need to store the actual classes
					StoredInfo<InfoClass> classes =
						this.state.storedInfo.getClasses();
				
					// Read in all the classes
					int count = __reply.readInt();
					for (int i = 0; i < count; i++)
					{
						// Ignore tag
						__reply.readByte();
						
						// Read the type ID
						JDWPId typeId = __reply.readId(
							JDWPIdKind.REFERENCE_TYPE_ID);
						
						// Ignore signature
						__reply.readString();
						
						// Ignore status
						__reply.readInt();
						
						// Setup class
						classes.get(__state, typeId);
					}
				});
		}
		
		// Inspect
		this.__inspect(state.storedInfo.getClasses());
	}
	
	/**
	 * Views a class stored on the disk.
	 *
	 * @param __event The event.
	 * @since 2024/01/22
	 */
	private void __viewClassDisk(ActionEvent __event)
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter(
			"Java Class Files", "class"));
		
		if (fileChooser.showOpenDialog(this) ==
			JFileChooser.APPROVE_OPTION)
		{
			try (InputStream in = Files.newInputStream(
				fileChooser.getSelectedFile().toPath(),
				StandardOpenOption.READ))
			{
				// Decode the class
				ClassFile classFile = ClassFile.decode(in);
				
				// Use standard viewer
				this.__viewClass(new JavaClassViewer(classFile));
			}
			catch (IOException __e)
			{
				Utils.throwableTraceDialog(this,
					"Failed to load class from disk", __e);
			}
		}
	}
	
	/**
	 * Views the given class.
	 *
	 * @param __viewer The class to view.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	private void __viewClass(ClassViewer __viewer)
		throws NullPointerException
	{
		if (__viewer == null)
			throw new NullPointerException("NARG");
		
		ShownClassDialog dialog = new ShownClassDialog(this,
			this.state, __viewer);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
	
	/**
	 * Views a class from the remote virtual machine.
	 *
	 * @param __event The event.
	 * @since 2024/01/22
	 */
	private void __viewClassNetwork(ActionEvent __event)
	{
		String option = (String)JOptionPane.showInputDialog(
			this,
			"Choose remote class",
			"Choose remote class",
			JOptionPane.QUESTION_MESSAGE,
			null,
			null,
			"java/lang/Class");
		
		// Was a class specified?
		if (option != null)
		{
			// If there are dots, it really should be slashes
			if (option.indexOf('.') >= 0)
				option = option.replace('.', '/');
			
			// Perform lookup
			ClassName className = new ClassName(option);
			this.state.lookupClass(className, (__info) -> {
				InfoClass lookAt;
				if (__info.length == 1)
					lookAt = __info[0];
				else
					lookAt = (InfoClass)JOptionPane.showInputDialog(
						this,
						"Select class:",
						"Multiple classes found",
						JOptionPane.QUESTION_MESSAGE,
						null,
						__info,
						__info[0]);
				
				// Look at the given class
				if (lookAt != null)
					this.__viewClass(new RemoteClassViewer(
						this.state, lookAt));
			}, (__e) -> {
				Utils.throwableTraceDialog(this,
					"Could not find class: " + className, __e);
			});
		}
	}
	
	/**
	 * Creates a toolbar button.
	 *
	 * @param __label The label to use.
	 * @param __tango The tango icon to use.
	 * @return The created button.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	private static JButton __barButton(String __label, String __tango)
		throws NullPointerException
	{
		if (__label == null || __tango == null)
			throw new NullPointerException("NARG");
		
		JButton button = new JButton();
		
		// Help
		button.setToolTipText(__label);
		button.setIcon(Utils.tangoIcon(__tango));
		
		return button;
	}
	
	/**
	 * Creates a toolbar button and adds it to the given toolbar.
	 *
	 * @param __toolBar The toolbar to add to.
	 * @param __label The label to use.
	 * @param __tango The tango icon to use.
	 * @return The created button.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	private static JButton __barButton(JToolBar __toolBar, String __label,
		String __tango)
		throws NullPointerException
	{
		if (__toolBar == null)
			throw new NullPointerException("NARG");
		
		JButton button = PrimaryFrame.__barButton(__label, __tango);
		
		__toolBar.add(button);
		
		return button;
	}
}
