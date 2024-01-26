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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
	
	/** Currently shown method. */
	private volatile ShownMethod _shownMethod;
	
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
		
		// Set listener for this to update everything
		__context.addListener(this);
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
		// Remove the old method being shown
		ShownMethod current = this._shownMethod;
		if (current != null)
			this.remove(current);
		
		// If there is nothing, just say as such... we do need both a thread
		// and a frame for this to even make sense
		if (__newThread == null || __newFrame == null)
		{
			this.info.setText("Nothing");
			return;
		}
		
		// Which thread are we looking at?
		this.info.setText(__newThread.toString());
		
		// Setup new view for the current method
		InfoMethod inMethod = __newFrame.inMethod();
		current = new ShownMethod(this.state,
			new RemoteMethodViewer(this.state, inMethod),
			this.context,
			true);
		this._shownMethod = current;
		this.add(current, BorderLayout.CENTER);
		
		// Make sure it is updated
		current.shownUpdate();
		
		// Repaint
		this.repaint();
	}
	
	/**
	 * Updates the current item.
	 *
	 * @since 2024/01/26
	 */
	public void update()
	{
		ShownMethod current = this._shownMethod;
		if (current != null)
			current.shownUpdate();
	}
}
