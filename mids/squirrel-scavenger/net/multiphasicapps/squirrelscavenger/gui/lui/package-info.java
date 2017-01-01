// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

/**
 * This package contains the user interface that uses the LUI (console) display
 * code to display the game to the user and allow for user input.
 *
 * The LUI code performs a sort of dodge for rendering 3D graphics. Essentially
 * a LCDUI Image is initialized and an OpenGL context is initialized which
 * targets that context. After rendering is complete, the resulting image
 * buffer is read and basic transformation to ASCII characters is performed.
 *
 * @since 2016/10/06
 */

package net.multiphasicapps.squirrelscavenger.gui.lui;

