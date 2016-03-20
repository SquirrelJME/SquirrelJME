// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.descriptors.BinaryNameSymbol;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MemberTypeSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This represents a single class loaded by the interpreter which is derived
 * from a loaded class file.
 *
 * @since 2016/03/01
 */
public class JVMClassFile
{
	/** The class file magic number. */
	public static final int MAGIC_NUMBER =
		0xCAFEBABE;
	
	/** Internal lock. */
	protected final Object lock =
		new Object();
	
	/** The owning engine. */
	protected final JVMEngine engine;
	
	/** The target class. */
	protected final JVMClass target;
	
	/** The version of this class file. */
	private volatile JVMClassVersion _version;
	
	/** The class constant pool. */
	private volatile JVMConstantPool _constantpool;
	
	/** Did this already? */
	private volatile boolean _did;
	
	/**
	 * Initializes the class loader.
	 *
	 * @param __eng The engine which is loading this class.
	 * @param __targ The target class to write data into, it is undefined
	 * behavior if it already has details set.
	 * @throws NUllPointerException On null arguments.
	 * @since 2016/03/19
	 */
	public JVMClassFile(JVMEngine __eng, JVMClass __targ)
		throws NullPointerException
	{
		// Check
		if (__eng == null || __targ == null)
			throw new NullPointerException("NARG");
		
		// Set
		engine = __eng;
		target = __targ;
	}
	
	/**
	 * Parses the class file and loads the information into the target class.
	 *
	 * @param __is The input stream to read class data from.
	 * @return {@code this}.
	 * @throws IOException On read errors.
	 * @throws JVMClassFormatError If the class is badly formatted.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/19
	 */
	public JVMClassFile parse(InputStream __is)
		throws IllegalStateException, IOException, JVMClassFormatError,
			NullPointerException
	{
		// Already done?
		synchronized (lock)
		{
			if (_did)
				throw new IllegalStateException("IN0w");
			_did = true;
		}
		
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Setup an input stream for parsing the data
		DataInputStream das = new DataInputStream(__is);
		
		// Check the magic number
		int clmagic;
		if (MAGIC_NUMBER != (clmagic = das.readInt()))
			throw new JVMClassFormatError(String.format("IN01 %08x %08x",
				clmagic, MAGIC_NUMBER));
		
		// Read the class version number, this modifies if certain
		// instructions are handled and how they are verified (StackMap vs
		// StackMapTable)
		JVMClassVersion version;
		_version = version = JVMClassVersion.findVersion(
			das.readUnsignedShort() | (das.readUnsignedShort() << 16));
		if (version.compareTo(JVMClassVersion.MAX_VERSION) > 0)
			throw new JVMClassVersionError(String.format("IN02 %s %s",
				version, JVMClassVersion.MAX_VERSION));
		
		// Initialize the constant pool
		JVMConstantPool constantpool;
		_constantpool = constantpool = new JVMConstantPool(this, das);
		
		// Read class access flags
		target.setFlags(new JVMClassFlags(das.readUnsignedShort()));
		
		// Set current class name
		target.setThisName(constantpool.<JVMConstantEntry.ClassName>getAs(
			das.readUnsignedShort(), JVMConstantEntry.ClassName.class).
			symbol());
		
		// Set super class name
		int sid = das.readUnsignedShort();
		if (sid != 0)
			target.setSuperName(constantpool.<JVMConstantEntry.ClassName>getAs(
				sid, JVMConstantEntry.ClassName.class).symbol());
		else
			target.setSuperName(null);
		
		// Read interface count
		int nints = das.readUnsignedShort();
		JVMClassInterfaces ints = target.getInterfaces();
		ClassNameSymbol ix;
		for (int i = 0; i < nints; i++)
			if (!ints.add((ix = constantpool.<JVMConstantEntry.ClassName>getAs(
				das.readUnsignedShort(), JVMConstantEntry.ClassName.class).
					symbol())))
				throw new JVMClassVersionError(String.format("IN11 %s %s",
					ints, ix));
		
		// Read fields
		int nf = das.readUnsignedShort();
		JVMFields flds = target.getFields();
		for (int i = 0; i < nf; i++)
			flds.put(__readField(das));
		
		// Read methods
		int nm = das.readUnsignedShort();
		JVMMethods mths = target.getMethods();
		for (int i = 0; i < nm; i++)
			mths.put(__readMethod(das));
		
		// No class attributes are used by the Java ME VM, thus they can
		// completely be ignored as if they did not exist.
		int numcan = das.readUnsignedShort();
		for (int i = 0; i < numcan; i++)
		{
			// Ignore the name index
			das.readUnsignedShort();
			
			// Skip the length
			__skipAttribute(das);
		}
		
		// If this is not EOF, then the class has extra junk following it
		if (das.read() >= 0)
			throw new JVMClassFormatError("IN08");
		
		// Self
		return this;
	}
	
