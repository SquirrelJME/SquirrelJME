// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.lcduidemo;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * This is a demo for lists.
 *
 * @since 2018/12/02
 */
public class Lists
	extends MIDlet
{
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	protected void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	protected void startApp()
		throws MIDletStateChangeException
	{
		// Setup list
		List list = new List("List Demo", List.MULTIPLE);
		
		// Exit command
		list.addCommand(Exit.command);
		list.setCommandListener(new Exit());
		
		// Add things to the list
		list.append("Sciurus aberti", null);
		list.append("Sciurus aestuans", null);
		list.append("Sciurus alleni", null);
		list.append("Sciurus anomalus", null);
		list.append("Sciurus arizonensis", null);
		list.append("Sciurus aureogaster", null);
		list.append("Sciurus carolinensis", null);
		list.append("Sciurus colliaei", null);
		list.append("Sciurus deppei", null);
		list.append("Sciurus flammifer", null);
		list.append("Sciurus gilvigularis", null);
		list.append("Sciurus granatensis", null);
		list.append("Sciurus griseus", null);
		list.append("Sciurus ignitus", null);
		list.append("Sciurus igniventris", null);
		list.append("Sciurus kaibabensis", null);
		list.append("Sciurus lis", null);
		list.append("Sciurus nayaritensis", null);
		list.append("Sciurus niger", null);
		list.append("Sciurus oculatus", null);
		list.append("Sciurus pucheranii", null);
		list.append("Sciurus pyrrhinus", null);
		list.append("Sciurus richmondi", null);
		list.append("Sciurus sanborni", null);
		list.append("Sciurus spadiceus", null);
		list.append("Sciurus stramineus", null);
		list.append("Sciurus variegatoides", null);
		list.append("Sciurus vulgaris", null);
		list.append("Sciurus yucatanensis", null);
		list.append("Squirrels are cute!", null);
		list.append("Chipmunks too!", null);
		list.append("Rats are soft!", null);
		list.append("And mice are nice!", null);
		
		// Use a certain font for some items
		list.setFont(2,
			Font.getFont(Font.FACE_SYSTEM, 0, Font.SIZE_SMALL));
		list.setFont(4,
			Font.getFont(Font.FACE_MONOSPACE, 0, Font.SIZE_MEDIUM));
		list.setFont(6,
			Font.getFont(Font.FACE_PROPORTIONAL, 0, Font.SIZE_MEDIUM));
		list.setFont(8,
			Font.getFont(Font.FACE_MONOSPACE, 0, Font.SIZE_SMALL));
		
		// Select some items for us
		list.setSelectedIndex(1, true);
		list.setSelectedIndex(6, true);
		list.setSelectedIndex(12, true);
		
		// Disable some items
		list.setEnabled(8, false);
		list.setEnabled(12, false);
		
		// Display the list
		Display.getDisplay(this).setCurrent(list);
	}
}

