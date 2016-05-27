// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ci;

/**
 * This represents the type of tag that a constant pool entry may be.
 *
 * @since 2016/04/24
 */
public enum CIPoolTag
{
	/** The UTF constant tag. */
	UTF8,
	
	/** Integer constant. */
	INTEGER,
	
	/** Float constant. */
	FLOAT,
	
	/** Long constant. */
	LONG,
	
	/** Double constant. */
	DOUBLE,
	
	/** Reference to another class. */
	CLASS,
	
	/** String constant. */
	STRING,
	
	/** Field reference. */
	FIELDREF,
	
	/** Method reference. */
	METHODREF,
	
	/** Interface method reference. */
	INTERFACEMETHODREF,
	
	/** Name and type. */
	NAMEANDTYPE,
	
	/** Method handle (illegal). */
	METHODHANDLE,
	
	/** Method type (illegal). */
	METHODTYPE,
	
	/** Invoke dynamic call site (illegal). */
	INVOKEDYNAMIC,
	
	/** End. */
	;
}