	/**
	 * Reads the attribute length and then skips it
	 *
	 * @param __das Data source.
	 * @throws IOException On read errors.
	 * @throws JVMClassFormatError If EOF was reached.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/20
	 */
	private void __skipAttribute(DataInputStream __das)
		throws IOException, JVMClassFormatError, NullPointerException
	{
		// Check
		if (__das == null)
			throw new NullPointerException("NARG");
		
		// Ignore the length, but fail if EOF is reached
		int alen = __das.readUnsignedShort();
		for (int w = 0; w < alen; w++)
			if (__das.read() < 0)
				throw new EOFException("IN09");
	}
	
	/**
	 * Reads the name of the attribute.
	 *
	 * @param __das The data source.
	 * @return The current attribute to be read.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/20
	 */
	private String __readAttributeName(DataInputStream __das)
		throws IOException, NullPointerException
	{
		// Check
		if (__das == null)
			throw new NullPointerException("NARG");
		
		// Read it in
		return _constantpool.<JVMConstantEntry.UTF8>getAs(
			__das.readUnsignedShort(), JVMConstantEntry.UTF8.class).toString();
	}
	
	/**
	 * Reads a single field.
	 *
	 * @param __das Data source.
	 * @return The read field.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/20
	 */
	private JVMField __readField(DataInputStream __das)
		throws IOException, NullPointerException
	{
		// Check
		if (__das == null)
			throw new NullPointerException("NARG");
		
		// Read flags
		JVMFieldFlags flags = new JVMFieldFlags(__das.readUnsignedShort());
		
		// Read name and type key
		JVMMemberKey<FieldSymbol> key = this.<FieldSymbol>__readNAT(__das,
			FieldSymbol.class);
		
		// Setup
		JVMField rv = new JVMField(target, key);
		rv.setFlags(flags);
		
		// Read in attributes?
		int nas = __das.readUnsignedShort();
		for (int i = 0; i < nas; i++)
		{
			// Read attribute name
			String an = __readAttributeName(__das);
			
			// Depends on the name
			switch (an)
			{
					// Only care about constants
				case "ConstantValue":
					throw new Error("TODO");
				
					// Ignored
				default:
					__skipAttribute(__das);
					break;
			}
		}
		
		// Return it
		return rv;
	}
	
	/**
	 * Reads a single method.
	 *
	 * @param __das Data source.
	 * @return The read method.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/20
	 */
	private JVMMethod __readMethod(DataInputStream __das)
		throws IOException, NullPointerException
	{
		// Check
		if (__das == null)
			throw new NullPointerException("NARG");
		
		// Read flags
		JVMMethodFlags flags = new JVMMethodFlags(__das.readUnsignedShort());
		
		// Read name and type key
		JVMMemberKey<MethodSymbol> key = this.<MethodSymbol>__readNAT(__das,
			MethodSymbol.class);
		
		// Setup
		JVMMethod rv = new JVMMethod(target, key);
		rv.setFlags(flags);
		
		// Read in attributes?
		int nas = __das.readUnsignedShort();
		for (int i = 0; i < nas; i++)
		{
			// Read attribute name
			String an = __readAttributeName(__das);
			
			// Depends on the name
			switch (an)
			{
					// Only care about code
				case "Code":
					throw new Error("TODO");
					
					// Ignored
				default:
					__skipAttribute(__das);
					break;
			}
		}
		
		// Return it
		return rv;
	}
	
	/**
	 * Reads the name and type information which a field uses.
	 *
	 * @param <S> The type of symbol to read.
	 * @param __das The input data source.
	 * @param __cl The class that the type is.
	 * @return The read type.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/20
	 */
	private <S extends MemberTypeSymbol> JVMMemberKey<S> __readNAT(
		DataInputStream __d, Class<S> __cl)
		throws IOException, NullPointerException
	{
		// Check
		if (__d == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// Read name
		IdentifierSymbol name = new IdentifierSymbol(
			_constantpool.<JVMConstantEntry.UTF8>getAs(__d.readUnsignedShort(),
				JVMConstantEntry.UTF8.class).toString());
		
		// Read type
		S type = __cl.cast(MemberTypeSymbol.create(
			_constantpool.<JVMConstantEntry.UTF8>getAs(__d.readUnsignedShort(),
				JVMConstantEntry.UTF8.class).toString()));
		
		// Construct key
		return new JVMMemberKey<>(name, type);
	}
}

