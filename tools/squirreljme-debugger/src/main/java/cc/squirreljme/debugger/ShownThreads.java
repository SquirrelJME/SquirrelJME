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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Objects;
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
	
	/** The thread context. */
	protected final ContextThreadFrame context;
	
	/** The action listener. */
	private final ActionListener _actionListener;
	
	/** The item listener. */
	private final ItemListener _itemListener;
	
	/** The currently viewed thread. */
	private volatile ShownThread _current;
	
	/** Last known threads. */
	private volatile InfoThread[] _lastKnown;
	
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
		
		// Last known threads, blank for now
		this._lastKnown = new InfoThread[0];
		
		// Set up a timer to updates threads
		Timer tick = new Timer(5000, (__event) -> {
				this.__updateAll();
			});
		tick.setInitialDelay(3000);
		tick.start();
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
		// Update all threads, this will call the updater later
		this.__updateAll();
	}
	
	/**
	 * Updates the shown threads, should be called in the event loop.
	 *
	 * @since 2024/01/25
	 */
	public void update()
	{
		// Obtain all resultant threads, do not perform any updates if blank
		InfoThread[] threads;
		synchronized (this)
		{
			threads = this._lastKnown;
		}
		if (threads == null || threads.length == 0)
			return;
		
		// Context thread for potential selecting
		InfoThread contextThread = this.context.getThread();
		
		// Sort again
		Arrays.sort(threads);
		
		// We will be working on this combo box much
		JComboBox<InfoThread> combo = this.combo;
		
		// Remove all listeners so changing items does not mess things up
		combo.removeActionListener(this._actionListener);
		combo.removeItemListener(this._itemListener);
		
		// The current selected item
		InfoThread selected = (InfoThread)combo.getSelectedItem();
		boolean redoSelection = false;
		boolean didFind = false;
		
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
			{
				didFind = true;
				threads[found] = null;
			}
		}
		
		// Go through and add any threads that are new
		for (InfoThread thread : threads)
			if (thread != null)
				combo.addItem(thread);
		
		// Need to redo the selection? Just select the first then
		if (!didFind || redoSelection || contextThread == null)
		{
			// Select it
			InfoThread forceSelect = threads[0];
			combo.setSelectedItem(forceSelect);
			
			// Force it to be shown
			this.context.optional(forceSelect);
		}
		
		// Make sure our context thread is always selected
		else
			combo.setSelectedItem(contextThread);
		
		// Set updater for when the combo box value changes
		combo.addActionListener(this._actionListener);
		combo.addItemListener(this._itemListener);
		
		// If there is nothing shown, try to show whatever is in the context
		if (this._current == null && contextThread != null)
			this.__context(contextThread);

		// Repaint
		Utils.revalidate(combo);
		Utils.revalidate(this);
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
		
		// Did the thread change? Context call will eventually cause an update
		if (thread != null)
		{
			this.context.set(this.state, thread);
			Utils.swingInvoke(() -> this.__context(thread));
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
		
		// Update thread, the context call will eventually cause an update
		if (thread != null)
		{
			this.context.set(this.state, thread);
			Utils.swingInvoke(() -> this.__context(thread));
		}
	}
	
	/**
	 * Updates the context thread view.
	 *
	 * @param __context The new context thread.
	 * @since 2024/02/01
	 */
	private void __context(InfoThread __context)
	{
		// Need to determine if the thread is changing
		ShownThread shown = this._current;
		
		// Only update the shown info if the thread changed
		if (shown != null && __context != null &&
			!Objects.equals(shown.thread, __context))
		{
			// Remove old thread being shown
			this._current = null;
			this.remove(shown);
			shown = null;
		}
		
		// Set new item?
		if (shown == null && __context != null)
		{
			// Add in new thread
			shown = new ShownThread(this.state, __context,
				this.context);
			this.add(shown, BorderLayout.CENTER);
			this._current = shown;
			
			// Force it to update quicker
			shown.update();
		}
		
		// Have the shown information update
		else if (shown != null)
			shown.update();
		
		// Repaint
		Utils.revalidate(this);
	}
	
	/**
	 * Updates all known threads.
	 *
	 * @since 2024/02/01
	 */
	private void __updateAll()
	{
		this.state.allThreads((__threads) -> {
				// Use these threads instead
				synchronized (this)
				{
					this._lastKnown = __threads;
				}
				
				// Update everything
				Utils.swingInvoke(this::update);
			});
	}
}
