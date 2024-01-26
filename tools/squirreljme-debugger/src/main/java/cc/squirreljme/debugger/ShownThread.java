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
import java.awt.Font;
import java.util.Objects;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
		InfoThread thread = this.thread;
		JLabel suspendLabel = this.suspendLabel;
		DebuggerState state = this.state;
		SequentialPanel sequence = this.sequence;
		
		// Inform of the current thread suspension state
		int suspendCount = thread.suspendCount.update(state, 0);
		if (suspendCount > 0)
		{
			// Show as suspended
			suspendLabel.setText(
				String.format("Suspended %d time%s.", suspendCount,
					(suspendCount == 1 ? "" : "s")));
			
			// Get all frames in the thread
			InfoFrame[] frames = thread.frames.update(state);
			
			// Frames are given where the top is at the bottom, so reverse
			// the order for a more sensible stack
			/*Collections.reverse(Arrays.asList(frames));*/
			
			// Clear all frames
			sequence.removeAll();
			
			// Add sequences for all frames
			ContextThreadFrame context = this.context;
			InfoClass inClass = null;
			for (int i = 0, n = frames.length; i < n; i++)
			{
				InfoFrame frame = frames[i];
				InfoClass newClass = frame.location.inClass;
				
				// Did the class change? Add banner for it
				if (!Objects.equals(inClass, newClass))
				{
					// Add label for the class
					JLabel atClass = new JLabel(newClass.toString());
					atClass.setFont(atClass.getFont().deriveFont(
						Font.ITALIC));
					sequence.add(atClass);
					
					// We are in this class now
					inClass = newClass;
				}
				
				// Make a pretty button
				ShownContextFrame shownFrame =
					new ShownContextFrame(frame, context);
				
				// Add it to the sequence
				sequence.add(shownFrame);
				
				// Update it quickly
				shownFrame.update();
			}
		}
		else
		{
			// Show as running
			suspendLabel.setText("Running");
			
			synchronized (this)
			{
				// No frames are valid, so cannot show anything
				sequence.removeAll();
			}
		}
		
		// Repaint
		this.repaint();
	}
}
