/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Standard attributes.
 * 
 * @since 2026/03/26
 */

#ifndef SJME_C_SQUIRRELJME_STDATTR_H
#define SJME_C_SQUIRRELJME_STDATTR_H

#include "sjme/config.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_STDATTR_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/* Visual C SAL 2.0 Annotations. */
#if SJME_CONFIG_MSVC_VERSION_LEAST(SJME_CONFIG_MSVC_VERSION_2010)
	#include <sal.h>

	/** Return value must be checked. */
	#define sjme_attrCheckReturn _Must_inspect_result_

	/** Deprecated. */
	#define sjme_attrDeprecated __declspec(deprecated)

	/** Formatted string argument. */
	#define sjme_attrFormatArg _Printf_format_string_

	/** Input cannot be null. */
	#define sjme_attrInNotNull _In_

	/** Input can be null. */
	#define sjme_attrInNullable _In_opt_

	/** Takes input and produces output. */
	#define sjme_attrInOutNotNull _In_ _Out_

	/** Input value range. */
	#define sjme_attrInRange(lo, hi) _In_range_((lo), (hi))

	/** Method takes input. */
	#define sjme_attrInValue _In_

	/** Returns nullable value. */
	#define sjme_attrReturnNullable _Outptr_result_maybenull_z_

	/** Method gives output. */
	#define sjme_attrOutNotNull _Out_

	/** Method output can be null. */
	#define sjme_attrOutNullable _Out_opt_

	/** Output to buffer. */
	#define sjme_attrOutNotNullBuf(lenArg) _Out_writes_(lenArg)

	/** Output value range. */
	#define sjme_attrOutRange(lo, hi) _Out_range_((lo), (hi))

/* Older Visual C++. */
#elif SJME_CONFIG_MSVC_VERSION_LEAST(SJME_CONFIG_MSVC_VERSION_6)
	#include <sal.h>

	/** Return value must be checked. */
	#define sjme_attrCheckReturn __checkReturn

	/** Formatted string argument. */
	#define sjme_attrFormatArg __format_string

	/** Input cannot be null. */
	#define sjme_attrInNotNull __in

	/** Input can be null. */
	#define sjme_attrInNullable __in_opt

	/** Takes input and produces output. */
	#define sjme_attrInOutNotNull __in __out

	/** Method takes input. */
	#define sjme_attrInValue __in

	/** Returns nullable value. */
	#define sjme_attrReturnNullable __maybenull

	/** Method gives output. */
	#define sjme_attrOutNotNull __out

	/** Method output can be null. */
	#define sjme_attrOutNullable __out_opt

#elif defined(SJME_CONFIG_HAS_CLANG) || defined(SJME_CONFIG_HAS_GCC)
	/* Clang has special analyzer stuff, but also same as GCC otherwise. */
	#if defined(SJME_CONFIG_HAS_CLANG)
		/** Returns nullable value. */
		#define sjme_attrReturnNullable _Nullable_result
	#endif

	#if SJME_CONFIG_GCC_VERSION_LEAST(4, 4)
		/** Artificial function. */
		#define sjme_attrArtificial __attribute__((artificial))
	#endif

	/** Check return value. */
	#define sjme_attrCheckReturn __attribute__((warn_unused_result))

	/** Deprecated. */
	#define sjme_attrDeprecated __attribute__((deprecated))

	#if SJME_CONFIG_GCC_VERSION_LEAST(4, 4)
		/** Disable optimization. */
		#define sjme_noOptimize __attribute__((optimize("O0")))
	#endif

	/**
	 * Formatted string.
	 *
	 * @param formatIndex The formatted string index.
	 * @param vaIndex The index of @c ... or @c va_list .
	 * @since 2023/08/05
	 */
	#define sjme_attrFormatOuter(formatIndex, vaIndex) \
		__attribute__((__format__(__printf__, formatIndex + 1, vaIndex + 1)))

	/** Indicates a callback. */
	#define sjme_attrCallback __attribute__((callback))

	/** Not used. */
	#define sjme_attrUnused __attribute__((unused))

	/** Not used enum constant. */
	#define sjme_attrUnusedEnum(x) x sjme_attrUnused
#endif

#if !defined(sjme_attrCallback)
	/** Indicates a callback. */
	#define sjme_attrCallback
#endif

#if !defined(sjme_attrCheckReturn)
	/** Return value must be checked. */
	#define sjme_attrCheckReturn
#endif

#if !defined(sjme_attrDeprecated)
	/** Deprecated. */
	#define sjme_attrDeprecated
#endif

#if !defined(sjme_attrFormatArg)
	/** Formatted string argument. */
	#define sjme_attrFormatArg
#endif

#if !defined(sjme_attrFormatOuter)
	/**
	 * Formatted string.
	 *
	 * @param formatIndex The formatted string index.
	 * The index of @c ... or @c va_list .
	 * @since 2023/08/05
	 */
	#define sjme_attrFormatOuter(formatIndex, vaIndex)
