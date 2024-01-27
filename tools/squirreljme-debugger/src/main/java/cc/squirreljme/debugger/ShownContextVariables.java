// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPValue;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Shows variable information.
 *
 * @since 2024/01/26
 */
public class ShownContextVariables
	extends JPanel
	implements ContextThreadFrameListener
{
	/** The shown context. */
	protected final ContextThreadFrame context;
	
	/** The debugger state. */
	protected final DebuggerState state;
	
	/** The variable sequences. */
	protected final SequentialPanel sequence;
	
	/**
	 * Initializes the shown context variables.
	 *
	 * @param __state The debugger state.
	 * @param __context The current context.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/26
	 */
	public ShownContextVariables(DebuggerState __state,
		ContextThreadFrame __context)
		throws NullPointerException
	{
		if (__state == null || __context == null)
			throw new NullPointerException("NARG");
		
		// Store for later
		this.state = __state;
		this.context = __context;
		
		// Border layout is always clean
		this.setLayout(new BorderLayout());
		
		// Sequence for shown variables
		SequentialPanel sequence = new SequentialPanel(true);
		this.sequence = sequence;
		this.add(sequence.panel(), BorderLayout.CENTER);
		
		// Set updater to show locals
		__context.addListener(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/26
	 */
	@Override
	public void contextChanged(InfoThread __oldThread, InfoFrame __oldFrame,
		FrameLocation __oldLocation, InfoThread __newThread,
		InfoFrame __newFrame, FrameLocation __newLocation)
	{
		this.update();
	}
	
	/**
	 * Updates the shown variables.
	 *
	 * @since 2024/01/26
	 */
	public void update()
	{
		SequentialPanel sequence = this.sequence;
		DebuggerState state = this.state;
		
		// If there is no frame, then there is no purpose to this
		// Or if it is not suspended...
		InfoFrame inFrame = this.context.getFrame();
		if (inFrame == null ||
			inFrame.inThread.suspendCount.update(state) <= 0)
		{
			sequence.removeAll();
			return;
		}
		
		// Get all values
		JDWPValue[] values = inFrame.variables.update(state);
		if (values != null)
		{
			// Debug
			Debugging.debugNote("Read variables: %s",
				Arrays.asList(values));
			
			// Clear everything and re-add
			sequence.removeAll();
			for (int i = 0, n = values.length; i < n; i++)
				sequence.add(new KeyValuePanel(
					new JLabel(Integer.toString(i)),
					new JLabel(Objects.toString(values[i],
						"null"))));
		}
	}
}
