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
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.classfile.MethodFlags;
import net.multiphasicapps.classfile.MethodNameAndType;

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
	
	/** The preferences to use for local classes. */
	protected final Preferences preferences;
	
	/** Currently shown method. */
	private volatile ShownMethod _shownMethod;
	
	/** The method we are looking at. */
	private volatile InfoMethod _lookingAt;
	
	/**
	 * Initializes the context method shower.
	 *
	 * @param __state The current state.
	 * @param __context The context to show.
	 * @param __preferences Preferences to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/25
	 */
	public ShownContextMethod(DebuggerState __state,
		ContextThreadFrame __context, Preferences __preferences)
	{
		if (__state == null || __context == null)
			throw new NullPointerException("NARG");
		
		// Store for later
		this.state = __state;
		this.context = __context;
		this.preferences = __preferences;
		
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
				// Should we load a local class or remote?
				MethodViewer remote = new RemoteMethodViewer(this.state,
					inMethod);
				Method localMethod = ShownContextMethod.__possiblyLocal(
					this.state, remote, this.preferences);
				MethodViewer viewer;
				if (localMethod != null)
					viewer = new JavaMethodViewer(localMethod);
				
				// Use remote method
				else
					viewer = remote;
				
				// Initialize view
				current = new ShownMethod(this.state, viewer, this.context,
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
		Utils.revalidate(this.info);
		Utils.revalidate(this);
	}
	
	/**
	 * Should this use local classes?
	 *
	 * @param __state The state to load from.
	 * @param __inMethod The method to look in.
	 * @param __preferences The preferences used.
	 * @return If a local class should be loaded in.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/29
	 */
	private static Method __possiblyLocal(DebuggerState __state,
		MethodViewer __inMethod, Preferences __preferences)
		throws NullPointerException
	{
		if (__inMethod == null || __preferences == null)
			throw new NullPointerException("NARG");
		
		// Ignore for native and abstract as there will never be byte code
		// Also if we do not know the class we are in, we cannot do anything
		// either
		if (__inMethod.isAbstract() || __inMethod.isNative() ||
			__inMethod.inClass() == null)
			return null;
		
		// If we are doing local classes only or when the VM has no
		// instructions to give... then we use a local class assuming it
		// exists
		InstructionViewer[] instructions = __inMethod.instructions();
		if (instructions == null || instructions.length == 0 ||
			__preferences.isLocalClassOnly())
		{
			// We need to load the class first
			ClassFile[] classFiles = Utils.loadClass(__inMethod.inClass(),
				__preferences);
			if (classFiles == null || classFiles.length == 0)
				return null;
			
			// Try to find the matching method in the first class that matches
			MethodNameAndType wantNat = __inMethod.methodNameAndType();
			for (ClassFile classFile : classFiles)
				for (Method method : classFile.methods())
					if (Objects.equals(wantNat, method.nameAndType()))
						return method;
		}
		
		// Use remote class
		return null;
	}
}
