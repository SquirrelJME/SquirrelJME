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
import java.awt.BorderLayout;
import java.util.Objects;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
	
	/** Pre-created key and values. */
	private final KeyValuePanel[] _keyValues;
	
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
		
		// Pre-create all key/value pairs to reduce allocation/de-allocation
		// strain
		KeyValuePanel[] keyValues =
			new KeyValuePanel[InfoFrameLocals.MAX_LOCALS];
		for (int i = 0, n = InfoFrameLocals.MAX_LOCALS; i < n; i++)
			keyValues[i] = new KeyValuePanel(
				new JLabel(Integer.toString(i)),
				new JLabel());
		this._keyValues = keyValues;
		
		// Border layout is always clean
		this.setLayout(new BorderLayout());
		
		// Sequence for shown variables
		SequentialPanel sequence = new SequentialPanel(true);
		this.sequence = sequence;
		this.add(sequence.panel(), BorderLayout.CENTER);
		
		// Set updater to show locals
		__context.addListener(this);
		
		// Request that everything gets updated
		Utils.swingInvoke(this::update);
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
		
		// Get the frame, if there is none then do nothing other than
		// removing everything
		InfoFrame inFrame = this.context.getFrame();
		if (inFrame == null)
		{
			// Nothing to actually show...
			sequence.removeAll();
			
			return;
		}
		
		// Otherwise, request an update of all the variables
		inFrame.variables.update(state, (__state, __value) -> {
			InfoFrameLocals locals = __value.get();
			if (locals != null)
				Utils.swingInvoke(() -> {
					this.__updateLocals(locals);
				});
		});
		
		// Repaint
		Utils.revalidate(this);
	}
	
	/**
	 * Updates the shown locals.
	 *
	 * @param __locals The locals to show.
	 * @since 2024/01/27
	 */
	private void __updateLocals(InfoFrameLocals __locals)
	{
		SequentialPanel sequence = this.sequence;
		
		// Ignore if there are none
		if (__locals == null)
			return;
		
		// Clear sequence chain
		sequence.removeAll();
		
		// Get pre-created key/value sequences
		KeyValuePanel[] keyValues = this._keyValues;
		
		// Add in for every local
		for (int index = 0, numLocals = InfoFrameLocals.MAX_LOCALS;
			index < numLocals; index++)
		{
			// Skip any null values
			JDWPValue value = __locals.get(index);
			if (value == null)
				continue;
			
			// Update the key/value pair
			KeyValuePanel keyValue = keyValues[index];
			JLabel subValue = (JLabel)keyValue.value;
			subValue.setText(Objects.toString(value, "null"));
			
			// Add in key sequence
			sequence.add(keyValue);
			
			// Make sure it gets updated
			Utils.revalidate(subValue);
			Utils.revalidate(keyValue);
		}
		
		// Repaint
		Utils.revalidate(this);
	}
}
