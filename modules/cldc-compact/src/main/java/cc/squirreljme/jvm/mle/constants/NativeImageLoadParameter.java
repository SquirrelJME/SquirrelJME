// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * Parameters for native image loading.
 *
 * @since 2021/12/05
 */
@Exported
public interface NativeImageLoadParameter
{
	/** The number of initial parameters. */
	@Exported
	byte STORED_PARAMETER_COUNT =
		0;
	
	/** Is an alpha channel used? */
	@Exported
	byte USE_ALPHA =
		1;
	
	/** The image width. */
	@Exported
	byte WIDTH =
		2;
	
	/** The image height. */
	@Exported
	byte HEIGHT =
		3;
	
	/** The length of a scan. */
	@Exported
	byte SCAN_LENGTH =
		4;
	
	/** The number of currently possible parameters. */
	@Exported
	byte NUM_PARAMETERS =
		5;
}
