// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This represents the runtime visibility of the annotations.
 *
 * @since 2018/05/15
 */
public enum AnnotationRuntimeVisibility
{
	/** They are visible at run-time. */
	VISIBLE,
	
	/** They are invisible at run-time. */
	INVISIBLE,
	
	/** End. */
	;
}

