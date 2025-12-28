// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.Language;

/**
 * Content type utilities.
 *
 * @since 2025/12/27
 */
@SquirrelJMEVendorApi
public final class ContentTypeUtil
{
	/**
	 * Not used.
	 *
	 * @since 2025/12/27
	 */
	private ContentTypeUtil()
	{
	}
	
	/**
	 * Guesses the content type based on the file path.
	 *
	 * @param __path The path to guess the content type for.
	 * @return The content type.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	@Language("mime-type-reference")
	public static String guessByPath(String __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
}
