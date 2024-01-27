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
import java.util.Objects;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Shows the current context method.
 *
 * @since 2024/01/25
 */
public class ShownContextMethod
	extends JPanel
	implements ContextThreadFrameListener
{
	/** The current context. */
	protected final ContextThreadFrame context;
	
	/** The debugger state. */
	protected final DebuggerState state;
	
	/** Information label. */
	protected final JLabel info;
	
	/** Variables used. */
	protected final ShownContextVariables variables;
	
	/** Currently shown method. */
	private volatile ShownMethod _shownMethod;
	
	/** The method we are looking at. */
	private volatile InfoMethod _lookingAt;
	
	/**
	 * Initializes the context method shower.
	 *
	 * @param __state The current state.
	 * @param __context The context to show.
	 * @since 2024/01/25
	 */
	public ShownContextMethod(DebuggerState __state,
		ContextThreadFrame __context)
	{
		if (__state == null || __context == null)
			throw new NullPointerException("NARG");
		
		this.state = __state;
		this.context = __context;
		
		// Border layout is pretty clean
		this.setLayout(new BorderLayout());
		
		// Information label
		JLabel info = new JLabel();
		info.setHorizontalAlignment(SwingConstants.CENTER);
		this.info = info;
		this.add(info, BorderLayout.PAGE_START);
		
		// Add view of local variables
		ShownContextVariables variables = new ShownContextVariables(__state,
			__context);
		this.variables = variables;
		this.add(variables, BorderLayout.PAGE_END);
		
		// Set listener for this to update everything
		__context.addListener(this);
		
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
		// Use normal update
		this.update();
	}
	
	/**
	 * Updates the current item.
	 *
	 * @since 2024/01/26
	 */
	public void update()
	{
		ShownMethod current = this._shownMethod;
		
		// Get frame and the method we are in
		InfoFrame inFrame = this.context.getFrame();
		InfoMethod inMethod = (inFrame == null ? null : inFrame.inMethod());
		
		// If there is no context, then we cannot show anything
		if (inMethod == null || inFrame == null)
		{
			// Remove old one if it is there
			if (current != null)
			{
				this.remove(current);
				current = null;
			}
		}
		
		// Do we need to replace the method being shown?
		else if (current == null || !Objects.equals(this._lookingAt, inMethod))
		{
			// Remove old one if it is there
			if (current != null)
			{
				this.remove(current);
				current = null;
			}
			
			// Setup new view for the current method
			if (inMethod != null)
			{
				current = new ShownMethod(this.state,
					new RemoteMethodViewer(this.state, inMethod), this.context,
					true);
				this._shownMethod = current;
				this._lookingAt = inMethod;
				this.add(current, BorderLayout.CENTER);
				
				// Make sure it gets updated
				current.shownUpdate();
			}
		}
		
		// Update it regardless
		else
			current.shownUpdate();
		
		// If there is nothing, just say as such...
		if (current == null)
			this.info.setText("Nothing");
		
		// Otherwise describe the current frame
		else
			this.info.setText(String.format("%s @ %d",
				inMethod, inFrame.location.index));
		
		// Update variables
		this.variables.update();
		
		// Repaint
		this.repaint();
	}
}
