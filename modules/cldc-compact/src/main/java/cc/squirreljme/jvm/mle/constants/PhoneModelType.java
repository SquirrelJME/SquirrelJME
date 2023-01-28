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
 * This represents potential simulated phone models.
 *
 * @since 2022/02/14
 */
@Exported
public interface PhoneModelType
{
	/** Generic SquirrelJME. */
	@Exported
	byte GENERIC =
		0;
	
	/** NTT Docomo D503i. */
	@Exported
	byte NTT_DOCOMO_D503I =
		1;
	
	/** NTT Docomo F503i. */
	@Exported
	byte NTT_DOCOMO_F503I =
		2;
	
	/** NTT Docomo So503i. */
	@Exported
	byte NTT_DOCOMO_SO503I =
		3;
	
	/** NTT Docomo P503i. */
	@Exported
	byte NTT_DOCOMO_P503I =
		4;
}
