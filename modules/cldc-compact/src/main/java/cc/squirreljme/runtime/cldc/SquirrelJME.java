// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc;

import cc.squirreljme.jvm.mle.constants.PhoneModelType;

/**
 * Contains information on SquirrelJME.
 *
 * @since 2018/12/05
 */
public final class SquirrelJME
{
	/** The major version of SquirrelJME. */
	public static final byte MAJOR_VERSION =
		0;
	
	/** The minor version of SquirrelJME. */
	public static final byte MINOR_VERSION =
		3;
	
	/** The release version of SquirrelJME. */
	public static final byte RELEASE_VERSION =
		0;
	
	/** The version of this SquirrelJME runtime. */
	public static final String RUNTIME_VERSION =
		"0.3.0";
	
	/** The microedition platform. */
	public static final String MICROEDITION_PLATFORM =
		"SquirrelJME/0.3.0";
	
	/**
	 * Not used.
	 * 
	 * @since 2022/02/14
	 */
	private SquirrelJME()
	{
	}
	
	/**
	 * Returns the {@code microedition.platform} property of the machine.
	 *
	 * @param __phoneModel The current phone model.
	 * @since 2022/02/14
	 */
	public static String platform(int __phoneModel)
	{
		// This can vary
		String base = SquirrelJME.MICROEDITION_PLATFORM;
		switch (__phoneModel)
		{
			case PhoneModelType.NTT_DOCOMO_D503I:
				return base + " D503i";
			
			case PhoneModelType.NTT_DOCOMO_F503I:
				return base + " F503i";
			
			case PhoneModelType.NTT_DOCOMO_SO503I:
				return base + " So503i";
			
			case PhoneModelType.NTT_DOCOMO_P503I:
				return base + " P503i";
		}
		
		// Unknown
		return base;
	}
}

