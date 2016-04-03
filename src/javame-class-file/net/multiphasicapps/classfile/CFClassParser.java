// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
public class CFClassParser
{
	/** The class file magic number. */
	public static final int MAGIC_NUMBER =
		0xCAFEBABE;
	
	/** Lock to prevent doing double the work. */
	protected final Object lock =
		new Object();
	
	/** Fields which were parsed. */
	final Map<CFMemberKey<FieldSymbol>, CFField> _fields =
		new LinkedHashMap<>();
	
	/** Methods which were parsed. */
	final Map<CFMemberKey<MethodSymbol>, CFMethod> _methods =
		new LinkedHashMap<>();
	
	/** Class interfaces. */
	final Set<BinaryNameSymbol> _interfaces =
		new LinkedHashSet<>();
	
	/** The version of this class file. */
	volatile CFClassVersion _version;
	
	/** The class constant pool. */
	volatile CFConstantPool _constantpool;
	
	/** Class flags. */
	volatile CFClassFlags _flags;
	
	/** The current class name. */
	volatile BinaryNameSymbol _thisname;
	
	/** The super class name. */
	volatile BinaryNameSymbol _supername;
	
	/** Did this already? */
	volatile boolean _did;
	
	/**
	 * Initializes the class file parser.
	 *
	 * @since 2016/03/19
	 */
	public CFClassParser()
		throws NullPointerException
	{
	}
	
	/**
	 * Parses the class file and loads the information into the target class.
	 *
	 * @param __is The input stream to read class data from.
	 * @return A class which contains the given parsed data.
	 * @throws IOException On read errors.
	 * @throws CFFormatException If the class is badly formatted.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/19
	 */
	public CFClass parse(InputStream __is)
		throws IllegalStateException, IOException, CFFormatException,
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
		// @{squirreljme.error IN01 The magic number of the class is not a
		// valid one for the standard class file format. Expects 0xCAFEBABE,
		// however the given magic number was specified.}
		int clmagic;
		if (MAGIC_NUMBER != (clmagic = das.readInt()))
			throw new CFFormatException(String.format("IN01 %08x", clmagic));
		
		// Read the class version number, this modifies if certain
		// instructions are handled and how they are verified (StackMap vs
		// StackMapTable)
		// @{squirreljme.error IN02 The input class file version is either too
		// new or too old. The specified class file version must be within the
		// specified range.}
		CFClassVersion version;
		_version = version = CFClassVersion.findVersion(
			das.readUnsignedShort() | (das.readUnsignedShort() << 16));
		if (version.compareTo(CFClassVersion.MAX_VERSION) > 0 ||
			version.compareTo(CFClassVersion.MIN_VERSION) < 0)
			throw new CFClassVersionError(String.format("IN02 %s != [%s, %s]",
				version, CFClassVersion.MIN_VERSION,
				CFClassVersion.MAX_VERSION));
		
		// Initialize the constant pool
		CFConstantPool constantpool;
		_constantpool = constantpool = new CFConstantPool(this, das);
		
		// Read class access flags
		_flags = new CFClassFlags(das.readUnsignedShort());
		
		// Set current class name
		_thisname = constantpool.<CFConstantEntry.ClassName>getAs(
			das.readUnsignedShort(), CFConstantEntry.ClassName.class).
			symbol().asBinaryName();
		
		// Set super class name
		int sid = das.readUnsignedShort();
		if (sid != 0)
			_supername = constantpool.<CFConstantEntry.ClassName>getAs(
				sid, CFConstantEntry.ClassName.class).symbol().asBinaryName();
		else
			_supername = null;
		
		// Read interface count
		// @{squirreljme.error IN11 The class file to parse has a duplicated
		// interface in its implemented interfaces list, an interface may
		// only be specified once. (Existing interfaces, the duplicate
		// interface)}
		int nints = das.readUnsignedShort();
		Set<BinaryNameSymbol> ints = _interfaces;
		BinaryNameSymbol ix;
		for (int i = 0; i < nints; i++)
			if (!ints.add((ix = constantpool.<CFConstantEntry.ClassName>getAs(
				das.readUnsignedShort(), CFConstantEntry.ClassName.class).
					symbol().asBinaryName())))
				throw new CFFormatException(String.format("IN11 %s %s",
					ints, ix));
		
		// Read fields
		int nf = das.readUnsignedShort();
		Map<CFMemberKey<FieldSymbol>, CFField> flds = _fields;
		for (int i = 0; i < nf; i++)
		{
			CFField x = __readField(das);
			
			// @{squirreljme.error IN2r Class has a duplicate field.}
			if (null != flds.put(x.nameAndType(), x))
				throw new CFFormatException(String.format("IN2r %s", x));
		}
		
