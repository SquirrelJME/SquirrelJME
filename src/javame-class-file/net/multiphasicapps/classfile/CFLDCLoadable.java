// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This represents a constant pool entry which can be used with the {@code ldc}
 * family of instructions.
 *
 * @since 2016/04/15
 */
public interface CFLDCLoadable
	extends CFConstantEntryKind
{
	/**
	 * Returns the value of this loadable entry.
	 *
	 * @return The value of this loadable entry.
	 * @since 2016/04/19
	 */
	public abstract Object getValue();
	
	/**
	 * Is this a wide loadable entry?
	 *
	 * @return {@code true} if it consumes two variable spaces.
	 * @since 2016/04/19
	 */
	public abstract boolean isWide();
	
	/**
	 * A loadable reference which is narrow.
	 *
	 * @since 2016/04/15
	 */
	public static interface Narrow
		extends CFLDCLoadable
	{
	}
	
	/**
	 * A loadable reference which is wide.
	 *
	 * @since 2016/04/15
	 */
	public static interface Wide
		extends CFLDCLoadable
	{
	}
}

