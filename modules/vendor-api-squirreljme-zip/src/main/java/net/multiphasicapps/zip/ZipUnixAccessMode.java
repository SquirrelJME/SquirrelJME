// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip;

/**
 * This represents UNIX access modes which may be declared in the ZIP.
 *
 * @since 2016/08/07
 */
public enum ZipUnixAccessMode
	implements ZipEntryAttribute
{
	/** User read. */
	USER_READ,
	
	/** User write. */
	USER_WRITE,
	
	/** User execute. */
	USER_EXECUTE,
	
	/** Group read. */
	GROUP_READ,
	
	/** Group write. */
	GROUP_WRITE,
	
	/** Group execute. */
	GROUP_EXECUTE,
	
	/** Other read. */
	OTHER_READ,
	
	/** Other write. */
	OTHER_WRITE,
	
	/** Other execute. */
	OTHER_EXECUTE,
	
	/** End. */
	;
}

