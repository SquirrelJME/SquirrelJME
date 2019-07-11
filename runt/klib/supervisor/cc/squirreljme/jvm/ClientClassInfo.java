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
	implements AutoCloseable
{
	/** The pointer to the class information. */
	public final int classinfopointer;
	
	/** The pointer to the mini-class information. */
	public final int miniclassaddress;
	
	/** The mini accessor instance which exists. */
	private volatile MiniClassAccessor _miniaccessor;
	
	/** The number of times the accessor is open. */
	private volatile int _opencount;
	
	/**
	 * Initializes the client class information.
	 *
	 * @param __cip The class info pointer.
	 * @param __minip The mini class address.
	 * @since 2019/06/24
	 */
	public ClientClassInfo(int __cip, int __minip)
	{
		this.classinfopointer = __cip;
		this.miniclassaddress = __minip;
	}
	
	/**
	 * Returns the mini-class accessor.
	 *
	 * @return The mini-class accessor.
	 * @since 2019/07/11
	 */
	public final MiniClassAccessor accessor()
	{
		// Detail details
		int opencount = this._opencount;
		MiniClassAccessor rv = this._miniaccessor;
		
		// If this is just being opened, then create an accessor for it
		if (opencount == 0)
			this._miniaccessor = (rv =
				new MiniClassAccessor(this.miniclassaddress));
		
		// Count up
		this._opencount = opencount + 1;
		
		// Use it
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/07/11
	 */
	@Override
	public final void close()
	{
		// Get current count
		int opencount = this._opencount;
		
		// Reduce the count
		if (opencount > 0)
			opencount--;
		
		// Store new count
		this._opencount = 0;
		
		// If there are no counts then clear the object so it gets GCed
		if (opencount == 0)
			this._miniaccessor = null;
	}
}

