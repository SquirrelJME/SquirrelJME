// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
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
	public static final int PUBLIC =
		0x0001;
	
	/** Final. */
	public static final int FINAL =
		0x0010;
	
	/** Super. */
	public static final int SUPER =
		0x0020;
	
	/** Interface. */
	public static final int INTERFACE =
		0x0200;
	
	/** Abstract. */
	public static final int ABSTRACT =
		0x0400;
	
	/** Synthetic. */
	public static final int SYNTHETIC =
		0x1000;
	
	/** Annotation. */
	public static final int ANNOTATION =
		0x2000;
	
	/** Enum. */
	public static final int ENUM =
		0x4000;
}

