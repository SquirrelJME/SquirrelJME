// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

/**
 * This package contains classes which are used to write to ZIP files in a
 * stream based fashion. Entries are written one at a time and are directly
 * placed in the output stream. When the ZIP is closed the final index is
 * written.
 *
 * @since 2016/07/09
 */

package net.multiphasicapps.zip.streamwriter;

