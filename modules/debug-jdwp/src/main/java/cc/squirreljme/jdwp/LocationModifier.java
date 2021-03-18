// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Modifier for locations.
 * 
 * Use with {@link EventKind#BREAKPOINT}, {@link EventKind#FIELD_ACCESS},
 * {@link EventKind#FIELD_MODIFICATION}, {@link EventKind#SINGLE_STEP},
 * and {@link EventKind#EXCEPTION}.
 *
 * @since 2021/03/17
 */
public final class LocationModifier
	implements EventModifier
{
	/** The class this is in. */
	protected final JDWPClass inClass;
	
	/** The method this is in. */
	protected final JDWPMethod inMethod;
	
	/** The index to bind to. */
	protected final long index;
	
	/**
	 * Initializes the location modifier.
	 * 
	 * @param __inClass The class this is in.
	 * @param __inMethod The method this in.
	 * @param __index The index to trigger at.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/17
	 */
	public LocationModifier(JDWPClass __inClass, JDWPMethod __inMethod,
		long __index)
		throws NullPointerException
	{
		if (__inClass == null || __inMethod == null)
			throw new NullPointerException("NARG");
		
		this.inClass = __inClass;
		this.inMethod = __inMethod;
		this.index = __index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/17
	 */
	@Override
	public String toString()
	{
		return String.format("Location(class=%s,method=%s,index=%d)",
			this.inClass, this.inMethod, this.index);
	}
}
