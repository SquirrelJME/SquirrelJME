// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.archive;

import java.util.Map;
import java.util.Set;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.narf.classinterface.NCIClass;
import net.multiphasicapps.narf.classinterface.NCIClassFlags;
import net.multiphasicapps.narf.classinterface.NCIField;
import net.multiphasicapps.narf.classinterface.NCIFieldID;
import net.multiphasicapps.narf.classinterface.NCIMethod;
import net.multiphasicapps.narf.classinterface.NCIMethodID;
import net.multiphasicapps.narf.classinterface.NCIPool;
import net.multiphasicapps.narf.classinterface.NCIVersion;

/**
 * This is a class which implements nothing and is used as a flag to indicate
 * that a given class was not found.
 *
 * @since 2016/05/18
 */
final class __MissingClass__
	implements NCIClass
{
	/**
	 * Initializes the missing class.
	 *
	 * @since 2016/05/18
	 */
	__MissingClass__()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/28
	 */
	@Override
	public NCIPool constantPool()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/28
	 */
	@Override
	public Map<NCIFieldID, NCIField> fields()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/28
	 */
	@Override
	public NCIClassFlags flags()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/28
	 */
	@Override
	public Set<ClassNameSymbol> interfaceNames()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/28
	 */
	@Override
	public Map<NCIMethodID, NCIMethod> methods()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/28
	 */
	@Override
	public NCIClass outerClass()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/28
	 */
	@Override
	public ClassNameSymbol superName()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/28
	 */
	@Override
	public ClassNameSymbol thisName()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/28
	 */
	@Override
	public NCIVersion version()
	{
		throw new RuntimeException("WTFX");
	}
}

