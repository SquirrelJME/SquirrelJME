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
 * Modifier keys for when key and mouse events occur.
 *
 * @since 2020/08/02
 */
public interface UIKeyModifier
{
	/** Alt key modifier. */
	int MODIFIER_ALT =
		65536;
		
	/** Ctrl key modifier. */
	int MODIFIER_CHR =
		8388608;
	
	/** Command key modifier. */
	int MODIFIER_COMMAND =
		4194304;
	
	/** Ctrl key modifier. */
	int MODIFIER_CTRL =
		262144;
	
	/** Mask for all the modifier keys. */
	int MODIFIER_MASK =
		13041664;
	
	/** Shift key modifier. */
	int MODIFIER_SHIFT =
		131072;
}
