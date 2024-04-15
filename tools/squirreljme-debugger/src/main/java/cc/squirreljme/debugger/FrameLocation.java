// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

/**
 * Location information which defines where a frame is.
 *
 * @since 2024/01/25
 */
public class FrameLocation
{
	/** The class this is in. */
	protected final InfoClass inClass;
	
	/** The method this is in. */
	protected final InfoMethod inMethod;
	
	/** The thread this is in. */
	protected final InfoThread inThread;
	
	/** The index of the location. */
	protected final long index;
	
	/**
	 * Initializes the location.
	 *
	 * @param __inThread The thread this is in.
	 * @param __inClass The class this is in.
	 * @param __inMethod The method this is in.
	 * @param __index The frame location.
	 * @since 2024/01/25
	 */
	public FrameLocation(InfoThread __inThread, InfoClass __inClass,
		InfoMethod __inMethod, long __index)
	{
		if (__inClass == null || __inThread == null)
			throw new NullPointerException("NARG");
		
		this.inThread = __inThread;
		this.inClass = __inClass;
		this.inMethod = __inMethod;
		this.index = __index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/25
	 */
	@Override
	public String toString()
	{
		return String.format("%s:%s @ %d",
			this.inMethod.name.getOrDefault(null),
			this.inMethod.type.getOrDefault(null),
			this.index);
	}
}
