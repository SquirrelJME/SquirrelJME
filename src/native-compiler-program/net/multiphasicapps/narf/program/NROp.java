// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.program;

/**
 * This represents a single operation in a program.
 *
 * @since 2016/05/08
 */
public abstract class NROp
{
	/**
	 * Initializes the base operation.
	 *
	 * @since 2016/05/09
	 */
	NROp()
	{
	}
	
	/**
	 * This represents an operation which may branch to the start of a basic
	 * block.
	 *
	 * @since 2016/05/09
	 */
	public static interface TypeBranch
	{
	}
	
	/**
	 * This is a flagging interface attached to operations to indicate that
	 * they are terminal operations and must be the last operation in a basic
	 * block.
	 *
	 * @since 2016/05/09
	 */
	public static interface TypeTerminal
	{
	}
}

