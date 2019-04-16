// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.mini;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.multiphasicapps.classfile.InvalidClassFormatException;

/**
 * This represents the raw minimized header of a class.
 *
 * @since 2019/04/16
 */
public final class MinimizedClassHeader
{
	/** The magic number for the header. */
	public static final int MAGIC_NUMBER =
		0x00586572;
	
	/** Unused A. */
	public final int unuseda;
	
	/** Unused B. */
	public final int unusedb;
	
	/** Number of entries in constant pool. */
	public final int poolcount;
	
	/** Class flags. */
	public final int classflags;
	
	/** Name of class. */
	public final int classname;
	
	/** Super class name. */
	public final int classsuper;
	
	/** Interfaces in class. */
	public final int classints;
	
	/** Class type. */
	public final int classtype;
	
	/** Class version. */
	public final int classvers;
	
	/** Class source filename. */
	public final int classsfn;
	
	/** Static field count. */
	public final int sfcount;
	
	/** Static field bytes. */
	public final int sfbytes;
	
	/** Static field objects. */
	public final int sfobjs;
	
	/** Instance field count. */
	public final int ifcount;
	
	/** Instance field bytes. */
	public final int ifbytes;
	
	/** Instance field objects. */
	public final int ifobjs;
	
	/** Static method count. */
	public final int smcount;
	
	/** Instance method count. */
	public final int imcount;
	
	/** Offset to pool data. */
	public final int pooloff;
	
	/** Pool data size. */
	public final int poolsize;
	
	/** Static field data offset. */
	public final int sfoff;
	
	/** Static field data size. */
	public final int sfsize;
	
	/** Interface field data offset. */
	public final int ifoff;
	
	/** Interface field data size. */
	public final int ifsize;
	
	/** Static method data offset. */
	public final int smoff;
	
	/** Static method data size. */
	public final int smsize;
	
	/** Instance method data offset. */
	public final int imoff;
	
	/** Instance method data size. */
	public final int imsize;
	
	/** File size. */
	public final int filesize;
	
	/** Size of all the non-header data. */
	public final int datasize;
	
	/**
	 * Initializes the class header.
	 *
	 * @param __vx The raw values.
	 * @since 2019/04/16
	 */
	public MinimizedClassHeader(int... __vx)
	{
		int at = 0;
		
		// Unused
		this.unuseda = __vx[at++];
		this.unusedb = __vx[at++];
		
		// Constant pool
		this.poolcount = __vx[at++];
		
		// Class information
		this.classflags = __vx[at++];
		this.classname = __vx[at++];
		this.classsuper = __vx[at++];
		this.classints = __vx[at++];
		this.classtype = __vx[at++];
		this.classvers = __vx[at++];
		this.classsfn = __vx[at++];

		// Static and instance fields
		this.sfcount = __vx[at++];
		this.sfbytes = __vx[at++];
		this.sfobjs = __vx[at++];
		this.ifcount = __vx[at++];
		this.ifbytes = __vx[at++];
		this.ifobjs = __vx[at++];

		// Static and instance methods
		this.smcount = __vx[at++];
		this.imcount = __vx[at++];

		// Location and size of constant pool
		this.pooloff = __vx[at++];
		this.poolsize = __vx[at++];
		
		// Static and instance field data
		this.sfoff = __vx[at++];
		this.sfsize = __vx[at++];
		this.ifoff = __vx[at++];
		this.ifsize = __vx[at++];
			
		// Static and instance method data
		this.smoff = __vx[at++];
		this.smsize = __vx[at++];
		this.imoff = __vx[at++];
		this.imsize = __vx[at++];
			
		// File size
		this.filesize = __vx[at++];
		this.datasize = __vx[at++];
	}
	
	/**
	 * Decodes the minimized class header.
	 *
	 * @param __is The bytes to decode from.
	 * @return The resulting minimized class header.
	 * @throws InvalidClassFormatException If the class is not formatted
	 * correctly.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/16
	 */
	public static final MinimizedClassHeader decode(InputStream __is)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__is == null)
			throw new NullPointerException("NARG");
		
		DataInputStream dis = new DataInputStream(__is);
		
		// {@squirreljme.error JC2l Invalid minimized class magic number.}
		if (MAGIC_NUMBER != dis.readInt())
			throw new InvalidClassFormatException("JC2l");
		
		// Read in all the data
		return new MinimizedClassHeader(
			// Unused
			/* unuseda */ dis.readUnsignedShort(),
			/* unusedb */ dis.readUnsignedShort(),
			
			// Constant pool
			/* poolcount */ dis.readUnsignedShort(),
			
			// Class header
			/* classflags */ dis.readInt(),
			/* classname */ dis.readUnsignedShort(),
			/* classsuper */ dis.readUnsignedShort(),
			/* classints */ dis.readUnsignedShort(),
			/* classtype */ dis.readByte(),
			/* classvers */ dis.readByte(),
			/* classsfn */ dis.readUnsignedShort(),
	
			// Static and instance fields
			/* sfcount */ dis.readUnsignedShort(),
			/* sfbytes */ dis.readUnsignedShort(),
			/* sfobjs */ dis.readUnsignedShort(),
			/* ifcount */ dis.readUnsignedShort(),
			/* ifbytes */ dis.readUnsignedShort(),
			/* ifobjs */ dis.readUnsignedShort(),
	
			// Static and instance methods
			/* smcount */ dis.readUnsignedShort(),
			/* imcount */ dis.readUnsignedShort(),
	
			// Location and size of constant pool
			/* pooloff */ dis.readInt(),
			/* poolbytes */ dis.readInt(),
		
			// Static and instance field data
			/* sfoff */ dis.readInt(),
			/* sfbytes */ dis.readInt(),
			/* ifoff */ dis.readInt(),
			/* ifbytes */ dis.readInt(),
			
			// Static and instance method data
			/* smoff */ dis.readInt(),
			/* smbytes */ dis.readInt(),
			/* imoff */ dis.readInt(),
			/* imbytes */ dis.readInt(),
			
			// File size
			/* filesize */ dis.readInt(),
			/* datasize */ dis.readInt());
	}	
}

