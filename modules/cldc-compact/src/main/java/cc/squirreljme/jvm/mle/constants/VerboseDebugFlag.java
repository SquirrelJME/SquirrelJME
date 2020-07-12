// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * Verbosity flags.
 *
 * @since 2020/07/11
 */
public interface VerboseDebugFlag
{
	/** All verbosity settings. */
	int ALL =
		0xFFFF_FFFF;
	
	/** Be verbose on the called instructions. */
	byte INSTRUCTIONS =
		0b0000_0001;
	
	/** Be verbose on the entered methods. */
	byte METHOD_ENTRY =
		0b0000_0010;
	
	/** Be verbose on exited methods. */
	byte METHOD_EXIT =
		0b0000_0100;
	
	/** Be verbose on MLE calls. */
	byte MLE_CALL =
		0b0000_1000;
	
	/** Be verbose on static invocations. */
	byte INVOKE_STATIC =
		0b0001_0000;
}
