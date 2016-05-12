// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.bytecode;

/**
 * This represents the type of value which is stored in a variable.
 *
 * @since 2016/05/12
 */
public enum NBCVariableType
{
	/** 32-bit Integer. */
	INTEGER,
	
	/** 64-bit Integer. */
	LONG,
	
	/** 32-bit Float. */
	FLOAT,
	
	/** 64-bit Double. */
	DOUBLE,
	
	/** Object. */
	OBJECT,
	
	/** End. */
	;
}

