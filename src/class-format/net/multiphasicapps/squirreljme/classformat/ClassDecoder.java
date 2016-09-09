// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

import java.io.DataInputStream;
import java.io.IOException;
import net.multiphasicapps.io.region.SizeLimitedInputStream;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;

/**
 * This performs the decoding of the class file format.
 *
 * @since 2016/06/28
 */
public final class ClassDecoder
	extends __HasAttributes__
{
	/** The object class. */
	private static final ClassNameSymbol _OBJECT_CLASS =
		ClassNameSymbol.of("java/lang/Object");
	
	/** The magic number of the class file. */
	public static final int MAGIC_NUMBER =
		0xCAFE_BABE;
	
	/** The input data source. */
	protected final DataInputStream input;
	
	/** The output description stream. */
	protected final ClassDescriptionStream output;
	
	/** Was the class decoded already? */
	private volatile boolean _did;
	
	/** The version of this class file. */
	private volatile ClassVersion _version;
	
	/** The constant pool of the class. */
	private volatile ClassConstantPool _pool;
	
	/** Class flags. */
	private volatile ClassClassFlags _flags;
	
	/** The name of this class. */
	private volatile ClassNameSymbol _classname;
	
	/**
	 * This initializes the decoder for classes.
	 *
	 * @param __dis The source for data input.
	 * @param __out The interface that is told class details.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/28
	 */
	public ClassDecoder(DataInputStream __dis, ClassDescriptionStream __out)
		throws NullPointerException
	{
		// Check
		if (__dis == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.input = __dis;
		this.output = __out;
	}
	
	/**
	 * This performs the actual decoding of the class file.
	 *
	 * @throws IllegalStateException If the class was already decoded.
	 * @throws IOException On read errors.
	 * @throws ClassFormatException If the class file format is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/28
	 */
	public final void decode()
		throws IllegalStateException, IOException, ClassFormatException,
			NullPointerException
	{
		// {@squirreljme.error AY0i A class may only be decoded once.}
		if (this._did)
			throw new IllegalStateException("AY0i");
		this._did = true;
		
		// Get data source
		DataInputStream input = this.input;
		ClassDescriptionStream output = this.output;
		
		// {@squirreljme.error AY12 The magic number of the input data stream
		// does not match that of the Java class file. (The magic number which
		// was read)}
		int fail;
		if ((fail = input.readInt()) != MAGIC_NUMBER)
			throw new ClassFormatException(String.format("AY12 %08x", fail));
		
		// {@squirreljme.error AY13 The version number of the input class file
		// is not valid. (The version number)}
		int cver = input.readShort() | (input.readShort() << 16);
		ClassVersion version = ClassVersion.findVersion(cver);
		this._version = version;
		if (version == null)
			throw new ClassFormatException(String.format("AY13 %d.%d", cver >>> 16,
				(cver & 0xFFFF)));
		
		// Report it
		output.version(version);
		
		// Decode the constant pool
		ClassConstantPool pool = new ClassConstantPool(input, this);
		this._pool = pool;
		
		// Read the flags for this class
		ClassClassFlags cf = __FlagDecoder__.__class(
			input.readUnsignedShort());
		this._flags = cf;
		
		// Read class name
		int clnamedx = input.readUnsignedShort();
		ClassNameSymbol clname = pool.get(clnamedx).<ClassNameSymbol>get(
			true, ClassNameSymbol.class);
		boolean isobject = (clname.equals(_OBJECT_CLASS));
		
		// Send class name
		this._classname = clname;
		output.className(clname);
		
		// Send pool
		output.constantPool(pool);
		
		// Send class flags
		output.classFlags(cf);
		
		// Read super class
		int sudx = input.readUnsignedShort();
		ClassNameSymbol suname = pool.get(sudx).<ClassNameSymbol>optional(
			true, ClassNameSymbol.class);
		
		// {@squirreljme.error AY0m The Object class must have no super
		// class and any non-Object class must have a super class.
		// (The class name; The super-class name)}
		if ((suname != null) == isobject)
			throw new ClassFormatException(String.format("AY0m %s %s", clname,
				suname));
		
		// {@squirreljme.error AY0n Interfaces must extend the Object
		// class. (Class flags; The super-class name)}
		if (cf.isInterface() && !suname.equals(_OBJECT_CLASS))
			throw new ClassFormatException(String.format("AY0n %s %s", cf,
				suname));
		
		// Send
		output.superClass(suname);
		
		// Read in interfaces
		int icount = input.readUnsignedShort();
		ClassNameSymbol[] ifaces = new ClassNameSymbol[icount];
		int[] ifdxs = new int[icount];
		for (int i = 0; i < icount; i++)
		{
			int idx = input.readUnsignedShort();
			ifaces[i] = pool.get(idx).<ClassNameSymbol>get(true,
				ClassNameSymbol.class);
			ifdxs[i] = idx;
		}
		
		// Send
		output.interfaceClasses(ifaces, ifdxs);
		
		// Handle fields
		int fcount = input.readUnsignedShort();
		output.fieldCount(fcount);
		for (int i = 0; i < fcount; i++)
			new __FieldDecoder__(cw, input, pool, cf).__decode();
		
		// Handle methods
		int mcount = input.readUnsignedShort();
		output.methodCount(mcount);
		for (int i = 0; i < mcount; i++)
			new __MethodDecoder__(cw, input, pool, cf, this).__decode();
		
		// Handle class attributes
		int na = input.readUnsignedShort();
		for (int i = 0; i < na; i++)
			__readAttribute(pool, input);
		
		// End class
		output.endClass();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/27
	 */
	@Override
	void __handleAttribute(String __name, DataInputStream __is)
		throws IOException
	{
		// Check
		if (__name == null || __is == null)
			throw new NullPointerException("NARG");
		
		// No class attributes are handled at all, so ignore them all
	}
}

