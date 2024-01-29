// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Preferences for the debugger.
 *
 * @since 2024/01/27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Preferences
{
	/** Automatically resume on connect? */
	@Builder.Default
	protected volatile boolean resumeOnConnect =
		true;
	
	/** The last address used in the argument-less start dialog. */
	@Builder.Default
	protected volatile String lastAddress =
		":5005";
}
