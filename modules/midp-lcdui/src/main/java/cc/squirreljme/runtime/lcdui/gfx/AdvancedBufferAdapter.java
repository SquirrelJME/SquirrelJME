// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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

