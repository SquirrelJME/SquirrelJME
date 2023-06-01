/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Device tree API.
 * 
 * @since 2022/12/26
 */

#ifndef SQUIRRELJME_DEVTREE_H
#define SQUIRRELJME_DEVTREE_H

#include "frontend/ieee1275/ieee1275.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_DEVTREE_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Locates the given device within the device tree.
 *
 * @param name The name of the device to locate.
 * @return Will return the @c phandle or @c SJME_IEEE1275_INVALID_PHANDLE if
 * not valid.
 * @since 2022/12/26
 */
sjme_ieee1275PHandle sjme_ieee1275FindDevice(const char* const name);

/**
 * Returns the fully qualified path name to the specified @c iHandle storing
 * as many bytes as possible.
 *
 * If the actual path is too large for the buffer, then the extra characters
 * and terminating NUL are not stored.
 *
 * @param iHandle The iHandle to get the path of.
 * @param dest The destination character array.
 * @param destLen The length of the destination array.
 * @return The length of the path without any null terminator,
 * or @c -1 if @c ihandle is not valid.
 * @since 2022/12/26
 */
sjme_jint sjme_ieee1275GetIHandlePath(const sjme_ieee1275IHandle iHandle,
	char* const dest, const size_t destLen);

/**
 * Returns property data from a PHandle.
 *
 * @param devNode The device node to read a property from.
 * @param name The name of the property to get.
 * @param dest The destination buffer.
 * @param destLen The destination buffer length.
 * @return The size of the actual property if it is valid which may be
 * more than @c destLen, or @c -1 if the property name does not exist.
 * @since 2022/12/26
 */
sjme_jint sjme_ieee1275GetProp(const sjme_ieee1275PHandle devNode,
	const char* const name, void* const dest, const size_t destLen);

/**
 * This is used to obtain memory addresses, ihandles, and phandles; note that
 * on some platforms integer values are swapped and @c sjme_ieee1275GetProp
 * will not perform any swapping.
 *
 * Because on x86:
 *  OB says: stdout 03df363c
 *           stdin  03df42a8
 * But QEMU says:
 * (gdb) print/x *(uint32_t*)a_Dest
 * $27 = 0x3c36df03	// stdout
 * (gdb) print/x *(uint32_t*)a_Dest
 * $31 = 0xa842df03	// stdin
 *
 * So basically, if an integer property wants to be obtained use this instead
 * as it will basically swap the entire buffer around.
 *
 * @param devNode The device node to read a property from.
 * @param name The name of the property to get.
 * @param dest The destination buffer.
 * @param destLen The destination buffer length.
 * @return The size of the actual property if it is valid which may be
 * more than @c destLen, or @c -1 if the property name does not exist.
 * @since 2022/12/26
 */
sjme_jint sjme_ieee1275GetPropAsInt(const sjme_ieee1275PHandle devNode,
	const char* const name, void* const dest, const size_t destLen);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_DEVTREE_H
}
		#undef SJME_CXX_SQUIRRELJME_DEVTREE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_DEVTREE_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_DEVTREE_H */
