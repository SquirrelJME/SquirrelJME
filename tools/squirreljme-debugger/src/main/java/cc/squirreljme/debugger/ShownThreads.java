// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Shows all the running threads within the virtual machine and allows
 * them to be selected accordingly.
 *
 * @since 2024/01/24
 */
public class ShownThreads
	extends JPanel
{
	/** The debugger state. */
	protected final DebuggerState state;
	
	/** Thread list combo box. */
	protected final JComboBox<InfoThread> combo;
	
	/** The timer. */
	protected final Timer ticker;
	
	/**
	 * Initializes the thread shower.
	 *
	 * @param __state The debugger state.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/24
	 */
	public ShownThreads(DebuggerState __state)
		throws NullPointerException
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		this.state = __state;
		
		// Setup combo box to use for threads
		JComboBox<InfoThread> combo = new JComboBox<>();
		this.combo = combo;
		
		// Border Layout makes things simple
		this.setLayout(new BorderLayout());
		
		// Place thread combo at the top
		this.add(combo, BorderLayout.PAGE_START);
		
		// Add an automatic timer to update the available threads
		Timer ticker = new Timer(3000, this::__tick);
		this.ticker = ticker;
		ticker.setRepeats(true);
		ticker.start();
	}
	
	/**
	 * Called on the thread ticker.
	 *
	 * @param __event The event for updating.
	 * @since 2024/01/24
	 */
	private void __tick(ActionEvent __event)
	{
		Debugging.debugNote("Update threads...");
		
		try
		{
			// Obtain all resultant threads
			StoredInfo<InfoThread> stored = this.state.storedInfo.getThreads();
			InfoThread[] threads = stored.all(this.state, InfoThread[].class);
			
			// If there are no threads then not much can be done
			JComboBox<InfoThread> combo = this.combo;
			if (threads == null || threads.length == 0)
			{
				// Remove everything since there is nothing
				combo.removeAllItems();
				
				return;
			}
			
			// Get the currently selected item
			Object currentSel = combo.getSelectedItem();
			
			// Is the selected item still in the combo?
			boolean stillIn = false;
			if (currentSel != null)
				for (InfoThread thread : threads)
					if (currentSel == thread)
					{
						stillIn = true;
						break;
					}
			
			// If the selection is no longer there, change it
			if (currentSel == null || !stillIn)
				currentSel = threads[0];
			
			// Add in all threads, ignore dead ones
			combo.removeAllItems();
			for (InfoThread thread : threads)
				if (!thread.isDead.getOrDefault(false))
					combo.addItem(thread);
			
			// Set the selected item
			combo.setSelectedItem(currentSel);
		}
		catch (Throwable __t)
		{
			__t.printStackTrace();
		}
	}
}
