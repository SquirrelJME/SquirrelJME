#parse("C File Header.h")

/**
 * Describe this.
 * 
 * @since ${YEAR}/${MONTH}/${DAY}
 */

#[[#ifndef]]# SJME_C_${INCLUDE_GUARD}
#[[#define]]# SJME_C_${INCLUDE_GUARD}

/* Anti-C++. */
#[[#ifdef]]# __cplusplus
#[[#ifndef]]# SJME_CXX_IS_EXTERNED
#[[#define]]# SJME_CXX_IS_EXTERNED
#[[#define]]# SJME_CXX_${INCLUDE_GUARD}
extern "C"
{
#[[#endif]]# /* #[[#ifdef]]# SJME_CXX_IS_EXTERNED */
#[[#endif]]# /* #[[#ifdef]]# __cplusplus */

/*--------------------------------------------------------------------------*/

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#[[#ifdef]]# __cplusplus
#[[#ifdef]]# SJME_CXX_${INCLUDE_GUARD}
}
#[[#undef]]# SJME_CXX_${INCLUDE_GUARD}
#[[#undef]]# SJME_CXX_IS_EXTERNED
#[[#endif]]# /* #[[#ifdef]]# SJME_CXX_${INCLUDE_GUARD} */
#[[#endif]]# /* #[[#ifdef]]# __cplusplus */

#[[#endif]]# /* SJME_C_${INCLUDE_GUARD} */
