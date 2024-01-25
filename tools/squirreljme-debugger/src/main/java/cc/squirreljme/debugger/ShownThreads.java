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
import java.awt.event.ItemEvent;
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
	
	/** The currently viewed thread. */
	private volatile ShownThread _current;
	
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
		
		// Set updater for when the combo box value changes
		combo.addItemListener(this::__chooseThread);
	}
	
	/**
	 * Updates the shown threads.
	 *
	 * @since 2024/01/25
	 */
	public void update()
	{
		// Force tick
		this.__tick(null);
		
		// If there is a current item, make sure it gets updated
		ShownThread current = this._current;
		if (current != null)
			current.update();
	}
	
	/**
	 * Chooses an updated thread.
	 *
	 * @param __event The event used.
	 * @since 2024/01/24
	 */
	private void __chooseThread(ItemEvent __event)
	{
		// Do nothing if nothing was set
		InfoThread item = (InfoThread)__event.getItem();
		if (item == null)
			return;
		
		// If the thread has not changed, do nothing
		ShownThread current = this._current;
		if (current != null && current.thread == item)
			return;
		
		// Remove old thread being shown
		if (current != null)
			this.remove(current);
		
		// Add in new thread
		current = new ShownThread(this.state, item);
		this.add(current, BorderLayout.CENTER);
		this._current = current;
		
		// Force it to update quicker
		current.update();
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
		
		// Add in all threads, ignore dead and disposed ones
		combo.removeAllItems();
		for (InfoThread thread : threads)
			if (!thread.isDisposed() &&
				!thread.isDead.getOrDefault(false))
				combo.addItem(thread);
		
		// Set the selected item
		combo.setSelectedItem(currentSel);
		
		// If there is a current item, make sure it gets updated
		ShownThread current = this._current;
		if (current != null)
			current.update();
	}
}
