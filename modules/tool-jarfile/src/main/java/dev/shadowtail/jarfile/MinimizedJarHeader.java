// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.classfile.InvalidClassFormatException;

/**
 * This represents the header for a minimized Jar file.
 *
 * @since 2019/04/27
 */
public final class MinimizedJarHeader
{
	/** Magic number for the JAR. */
	public static final int MAGIC_NUMBER =
		0x00456570;
	
	/** The size of the header without the magic number. */
	public static final int HEADER_SIZE_WITHOUT_MAGIC =
		72;
	
	/** The size of the header with the magic number. */
	public static final int HEADER_SIZE_WITH_MAGIC =
		76;
	
	/** Size of table of contents entries. */
	public static final int TOC_ENTRY_SIZE =
		16;
	
	/** Number of resources. */
	public final int numrc;
	
	/** Table of contents offset. */
	public final int tocoffset;
	
	/** Manifest offset. */
	public final int manifestoff;
	
	/** Manifest length. */
	public final int manifestlen;
	
	/** Boot initializer offset. */
	public final int bootoffset;
	
	/** Boot initializer size. */
	public final int bootsize;
	
	/** The boot pool offset. */
	public final int bootpool;
	
	/** Static field basein RAM. */
	public final int bootsfieldbase;
	
	/** The start method offset. */
	public final int bootstart;
	
	/** System call static field pointer. */
	public final int syscallsfp;
	
	/** System call handler code address .*/
	public final int syscallhandler;
	
	/** System call pool address. */
	public final int syscallpool;
	
	/** The ClassInfo for {@code byte[]}. */
	public final int bootclassidba;
	
	/** The ClassInfo for {@code byte[][]}. */
	public final int bootclassidbaa;
	
	/** Static constant pool offset. */
	public final int staticpooloff;
	
	/** Static constant pool size. */
	public final int staticpoolsize;
	
	/** Runtime constant pool offset. */
	public final int runtimepooloff;
	
	/** Runtime constant pool size. */
	public final int runtimepoolsize;
	
	/**
	 * Initializes the Jar header.
	 *
	 * @param __fs Fields.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	public MinimizedJarHeader(int... __fs)
		throws NullPointerException
	{
		if (__fs == null)
			throw new NullPointerException("NARG");
		
		int at = 0;
		
		// Table of contents
		this.numrc = __fs[at++];
		this.tocoffset = __fs[at++];
		
		// The offset to the manifest and its length
		this.manifestoff = __fs[at++];
		this.manifestlen = __fs[at++];
		
		// Boot initializer
		this.bootoffset = __fs[at++];
		this.bootsize = __fs[at++];
		this.bootpool = __fs[at++];
		this.bootsfieldbase = __fs[at++];
		this.bootstart = __fs[at++];
		this.syscallsfp = __fs[at++];
		this.syscallhandler = __fs[at++];
		this.syscallpool = __fs[at++];
		this.bootclassidba = __fs[at++];
		this.bootclassidbaa = __fs[at++];
		
		// Static and run-time constant pool
		this.staticpooloff = __fs[at++];
		this.staticpoolsize = __fs[at++];
		this.runtimepooloff = __fs[at++];
		this.runtimepoolsize = __fs[at++];
	}
	
	/**
	 * Decodes the JAR header.
	 *
	 * @param __in The input stream.
	 * @return The resulting header.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	public static final MinimizedJarHeader decode(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Need to read fields
		DataInputStream din = new DataInputStream(__in);
		
		// {@squirreljme.error BC0a Invalid minimized Jar magic number.
		// (The read magic number; The expected magic number)}
		int wasmagic;
		if (MinimizedJarHeader.MAGIC_NUMBER != (wasmagic = din.readInt()))
			throw new InvalidClassFormatException(String.format(
				"BC0a %08x %08x", wasmagic, MinimizedJarHeader.MAGIC_NUMBER));
		
		// Build
		return new MinimizedJarHeader(
			/* numrc */ din.readInt(),
			/* tocoffset */ din.readInt(),
			
			// Manifest
			/* manifestoff */ din.readInt(),
			/* manifestlen */ din.readInt(),
			
			// Boot initializer
			/* bootoffset */ din.readInt(),
			/* bootsize */ din.readInt(),
			/* bootpool */ din.readInt(),
			/* bootsfieldbase */ din.readInt(),
			/* bootstart */ din.readInt(),
			/* syscallsfp */ din.readInt(),
			/* syscallhandler */ din.readInt(),
			/* syscallpool */ din.readInt(),
			/* bootclassidba */ din.readInt(),
			/* bootclassidbaa */ din.readInt(),
			
			// Static and runtime pool
			/* staticpooloff */ din.readInt(),
			/* staticpoolsize */ din.readInt(),
			/* runtimepooloff */ din.readInt(),
			/* runtimepoolsize */ din.readInt());
	}
}

