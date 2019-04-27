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
	
	/** Magic number for the end of file. */
	public static final int END_MAGIC_NUMBER =
		0x51756161;
	
	/** The size of the header without the magic number. */
	public static final int HEADER_SIZE_WITHOUT_MAGIC =
		28;
	
	/** The size of the header with the magic number. */
	public static final int HEADER_SIZE_WITH_MAGIC =
		HEADER_SIZE_WITHOUT_MAGIC + 4;
	
	/** Number of resources. */
	public final int numrc;
	
	/** Table of contents offset. */
	public final int tocoffset;
	
	/** Boot Pool offset. */
	public final int bootpooloff;
	
	/** Boot RAM offset. */
	public final int bootramoff;
	
	/** Boot RAM size. */
	public final int bootramsize;
	
	/** Initialize RAM offset. */
	public final int initramoff;
	
	/** Initialize RAM size. */
	public final int initramsize;
	
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
		
		// Boot RAM
		this.bootpooloff = __fs[at++];
		this.bootramoff = __fs[at++];
		this.bootramsize = __fs[at++];
		this.initramoff = __fs[at++];
		this.initramsize = __fs[at++];
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
		
		// {@squirreljme.error BC01 Invalid minimized Jar magic number.}
		if (MAGIC_NUMBER != din.readInt())
			throw new InvalidClassFormatException("BC01");
		
		// Build
		return new MinimizedJarHeader(
			/* numrc */ din.readInt(),
			/* tocoffset */ din.readInt(),
			
			/* bootpooloff */ din.readInt(),
			/* bootramoff */ din.readInt(),
			/* bootramlen */ din.readInt(),
			/* initramoff */ din.readInt(),
			/* initramlen */ din.readInt());
	}
}

