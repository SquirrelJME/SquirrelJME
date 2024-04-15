// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import lombok.AllArgsConstructor;

/**
 * Default SummerCoat Variants.
 *
 * @since 2022/09/30
 */
@AllArgsConstructor
public enum BangletVariant
{
	/** None. */
	NONE("", "none:none:none:none"),
	
	/** Pure SummerCoat (only SummerCoat). */
	SUMMERCOAT("Pure",
		"summercoat:pure:summercoat:none"),
	
	/* End. */
	;
	
	/** The title for this variant. */
	public final String properNoun;
	
	/** The banglet for this variant. */
	public final String banglet;
}
