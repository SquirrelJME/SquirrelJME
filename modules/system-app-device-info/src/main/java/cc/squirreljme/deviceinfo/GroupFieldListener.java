// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.deviceinfo;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

/**
 * Handles events for field and group navigation.
 *
 * @since 2025/12/06
 */
public class GroupFieldListener
	implements CommandListener
{
	/** The main list to go back to. */
	protected final GroupList main;
	
	/** The display that is used. */
	protected final Display display;
	
	/**
	 * Initializes the group field listener.
	 *
	 * @param __d The display used.
	 * @param __main The main group to return to.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/06
	 */
	public GroupFieldListener(Display __d, GroupList __main)
		throws NullPointerException
	{
		if (__d == null || __main == null)
			throw new NullPointerException("NARG");
		
		this.display = __d;
		this.main = __main;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/06
	 */
	@Override
	public void commandAction(Command __command, Displayable __displayable)
	{
		// Go back to the main list?
		if (__command == GroupList.BACK)
			this.display.setCurrent(this.main);
		
		// Exit the application?
		else if (__command == GroupList.EXIT)
			System.exit(0);
		
		// Select the given group
		else if (__command == GroupList.SELECT)
		{
			GroupList list = (GroupList)__displayable;
			FieldList[] groups = list._groups;
			
			// Make sure it is valid
			int dx = list.getSelectedIndex();
			if (dx < 0 || dx >= groups.length)
				dx = 0;
				
			// Show these fields
			this.display.setCurrent(groups[dx]);
		}
	}
}
