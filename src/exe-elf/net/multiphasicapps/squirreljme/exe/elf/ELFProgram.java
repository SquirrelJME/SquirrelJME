// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe.elf;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a single program in the program header which can be loaded or not
 * loaded, this is used by the actual system.
 *
 * @since 2016/08/16
 */
public final class ELFProgram
	extends __ELFBaseEntry__
{
	/** Flags for the header. */
	protected final Set<ELFProgramFlag> flags =
		new HashSet<>();
	
	/** The name of this namespace. */
	final String _name;
	
	/** The namespace data. */
	final byte[] _data;
	
	/** The namespace length. */
	final int _length;
	
	/** Also create a section? */
	volatile boolean _sectionalso =
		true;
	
	/**
	 * Initializes the program from the given input stream.
	 *
	 * @param __eo The owning ELF.
	 * @param __n The name of the namespace.
	 * @param __is Is input stream of the namespace data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/16
	 */
	ELFProgram(ELFOutput __eo, String __n, InputStream __is)
		throws IOException, NullPointerException
	{
		super(__eo);
		
		// Check
		if (__n == null || __is == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._name = __n;
		
		// Copy the data
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			// Copy all of the data
			byte[] buf = new byte[4096];
			for (;;)
			{
				int rc = __is.read(buf);
				
				// EOF?
				if (rc < 0)
					break;
				
				// Copy
				baos.write(buf, 0, rc);
			}
			
			// Setup buffer
			byte[] dest = baos.toByteArray();
			this._data = dest;
			this._length = dest.length;
		}
	}
	
	/**
	 * Initializes the program using the specified buffer data.
	 *
	 * @param __eo The owning ELF.
	 * @param __n The name of the namespace.
	 * @param __b The array to use.
	 * @param __o The offset.
	 * @param __l The length.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/16
	 */
	ELFProgram(ELFOutput __eo, String __n, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		super(__eo);
		
		// Check
		if (__b == null || __n == null)
			throw new NullPointerException("NARG");
		int bn = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > bn)
			throw new IndexOutOfBoundsException("AIOB");
		
		// Set
		this._name = __n;
		byte[] dest = new byte[__l];
		System.arraycopy(__b, __o, dest, 0, __l);
		this._data = dest;
		this._length = __l;
	}
	
	/**
	 * Sets the flags for the program.
	 *
	 * @param __f The flags to set, if this is {@code null} or the first
	 * element is {@code null} they are cleared.
	 * @since 2016/08/16
	 */
	public void setFlags(ELFProgramFlag... __f)
	{
		// Lock
		synchronized (this.lock)
		{
			// Clear flags?
			Set<ELFProgramFlag> flags = this.flags;
			if (__f == null || __f[0] == null)
				flags.clear();
			
			// Set
			else
				for (ELFProgramFlag q : __f)
					flags.add(q);
		}
	}
	
	/**
	 * Sets whether or not a section should be made for this program area.
	 *
	 * @param __s If {@code true} then a section is made along with this
	 * program area.
	 * @since 2016/08/16
	 */
	public void setMakeSection(boolean __s)
	{
		// Lock
		synchronized (this.lock)
		{
			this._sectionalso = __s;
		}
	}
}

