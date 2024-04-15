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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * A panel which shows a single Java method.
 *
 * @since 2024/01/21
 */
public class ShownMethod
	extends JPanel
	implements ContextThreadFrameListener
{
	/** The viewer for the method being shown. */
	protected final MethodViewer viewer;
	
	/** The label which says what method this is. */
	protected final JLabel whatLabel;
	
	/** The sequential panel view. */
	protected final SequentialPanel seqPanel;
	
	/** The optional debugger state. */
	protected final DebuggerState state;
	
	/** The current context, is optional. */
	protected final ContextThreadFrame context;
	
	/** All the current instruction showers. */
	private volatile ShownInstruction[] _shownInstructions;
	
	
	/**
	 * Initializes the method viewer.
	 *
	 * @param __state The debugger state, used for breakpoint and flow
	 * control.
	 * @param __viewer The method to view.
	 * @param __context The current thread context, is optional.
	 * @param __scroll Scroll the panel view?
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	public ShownMethod(DebuggerState __state, MethodViewer __viewer,
		ContextThreadFrame __context, boolean __scroll)
		throws NullPointerException
	{
		if (__viewer == null)
			throw new NullPointerException("NARG");
		
		this.state = __state;
		this.context = __context;
		this.viewer = __viewer;
		
		// Use border layout for this panel since it is cleaner
		this.setLayout(new BorderLayout());
		
		// Set up the label description what we are looking at
		JLabel whatLabel = new JLabel();
		whatLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		// Set at the top
		this.add(whatLabel, BorderLayout.PAGE_START);
		this.whatLabel = whatLabel;
		
		// Setup sequential panel
		SequentialPanel seqPanel = new SequentialPanel(__scroll);
		this.add(seqPanel.panel(), BorderLayout.CENTER);
		this.seqPanel = seqPanel;
		
		// If the context changes, update this so that we get the most
		// up-to-date information
		if (__context != null)
			__context.addListener(this);
		
		// Request that everything gets updated
		Utils.swingInvoke(this::shownUpdate);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/27
	 */
	@Override
	public void contextChanged(InfoThread __oldThread, InfoFrame __oldFrame,
		FrameLocation __oldLocation, InfoThread __newThread,
		InfoFrame __newFrame, FrameLocation __newLocation)
	{
		Utils.swingInvoke(this::shownUpdate);
	}
	
	/**
	 * Updates the given view.
	 * 
	 * @since 2024/01/21
	 */
	public void shownUpdate()
	{
		MethodViewer viewer = this.viewer;
		
		// Update with our location
		this.whatLabel.setText(String.format("%s%s%s::%s",
			(viewer.isNative() ? "native " : ""),
			(viewer.isAbstract() ? "abstract " : ""),
			viewer.inClass(), viewer.methodNameAndType()));
		Utils.revalidate(this.whatLabel);
		
		// Cannot show any byte code if this is abstract or native
		if (viewer.isNative() || viewer.isAbstract())
			return;
		
		// Do we need to get the instructions to show?
		ShownInstruction[] shownInstructions = this._shownInstructions;
		if (shownInstructions == null)
		{
			// If this is looking at a remote method, we will have to belay
			// showing it while it is being read from the remote VM
			if (viewer instanceof RemoteMethodViewer)
				((RemoteMethodViewer)viewer).info.byteCode.update(this.state,
					(__state, __value) -> {
						InfoByteCode byteCode = __value.get();
						if (byteCode != null)
							Utils.swingInvoke(() -> {
								this.__updateInstructions(
									byteCode.instructions());
							});
					});
			else
				this.__updateInstructions(viewer.instructions());
		}
		
		// Go through an update everything accordingly
		shownInstructions = this._shownInstructions;
		if (shownInstructions != null)
			for (ShownInstruction shown : shownInstructions)
				if (shown.shownUpdate())
					this.seqPanel.lookAt(shown);
		
		// Repaint
		Utils.revalidate(this);
	}
	
	/**
	 * Updates the shown instructions.
	 *
	 * @param __instructions The instructions to show.
	 * @since 2024/01/27
	 */
	private void __updateInstructions(InstructionViewer[] __instructions)
	{
		// Do nothing yet if we do not know what instructions exist
		if (__instructions == null)
			return;
	
		// All the instructions are placed here, however prevent them from
		// being doubly placed
		SequentialPanel seqPanel = this.seqPanel;
		if (seqPanel.hasItems())
		{	
			// Perform another update
			this.shownUpdate();
			
			return;
		}
		
		// Optional state for breakpoints, etc.
		DebuggerState state = this.state;
		ContextThreadFrame context = this.context;
		
		// Add everything to the grid view
		int count = __instructions.length;
		ShownInstruction[] shownInstructions = new ShownInstruction[count];
		for (int i = 0; i < count; i++)
		{
			// Initialize a shower!
			ShownInstruction shown = new ShownInstruction(
				state, __instructions[i], context, i);
			
			// Show it
			shownInstructions[i] = shown;
			seqPanel.add(shown);
			
			// Force an update
			shown.shownUpdate();
		}
		
		// Store for later updates
		this._shownInstructions = shownInstructions;
		
		// Perform another update
		this.shownUpdate();
	}
}
