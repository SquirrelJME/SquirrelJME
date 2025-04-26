// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.tool.rms;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * RMS format, used for {@link RMSCommand#EXPORT} and
 * {@link RMSCommand#IMPORT} for interop purposes.
 *
 * @since 2025/04/14
 */
public enum RMSFormat
{
	/** Data only, no header or other information. */
	DATA("data")
	{
	},
	
	/** DoJa SDK. */
	DOJA_SDK("dojasdk")
	{
	},
	
	/** FreeJ2ME. */
	FREEJ2ME("freej2me")
	{
	},
	
	/** J2ME Loader, derived from Microemulator. */
	J2ME_LOADER("j2meloader")
	{
	},
	
	/** Microemulator. */
	MICROEMULATOR("microemulator")
	{
	},
	
	/** SquirrelJME. */
	SQUIRRELJME("squirreljme")
	{
	},
	
	/* End. */
	;
	
	RMSFormat(String __id)
	{
		throw Debugging.todo();
	}
}
