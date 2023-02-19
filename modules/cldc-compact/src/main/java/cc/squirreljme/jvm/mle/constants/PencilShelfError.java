// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.jvm.mle.PencilShelf;
import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Errors for {@link PencilShelf}.
 *
 * @since 2023/02/19
 */
@SquirrelJMEVendorApi
public interface PencilShelfError
{
	/**
	 * {@link IllegalArgumentException} for {@link
	 * PencilShelf#hardwareCopyArea(PencilBracket, int, int, int, int, int,
	 * int, int)}.
	 */
	@SquirrelJMEVendorApi
	byte COPY_AREA_ILLEGAL_ARGUMENT =
		1;
	
	/**
	 * {@link IllegalStateException} for {@link
	 * PencilShelf#hardwareCopyArea(PencilBracket, int, int, int, int, int,
	 * int, int)}.
	 */
	@SquirrelJMEVendorApi
	byte COPY_AREA_ILLEGAL_STATE =
		2;
}
