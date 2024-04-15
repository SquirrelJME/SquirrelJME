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
import net.multiphasicapps.classfile.ClassName;

/**
 * Views remote classes.
 *
 * @since 2024/01/22
 */
public class RemoteClassViewer
	implements ClassViewer
{
	/** The class information. */
	protected final InfoClass classInfo;
	
	/** The debugger state. */
	protected final DebuggerState state;
	
	/** Methods within the method. */
	private volatile MethodViewer[] _methods;
	
	/**
	 * Initializes the remote class viewer.
	 *
	 * @param __state The state for communication and otherwise.
	 * @param __classInfo The class to view.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public RemoteClassViewer(DebuggerState __state, InfoClass __classInfo)
		throws NullPointerException
	{
		if (__classInfo == null)
			throw new NullPointerException("NARG");
		
		this.state = __state;
		this.classInfo = __classInfo;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public MethodViewer[] methods()
	{
		// Are the methods already known?
		MethodViewer[] methods = this._methods;
		if (methods != null)
			return methods.clone();
		
		// Get the methods that the class has
		InfoMethod[] infos = this.classInfo.methods();
		int count = (infos == null ? 0 : infos.length);
		
		// Wrap all the methods in remote viewers
		DebuggerState state = this.state;
		methods = new MethodViewer[count];
		for (int i = 0; i < count; i++)
			methods[i] = new RemoteMethodViewer(state, infos[i]);
		
		// Cache for later
		this._methods = methods;
		
		// Return all of our known classes
		return methods.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public ClassName thisName()
	{
		return this.classInfo.thisName();
	}
}
