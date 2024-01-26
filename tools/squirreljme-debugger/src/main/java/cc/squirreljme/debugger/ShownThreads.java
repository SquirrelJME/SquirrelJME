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
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Objects;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.MutableComboBoxModel;

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
	
	/** The thread context. */
	protected final ContextThreadFrame context;
	
	/** The action listener. */
	private final ActionListener _actionListener;
	
	/** The item listener. */
	private final ItemListener _itemListener;
	
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
		
		// Store listeners
		this._actionListener = this::__chooseThread;
		this._itemListener = this::__chooseThread;
		
		// Set updater for when the combo box value changes
		combo.addActionListener(this._actionListener);
		combo.addItemListener(this._itemListener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/25
	 */
	@Override
	public void contextChanged(InfoThread __oldThread, InfoFrame __oldFrame,
		FrameLocation __oldLocation, InfoThread __newThread,
		InfoFrame __newFrame, FrameLocation __newLocation)
	{
		// Force an update
		this.update();
	}
	
	/**
	 * Updates the shown threads, should be called in the event loop.
	 *
	 * @since 2024/01/25
	 */
	public void update()
	{
		// Obtain all resultant threads
		StoredInfo<InfoThread> stored = this.state.storedInfo.getThreads();
		InfoThread[] threads = stored.all(this.state, InfoThread[].class);
		
		// Need to update
		InfoThread contextThread = this.context.getThread();
		
		// We will be working on this combo box much
		JComboBox<InfoThread> combo = this.combo;
		
		// Remove all listeners so changing items does not mess things up
		combo.removeActionListener(this._actionListener);
		combo.removeItemListener(this._itemListener);
		
		// The current selected item
		InfoThread selected = (InfoThread)combo.getSelectedItem();
		boolean redoSelection = false;
		
		// Go through all the items because we will be adding and removing
		// them accordingly
		for (int i = combo.getItemCount() - 1; i >= 0; i--)
		{
			// Get the thread here
			InfoThread at = combo.getItemAt(i);
			
			// See if we still know about this thread, if a thread is dead
			// or disposed of then do not bother showing it at all
			int found = -1;
			if (at != null && !at.isDisposed() &&
				!at.isDead.getOrDefault(false))
				for (int j = 0, m = threads.length; j < m; j++)
					if (Objects.equals(at, threads[j]))
					{
						found = j;
						break;
					}
			
			// If not found, then remove the thread
			if (found < 0)
			{
				// If this was the selected item, we need to change it
				if (Objects.equals(selected, at))
					redoSelection = true;
				
				// Remove it
				combo.removeItemAt(i);
			}
			
			// Otherwise clear it to mark as not added
			else
				threads[found] = null;
		}
		
		// Go through and add any threads that are new
		for (InfoThread thread : threads)
			if (thread != null)
				combo.addItem(thread);
		
		// Need to redo the selection? Just select the first then
		if (redoSelection)
			combo.setSelectedItem(0);
		
		// Make sure our context thread is always selected
		else
			combo.setSelectedItem(contextThread);
		
		// Set updater for when the combo box value changes
		combo.addActionListener(this._actionListener);
		combo.addItemListener(this._itemListener);

		// Repaint???
		combo.repaint();
		
		// Need to determine if the thread is changing
		ShownThread shown = this._current;
		
		// Only update the shown info if the thread changed
		if (shown != null/* && !Objects.equals(shown.thread, contextThread)*/)
		{
			// Remove old thread being shown
			this._current = null;
			this.remove(shown);
			shown = null;
		}
		
		// Set new item?
		if (shown == null)
		{
			// It is possible for a thread to get unselected
			if (contextThread != null)
			{
				// Add in new thread
				shown = new ShownThread(this.state, contextThread,
					this.context);
				this.add(shown, BorderLayout.CENTER);
				this._current = shown;
				
				// Force it to update quicker
				shown.update();
			}
		}
		
		// Have the shown information update
		else
			shown.update();
		
		// Repaint
		this.repaint();
	}
	
	/**
	 * Chooses the updated thread.
	 *
	 * @param __event The event.
	 * @since 2024/01/26
	 */
	private void __chooseThread(ActionEvent __event)
	{
		InfoThread thread = (InfoThread)this.combo.getSelectedItem();
		
		// Debug
		Debugging.debugNote("Chose %s (ActionEvent)", thread);
		
		// Did the thread change?
		if (thread != null)
		{
			this.context.set(this.state, thread);
			this.update();
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
		// Do nothing if nothing was set, or we deselected the item
		InfoThread thread = (InfoThread)__event.getItem();
		if (thread == null || __event.getStateChange() != ItemEvent.SELECTED)
			return;
		
		// Debug
		Debugging.debugNote("Chose %s (ItemEvent)", thread);
		
		// Update thread
		if (thread != null)
		{
			this.context.set(this.state, thread);
			this.update();
		}
	}
}
