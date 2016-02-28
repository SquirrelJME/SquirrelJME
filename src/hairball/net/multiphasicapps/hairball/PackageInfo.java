// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.hairball;

import java.nio.file.Path;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * This contains package information.
 *
 * @since 2016/02/28
 */
public class PackageInfo
{
	/** Is a package manifest? */
	public static final Attributes.Name HAIRBALL_IS_PACKAGE =
		new Attributes.Name("Hairball-Is-Package");
	
	/** Dependencies? */
	public static final Attributes.Name HAIRBALL_DEPENDS =
		new Attributes.Name("Hairball-Depends");
	
	/** The package root. */
	protected final Path root;	
	
	/**
	 * The path to the package root.
	 *
	 * @param __p Package root.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/02/28
	 */
	public PackageInfo(Path __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException();
		
		// Set
		root = __p;
	}
	
	/**
	 * Is this a valid package?
	 *
	 * @return {@code true} if it is.
	 * @since 2016/02/28
	 */
	public boolean isValid()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/02/28
	 */
	@Override
	public String toString()
	{
		return root.getFileName().toString();
	}
}

