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

import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.narf.classinterface.NCIClass;

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
	
	/**
	 * Initializes the class.
	 *
	 * @param __in The class data source.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/24
	 */
	public NCFClass(InputStream __in)
		throws IOException, NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Wrap
		DataInputStream dis = new DataInputStream(__in);
		
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
		version = CFClassVersion.findVersion(
			das.readUnsignedShort() | (das.readUnsignedShort() << 16));
		if (version.compareTo(CFClassVersion.MAX_VERSION) > 0 ||
			version.compareTo(CFClassVersion.MIN_VERSION) < 0)
			throw new CFClassVersionError(String.format("CF18 %s != [%s, %s]",
				version, CFClassVersion.MIN_VERSION,
				CFClassVersion.MAX_VERSION));
		
		throw new Error("TODO");
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

