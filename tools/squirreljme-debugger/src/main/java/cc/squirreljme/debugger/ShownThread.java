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
import java.awt.Font;
import java.util.Objects;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.multiphasicapps.classfile.ClassName;

/**
 * Shows a thread's stack trace.
 *
 * @since 2024/01/24
 */
public class ShownThread
	extends JPanel
	implements ContextThreadFrameListener
{
	/** The thread being viewed. */
	protected final InfoThread thread;
	
	/** The state of the debugger. */
	protected final DebuggerState state;
	
	/** Current suspension state? */
	protected final JLabel suspendLabel;
	
	/** The sequential panel for thread frames. */
	protected final SequentialPanel sequence;
	
	/** The context frame. */
	protected final ContextThreadFrame context;
	
	/**
	 * Initializes the shown thread.
	 *
	 * @param __state The debugger state.
	 * @param __thread The thread to show.
	 * @param __context The context for this thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/24
	 */
	public ShownThread(DebuggerState __state, InfoThread __thread,
		ContextThreadFrame __context)
		throws NullPointerException
	{
		if (__state == null || __thread == null || __context == null)
			throw new NullPointerException("NARG");
		
		this.state = __state;
		this.thread = __thread;
		this.context = __context;
		
		// Add updater for the context
		__context.addListener(this);
		
		// Border layout is clean
		this.setLayout(new BorderLayout());
		
		// Add suspension state label
		JLabel suspendLabel = new JLabel();
		this.suspendLabel = suspendLabel;
		this.add(suspendLabel, BorderLayout.PAGE_START);
		
		// Sequential panel which is used for the thread frames
		SequentialPanel sequence = new SequentialPanel(true);
		this.sequence = sequence;
		this.add(sequence.panel(), BorderLayout.CENTER);
		
		// Request that everything gets updated
		Utils.swingInvoke(this::update);
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
	 * Updates the shown information.
	 *
	 * @since 2024/01/25
	 */
	public void update()
	{
		InfoThread inThread = this.thread;
		DebuggerState state = this.state;
		
		// Update suspension count
		inThread.suspendCount.update(state, (__state, __value) -> {
			int count = __value.get();
			Utils.swingInvoke(() -> {
				this.__updateSuspend(count);
			});
		});
		
		// Update the thread information
		inThread.frames.update(state, (__state, __value) -> {
			InfoFrame[] frames = __value.get();
			Utils.swingInvoke(() -> {
				this.__updateFrames(frames);
			});
		});
		
		// Repaint
		Utils.revalidate(this);
	}
	
	/**
	 * Updates all frames.
	 *
	 * @param __frames The frames to update with.
	 * @since 2024/01/26
	 */
	private void __updateFrames(InfoFrame[] __frames)
	{
		SequentialPanel sequence = this.sequence;
		
		// Nothing to actually show? Do not perform any updating just in case
		// the information is invalid for just a moment
		if (__frames == null || __frames.length == 0)
			return;
		
		// Clear all current frames
		sequence.removeAll();
		
		// Add sequences for all frames
		ContextThreadFrame context = this.context;
		InfoClass currentClass = null;
		for (int i = 0, n = __frames.length; i < n; i++)
		{
			InfoFrame frame = __frames[i];
			InfoClass newClass = frame.location.inClass;
			
			// Did the class change? Add banner for it
			if ((currentClass == null && newClass != null) ||
				!Objects.equals(currentClass, newClass))
			{
				ClassName newThisName = newClass.thisName();
				
				// Add label for the class
				JLabel atClass = new JLabel(
					Objects.toString(newThisName, "null"));
				atClass.setFont(atClass.getFont().deriveFont(
					Font.ITALIC));
				sequence.add(atClass);
				
				// We are in this class now
				currentClass = newClass;
			}
			
			// Make a pretty button
			ShownContextFrame shownFrame =
				new ShownContextFrame(frame, context);
			
			// Add it to the sequence
			sequence.add(shownFrame);
			
			// Update it quickly
			shownFrame.update();
		}
		
		// Repaint
		Utils.revalidate(this);
	}
	
	/**
	 * Updates the shown suspension count.
	 *
	 * @param __count The suspension count used.
	 * @since 2024/01/26
	 */
	private void __updateSuspend(int __count)
	{
		JLabel suspendLabel = this.suspendLabel;
		
		// Show as suspended
		if (__count > 0)
			suspendLabel.setText(
				String.format("Suspended %d time%s.", __count,
					(__count == 1 ? "" : "s")));
		
		// Show as running
		else
			suspendLabel.setText("Running");
		
		// Repaint
		Utils.revalidate(this);
	}
}