#endif

#if !defined(sjme_attrInValue)
	/** Method takes input. */
	#define sjme_attrInValue
#endif

#if !defined(sjme_attrInRange)
	/** Input value range. */
	#define sjme_attrInRange(lo, hi)
#endif

#if !defined(sjme_attrReturnNullable)
	/** Returns a nullable value. */
	#define sjme_attrReturnNullable
#endif

#if !defined(sjme_attrInValue)
	/** Takes input value. */
	#define sjme_attrInValue
#endif

#if !defined(sjme_attrInNotNull)
	/** Cannot be null. */
	#define sjme_attrInNotNull sjme_attrInValue
#endif

#if !defined(sjme_attrInNullable)
	/** Nullable. */
	#define sjme_attrInNullable sjme_attrInValue
#endif

#if !defined(sjme_attrOutNotNull)
	/** Method gives output. */
	#define sjme_attrOutNotNull sjme_attrInNotNull
#endif

#if !defined(sjme_attrOutNullable)
	/** Method output can be null. */
	#define sjme_attrOutNullable sjme_attrInNullable
#endif

#if !defined(sjme_attrInOutNotNull)
	/** Takes input and produces output. */
	#define sjme_attrInOutNotNull sjme_attrInNotNull sjme_attrOutNotNull
#endif

#if !defined(sjme_attrInOutNullable)
	/** Takes input and produces output. */
	#define sjme_attrInOutNullable sjme_attrInNullable sjme_attrOutNullable
#endif

#if !defined(sjme_attrInNotNullBuf)
	/** Input to buffer. */
	#define sjme_attrInNotNullBuf(lenArg) sjme_attrInNotNull
#endif

#if !defined(sjme_attrOutNotNullBuf)
	/** Output to buffer. */
	#define sjme_attrOutNotNullBuf(lenArg) sjme_attrOutNotNull
#endif

#if !defined(sjme_attrInOutNotNullBuf)
	/** Input/output to/from buffer. */
	#define sjme_attrInOutNotNullBuf(lenArg) \
		sjme_attrInNotNullBuf(lenArg) sjme_attrOutNotNullBuf(lenArg)
#endif

#if !defined(sjme_attrOutRange)
	/** Output value range. */
	#define sjme_attrOutRange(lo, hi)
#endif

/** Positive value. */
#define sjme_attrInPositive sjme_attrInRange(0, INT32_MAX)

/** Non-zero positive value. */
#define sjme_attrInPositiveNonZero sjme_attrInRange(1, INT32_MAX)

/** Negative one to positive. */
#define sjme_attrInNegativeOnePositive sjme_attrInRange(-1, INT32_MAX)

/** Positive value. */
#define sjme_attrOutPositive sjme_attrOutRange(0, INT32_MAX)

/** Non-zero positive value. */
#define sjme_attrOutPositiveNonZero sjme_attrOutRange(1, INT32_MAX)

/** Negative one to positive. */
#define sjme_attrOutNegativeOnePositive sjme_attrOutRange(-1, INT32_MAX)

#if !defined(sjme_flexibleArrayCount)
	/** Flexible array count, zero by default. */
	#define sjme_flexibleArrayCount 0
#endif

#if !defined(sjme_flexibleArrayCountUnion)
	/** Flexible array count but for unions. */
	#define sjme_flexibleArrayCountUnion 1
#endif

#if !defined(sjme_attrUnused)
	/** Unused value. */
	#define sjme_attrUnused
#endif

#if !defined(sjme_attrUnusedEnum)
	/** Unused enumeration element. */
	#define sjme_attrUnusedEnum(x) x
#endif

#if !defined(sjme_attrArtificial)
	/** Artificial function. */
	#define sjme_attrArtificial
#endif

#if !defined(sjme_attrOutModify)
	/** Modifies the output. */
	#define sjme_attrOutModify
#endif

#if !defined(sjme_attrOutOverwrite)
	/** Overwrites the output. */
	#define sjme_attrOutOverwrite
#endif

#if !defined(sjme_inline)
	#if !defined(SJME_CONFIG_HAS_MSVC) || \
		SJME_CONFIG_MSVC_VERSION_LEAST(SJME_CONFIG_MSVC_VERSION_2010)
		/** Inline function. */
		#define sjme_inline inline
	#else
		/** Inline function. */
		#define sjme_inline __inline
	#endif
#endif

#if !defined(sjme_noOptimize)
	/** Disable optimization. */
	#define sjme_noOptimize
#endif

/* If building with libwine, we have to match the system calling convention. */
#if defined(SJME_CONFIG_HAS_OS_WINDOWS_16)
	/** SquirrelJME exported calling convention. */
	#define sjme_attrExportCall FAR PASCAL
#elif defined(SJME_CONFIG_HAS_OS_WINDOWS) || \
	defined(SJME_CONFIG_HAS_OS_WINDOWS_32)
	/** SquirrelJME exported calling convention. */
	#define sjme_attrExportCall __stdcall
