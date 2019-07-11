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
	/** Offset of the resource count. */
	public static final int NUMRC_OFFSET =
		4;
		
	/** Offset of the table of contents. */
	public static final int TOC_OFFSET_OFFSET =
		8;
	
	/** TOC hashcode offset. */
	public static final int TOC_HASHCODE_OFFSET =
		0;
	
	/** TOC name offset. */
	public static final int TOC_NAME_OFFSET =
		4;
	
	/** TOC Data offset. */
	public static final int TOC_DATA_OFFSET =
		8;
	
	/** TOC Size offset. */
	public static final int TOC_SIZE_OFFSET =
		12;
	
	/** Size of table of contents entries. */
	public static final int TOC_ENTRY_SIZE =
		16;
	
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
		
		// Hash code for this string
		int hash = __name.hashCode();
		
		// Scan through the table of contents
		int bp = this.address,
			sp = bp + Assembly.memReadJavaInt(bp, TOC_OFFSET_OFFSET);
		for (int i = 0, n = Assembly.memReadJavaInt(bp, NUMRC_OFFSET); i < n;
			i++, sp += TOC_ENTRY_SIZE)
		{
			// Hash code does not match
			if (hash != Assembly.memReadJavaInt(sp, TOC_HASHCODE_OFFSET))
				continue;
			
			// Is at this index
			if (__name.equals(JVMFunction.jvmLoadString(
				bp + Assembly.memReadJavaInt(sp, TOC_NAME_OFFSET))))
				return i;
		}
		
		// Not found
		return -1;
	}
	
	/**
	 * Returns the data pointer of the given index.
	 *
	 * @param __dx The index to get the data for.
	 * @return The pointer to the index data or {@code 0} if it is not valid.
	 * @since 2019/07/11
	 */
	public final int resourceData(int __dx)
	{
		// Get base address of the library
		int bp = this.address;
		
		// If the resource is out of range, then ignore because some invalid
		// memory will be used which would be bad
		if (__dx < 0 || __dx >= Assembly.memReadJavaInt(bp, NUMRC_OFFSET))
			return 0;
		
		// Read from the table of contents, the offset to the data.
		return bp + Assembly.memReadJavaInt(
			bp + Assembly.memReadJavaInt(bp, TOC_OFFSET_OFFSET),
			(TOC_ENTRY_SIZE * __dx) + TOC_DATA_OFFSET);
	}
}

