// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

/**
 * Returns the closeness of the pointer.
 *
 * @since 2023/06/24
 */
public enum CPointerCloseness
{
	/** Near pointers, which are also the default. */
	NEAR(""),
	
	/** Far pointers. */
	FAR("far"),
	
	/** Huge pointers. */
	HUGE("huge"),
	
	/* End. */
	;
	
	/** The token used for the closeness. */
	protected final String token;
	
	/**
	 * Initializes the closeness level.
	 * 
	 * @param __token The token used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	CPointerCloseness(String __token)
		throws NullPointerException
	{
		this.token = __token;
	}
}