#else
	/** SquirrelJME exported calling convention. */
	#define sjme_attrExportCall
#endif

#if defined(SJME_CONFIG_HAS_MSVC)
	/** Align to 32-bit. */
	#define sjme_align32 __declspec(align(4))
#elif defined(SJME_CONFIG_HAS_GCC) || defined(SJME_CONFIG_HAS_CLANG)
	/** Align to 32-bit. */
	#define sjme_align32 __attribute__((aligned(4)))
#else
	/** Align to 32-bit. */
	#define sjme_align32
#endif

#if defined(SJME_CONFIG_HAS_MSVC)
	/** Align to 64-bit. */
	#define sjme_align64 __declspec(align(8))
#elif defined(SJME_CONFIG_HAS_GCC) || defined(SJME_CONFIG_HAS_CLANG)
	/** Align to 64-bit. */
	#define sjme_align64 __attribute__((aligned(8)))
#else
	/** Align to 64-bit. */
	#define sjme_align64
#endif

#if SJME_CONFIG_HAS_POINTER == 64
	/** Align to pointer. */
	#define sjme_alignPointer sjme_align64
#elif SJME_CONFIG_HAS_POINTER == 32
	/** Align to pointer. */
	#define sjme_alignPointer sjme_align32
#else
	/** Align to pointer. */
	#define sjme_alignPointer
#endif

#if defined(SJME_CONFIG_HAS_GCC) || defined(SJME_CONFIG_HAS_CLANG)
	/** Packed enumeration. */
	#define sjme_attrPackedEnumByte(name) __attribute__((packed)) name
#else
	/** Packed enumeration. */
	#define sjme_attrPackedEnumByte(name) name
#endif

#if defined(SJME_CONFIG_HAS_GCC) || defined(SJME_CONFIG_HAS_CLANG)
	/** Packed structure. */
	#define sjme_packed __attribute__((packed))
#else
	/** Packed structure. */
	#define sjme_packed
#endif

#if defined(SJME_CONFIG_HAS_ARCH_IA16)
	/** Full address range pointer. */
	#define sjme_attrHugeP huge
#else
	/** Full address range pointer. */
	#define sjme_attrHugeP
#endif

#if defined(SJME_CONFIG_HAS_GCC)
	/** Optimize this specific function. */
	#define sjme_attrOptimize __attribute__((optimize("-Os")))
#elif defined(SJME_CONFIG_HAS_MSVC)
	/** Optimize this specific function. */
	#define sjme_attrOptimize __pragma(optimize("t", on))
#else
	/** Optimize this specific function. */
	#define sjme_attrOptimize
#endif

#if SJME_CONFIG_GCC_VERSION_LEAST(17, 0) || \
	SJME_CONFIG_CLANG_VERSION_LEAST(21, 1)
	/** Function pointer implementation. */
	#define sjme_fp(funcPtr) \
		__attribute__((cfi_salt(#funcPtr)))
#else
	/** Function pointer implementation. */
	#define sjme_fp(funcPtr)
#endif

/** Force specific size for an enum. */
#define sjme_enumInt(type) \
	SJME__enum_##type##_MAXV = INT32_MAX, \
	SJME__enum_##type##_ZERO = 0

#if SJME_CONFIG_GCC_VERSION_LEAST(9, 0)
	/** Generic pointer which should really be the given type. */
	#define sjme_pointerR(type) \
		__attribute__((copy((type)0))) sjme_pointer
#else
	/** Generic pointer which should really be the given type. */
	#define sjme_pointerR(type) \
		sjme_pointer
#endif

#if defined(SJME_CONFIG_HAS_MSVC)
	/**
	 * Adjusts the order of attributes and a primary token for different
	 * compilers, such as when one compiler accepts a specific sequence of
	 * tokens but another compiler fails with that sequence. An example
	 * would be @code struct sjme_attrAlign32 example @endcode and
	 * comparatively @code sjme_attrAlign32 struct example @endcode.
	 *
	 * @param primary The primary, usually a type such
	 * as @code struct @endcode.
	 * @param attr The attributes.
	 * @since 2026/06/16
	 */
	#define sjme_attrOrder(primary, attr) attr primary
#else
	/**
	 * Adjusts the order of attributes and a primary token for different
	 * compilers, such as when one compiler accepts a specific sequence of
	 * tokens but another compiler fails with that sequence. An example
	 * would be @code struct sjme_attrAlign32 example @endcode and
	 * comparatively @code sjme_attrAlign32 struct example @endcode.
	 *
	 * @param primary The primary, usually a type such
	 * as @code struct @endcode.
	 * @param attr The attributes.
	 * @since 2026/06/16
	 */
	#define sjme_attrOrder(primary, attr) primary attr
#endif

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_STDATTR_H
}
#undef SJME_CXX_SQUIRRELJME_STDATTR_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_STDATTR_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_STDATTR_H */
