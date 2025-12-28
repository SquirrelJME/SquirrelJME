// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.uri;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.NotNull;

/**
 * Scheme specific URI part, this does not have any restrictions to the URI
 * formatting similar to {@link UriGenericPart}.
 *
 * @since 2025/12/28
 */
@SquirrelJMEVendorApi
public final class UriSchemeSpecificPart
	extends UriPart<UriSchemeSpecificPart>
{
	@Override
	public int compareTo(@NotNull UriSchemeSpecificPart __b)
	{
		throw Debugging.todo();
	}
}
