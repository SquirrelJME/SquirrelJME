// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

/**
 * This is the base class for buffer adapters, instances of these classes are
 * used to translate the 32-bit integer format to lower quality buffer formats.
 *
 * @since 2019/03/24
 */
public abstract class AdvancedBufferAdapter
{
	/**
	 * Adapts the buffer data.
	 *
	 * @since 2019/03/24
	 */
	public abstract void adapt();
}

