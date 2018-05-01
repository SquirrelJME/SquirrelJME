// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support;

/**
 * Represents the source path set that are available for usage.
 *
 * @since 2018/04/30
 */
public enum SourcePathSetType
{
	/** Source code. */
	SOURCE,
	
	/** Compiled code, may include generated code. */
	COMPILED,
	
	/** End. */
	;
}

