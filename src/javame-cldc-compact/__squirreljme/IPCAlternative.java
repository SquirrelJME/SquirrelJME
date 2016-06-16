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
 * This interface is used to provide an alternative implementation of the
 * IPC interface which does not use the system call based version.
 *
 * @since 2016/05/30
 */
@Deprecated
public interface IPCAlternative
{
	/**
	 * Listens on the given socket and returns a socket identifier.
	 *
	 * @param __svid The service ID to listen on.
	 * @return The socket identifer on success or a negative value if the
	 * socket could not be created.
	 * @since 2016/05/31
	 */
	public abstract int listen(int __svid);
}

