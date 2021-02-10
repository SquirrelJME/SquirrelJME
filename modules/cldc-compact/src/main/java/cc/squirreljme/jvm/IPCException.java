// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This is an exception which was thrown in a cross RPC call.
 *
 * @since 2019/12/28
 */
@Deprecated
public class IPCException
	extends RuntimeException
{
	/** The class name note pointer. */
	@Deprecated
	protected final int classnotepointer;
	
	/**
	 * Initializes the exception with an unknown type.
	 *
	 * @since 2019/12/28
	 */
	@Deprecated
	public IPCException()
	{
		this.classnotepointer = 0;
	}
	
	/**
	 * Initializes the IPC Exception.
	 *
	 * @param __cnp The class note pointer which was used.
	 * @since 2019/12/28
	 */
	@Deprecated
	public IPCException(int __cnp)
	{
		this.classnotepointer = __cnp;
	}
}

