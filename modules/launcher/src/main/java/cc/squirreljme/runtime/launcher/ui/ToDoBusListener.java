// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.launcher.ui;

import cc.squirreljme.jvm.mle.TaskShelf;
import cc.squirreljme.jvm.mle.brackets.TaskBracket;
import cc.squirreljme.jvm.mle.constants.StandardBusIds;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.lcdui.Display;

/**
 * Listens on the To-Do bus for incoming messages.
 *
 * @since 2024/08/13
 */
public class ToDoBusListener
	implements Runnable
{
	/** The display to show a message on. */
	protected final Display display;
	
	/**
	 * Initializes the bus listener.
	 *
	 * @param __display The display to place a message on.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/13
	 */
	public ToDoBusListener(Display __display)
		throws NullPointerException
	{
		if (__display == null)
			throw new NullPointerException("NARG");
		
		this.display = __display;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/13
	 */
	@Override
	public void run()
	{
		// Note
		Debugging.debugNote("Waiting for any TODOs...");
		
		// Setup basic data fields
		TaskBracket[] from = new TaskBracket[1];
		int dataLen = 512;
		byte[] data = new byte[dataLen];
		
		// Message loop
		for (;;)
		{
			// Read message from the bus
			from[0] = null;
			int rxLen = TaskShelf.busReceive(from, StandardBusIds.BUS_TODO,
				true, data, 0, dataLen);
			
			// Not valid?
			if (rxLen < 0)
			{
				Debugging.debugNote("Too large a TODO! (%d > %d)",
					rxLen, dataLen);
				
				continue;
			}
			
			// Read in where it was
			String inClass = null;
			String inMethod = null;
			String inMethodType = null;
			try (DataInputStream dis = new DataInputStream(
				new ByteArrayInputStream(data, 0, rxLen)))
			{
				inClass = dis.readUTF();
				inMethod = dis.readUTF();
				inMethodType = dis.readUTF();
			}
			catch (IOException __ignored)
			{
			}
			
			// Emit message
			for (int i = 0; i < 10; i++)
				Debugging.todoNote("TODO BUS: %s.%s %s",
					inClass, inMethod, inMethodType);
			
			// Show on screen
			this.display.setCurrent(
				new ToDoDialog(inClass, inMethod, inMethodType));
		}
	}
}
