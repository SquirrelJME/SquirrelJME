// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf;

/**
 * This represents the type of value which is stored in a register.
 *
 * @since 2016/03/21
 */
public enum NARFRegisterType
{
	/** Integer. */
	INTEGER,
	
	/** Long. */
	LONG,
	
	/** Float. */
	FLOAT,
	
	/** Double. */
	DOUBLE,
	
	/** Objects. */
	OBJECT,
	
	/** End. */
	;
	
	/** An operation which is valid for int only. */
	public static final int VALIDITY_INT =
		INTEGER.validityFlag();
	
	/** An operation which is valid for long only. */
	public static final int VALIDITY_LONG =
		LONG.validityFlag();
	
	/** An operation which is valid for any integral type. */
	public static final int VALIDITY_INTEGRAL =
		VALIDITY_INT | VALIDITY_LONG;
	
	/** An operationg which is valid for float only. */
	public static final int VALIDITY_FLOAT =
		FLOAT.validityFlag();
	
	/** An operationg which is valid for double only. */
	public static final int VALIDITY_DOUBLE =
		DOUBLE.validityFlag();
	
	/** An operation which is valid for any floating point type. */
	public static final int VALIDITY_FLOATING =
		VALIDITY_FLOAT | VALIDITY_DOUBLE;
	
	/** An operation which is valid for any number type. */
	public static final int VALIDITY_NUMBER =
		VALIDITY_INTEGRAL | VALIDITY_FLOATING;
	
	/** An operation which is valid only for objects. */
	public static final int VALIDITY_OBJECT =
		OBJECT.validityFlag();
	
	/** An operation which is valid for anything. */
	public static final int VALIDITY_ALL =
		VALIDITY_OBJECT | VALIDITY_NUMBER;
	
	/**
	 * Initializes the register type.
	 *
	 * @since 2016/03/21
	 */
	private NARFRegisterType()
	{
	}
	
	/**
	 * Returns the validity flag mask for the type.
	 *
	 * @return The validity flag mask.
	 * @since 2016/03/21
	 */
	public int validityFlag()
	{
		return 1 << ordinal();
	}
}