		// Read methods
		int nm = das.readUnsignedShort();
		Map<CFMemberKey<MethodSymbol>, CFMethod> mths = _methods;
		for (int i = 0; i < nm; i++)
		{
			CFMethod x =__readMethod(das);
			
			// @{squirreljme.error IN2s Class has a duplicate method.}
			if (null != mths.put(x.nameAndType(), x))
				throw new CFFormatException(String.format("IN2s %s", x));
		}
		
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
		// @{squirreljme.error IN08 Extra bytes follow the end of the class
		// file data which is illegal.}
		if (das.read() >= 0)
			throw new CFFormatException("IN08");
		
		// Build the final class
		return new CFClass(this);
	}
	
	/**
	 * Returns the version of the class file.
	 *
	 * @return The class file version.
	 * @since 2016/03/25
	 */
	public CFClassVersion version()
	{
		return _version;
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
	String __readAttributeName(DataInputStream __das)
		throws IOException, NullPointerException
	{
		// Check
		if (__das == null)
			throw new NullPointerException("NARG");
		
		// Read it in
		return _constantpool.<CFConstantEntry.UTF8>getAs(
			__das.readUnsignedShort(), CFConstantEntry.UTF8.class).toString();
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
	private CFField __readField(DataInputStream __das)
		throws IOException, NullPointerException
	{
		// Check
		if (__das == null)
			throw new NullPointerException("NARG");
		
		// Read flags
		CFFieldFlags flags = new CFFieldFlags(__das.readUnsignedShort());
		
		// Read name and type key
		CFMemberKey<FieldSymbol> key = this.<FieldSymbol>__readNAT(__das,
			FieldSymbol.class);
		
		// Setup
		CFField rv = new CFField(target, key);
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
					try (DataInputStream cdis = new DataInputStream(
						new BufferAreaInputStream(__das,
							(((long)__das.readInt()) & 0xFFFFFFFFL))))
					{
						rv.setConstantValue(_constantpool.
							<CFConstantEntry.ConstantValue>getAs(
								cdis.readUnsignedShort(),
								CFConstantEntry.ConstantValue.class).
									getValue());
					}
					break;
				
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
	private CFMethod __readMethod(DataInputStream __das)
		throws IOException, NullPointerException
	{
		// Check
		if (__das == null)
			throw new NullPointerException("NARG");
		
		// Read flags
		CFMethodFlags flags = new CFMethodFlags(__das.readUnsignedShort());
		
		// Read name and type key
		CFMemberKey<MethodSymbol> key = this.<MethodSymbol>__readNAT(__das,
			MethodSymbol.class);
		
		// Setup
		CFMethod rv = new CFMethod(target, key);
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
					try (DataInputStream cdis = new DataInputStream(
						new BufferAreaInputStream(__das,
							(((long)__das.readInt()) & 0xFFFFFFFFL))))
					{
						// Setup code parse
						JVMCodeParser parser = new JVMCodeParser(this, rv,
							_constantpool);
						
						// Parse it
						parser.parse(cdis);
					}
					break;
					
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
	private <S extends MemberTypeSymbol> CFMemberKey<S> __readNAT(
		DataInputStream __d, Class<S> __cl)
		throws IOException, NullPointerException
	{
		// Check
		if (__d == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// Read name
		IdentifierSymbol name = new IdentifierSymbol(
			_constantpool.<CFConstantEntry.UTF8>getAs(__d.readUnsignedShort(),
				CFConstantEntry.UTF8.class).toString());
		
		// Read type
		S type = __cl.cast(MemberTypeSymbol.create(
			_constantpool.<CFConstantEntry.UTF8>getAs(__d.readUnsignedShort(),
				CFConstantEntry.UTF8.class).toString()));
		
		// Construct key
		return new CFMemberKey<>(name, type);
	}
	
	/**
	 * Reads the attribute length and then skips it
	 *
	 * @param __das Data source.
	 * @throws IOException On read errors.
	 * @throws CFFormatException If EOF was reached.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/20
	 */
	void __skipAttribute(DataInputStream __das)
		throws IOException, CFFormatException, NullPointerException
	{
		// Check
		if (__das == null)
			throw new NullPointerException("NARG");
		
		// Ignore the length, but fail if EOF is reached
		// Using != instead of < makes it so that attributes that are larger
		// than 2GiB can be properly skipped (although unlikely)
		// @{squirreljme.error IN09 End of file reached while skipping an
		// attribute.}
		int alen = __das.readInt();
		for (int w = 0; w != alen; w++)
			if (__das.read() < 0)
				throw new CFFormatException("IN09");
	}
}

