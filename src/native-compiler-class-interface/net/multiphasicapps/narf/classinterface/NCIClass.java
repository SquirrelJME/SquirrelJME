// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classinterface;

import java.util.Map;
import java.util.Set;
import net.multiphasicapps.descriptors.ClassNameSymbol;

/**
 * This represents information about a class such as its access flags and any
 * fields or methods it contains.
 *
 * @since 2016/04/21
 */
public interface NCIClass
{
	/**
	 * Returns the mapping of fields that this class contains.
	 *
	 * @return The fields this class contains.
	 * @since 2016/04/22
	 */
	public abstract Map<NCIFieldID, NCIField> fields();
	
	/**
	 * Returns the flags which are associated with this class.
	 *
	 * @return The associated class flags.
	 * @since 2016/04/23
	 */
	public abstract NCIClassFlags flags();
	
	/**
	 * Returns the name of the implemented interfaces.
	 *
	 * @return The set of implemented interfaces.
	 * @since 2016/04/22
	 */
	public abstract Set<ClassNameSymbol> interfaceNames();
	
	/**
	 * Returns the mappin of methods that this class contains.
	 *
	 * @return The methods this class contains.
	 * @since 2016/04/22
	 */
	public abstract Map<NCIMethodID, NCIMethod> methods();
	
	/**
	 * Returns the name of the super class.
	 *
	 * @return The class super name.
	 * @since 2016/04/22
	 */
	public abstract ClassNameSymbol superName();
	
	/**
	 * Returns the name of this class.
	 *
	 * @return The name of this class.
	 * @since 2016/04/22
	 */
	public abstract ClassNameSymbol thisName();
}

