// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

/**
 * The format of the virtual machine name.
 *
 * @since 2020/08/06
 */
public enum VMNameFormat
{
	/** lowercase. */
	LOWERCASE,
	
	/** camelCase. */
	CAMEL_CASE,
	
	/** ProperNoun. */
	PROPER_NOUN,
	
	/* End. */
	;
}
