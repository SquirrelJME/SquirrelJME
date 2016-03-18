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

/**
 * This represents a single class loaded by the interpreter which is derived
 * from a loaded class file.
 *
 * @since 2016/03/01
 */
public class JVMClassFile
	extends JVMClass
{
	/** The class file magic number. */
	public static final int MAGIC_NUMBER =
		0xCAFEBABE;
	
	/** The version of this class file. */
	protected final JVMClassVersion version;
	
	/** The class constant pool. */
	protected final JVMConstantPool constantpool;
	
	/** Class access flags. */
	protected final Set<JVMClassFlag> flags;
	
	/** The current class name. */
	protected final ClassNameSymbol thisname;
	
	/** The super class name. */
	protected final ClassNameSymbol supername;
	
	/** Interfaces. */
	protected final Set<ClassNameSymbol> interfacenames;
	
	/**
	 * Initializes the class data.
	 *
	 * @param __owner The owning interpreter engine.
	 * @param __cdata Class data for parsing.
	 * @throws JVMClassFormatError If the input class data is not
	 * correct.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	JVMClassFile(JVMEngine __owner, InputStream __cdata)
		throws JVMClassFormatError, IOException, NullPointerException
	{
		super(__owner);
		
		// Check
		if (__cdata == null)
			throw new NullPointerException();
		
		// Read the input class data
		DataInputStream das = new DataInputStream(__cdata);
		
		// Check the magic number
		int clmagic;
		if (MAGIC_NUMBER != (clmagic = das.readInt()))
			throw new JVMClassFormatError(String.format("IN01 %08x %08x",
				clmagic, MAGIC_NUMBER));
		
		// Read the class version number, this modifies if certain
		// instructions are handled and how they are verified (StackMap vs
		// StackMapTable)
		version = JVMClassVersion.findVersion(
			das.readUnsignedShort() | (das.readUnsignedShort() << 16));
		if (version.compareTo(JVMClassVersion.MAX_VERSION) > 0)
			throw new JVMClassVersionError(String.format("IN02 %s %s",
				 version, JVMClassVersion.MAX_VERSION));
		
		// Initialize the constant pool
		constantpool = new JVMConstantPool(this, das);
		
		// Read flags
		flags = JVMClassFile.<JVMClassFlag>__parseFlags(JVMClassFlag.FLAGS,
			das.readUnsignedShort());
		
		// Interface?
		if (isInterface())
		{
			// Must be abstract
			if (!isAbstract())
				throw new JVMClassFormatError("IN03");
			
			// cannot have some flags set
			if (isFinal() || isSpecialInvokeSpecial() || isEnum())
				throw new JVMClassFormatError("IN04");
		}
		
		// Normal class
		else
		{
			// Cannot be an annotation
			if (isAnnotation())
				throw new JVMClassFormatError("IN05");
				
			// Cannot be abstract and final
			if (isAbstract() && isFinal())
				throw new JVMClassFormatError("IN06");
		}
		
		// Read the class name data, fields, and methods
		try
		{
			// Get current class name
			thisname = constantpool.<JVMConstantEntry.ClassName>getAs(
				das.readUnsignedShort(), JVMConstantEntry.ClassName.class).
				symbol();
			
			// Super class name might be null
			int sid = das.readUnsignedShort();
			boolean isobj = "java/lang/Object".equals(thisname.toString());
			
			// Object never has a super class, however all other objects do
			if ((sid != 0 && isobj) || (sid == 0 && !isobj))
				throw new JVMClassFormatError(String.format("IN07 %s",
					thisname));
			
			// Lacks one
			else if (sid == 0)
				supername = null;
			
			// Has one
			else
				supername = constantpool.<JVMConstantEntry.ClassName>getAs(
					sid, JVMConstantEntry.ClassName.class).symbol();
			
			// Interfaces
			int numi = das.readUnsignedShort();
			Set<ClassNameSymbol> it = new LinkedHashSet<>();
			for (int i = 0; i < numi; i++)
				it.add(constantpool.<JVMConstantEntry.ClassName>getAs(
					das.readUnsignedShort(), JVMConstantEntry.ClassName.class).
					symbol());
			interfacenames = MissingCollections.<ClassNameSymbol>
				unmodifiableSet(it);
			
			// Target sets for members
			
			// Read fields and methods
			for (int ism = 0; ism <= 1; ism++)
			{
				// Is field? Is method?
				boolean isfield = (ism == 0);
				boolean ismethod = !isfield;
				
				// Read in members
				int mn = das.readUnsignedShort();
				for (int i = 0; i < mn; i++)
					throw new Error("TODO");
			}
			
			// Completeley ignore attributes, they are pointless here
			int numcan = das.readUnsignedShort();
			for (int i = 0; i < numcan; i++)
			{
				// Ignore the name index
				das.readUnsignedShort();
				
				// Ignore the length also, but fail if EOF is reached
				int alen = das.readUnsignedShort();
				for (int w = 0; w < alen; w++)
					if (das.read() < 0)
						throw new EOFException("IN09");
			}
		}
		
		// Bad index somewhere
		catch (IndexOutOfBoundsException|NullPointerException e)
		{
			throw new JVMClassFormatError(e);
		}
		
		// If this is not EOF, then the class has extra junk following it
		if (das.read() >= 0)
			throw new JVMClassFormatError("IN08");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/16
	 */
	@Override
	public JVMConstantPool constantPool()
	{
		return constantpool;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/16
	 */
	@Override
	public Set<JVMClassFlag> flags()
	{
		return flags;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/16
	 */
	@Override
	public Set<ClassNameSymbol> interfaceNames()
	{
		return interfacenames;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/16
	 */
	@Override
	public ClassNameSymbol superName()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/16
	 */
	@Override
	public ClassNameSymbol thisName()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/13
	 */
	@Override
	public JVMClassVersion version()
	{
		return version;
	}
	
	/**
	 * Parses input flags and returns an unmodifiable set view of them.
	 *
	 * @param <B> The flag type.
	 * @param __ll The input flags which are available.
	 * @param __in The input flag value.
	 * @return The set of flags, it is not modifiable.
	 * @throws JVMClassFormatError If illegal flags are specified.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/15
	 */
	static <B extends JVMBitFlag> Set<B> __parseFlags(List<B> __ll, int __in)
		throws JVMClassFormatError, NullPointerException
	{
		// Check
		if (__ll == null)
			throw new NullPointerException();
		
		// Target set
		Set<B> rv = new HashSet<>();
		
		// Parse flags
		int rem = __in;
		for (B b : __ll)
		{
			// Get the mask
			int mm = b.mask();
			
			// If it is set, clear it and add it
			if (0 != (rem & mm))
			{
				// Set
				rv.add(b);
				
				// Clear
				rem &= ~mm;
			}
		}
		
		// If non-zero then extra illegal flags remain
		if (rem != 0)
			throw new JVMClassFormatError(String.format("IN0a %x %x", __in,
				rem));
		
		// Lock it in
		return MissingCollections.<B>unmodifiableSet(rv);
	}
}

