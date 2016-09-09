// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This represents how a field is accessed.
 *
 * @since 2016/09/06
 */
public enum JITFieldAccessType
{
	/** Read static field. */
	READ_STATIC(true, false),
	
	/** Write static field. */
	WRITE_STATIC(true, true),
	
	/** Read instance field. */
	READ_INSTANCE(false, false),
	
	/** Write instance field. */
	WRITE_INSTANCE(false, true),
	
	/** End. */
	;
	
	/** Is this a static field access? */
	protected final boolean isstatic;
	
	/** Is this written to? */
	protected final boolean written;
	
	/**
	 * Initializes the field access type.
	 *
	 * @param __s Is this static?
	 * @param __w Is this written to?
	 * @since 2016/09/06
	 */
	private JITFieldAccessType(boolean __s, boolean __w)
	{
		this.isstatic = __s;
		this.written = __w;
	}
	
	/**
	 * Returns {@code true} if the field is an instance field.
	 *
	 * @return {@code true} if an instance.
	 * @since 2016/09/06
	 */
	public final boolean isInstance()
	{
		return !this.isstatic;
	}
	
	/**
	 * Returns {@code true} if the field is read from.
	 *
	 * @return {@code true} if read from.
	 * @since 2016/09/06
	 */
	public final boolean isRead()
	{
		return !this.written;
	}
	
	/**
	 * Returns {@code true} if the field is static.
	 *
	 * @return {@code true} if static.
	 * @since 2016/09/06
	 */
	public final boolean isStatic()
	{
		return this.isstatic;
	}
	
	/**
	 * Returns {@code true} if the field is written to.
	 *
	 * @return {@code true} if written to.
	 * @since 2016/09/06
	 */
	public final boolean isWritten()
	{
		return this.written;
	}
}

