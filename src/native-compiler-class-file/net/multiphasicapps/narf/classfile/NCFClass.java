// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classfile;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Map;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.IllegalSymbolException;
import net.multiphasicapps.narf.classinterface.NCIClass;
import net.multiphasicapps.narf.classinterface.NCIClassFlag;
import net.multiphasicapps.narf.classinterface.NCIClassFlags;
import net.multiphasicapps.narf.classinterface.NCIClassReference;
import net.multiphasicapps.narf.classinterface.NCIPool;
import net.multiphasicapps.narf.classinterface.NCIException;
import net.multiphasicapps.narf.classinterface.NCIField;
import net.multiphasicapps.narf.classinterface.NCIFieldID;
import net.multiphasicapps.narf.classinterface.NCIMethod;
import net.multiphasicapps.narf.classinterface.NCIMethodID;
import net.multiphasicapps.narf.classinterface.NCIVersion;

/**
 * This loads and handles the standard Java class file format.
 *
 * @since 2016/04/24
 */
public class NCFClass
	implements NCIClass
{
	/** The class file magic number. */
	protected static final int MAGIC_NUMBER =
		0xCAFE_BABE;
	
	/** The class version number. */
	protected final NCIVersion version;
	
	/** The constant pool of the class. */
	protected final NCIPool constantpool;
	
	/** The class flags. */
	protected final NCIClassFlags flags;
	
	/** The name of this class. */
	protected final ClassNameSymbol thisname;
	
	/** The name of the super class. */
	protected final ClassNameSymbol supername;
	
	/** The names of implemented interfaces. */
	protected final Set<ClassNameSymbol> interfacenames;
	
	/**
	 * Initializes the class.
	 *
	 * @param __in The class data source.
	 * @throws IOException On read errors.
	 * @throws NCIException On class format errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/24
	 */
	public NCFClass(InputStream __in)
		throws IOException, NCIException, NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Wrap
		DataInputStream das = new DataInputStream(__in);
		
		// Check the magic number
		// @{squirreljme.error CF17 The magic number of the class is not a
		// valid one for the standard class file format. Expects 0xCAFEBABE,
		// however the given magic number was specified.}
		int clmagic;
		if (MAGIC_NUMBER != (clmagic = das.readInt()))
			throw new NCIException(NCIException.Issue.BAD_MAGIC_NUMBER,
				String.format("CF17 %08x", clmagic));
		
		// Read the class version number, this modifies if certain
		// instructions are handled and how they are verified (StackMap vs
		// StackMapTable)
		// @{squirreljme.error CF18 The input class file version is either too
		// new or too old. The specified class file version must be within the
		// specified range.}
		version = NCIVersion.findVersion(
			das.readUnsignedShort() | (das.readUnsignedShort() << 16));
		if (version.compareTo(NCIVersion.MAX_VERSION) > 0 ||
			version.compareTo(NCIVersion.MIN_VERSION) < 0)
			throw new NCIException(NCIException.Issue.BAD_CLASS_VERSION,
				String.format("CF18 %s != [%s, %s]", version,
				NCIVersion.MIN_VERSION, NCIVersion.MAX_VERSION));
		
		// Decode the constant pool
		constantpool = new __PoolDecoder__(this, das).get();
		
		// Parse the class flags
		flags = __FlagDecoder__.__class(das.readUnsignedShort());
		
		try
		{
			// Read class name
			thisname = constantpool.<NCIClassReference>requiredAs(
				das.readUnsignedShort(), NCIClassReference.class).get().
				asBinaryName().asClassName();
		
			// Read super name
			NCIClassReference scr = constantpool.<NCIClassReference>nullableAs(
				das.readUnsignedShort(), NCIClassReference.class);
			if (scr != null)
				supername = scr.get().asBinaryName().asClassName();
			else
				supername = null;
			
			// Read implemented interfaces
			int ni = das.readUnsignedShort();
			Set<ClassNameSymbol> in = new LinkedHashSet<>();
			for (int i = 0; i < ni; i++)
				in.add(constantpool.<NCIClassReference>requiredAs(
					das.readUnsignedShort(), NCIClassReference.class).get().
					asBinaryName().asClassName());
			
			// Set
			interfacenames = MissingCollections.
				<ClassNameSymbol>unmodifiableSet(in);
		}
		
		// A given class name was likely an array
		catch (IllegalSymbolException e)
		{
			// {@squirreljme.error CF1j The name of the current, super, or
			// an implemented interface is not a valid binary name.}
			throw new NCIException(NCIException.Issue.BAD_CLASS_NAME, "CF1j",
				e);
		}
		
		if (true)
			throw new Error("TODO");
		
		// @{squirreljme.error CF1c Extra bytes follow the end of the class
		// file data which is illegal.}
		if (das.read() >= 0)
			throw new NCIException(NCIException.Issue.NOT_EOC, "CF1c");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public NCIPool constantPool()
	{
		return constantpool;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public Map<NCIFieldID, NCIField> fields()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public NCIClassFlags flags()
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
	public Map<NCIMethodID, NCIMethod> methods()
	{
		throw new Error("TODO");
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
	public NCIVersion version()
	{
		return version;
	}
}

