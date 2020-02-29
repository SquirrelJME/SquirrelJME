// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.lib;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.io.BinaryBlob;
import cc.squirreljme.jvm.io.MemoryBlob;
import cc.squirreljme.jvm.JVMFunction;

/**
 * This represents a single boot library.
 *
 * @since 2019/06/14
 */
public final class BootRomLibrary
	extends ClassLibrary
{
	/** Offset of the resource count. */
	public static final byte JAR_NUMRC_OFFSET =
		4;
		
	/** Offset of the table of contents. */
	public static final byte JAR_TOC_OFFSET_OFFSET =
		8;
		
	/** Manifest offset. */
	public static final byte JAR_MANIFESTOFF_OFFSET =
		12;
	
	/** Manifest length. */
	public static final byte JAR_MANIFESTLEN_OFFSET =
		16;
	
	/** Boot initializer offset. */
	public static final byte JAR_BOOTOFFSET_OFFSET =
		20;
	
	/** Boot initializer size. */
	public static final byte JAR_BOOTSIZE_OFFSET =
		24;
	
	/** The boot pool offset. */
	public static final byte JAR_BOOTPOOL_OFFSET =
		28;
	
	/** Static field basein RAM. */
	public static final byte JAR_BOOTSFIELDBASE_OFFSET =
		32;
	
	/** The start method offset. */
	public static final byte JAR_BOOTSTART_OFFSET =
		36;
	
	/** System call static field pointer. */
	public static final byte JAR_SYSCALLSFP_OFFSET =
		40;
	
	/** System call handler code address .*/
	public static final byte JAR_SYSCALLHANDLER_OFFSET =
		44;
	
	/** System call pool address. */
	public static final byte JAR_SYSCALLPOOL_OFFSET =
		48;
	
	/** The ClassInfo for {@code byte[]}. */
	public static final byte JAR_BOOTCLASSIDBA_OFFSET =
		52;
	
	/** The ClassInfo for {@code byte[][]}. */
	public static final byte JAR_BOOTCLASSIDBAA_OFFSET =
		56;
	
	/** Static constant pool offset. */
	public static final byte JAR_STATICPOOLOFF_OFFSET =
		60;
	
	/** Static constant pool size. */
	public static final byte JAR_STATICPOOLSIZE_OFFSET =
		64;
	
	/** Runtime constant pool offset. */
	public static final byte JAR_RUNTIMEPOOLOFF_OFFSET =
		68;
	
	/** Runtime constant pool size. */
	public static final byte JAR_RUNTIMEPOOLSIZE_OFFSET =
		72;
	
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
	@Deprecated
	protected final int address;
	
	/** The length of the JAR. */
	@Deprecated
	protected final int length;
	
	/** Manifest address. */
	protected final int manifestaddress;
	
	/** Manifest length. */
	protected final int manifestlength;
	
	/** Boot pool information. */
	protected final BootRomPoolInfo bootrompool;
	
	/** The blob for this JAR. */
	protected final BinaryBlob blob;
	
	/**
	 * Initializes the boot library.
	 *
	 * @param __name The name of the library.
	 * @param __addr The JAR address.
	 * @param __len The JAR length.
	 * @param __maddr The manifest address.
	 * @param __mlen The manifest length.
	 * @param __brpi The boot pool information from the ROM.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/19
	 */
	public BootRomLibrary(String __name, int __addr, int __len, int __maddr,
		int __mlen, BootRomPoolInfo __brpi)
		throws NullPointerException
	{
		if (__name == null || __brpi == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
		this.address = __addr;
		this.length = __len;
		this.manifestaddress = __maddr;
		this.manifestlength = __mlen;
		this.bootrompool = __brpi;
		
		// Initializes the blob
		this.blob = new MemoryBlob(__addr, __len);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/23
	 */
	@Override
	public final int indexOf(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Hash code for this string
		int hash = __name.hashCode();
		
		// Scan through the table of contents
		int bp = this.address,
			sp = bp + Assembly.memReadJavaInt(bp,
				BootRomLibrary.JAR_TOC_OFFSET_OFFSET);
		for (int i = 0, n = Assembly.memReadJavaInt(bp,
			BootRomLibrary.JAR_NUMRC_OFFSET); i < n;
			 i++, sp += BootRomLibrary.TOC_ENTRY_SIZE)
		{
			// Hash code does not match
			if (hash != Assembly.memReadJavaInt(sp,
				BootRomLibrary.TOC_HASHCODE_OFFSET))
				continue;
			
			// Is at this index
			if (__name.equals(JVMFunction.jvmLoadString(
				bp + Assembly.memReadJavaInt(sp,
					BootRomLibrary.TOC_NAME_OFFSET))))
				return i;
		}
		
		// Not found
		return -1;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/09/22
	 */
	@Override
	public final String libraryName()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/07/11
	 */
	@Override
	public final BinaryBlob resourceData(int __dx)
		throws IndexOutOfBoundsException
	{
		// Get base address of the library
		int bp = this.address;
		
		// {@squirreljme.error SV07 Attempt to access resource which was not
		// in range of the boot library.}
		if (__dx < 0 || __dx >= Assembly.memReadJavaInt(bp,
			BootRomLibrary.JAR_NUMRC_OFFSET))
			throw new IndexOutOfBoundsException("SV07");
		
		// Read from the table of contents, the offset to the data.
		int tocoffset = bp + Assembly.memReadJavaInt(bp,
			BootRomLibrary.JAR_TOC_OFFSET_OFFSET);
		return new MemoryBlob(bp + Assembly.memReadJavaInt(tocoffset,
				(BootRomLibrary.TOC_ENTRY_SIZE * __dx) + BootRomLibrary.TOC_DATA_OFFSET),
			Assembly.memReadJavaInt(tocoffset,
				(BootRomLibrary.TOC_ENTRY_SIZE * __dx) + BootRomLibrary.TOC_SIZE_OFFSET));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/11/18
	 */
	@Override
	public AbstractPoolParser splitPool(boolean __rt)
	{
		BinaryBlob blob = this.blob;
		
		// Static
		int jpo, jps;
		if (__rt)
		{
			jpo = blob.readJavaInt(BootRomLibrary.JAR_RUNTIMEPOOLOFF_OFFSET);
			jps = blob.readJavaInt(BootRomLibrary.JAR_RUNTIMEPOOLSIZE_OFFSET);
		}
		
		// Run-time
		else
		{
			jpo = blob.readJavaInt(BootRomLibrary.JAR_STATICPOOLOFF_OFFSET);
			jps = blob.readJavaInt(BootRomLibrary.JAR_STATICPOOLSIZE_OFFSET);
		}
		
		// This JAR has a pool in it
		AbstractPoolParser rv;
		if (jpo >= 0 && jps >= 0)
			rv = new ClassPoolParser(blob.subSection(jpo, jps));
		
		// Possibly using JAR pool
		else
		{
			BootRomPoolInfo bootrompool = this.bootrompool;
			if (bootrompool.isDefined(__rt))
				rv = new ClassPoolParser(
					new MemoryBlob(bootrompool.address(__rt),
						bootrompool.size(__rt)));
			
			// Not using any pool
			else
				rv = null;
		}
		
		return rv;
	}
}

