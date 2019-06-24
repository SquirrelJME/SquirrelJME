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
 * This represents a single boot library.
 *
 * @since 2019/06/14
 */
public final class BootLibrary
{
	/** The name of this library. */
	protected final String name;
	
	/** The absolute address of the JAR. */
	protected final int address;
	
	/** The length of the JAR. */
	protected final int length;
	
	/** Manifest address. */
	protected final int manifestaddress;
	
	/** Manifest length. */
	protected final int manifestlength;
	
	/** The table of contents address. */
	protected final int tocaddress;
	
	/** The number of entries in the table of contents. */
	protected final int entrycount;
	
	/**
	 * Initializes the boot library.
	 *
	 * @param __name The name of the library.
	 * @param __addr The JAR address.
	 * @param __len The JAR length.
	 * @param __maddr The manifest address.
	 * @param __mlen The manifest length.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/19
	 */
	public BootLibrary(String __name, int __addr, int __len, int __maddr,
		int __mlen)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
		this.address = __addr;
		this.length = __len;
		this.manifestaddress = __maddr;
		this.manifestlength = __mlen;
		
		// Read table of contents info
		this.tocaddress = 0;
		this.entrycount = 0;
	}
	
	/**
	 * Locates the given resource.
	 *
	 * @param __name The name of the resource to get.
	 * @return The index of the resource or {@code -1} if it was not found.
	 * @since 2019/06/23
	 */
	public final int indexOf(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		Assembly.breakpoint();
		throw new todo.TODO();
	}
}

