// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.deviceinfo;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;

/**
 * This browses over available groups.
 *
 * @since 2025/12/06
 */
public class GroupList
	extends List
{
	/** Exit the application. */
	public static final Command EXIT =
		new Command("Exit", Command.EXIT, 0);
	
	/** Go to the given group. */
	public static final Command SELECT =
		new Command("Select", Command.ITEM, 1);
	
	/** Go back to the main list. */
	public static final Command BACK =
		new Command("Back", Command.BACK, 2);
	
	/** Sub-lists for activation. */
	final FieldList[] _groups;
	
	/**
	 * Initializes the group list. 
	 *
	 * @param __d The display used.
	 * @param __groups The groups used.
	 * @since 2025/12/06
	 */
	public GroupList(Display __d, SpecificGroup... __groups)
		throws NullPointerException
	{
		super("Device Information", Choice.IMPLICIT);
		
		if (__d == null || __groups == null)
			throw new NullPointerException("NARG");
		
		// Listener used to navigate items
		CommandListener listener = new GroupFieldListener(__d, this);
		
		// Add exit and select commands for the main group
		this.addCommand(GroupList.EXIT);
		this.addCommand(GroupList.SELECT);
		this.setCommandListener(listener);
		this.setSelectCommand(GroupList.SELECT);
		
		// Add lists to process each group
		int n = __groups.length;
		FieldList[] subLists = new FieldList[n];
		for (int i = 0; i < n; i++)
		{
			// Use this group
			SpecificGroup group = __groups[i];
			if (group == null)
				throw new NullPointerException("NARG");
			
			// Setup list to activate into
			FieldList list = new FieldList(group.title, group.fields());
			subLists[i] = list;
			
			// Register the back command to return here
			list.addCommand(GroupList.BACK);
			list.setCommandListener(listener);
			
			// Add the actual group now
			this.append(group.title, null);
		}
		
		// Used for later activation
		this._groups = subLists;
	}
}
