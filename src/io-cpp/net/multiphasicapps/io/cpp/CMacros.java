// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.cpp;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.checked.CheckedMap;

/**
 * This contains a mutable set of macros which is used by the C preprocessor
 * so that it may replace macros as required by the language.
 *
 * This class is not thread safe.
 *
 * @since 2016/05/08
 */
public class CMacros
	extends AbstractMap<String, CMacro>
{
	/** The set of macros. */
	protected final Map<String, CMacro> map =
		CheckedMap.<String, CMacro>covariant(new HashMap<String, CMacro>(),
		String.class, CMacro.class);
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/08
	 */
	@Override
	public Set<Map.Entry<String, CMacro>> entrySet()
	{
		return map.entrySet();
	}
}

