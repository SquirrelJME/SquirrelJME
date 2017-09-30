// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.cff;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.io.region.SizeLimitedInputStream;

/**
 * This represents a single class file.
 *
 * @since 2017/09/26
 */
public final class ClassFile
{
	/** The magic number of the class file. */
	private static final int _MAGIC_NUMBER =
		0xCAFEBABE;
	
	/**
	 * Initializes the class file.
	 *
	 * @since 2017/09/26
	 */
	ClassFile()
	{
		throw new todo.TODO();
	}
	
	/**
	 * This parses the input stream as a class file and returns the
	 * representation of that class file.
	 *
	 * @param __is The input stream to source classes from.
	 * @return The decoded class file.
	 * @throws InvalidClassFormatException If the class file is not formatted
	 * correctly.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/26
	 */
	public static ClassFile decode(InputStream __is)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI2u The magic number for the class is not
		// valid. (The read magic number; The expected magic number)}
		DataInputStream in = new DataInputStream(__is);
		int magic = in.readInt();
		if (magic != _MAGIC_NUMBER)
			throw new InvalidClassFormatException(String.format(
				"JI2u %08x %08x", magic, _MAGIC_NUMBER));
		
		// {@squirreljme.error JI08 The version number of the input class
		// file is not valid. (The version number)}
		int cver = in.readShort() | (in.readShort() << 16);
		ClassVersion version = ClassVersion.findVersion(cver);
		if (version == null)
			throw new InvalidClassFormatException(String.format("JI08 %d.%d",
				cver >>> 16, (cver & 0xFFFF)));
		
		// Decode the constant pool
		Pool pool = Pool.decode(in);
		
		// Decode flags
		ClassFlags classflags = new ClassFlags(in.readUnsignedShort());
		
		// Name of the current class
		ClassName thisname = pool.<ClassName>require(ClassName.class,
			in.readUnsignedShort());
		
		// {@squirreljme.error JI0s Either Object has a superclass which it
		// cannot extend any class or any other class does not have a super
		// class. (The current class name; The super class name)}
		ClassName supername = pool.<ClassName>get(ClassName.class,
			in.readUnsignedShort());
		if (thisname.equals(new ClassName("java/lang/Object")) !=
			(supername == null))
			throw new InvalidClassFormatException(String.format("JI0s %s %s",
				thisname, supername));
		
		// Read interfaces
		int icount = in.readUnsignedShort();
		ClassName[] interfaces = new ClassName[icount];
		for (int i = 0; i < icount; i++)
			interfaces[i] = pool.<ClassName>require(ClassName.class,
				in.readUnsignedShort());
		
		// Read fields
		Field[] fields = Field.decode(version, thisname, classflags, pool, in);
		
		// Read methods
		Method[] methods = Method.decode(version, thisname, classflags, pool,
			in);
		
		// Handle attributes
		int na = in.readUnsignedShort();
		String[] attr = new String[1];
		int[] alen = new int[1];
		for (int j = 0; j < na; j++)
			try (DataInputStream ai = __nextAttribute(in, pool, attr, alen))
			{
				// Just do nothing with any attribute because to the VM none
				// of the information is really that important anyway
			}
		
		// {@squirreljme.error JI12 Expected end of the class to follow the
		// attributes in the class. (The name of this class)}
		if (in.read() >= 0)
			throw new InvalidClassFormatException(
				String.format("JI12 %s", thisname));
		
		throw new todo.TODO();
	}
	
	/**
	 * Reads the next attribute from the class.
	 *
	 * @param __in The input stream where bytes come from.
	 * @param __pool The constant pool.
	 * @param __aname The output name of the attribute which was just read.
	 * @param __alens The length of the attribute.
	 * @return The stream to the attribute which just has been read.
	 * @throws IOException On read errors.
	 * @throws InvalidClassFormatException If the attribute is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/09
	 */
	static DataInputStream __nextAttribute(DataInputStream __in,
		Pool __pool, String[] __aname, int[] __alens)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		// Check
		if (__aname == null || __alens == null)
			throw new NullPointerException("NARG");
		
		// The name is not parsed here
		__aname[0] = __pool.<UTFConstantEntry>require(UTFConstantEntry.class,
			__in.readUnsignedShort()).toString();
		
		// {@squirreljme.error JI1a Attribute exceeds 2GiB in length. (The
		// size of the attribute)}
		int len = __in.readInt();
		if (len < 0)
			throw new InvalidClassFormatException(String.format("JI1a %d",
				len & 0xFFFFFFFFL));
		
		// Used as a hint
		__alens[0] = len;
		
		// Setup reader
		return new DataInputStream(new SizeLimitedInputStream(__in, len, true,
			false));
	}
}

