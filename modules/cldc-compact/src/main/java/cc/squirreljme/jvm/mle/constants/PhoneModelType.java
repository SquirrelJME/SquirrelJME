// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * This represents potential simulated phone models.
 *
 * @since 2022/02/14
 */
public interface PhoneModelType
{
	/** Generic SquirrelJME. */
	byte GENERIC =
		0;
	
	/** NTT Docomo D503i. */
	byte NTT_DOCOMO_D503I =
		1;
	
	/** NTT Docomo F503i. */
	byte NTT_DOCOMO_F503I =
		2;
	
	/** NTT Docomo So503i. */
	byte NTT_DOCOMO_SO503I =
		3;
	
	/** NTT Docomo P503i. */
	byte NTT_DOCOMO_P503I =
		4;
}
