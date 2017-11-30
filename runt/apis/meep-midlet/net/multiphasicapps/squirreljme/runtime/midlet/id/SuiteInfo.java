// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.midlet.id;

import net.multiphasicapps.squirreljme.runtime.midlet.InvalidSuiteException;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.tool.manifest.JavaManifestKey;

/**
 * This contains all of the information which is provided by a suite.
 *
 * @since 2017/11/30
 */
public final class SuiteInfo
{
	/**
	 * Parses the specified manifest and decodes all of the information
	 * which describes the suite.
	 *
	 * @param __man The manifest to decode.
	 * @return The parsed suite information.
	 */
	public static final SuiteInfo parseManifest(JavaManifest __man)
		throws InvalidSuiteException, NullPointerException
	{
		if (__man == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

