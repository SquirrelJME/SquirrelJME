// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import __squirreljme.IPC;
import __squirreljme.IPCAlternative;
import __squirreljme.IPCException;

/**
 * This class provides the alternative implementation of SquirrelJME's IPC
 * interfaces which are used by all applications. This must be used by the
 * kernel because the normal IPC interface is purely virtual and on a host
 * JVM an exception will be thrown, while running on SquirrelJME it would
 * attempt to interact with actual processes running outside of the guest JVM.
 *
 * @since 2016/05/31
 */
public class KernelIPCAlternative
	implements IPCAlternative
{
	/** The owning kernel. */
	protected final Kernel kernel;
	
	/** The sockets which are currently used. */
	private final List<KernelIPCSocket> _sockets =
		new ArrayList<>();
	
	/** The socket identifier generator. */
	private final __IdentifierGenerator__<KernelIPCSocket> _socketsidgen =
		new __IdentifierGenerator__<>(this._sockets);
	
	/**
	 * This initializes the alternative IPC interface for usage by the kernel.
	 *
	 * @param __k The kernel which owns this alternative.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/31
	 */
	public KernelIPCAlternative(Kernel __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.kernel = __k;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/31
	 */
	@Override
	public int listen(int __svid)
	{
		// Check
		if (__svid <= 0)
			return IPC.ERROR_INVALID_SERVICE_ID;
		
		// Lock
		List<KernelIPCSocket> sockets = this._sockets;
		synchronized (sockets)
		{
			// Detect if a given service is already provided
			for (KernelIPCSocket s : sockets)
				if (s instanceof KernelIPCServer)
					if (((KernelIPCServer)s).serviceId() == __svid)
						return IPC.ERROR_SERVICE_IN_USE;
			
			// Could fail
			try
			{
				// Create a new socket for this service
				KernelIPCServer sock = new KernelIPCServer(this._socketsidgen.
					__next(), __svid);
				
				// Add to the list
				int at = Collections.<Object>binarySearch(sockets, sock,
					__IdentifierGenerator__._COMPARATOR);
				
				// {@squirreljme.error AY0c The socket using a given ID is
				// already taken. (The socket ID; The location of it)}
				if (at >= 0)
					throw new KernelException(String.format("AY0c %d %d",
						sock.id(), at));
				
				// Add it
				sockets.add(-(at + 1), sock);
				
				// Return the identifier
				return sock.id();
			}
			
			// Handles error codes
			catch (IPCException e)
			{
				return e.toErrorCode();
			}
		}
	}
}

