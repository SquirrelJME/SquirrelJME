// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.packfile;

/**
 * This represents the header for a minimized pack file, it mostly just
 * represents a number of JAR files which are combined into one.
 *
 * @since 2019/05/28
 */
public final class MinimizedPackHeader
{
	/** Magic number for the pack file. */
	public static final int MAGIC_NUMBER =
		0x58455223;
	
	/** The size of the header without the magic number. */
	public static final int HEADER_SIZE_WITHOUT_MAGIC =
		52;
	
	/** The size of the header with the magic number. */
	public static final int HEADER_SIZE_WITH_MAGIC =
		56;
	
	/** The offset to the BootJAR offset (which has BootRAM), with magic. */
	public static final int OFFSET_OF_BOOTJAROFFSET =
		16;
	
	/** The offset to the BootJAR size, with magic. */
	public static final int OFFSET_OF_BOOTJARSIZE =
		20;
	
	/** Size of individual table of contents entry. */
	public static final int TOC_ENTRY_SIZE =
		20;
	
	/** The number of jars in this packfile. (4) */
	public final int numjars;
	
	/** The offset to the table of contents. (8) */
	public final int tocoffset;
	
	/** The index of the JAR which should be the boot point. (12) */
	public final int bootjarindex;
	
	/** The offset into the packfile where the boot entry is. (16) */
	public final int bootjaroffset;
	
	/** The size of the boot jar. (20) */
	public final int bootjarsize;
	
	/** Initial class path library indexes. (24) */
	public final int booticpoffset;
	
	/** Initial class path library index count. (28) */
	public final int booticpsize;
	
	/** Initial main class to boot. (32) */
	public final int bootmainclass;
	
	/** Initial main entry type. (36) */
	public final int bootmaintype;
	
	/** Static constant pool offset. */
	public final int staticpooloff;
	
	/** Static constant pool size. */
	public final int staticpoolsize;
	
	/** Runtime constant pool offset. */
	public final int runtimepooloff;
	
	/** Runtime constant pool size. */
	public final int runtimepoolsize;
	
	/**
	 * Initializes the pack header.
	 *
	 * @param __fs Fields.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/28
	 */
	public MinimizedPackHeader(int... __fs)
		throws NullPointerException
	{
		if (__fs == null)
			throw new NullPointerException("NARG");
		
		int at = 0;
		
		// Jar and table of contents
		this.numjars = __fs[at++];
		this.tocoffset = __fs[at++];
		
		// Boot JAR that may be specified
		this.bootjarindex = __fs[at++];
		this.bootjaroffset = __fs[at++];
		this.bootjarsize = __fs[at++];
		this.booticpoffset = __fs[at++];
		this.booticpsize = __fs[at++];
		this.bootmainclass = __fs[at++];
		this.bootmaintype = __fs[at++];
		
		// Static and run-time constant pool
		this.staticpooloff = __fs[at++];
		this.staticpoolsize = __fs[at++];
		this.runtimepooloff = __fs[at++];
		this.runtimepoolsize = __fs[at++];
	}
}

