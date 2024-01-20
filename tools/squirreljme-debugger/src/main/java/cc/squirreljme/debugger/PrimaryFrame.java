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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

/**
 * Primary display window.
 *
 * @since 2024/01/19
 */
public class PrimaryFrame
	extends JFrame
{
	/** The debugger state. */
	protected final DebuggerState debuggerState;
	
	/** The status panel. */
	protected final StatusPanel statusPanel;
	
	/**
	 * Initializes the primary frame.
	 *
	 * @param __debuggerState The main debugging state.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public PrimaryFrame(DebuggerState __debuggerState)
		throws NullPointerException
	{
		if (__debuggerState == null)
			throw new NullPointerException("NARG");
		
		// Store state for later usage
		this.debuggerState = __debuggerState;
		
		// To keep it more easily workable
		this.setMinimumSize(new Dimension(640, 480));
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
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
		
		// Setup status panel
		StatusPanel statusPanel = new StatusPanel(__debuggerState);
		this.statusPanel = statusPanel;
		
		// Add that to the bottom
		this.add(statusPanel, BorderLayout.PAGE_END);
	}
	
	/**
	 * Opens the inspector on the item.
	 *
	 * @param __info The information to expect.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public void inspect(Info __info)
		throws NullPointerException
	{
		if (__info == null)
			throw new NullPointerException("NARG");
		
		// Depends on the type
		JDialog dialog;
		switch (__info.type)
		{
				// Thread
			case THREAD:
				dialog = new InspectThread(this, this.debuggerState,
					(InfoThread)__info);
				break;
				
				// Unknown, so ignore
			default:
				return;
		}
		
		// Show it
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
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
		if (__stored == null)
			throw new NullPointerException("NARG");
		
		InfoType type = __stored.type;
		
		// Check to see that we actually know stuff
		Info[] all = __stored.all();
		if (all.length == 0)
		{
			JOptionPane.showMessageDialog(this,
				String.format("There are no %s", type.description),
				"Nothing is known",
				JOptionPane.ERROR_MESSAGE,
				null);
			return;
		}
		
		// Pop up dialog asking what to inspect?
		Object result = JOptionPane.showInputDialog(this,
			String.format("Select %s to inspect", type.description),
			String.format("Choosing %s", type.description),
			JOptionPane.QUESTION_MESSAGE,
			null,
			all,
			all[0]);
		
		// When selected, show the inspector
		if (result != null)
			this.inspect((Info)result);
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
			this.debuggerState.capabilities);
		
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
		this.__inspect(this.debuggerState.storedInfo.getThread());
	}
}
