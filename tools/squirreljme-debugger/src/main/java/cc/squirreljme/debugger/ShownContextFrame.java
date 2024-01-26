// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Shown context frame.
 *
 * @since 2024/01/25
 */
public class ShownContextFrame
	extends JPanel
	implements ContextThreadFrameListener
{
	/** The current context. */
	protected final ContextThreadFrame context;
	
	/** The current frame. */
	protected final InfoFrame frame;
	
	/** The current button text. */
	protected final JButton text;
	
	/** The active pointer button. */
	protected final JButton active;
	
	/**
	 * Initializes the context frame.
	 *
	 * @param __frame The current frame.
	 * @param __context The context of the frame.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/25
	 */
	public ShownContextFrame(InfoFrame __frame, ContextThreadFrame __context)
		throws NullPointerException
	{
		if (__frame == null || __context == null)
			throw new NullPointerException("NARG");
		
		// Store for later
		this.frame = __frame;
		this.context = __context;
		
		// Use basic flow layout
		FlowLayout layout = new FlowLayout(FlowLayout.LEADING);
		layout.setHgap(0);
		layout.setVgap(0);
		this.setLayout(layout);
		
		// Add current active context button
		JButton active = new JButton();
		this.active = active;
		Utils.prettyTextButton(active);
		active.setIcon(Utils.tangoIcon("-"));
		this.add(active);
		
		// Setup a nice text button
		JButton text = new JButton(__frame.toString());
		this.text = text;
		Utils.prettyTextButton(text);
		this.add(text);
		
		// Set context when clicked
		text.addActionListener(this::__changeContext);
		
		// Called when context updates
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
		// Update
		this.update();
	}
	
	/**
	 * Updates the context.
	 *
	 * @since 2024/01/26
	 */
	public void update()
	{
		// Is this selected?
		JButton active = this.active;
		InfoFrame inFrame = this.context.getFrame();
		if (Objects.equals(this.frame, inFrame))
			active.setIcon(Utils.tangoIcon("go-next"));
		else
			active.setIcon(Utils.tangoIcon("-"));
	}
	
	/**
	 * Changes the current context.
	 *
	 * @param __event Ignored.
	 * @since 2024/01/25
	 */
	private void __changeContext(ActionEvent __event)
	{
		// Set new context
		this.context.set(this.frame);
	}
}
