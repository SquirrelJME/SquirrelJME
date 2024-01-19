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
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
		
		// Frame
		JMenuItem frameItem = new JMenuItem("Frame");
		frameItem.setMnemonic('F');
		frameItem.setAccelerator(
			KeyStroke.getKeyStroke(Character.valueOf('f'), metaMask));
		
		// View menu
		JMenu viewMenu = new JMenu("View");
		viewMenu.setMnemonic('V');
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
}
