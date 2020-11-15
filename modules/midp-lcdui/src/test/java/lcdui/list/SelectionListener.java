// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui.list;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 * Listener for item selection changes in lists.
 *
 * @since 2020/11/13
 */
public class SelectionListener
	implements CommandListener
{
	/** Items that have been selected. */
	public final Set<Integer> selectedItems =
		new LinkedHashSet<>();
	
	/** The last selected item. */
	public int lastSelected =
		-2;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/13
	 */
	@Override
	public void commandAction(Command __command, Displayable __displayable)
	{
		if (__command == List.SELECT_COMMAND)
			synchronized (this)
			{
				int index = ((List)__displayable).getSelectedIndex();
				
				this.selectedItems.add(index);
				this.lastSelected = index;
				
				Debugging.debugNote("New selection: %d", index);
			}
	}
}
