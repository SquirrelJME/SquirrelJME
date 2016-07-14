// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.ci.std;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IllegalSymbolException;
import net.multiphasicapps.squirreljme.java.ci.CIClass;
import net.multiphasicapps.squirreljme.java.ci.CIClassFlag;
import net.multiphasicapps.squirreljme.java.ci.CIClassFlags;
import net.multiphasicapps.squirreljme.java.ci.CIClassReference;
import net.multiphasicapps.squirreljme.java.ci.CIException;
import net.multiphasicapps.squirreljme.java.ci.CIField;
import net.multiphasicapps.squirreljme.java.ci.CIFieldID;
import net.multiphasicapps.squirreljme.java.ci.CIMethod;
import net.multiphasicapps.squirreljme.java.ci.CIMethodID;
import net.multiphasicapps.squirreljme.java.ci.CIPool;
import net.multiphasicapps.squirreljme.java.ci.CIVersion;
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;

/**
 * This loads and handles the standard Java class file format.
 *
 * @since 2016/04/24
 */
public final class CISClass
	implements CIClass
{
	/** The class file magic number. */
	protected static final int MAGIC_NUMBER =
		0xCAFE_BABE;
	
	/** The class version number. */
	protected final CIVersion version;
	
	/** The constant pool of the class. */
	protected final CIPool constantpool;
	
	/** The class flags. */
	protected final CIClassFlags flags;
	
	/** The name of this class. */
	protected final ClassNameSymbol thisname;
	
	/** The name of the super class. */
	protected final ClassNameSymbol supername;
	
	/** The names of implemented interfaces. */
	protected final Set<ClassNameSymbol> interfacenames;
	
	/** Class fields. */
	protected final Map<CIFieldID, CIField> fields;
	
	/** Class methods. */
	protected final Map<CIMethodID, CIMethod> methods;
	
	/**
	 * Initializes the class.
	 *
	 * @param __in The class data source.
	 * @throws IOException On read errors.
	 * @throws CIException On class format errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/24
	 */
	public CISClass(InputStream __in)
		throws IOException, CIException, NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Wrap
		DataInputStream das = new DataInputStream(__in);
		
		// Check the magic number
		// @{squirreljme.error AQ17 The magic number of the class is not a
		// valid one for the standard class file format. Expects 0xCAFEBABE,
		// however the given magic number was specified.}
		int clmagic;
		if (MAGIC_NUMBER != (clmagic = das.readInt()))
			throw new CIException(String.format("AQ17 %08x", clmagic));
		
		// Read the class version number, this modifies if certain
		// instructions are handled and how they are verified (StackMap vs
		// StackMapTable)
		// @{squirreljme.error AQ18 The input class file version is either too
		// new or too old. The specified class file version must be within the
		// specified range.}
		version = CIVersion.findVersion(
			das.readUnsignedShort() | (das.readUnsignedShort() << 16));
		if (version.compareTo(CIVersion.MAX_VERSION) > 0 ||
			version.compareTo(CIVersion.MIN_VERSION) < 0)
			throw new CIException(String.format("AQ18 %s != [%s, %s]", version,
				CIVersion.MIN_VERSION, CIVersion.MAX_VERSION));
		
		// Decode the constant pool
		constantpool = new __PoolDecoder__(this, das).get();
		
		// Parse the class flags
		flags = __FlagDecoder__.__class(das.readUnsignedShort());
		
		try
		{
			// Read class name
			thisname = constantpool.<CIClassReference>requiredAs(
				das.readUnsignedShort(), CIClassReference.class).get().
				asBinaryName().asClassName();
		
			// Read super name
			CIClassReference scr = constantpool.<CIClassReference>nullableAs(
				das.readUnsignedShort(), CIClassReference.class);
			if (scr != null)
				supername = scr.get().asBinaryName().asClassName();
			else
				supername = null;
			
			// Read implemented interfaces
			int ni = das.readUnsignedShort();
			Set<ClassNameSymbol> in = new LinkedHashSet<>();
			for (int i = 0; i < ni; i++)
				in.add(constantpool.<CIClassReference>requiredAs(
					das.readUnsignedShort(), CIClassReference.class).get().
					asBinaryName().asClassName());
			
			// Set
			interfacenames = UnmodifiableSet.<ClassNameSymbol>of(in);
		}
		
		// A given class name was likely an array
		catch (IllegalSymbolException e)
		{
			// {@squirreljme.error AQ1j The name of the current, super, or
			// an CIClassimplemented interface is not a valid binary name.}
			throw new CIException("AQ1j", e);
		}
		
		// Read in fields
		int nf = das.readUnsignedShort();
		Map<CIFieldID, CIField> of = new HashMap<>();
		for (int i = 0; i < nf; i++)
			__ReadMember__.__field(this, das, of);
		fields = UnmodifiableMap.<CIFieldID, CIField>of(of);
		
		
		// Read in methods
		int nm = das.readUnsignedShort();
		Map<CIMethodID, CIMethod> om = new HashMap<>();
		for (int i = 0; i < nm; i++)
			__ReadMember__.__method(this, das, om);
		methods = UnmodifiableMap.<CIMethodID, CIMethod>of(om);
		
		// Skip attributes
		int na = das.readUnsignedShort();
		for (int i = 0; i < na; i++)
		{
			// Skip name
			das.readUnsignedShort();
			
			// {@squirreljme.error AQ1w An attribute in the class has a
			// negative size.}
			int len = das.readInt();
			if (len < 0)
				throw new CIException("AQ1w");
			
			// Skip the length
			__ReadMember__.__skipBytes(das, len);
		}
		
		// {@squirreljme.error AQ1o Extra bytes follow the end of the class
		// file data which is illegal.}
		if (das.read() >= 0)
			throw new CIException("AQ1o");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public CIPool constantPool()
	{
		return constantpool;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public Map<CIFieldID, CIField> fields()
	{
		return fields;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public CIClassFlags flags()
	{
		return flags;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public Set<ClassNameSymbol> interfaceNames()
	{
		return interfacenames;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public Map<CIMethodID, CIMethod> methods()
	{
		return methods;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public CIClass outerClass()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public ClassNameSymbol superName()
	{
		return supername;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public ClassNameSymbol thisName()
	{
		return thisname;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public CIVersion version()
	{
		return version;
	}
}

