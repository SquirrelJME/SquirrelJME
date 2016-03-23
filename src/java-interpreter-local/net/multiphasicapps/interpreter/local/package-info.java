// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

/**
 * This package contains the local interpreter which is meant to be run on the
 * virtual machine. It is meant to be used by hairball for launching packages
 * without requiring reflection to load them, by the testing system to make
 * sure that generic code generation is sane, and by the native compilers which
 * are given programs for output code generation.
 *
 * @since 2016/03/23
 */

package net.multiphasicapps.interpreter.local;

