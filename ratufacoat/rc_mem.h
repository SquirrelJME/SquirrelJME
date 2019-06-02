/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * SquirrelJME RatufaCoat memory functions.
 *
 * @since 2019/06/02
 */

/** Header guard. */
#ifndef SJME_hGRATUFACOATRC_MEMHRC_MEMH
#define SJME_hGRATUFACOATRC_MEMHRC_MEMH

/** Include RatufaCoat header. */
#include "ratufac.h"

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXRATUFACOATRC_MEMHRC_MEMH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

/**
 * Allocates a pointer in the low 4GiB of memory for 32-bit pointer usage.
 * 
 * @param len The number of bytes to allocate.
 * @return The allocated memory.
 * @since 2019/05/31
 */
sjme_jaddress sjme_memalloc(size_t len);

/**
 * Frees a pointer which was previously allocated with sjme_memalloc.
 * 
 * @param p The pointer to free.
 * @since 2019/05/31
 */
void sjme_memfree(sjme_jaddress p);

/**
 * Reads a Java byte from memory.
 * 
 * @param p The address to read from.
 * @param o The offset.
 * @return The value at the address.
 * @since 2019/05/31
 */
sjme_jbyte sjme_memreadjbyte(sjme_jaddress p, sjme_jint o);

/**
 * Reads a Java int from memory.
 * 
 * @param p The address to read from.
 * @param o The offset.
 * @return The value at the address.
 * @since 2019/05/31
 */
sjme_jint sjme_memreadjint(sjme_jaddress p, sjme_jint o);

/**
 * Reads a Java short from memory.
 * 
 * @param p The address to read from.
 * @param o The offset.
 * @return The value at the address.
 * @since 2019/05/31
 */
sjme_jshort sjme_memreadjshort(sjme_jaddress p, sjme_jint o);

/**
 * Reads a Java char from memory.
 * 
 * @param p The address to read from.
 * @param o The offset.
 * @return The value at the address.
 * @since 2019/05/31
 */
sjme_jchar sjme_memreadjchar(sjme_jaddress p, sjme_jint o);

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXRATUFACOATRC_MEMHRC_MEMH
}
#undef SJME_cXRATUFACOATRC_MEMHRC_MEMH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXRATUFACOATRC_MEMHRC_MEMH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGRATUFACOATRC_MEMHRC_MEMH */

