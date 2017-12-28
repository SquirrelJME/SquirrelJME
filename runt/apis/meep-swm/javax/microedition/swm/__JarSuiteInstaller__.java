// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

/**
 * This is an installer which will install the specified byte array into the
 * host system.
 *
 * @since 2017/12/28
 */
final class __JarSuiteInstaller__
	extends SuiteInstaller
{
	/** Offset into the array. */
	protected final int offset;
	
	/** Number of bytes to read. */
	protected final int length;
	
	/** The data buffer. */
	private final byte[] _data;
	
	/**
	 * Initializes the installer for installing the given bytes.
	 *
	 * @param __b The JAR file data.
	 * @param __o Offset into the array.
	 * @param __l Length of the array.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/28
	 */
	__JarSuiteInstaller__(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		this._data = __b;
		this.offset = __o;
		this.length = __l;
	}
}

