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
 * This contains the client class information.
 *
 * @since 2019/06/24
 */
public final class ClientClassInfo
{
	/** The pointer to the class information. */
	public final int classinfopointer;
	
	/** The pointer to the mini-class information. */
	public final int miniclassaddress;
	
	/**
	 * Initializes the client class information.
	 *
	 * @param __cip
	 * @param __minip
	 * @since 2019/06/24
	 */
	public ClientClassInfo(int __cip, int __minip)
	{
		this.classinfopointer = __cip;
		this.miniclassaddress = __minip;
	}
}

