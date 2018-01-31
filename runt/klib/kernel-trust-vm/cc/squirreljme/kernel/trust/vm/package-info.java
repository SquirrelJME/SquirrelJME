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
 * This package contains the virtual machine which is used by the trust server.
 * The purpose of this virtual machine is so that the server can run the
 * {@link java.security.Permission} instances of classes without needing to
 * ask the client if a permission is valid (the client could lie) and without
 * needing to initialize the permission class (since it could do bad things).
 * In essence, this contains a minimal self-contained environment which allows
 * untrusted code to run in a trusted space.
 *
 * This virtual machine is minimal and does not support the entire CLDC
 * specification. It is only enough to run code within the
 * {@link java.security.Permission} implementations to check permissions.
 *
 * @since 2018/01/31
 */

package cc.squirreljme.kernel.trust.vm;

