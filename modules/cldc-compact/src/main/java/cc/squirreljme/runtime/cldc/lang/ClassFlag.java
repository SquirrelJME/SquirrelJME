// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

/**
 * This represent class flags and their values.
 *
 * @since 2018/12/04
 */
public interface ClassFlag
{
	/** Public. */
	int PUBLIC =
		0x0001;
	
	/** Final. */
	int FINAL =
		0x0010;
	
	/** Super. */
	int SUPER =
		0x0020;
	
	/** Interface. */
	int INTERFACE =
		0x0200;
	
	/** Abstract. */
	int ABSTRACT =
		0x0400;
	
	/** Synthetic. */
	int SYNTHETIC =
		0x1000;
	
	/** Annotation. */
	int ANNOTATION =
		0x2000;
	
	/** Enum. */
	int ENUM =
		0x4000;
}

