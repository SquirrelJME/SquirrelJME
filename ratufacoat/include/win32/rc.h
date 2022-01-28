/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Describe this.
 * 
 * @since 2021/10/17
 */

#ifndef SQUIRRELJME_RC_H
#define SQUIRRELJME_RC_H

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_RC_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Stringify. */
#define SJME_STRINGIFY(x) SJME_INTERNAL_STRINGIFY_X(x)

/** Stringify, do not use this internal one. */
#define SJME_INTERNAL_STRINGIFY_X(x) #x

/** Copyright. */
#define SQUIRRELJME_WINDOWS_COPYRIGHT \
	"Copyright 2015-2022 Stephanie Gawroriski"

/** Trademark. */
#define SQUIRRELJME_WINDOWS_TRADEMARK \
	"SquirrelJME, Lex The Squirrel and his likeness " \
	"are Trademarks of Stephanie Gawroriski 2016-2022"

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_RC_H
}
#undef SJME_CXX_SQUIRRELJME_RC_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_RC_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_RC_H */
