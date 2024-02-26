// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Represents an exception during Jar/Zip load.
 *
 * @since 2024/01/13
 */
public class JarFormatException
	extends Exception
{
	/**
	 * Initializes the exception with no message and no cause.
	 *
	 * @since 2024/01/13
	 */
	public JarFormatException()
	{
		super();
	}
	
	/**
	 * Initializes the exception with no message or cause. 
	 *
	 * @param __message The message to use.
	 * @since 2024/01/13
	 */
	public JarFormatException(String __message)
	{
		super(__message);
	}
}
