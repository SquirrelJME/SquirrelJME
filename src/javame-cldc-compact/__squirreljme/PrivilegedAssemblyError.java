// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package __squirreljme;

/**
 * This exception is thrown when the check to see if the code is currently
 * executing in kernel space fails before a block of assembly code is
 * executed.
 *
 * This error could potentially be handled in driver code in situations where
 * if assembly code is not available it can fallback to another means of
 * execution.
 *
 * @since 2016/05/27
 */
public class PrivilegedAssemblyError
	extends Error
{
	/**
	 * Initializes the exception.
	 *
	 * @since 2016/05/27
	 */
	public PrivilegedAssemblyError()
	{
	}
}

