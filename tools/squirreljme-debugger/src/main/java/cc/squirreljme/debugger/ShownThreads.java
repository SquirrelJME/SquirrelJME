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
	implements ContextThreadFrameListener
{
	/** The debugger state. */
	protected final DebuggerState state;
	
	/** Thread list combo box. */
	protected final JComboBox<InfoThread> combo;
	
	/** The timer. */
	protected final Timer ticker;
	
	/** The thread context. */
	protected final ContextThreadFrame context;
	
	/** The currently viewed thread. */
	private volatile ShownThread _current;
	
	/**
	 * Initializes the thread shower.
	 *
	 * @param __state The debugger state.
	 * @param __context The current thread context.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/24
	 */
	public ShownThreads(DebuggerState __state, ContextThreadFrame __context)
		throws NullPointerException
	{
		if (__state == null || __context == null)
			throw new NullPointerException("NARG");
		
		this.state = __state;
		this.context = __context;
		
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
	 * {@inheritDoc}
	 * @since 2024/01/25
	 */
	@Override
	public void contextChanged(InfoThread __oldThread, InfoFrame __oldFrame,
		InfoThread __newThread, InfoFrame __newFrame)
	{
		// Force an update
		this.update();
	}
	
	/**
	 * Updates the shown threads.
	 *
	 * @since 2024/01/25
	 */
	public void update()
	{
		// Need to determine if the thread is changing
		ShownThread shown = this._current;
		InfoThread thread = this.context.getThread();
		
		// If the thread has not changed, do nothing
		if (thread != null && shown != null && shown.thread == thread)
		{
			// But do update it because we do like that
			shown.update();
				
			return;
		}
		
		// Remove old thread being shown
		if (shown != null)
			this.remove(shown);
		
		// It is possible for a thread to get unselected
		if (thread != null)
		{
			// Add in new thread
			shown = new ShownThread(this.state, thread, this.context);
			this.add(shown, BorderLayout.CENTER);
			this._current = shown;
			
			// Force it to update quicker
			shown.update();
		}
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
		InfoThread thread = (InfoThread)__event.getItem();
		if (thread == null)
			return;
		
		// Update thread
		this.context.set(thread);
		
		// Force an update
		this.update();
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
		
		// Force an update
		this.update();
	}
}
