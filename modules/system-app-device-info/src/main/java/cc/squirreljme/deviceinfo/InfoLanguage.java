// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.deviceinfo;

/**
 * Language field information.
 *
 * @since 2025/12/06
 */
public enum InfoLanguage
	implements SpecificField
{
	/** Encoding. */
	ENCODING("Encoding", "microedition.encoding"),
	
	/** Locale. */
	LOCALE("Locale", "microedition.locale"),
	
	/* End. */
	;
	
	/** The key for the field. */
	protected final String key;
	
	/** The property for the field. */
	protected final String property;
	
	/**
	 * Initializes the field key.
	 *
	 * @param __key The key.
	 * @param __property The property.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/06
	 */
	InfoLanguage(String __key, String __property)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
		this.property = __property;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/06
	 */
	@Override
	public final String key()
	{
		return this.key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/06
	 */
	@Override
	public String value()
	{
		return System.getProperty(this.property);
	}
}
