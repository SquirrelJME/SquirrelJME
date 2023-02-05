// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * Parameters for native image loading.
 *
 * @since 2021/12/05
 */
public interface NativeImageLoadParameter
{
	/** The number of initial parameters. */
	byte STORED_PARAMETER_COUNT =
		0;
	
	/** Is an alpha channel used? */
	byte USE_ALPHA =
		1;
	
	/** The image width. */
	byte WIDTH =
		2;
	
	/** The image height. */
	byte HEIGHT =
		3;
	
	/** The length of a scan. */
	byte SCAN_LENGTH =
		4;
	
	/** The number of currently possible parameters. */
	byte NUM_PARAMETERS =
		5;
}
