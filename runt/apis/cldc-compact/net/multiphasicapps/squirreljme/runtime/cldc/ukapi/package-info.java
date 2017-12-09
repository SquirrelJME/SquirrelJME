// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

/**
 * This package contains userspace APIs which are used to interact with the
 * kernel.
 *
 * In kernel space, the API itself will always call kernel functions
 * without much hassle.
 *
 * In user space, the API will be linked to the packet system which attaches
 * the current client to the kernel.
 *
 * @since 2017/12/08
 */

package net.multiphasicapps.squirreljme.runtime.cldc.ukapi;

