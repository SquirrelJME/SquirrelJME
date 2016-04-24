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
import net.multiphasicapps.io.BufferAreaInputStream;
import net.multiphasicapps.narf.classinterface.NCIClassFlag;
import net.multiphasicapps.narf.classinterface.NCIClassFlags;

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
	volatile NCIClassFlags _flags;
	
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
			// {@squirreljme.error CF16 Classes may only be parsed once.}
			if (_did)
				throw new IllegalStateException("CF16");
			_did = true;
		}
		
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Setup an input stream for parsing the data
		DataInputStream das = new DataInputStream(__is);
		
		// Check the magic number
		// @{squirreljme.error CF17 The magic number of the class is not a
		// valid one for the standard class file format. Expects 0xCAFEBABE,
		// however the given magic number was specified.}
		int clmagic;
		if (MAGIC_NUMBER != (clmagic = das.readInt()))
			throw new CFFormatException(String.format("CF17 %08x", clmagic));
		
		// Read the class version number, this modifies if certain
		// instructions are handled and how they are verified (StackMap vs
		// StackMapTable)
		// @{squirreljme.error CF18 The input class file version is either too
		// new or too old. The specified class file version must be within the
		// specified range.}
		CFClassVersion version;
		_version = version = CFClassVersion.findVersion(
			das.readUnsignedShort() | (das.readUnsignedShort() << 16));
		if (version.compareTo(CFClassVersion.MAX_VERSION) > 0 ||
			version.compareTo(CFClassVersion.MIN_VERSION) < 0)
			throw new CFClassVersionError(String.format("CF18 %s != [%s, %s]",
				version, CFClassVersion.MIN_VERSION,
				CFClassVersion.MAX_VERSION));
		
		// Initialize the constant pool
		CFConstantPool constantpool;
		_constantpool = constantpool = new CFConstantPool(this, das);
		
		// Read class access flags
		_flags = __decodeClassFlags(das.readUnsignedShort());
		
		// Set current class name
		_thisname = constantpool.<CFClassName>getAs(
			das.readUnsignedShort(), CFClassName.class).
			symbol().asBinaryName();
		
		// Set super class name
		int sid = das.readUnsignedShort();
		if (sid != 0)
			_supername = constantpool.<CFClassName>getAs(
				sid, CFClassName.class).symbol().asBinaryName();
		else
			_supername = null;
		
		// Read interface count
		// @{squirreljme.error CF19 The class file to parse has a duplicated
		// interface in its implemented interfaces list, an interface may
		// only be specified once. (Existing interfaces, the duplicate
		// interface)}
		int nints = das.readUnsignedShort();
		Set<BinaryNameSymbol> ints = _interfaces;
		BinaryNameSymbol ix;
		for (int i = 0; i < nints; i++)
			if (!ints.add((ix = constantpool.<CFClassName>getAs(
				das.readUnsignedShort(), CFClassName.class).
					symbol().asBinaryName())))
				throw new CFFormatException(String.format("CF19 %s %s",
					ints, ix));
		
		// Read fields
		int nf = das.readUnsignedShort();
		Map<CFMemberKey<FieldSymbol>, CFField> flds = _fields;
		for (int i = 0; i < nf; i++)
		{
			CFField x = __readField(das);
			
			// @{squirreljme.error CF1a Class has a duplicate field.}
			if (null != flds.put(x.nameAndType(), x))
				throw new CFFormatException(String.format("CF1a %s", x));
		}
		
		// Read methods
		int nm = das.readUnsignedShort();
		Map<CFMemberKey<MethodSymbol>, CFMethod> mths = _methods;
		for (int i = 0; i < nm; i++)
		{
			CFMethod x =__readMethod(das);
			
			// @{squirreljme.error CF1b Class has a duplicate method.}
			if (null != mths.put(x.nameAndType(), x))
				throw new CFFormatException(String.format("CF1b %s", x));
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
		// @{squirreljme.error CF1c Extra bytes follow the end of the class
		// file data which is illegal.}
		if (das.read() >= 0)
			throw new CFFormatException("CF1c");
		
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
	 * Decodes the class flag set.
	 *
	 * @param __i The input flag field.
	 * @since 2016/04/23
	 */
	public NCIClassFlags __decodeClassFlags(int __i)
	{
		Set<NCIClassFlag> fl = new HashSet<>();
		
		// Public?
		if (0 != (__i & 0x0001))
			fl.add(NCIClassFlag.PUBLIC);
		
		// Final?
		if (0 != (__i & 0x0010))
			fl.add(NCIClassFlag.FINAL);
		
		// Super?
		if (0 != (__i & 0x0020))
			fl.add(NCIClassFlag.SUPER);
		
		// Interface?
		if (0 != (__i & 0x0200))
			fl.add(NCIClassFlag.INTERFACE);
		
		// Synthetic?
		if (0 != (__i & 0x1000))
			fl.add(NCIClassFlag.SYNTHETIC);
		
		// Annotation?
		if (0 != (__i & 0x20000))
			fl.add(NCIClassFlag.ANNOTATION);
		
		// Enumeration?
		if (0 != (__i & 0x4000))
			fl.add(NCIClassFlag.ENUM);
		
		// Build it
		return new NCIClassFlags(fl);
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
		
		// Read in attributes?
		int nas = __das.readUnsignedShort();
		Object constantvalue = null;
		for (int i = 0; i < nas; i++)
		{
			// Read attribute name
			String an = CFAttributeUtils.readName(_constantpool, __das);
			
			// Depends on the name
			switch (an)
			{
					// Only care about constants
				case "ConstantValue":	
					// {@squirreljme.error CF07 A field must not have more
					// than one constant value.}
					if (constantvalue != null)
						throw new CFFormatException("CF07");
					
					// Read
					try (DataInputStream cdis = new DataInputStream(
						new BufferAreaInputStream(__das,
							(((long)__das.readInt()) & 0xFFFFFFFFL))))
					{
						constantvalue = _constantpool.
							<CFConstantValue>getAs(
								cdis.readUnsignedShort(),
								CFConstantValue.class).
									getValue();
					}
					break;
				
					// Ignored
				default:
					__skipAttribute(__das);
					break;
			}
		}
		
		// Return it
		return new CFField(key, flags, constantvalue);
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
		
		// Read in attributes?
		int nas = __das.readUnsignedShort();
		byte[] codeattr = null;
		for (int i = 0; i < nas; i++)
		{
			// Read attribute name
			String an = CFAttributeUtils.readName(_constantpool, __das);
			
			// Depends on the name
			switch (an)
			{
					// Only care about code
				case "Code":
					// Read in byte array of the data.
					int cal = __das.readInt();
					
					// {@squirreljme.error CF08 The code attribute of a method
					// has negative length. (The length of the code attribute)}
					if (cal < 0)
						throw new CFFormatException(String.format("CF08 %d",
							cal));
					
					// {@squirreljme.error CF09 A method must only have no
					// code attribute or a single one defined.}
					if (codeattr != null)
						throw new CFFormatException("CF09");
					
					// Handle attribute
					codeattr = new byte[cal];
					try (InputStream is = new BufferAreaInputStream(__das,
						cal))
					{
						// Allocate attribute data
						int total = 0;
						while (total < cal)
						{
							// Read
							int rc = is.read(codeattr, total, cal - total);
							
							// {@squirreljme.error CF0a Reached end of file
							// while reading the code attribute.}
							if (rc < 0)
								throw new CFFormatException("CF0a");
							
							// Add it
							total += rc;
						}
					}
					break;
					
					// Ignored
				default:
					__skipAttribute(__das);
					break;
			}
		}
		
		// Build it
		return new CFMethod(key, flags, codeattr, _version);
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
			_constantpool.<CFUTF8>getAs(__d.readUnsignedShort(),
				CFUTF8.class).toString());
		
		// Read type
		S type = __cl.cast(MemberTypeSymbol.create(
			_constantpool.<CFUTF8>getAs(__d.readUnsignedShort(),
				CFUTF8.class).toString()));
		
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
		// @{squirreljme.error CF0b End of file reached while skipping an
		// attribute.}
		int alen = __das.readInt();
		for (int w = 0; w != alen; w++)
			if (__das.read() < 0)
				throw new CFFormatException("CF0b");
	}
}

