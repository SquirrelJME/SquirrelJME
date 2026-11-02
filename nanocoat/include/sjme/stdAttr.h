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

#pragma region(sal)
	
#if SJME_CONFIG_MSVC_VERSION_LEAST(SJME_CONFIG_MSVC_VERSION_6)
	#include <sal.h>

	#if SJME_CONFIG_MSVC_VERSION_LEAST(SJME_CONFIG_MSVC_VERSION_2010)
		/** Has Visual C SAL 2.0 Annotations. */
		#define SJME_CONFIG_HAS_MSVC_SAL 2
	#else
		/** Has Visual C SAL 1.0 Annotations. */
		#define SJME_CONFIG_HAS_MSVC_SAL 1
	#endif
	
	/** Is the MSVC SAL version the specified version? */
	#define SJME_CONFIG_MSVC_SAL_LEAST(against) \
		(SJME_CONFIG_HAS_MSVC_SAL >= against)
#else
	/** Is the MSVC SAL version the specified version? */
	#define SJME_CONFIG_MSVC_SAL_LEAST(against) 0
#endif
	
#pragma endregion(sal)
#pragma region(linting)
	
#if SJME_CONFIG_MSVC_SAL_LEAST(2)
	/** Return value must be checked. */
	#define sjme_attrCheckReturn _Must_inspect_result_
#elif SJME_CONFIG_MSVC_SAL_LEAST(1)
	/** Return value must be checked. */
	#define sjme_attrCheckReturn __checkReturn
#elif defined(SJME_CONFIG_HAS_GCC_CLONE)
	/** Check return value. */
	#define sjme_attrCheckReturn __attribute__((warn_unused_result))
#else
	/** Check return value. */
	#define sjme_attrCheckReturn
#endif
	
#if SJME_CONFIG_MSVC_SAL_LEAST(2)
	/** Deprecated. */
	#define sjme_attrDeprecated __declspec(deprecated)
#elif SJME_CONFIG_GCC_VERSION_LEAST(3, 0) || \
	defined(SJME_CONFIG_HAS_CLANG)
	/** Deprecated. */
	#define sjme_attrDeprecated __attribute__((deprecated))
#else
	/** Deprecated. */
	#define sjme_attrDeprecated
#endif
	
#if SJME_CONFIG_MSVC_SAL_LEAST(2)
	/** Formatted string argument. */
	#define sjme_attrFormatArg _Printf_format_string_
#elif SJME_CONFIG_MSVC_SAL_LEAST(1)
	/** Formatted string argument. */
	#define sjme_attrFormatArg __format_string
#else
	/** Formatted string argument. */
	#define sjme_attrFormatArg
#endif
	
#if defined(SJME_CONFIG_HAS_GCC_CLONE)
	/**
	 * Formatted string.
	 *
	 * @param formatIndex The formatted string index.
	 * @param vaIndex The index of @c ... or @c va_list .
	 * @since 2023/08/05
	 */
	#define sjme_attrFormatOuter(formatIndex, vaIndex) \
		__attribute__((__format__(__printf__, formatIndex + 1, vaIndex + 1)))
#else
	/**
	 * Formatted string.
	 *
	 * @param formatIndex The formatted string index.
	 * The index of @c ... or @c va_list .
	 * @since 2023/08/05
	 */
	#define sjme_attrFormatOuter(formatIndex, vaIndex)
#endif
	
#if SJME_CONFIG_MSVC_SAL_LEAST(2)
	/** Method takes input. */
	#define sjme_attrInValue _In_
#elif SJME_CONFIG_MSVC_SAL_LEAST(1)
	/** Method takes input. */
	#define sjme_attrInValue __in
#else
	/** Method takes input. */
	#define sjme_attrInValue
#endif
	
#if SJME_CONFIG_MSVC_SAL_LEAST(2)
	/** Input cannot be null. */
	#define sjme_attrInNotNull _In_
#elif SJME_CONFIG_MSVC_SAL_LEAST(1)
	/** Input cannot be null. */
	#define sjme_attrInNotNull __in
#else
	/** Cannot be null. */
	#define sjme_attrInNotNull sjme_attrInValue
#endif
	
#if SJME_CONFIG_MSVC_SAL_LEAST(2)
	/** Input can be null. */
	#define sjme_attrInNullable _In_opt_
#elif SJME_CONFIG_MSVC_SAL_LEAST(1)
	/** Input can be null. */
	#define sjme_attrInNullable __in_opt
#else
	/** Nullable. */
	#define sjme_attrInNullable sjme_attrInValue
#endif
	
#if SJME_CONFIG_MSVC_SAL_LEAST(2)
	/** Method gives output. */
	#define sjme_attrOutNotNull _Out_
#elif SJME_CONFIG_MSVC_SAL_LEAST(1)
	/** Method gives output. */
	#define sjme_attrOutNotNull __out
#else
	/** Method gives output. */
	#define sjme_attrOutNotNull
#endif
	
#if SJME_CONFIG_MSVC_SAL_LEAST(2)
	/** Method output can be null. */
	#define sjme_attrOutNullable _Out_opt_
#elif SJME_CONFIG_MSVC_SAL_LEAST(1)
	/** Method output can be null. */
	#define sjme_attrOutNullable __out_opt
#else
	/** Method output can be null. */
	#define sjme_attrOutNullable
#endif
	
#if 0
#else
	/** Modifies the output. */
	#define sjme_attrOutModify
#endif
	
#if 0
#else
	/** Overwrites the output. */
	#define sjme_attrOutOverwrite
#endif
	
#if SJME_CONFIG_MSVC_SAL_LEAST(2)
	/** Takes input and produces output. */
	#define sjme_attrInOutNotNull _In_ _Out_
#elif SJME_CONFIG_MSVC_SAL_LEAST(1)
	/** Takes input and produces output. */
	#define sjme_attrInOutNotNull __in __out
#else
	/** Takes input and produces output. */
	#define sjme_attrInOutNotNull sjme_attrInNotNull sjme_attrOutNotNull
#endif
	
#if SJME_CONFIG_MSVC_SAL_LEAST(2)
	/** Takes input and produces output. */
	#define sjme_attrInOutNullable _In_opt_ _Out_opt_
#elif SJME_CONFIG_MSVC_SAL_LEAST(1)
	/** Takes input and produces output. */
	#define sjme_attrInOutNullable __in_opt __out_opt
#else
	/** Takes input and produces output. */
	#define sjme_attrInOutNullable sjme_attrInNullable sjme_attrOutNullable
#endif
	
#if SJME_CONFIG_MSVC_SAL_LEAST(2)
	/** Input value range. */
	#define sjme_attrInRange(lo, hi) _In_range_((lo), (hi))
#else
	/** Input value range. */
	#define sjme_attrInRange(lo, hi)
#endif
	
#if SJME_CONFIG_MSVC_SAL_LEAST(2)
	/** Output value range. */
	#define sjme_attrOutRange(lo, hi) _Out_range_((lo), (hi))
#else
	/** Output value range. */
	#define sjme_attrOutRange(lo, hi)
#endif

	
#if SJME_CONFIG_MSVC_SAL_LEAST(2)
	/** Returns nullable value. */
	#define sjme_attrReturnNullable _Outptr_result_maybenull_z_
#elif SJME_CONFIG_MSVC_SAL_LEAST(1)
	/** Returns nullable value. */
	#define sjme_attrReturnNullable __maybenull
#elif defined(SJME_CONFIG_HAS_CLANG)
	/** Returns nullable value. */
	#define sjme_attrReturnNullable _Nullable_result
#else
	/** Returns a nullable value. */
	#define sjme_attrReturnNullable
#endif
	
#if SJME_CONFIG_MSVC_SAL_LEAST(2)
	/** Output to buffer. */
	#define sjme_attrOutNotNullBuf(lenArg) _Out_writes_(lenArg)
#else
	/** Output to buffer. */
	#define sjme_attrOutNotNullBuf(lenArg) \
		sjme_attrOutNotNull sjme_attrOutModify
#endif
	
#if 0
#else
	/** Input to buffer. */
	#define sjme_attrInNotNullBuf(lenArg) sjme_attrInNotNull
#endif
	
#if 0
#else
	/** Input/output to/from buffer. */
	#define sjme_attrInOutNotNullBuf(lenArg) \
		sjme_attrInNotNullBuf(lenArg) sjme_attrOutNotNullBuf(lenArg)
#endif
	
#if SJME_CONFIG_GCC_VERSION_LEAST(4, 4)
	/** Artificial function. */
	#define sjme_attrArtificial __attribute__((artificial))
#else
	/** Artificial function. */
	#define sjme_attrArtificial
#endif
	
#if SJME_CONFIG_GCC_VERSION_LEAST(3, 0)
	/** Not used. */
	#define sjme_attrUnused __attribute__((unused))
#else
	/** Unused value. */
	#define sjme_attrUnused
#endif
	
#if defined(SJME_CONFIG_HAS_GCC_CLONE)
	/** Indicates a callback. */
	#define sjme_attrCallback __attribute__((callback))
#else
	/** Indicates a callback. */
	#define sjme_attrCallback
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
	
#pragma endregion(linting)
#pragma region(compilerAttrs)
	
#if SJME_CONFIG_GCC_VERSION_LEAST(4, 4)
	/** Do not reorder calls. */
	#define sjme_keepOrder \
		__attribute__((no_reorder)) __attribute__((noinline)) /* NOLINT */
#elif defined(SJME_CONFIG_HAS_MSVC)
	/** Do not reorder calls. */
	#define sjme_keepOrder __declspec(noinline)
#else
	/** Do not reorder calls. */
	#define sjme_keepOrder
#endif
	
#if SJME_CONFIG_GCC_VERSION_LEAST(4, 4)
	/** Disable optimization. */
	#define sjme_noOptimize __attribute__((optimize("O0"))) /* NOLINT */
#elif defined(SJME_CONFIG_HAS_MSVC)
	/** Optimize this specific function. */
	#define sjme_noOptimize __pragma(optimize("t", off))
#else
	/** Disable optimization. */
	#define sjme_noOptimize
#endif
	
#if SJME_CONFIG_GCC_VERSION_LEAST(3, 0)
	/** Optimize this specific function. */
	#define sjme_attrOptimize __attribute__((optimize("-Os"))) /* NOLINT */
#elif defined(SJME_CONFIG_HAS_MSVC)
	/** Optimize this specific function. */
	#define sjme_attrOptimize __pragma(optimize("t", on))
#else
	/** Optimize this specific function. */
	#define sjme_attrOptimize
#endif
	
#pragma endregion(compilerAttrs)
#pragma region(pseudoAttrs)
	
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
	
/** Not used enum constant. */
#define sjme_attrUnusedEnum(x) x sjme_attrUnused
	
#pragma endregion(pseudoAttrs)
#pragma region(visibilityLinkage)
	
#if defined(SJME_CONFIG_HAS_GCC_CLONE) || \
	SJME_CONFIG_MSVC_VERSION_LEAST(SJME_CONFIG_MSVC_VERSION_2010)
	/** Inline function. */
	#define sjme_inline inline
#elif defined(SJME_CONFIG_HAS_MSVC)
	/** Inline function. */
	#define sjme_inline __inline
#else
	/** Inline function. */
	#define sjme_inline
#endif

#if defined(SJME_CONFIG_HAS_OS_WINDOWS) && \
	!defined(SJME_CONFIG_HAS_OS_WINDOWS_WINE)
	/** Symbol is exported through a library. */
	#define sjme_attrExport __declspec(dllexport)
#elif SJME_CONFIG_GCC_VERSION_LEAST(3, 0) || \
	defined(SJME_CONFIG_HAS_CLANG)
	/** Symbol is exported through a library. */
	#define sjme_attrExport __attribute__((visibility("default")))
#else
	/** Symbol is exported through a library. */
	#define sjme_attrExport
#endif

#if SJME_CONFIG_GCC_VERSION_LEAST(3, 0) || \
	defined(SJME_CONFIG_HAS_CLANG)
	/** Symbol is hidden in a library. */
	#define sjme_attrHidden __attribute__((visibility("hidden")))
#else
	/** Symbol is hidden in a library. */
	#define sjme_attrHidden
#endif

/* Weak does not work on Windows, select_any is an "equivalent". */
#if defined(SJME_CONFIG_HAS_ATTR_SELECT_ANY)
	/** Select any symbol weakly, if possible. */
	#define sjme_attrWeak
#else
	#if defined(SJME_CONFIG_HAS_GCC) || \
		defined(SJME_CONFIG_HAS_CLANG)
		/** Select any symbol weakly, if possible. */
		#define sjme_attrWeak __attribute__((weak))
	#else
		/** Select any symbol weakly, if possible. */
		#define sjme_attrWeak
	#endif
#endif
	
/* Weak does not work on Windows, select_any is an "equivalent". */
#if defined(SJME_CONFIG_HAS_ATTR_SELECT_ANY)
	#if defined(SJME_CONFIG_HAS_MSVC)
		/** Select any symbol that exists, if possible. */
		#define sjme_attrSelectAny __declspec(selectany)
	#elif defined(SJME_CONFIG_HAS_GCC) || \
		defined(SJME_CONFIG_HAS_CLANG)
		/** Select any symbol that exists, if possible. */
		#define sjme_attrSelectAny __attribute__((selectany))
	#else
		/** Select any symbol that exists, if possible. */
		#define sjme_attrSelectAny
	#endif
#else
	/** Select any symbol that exists, if possible. */
	#define sjme_attrSelectAny
#endif
	
#if defined(SJME_CONFIG_HAS_ATTR_SELECT_ANY)
	/**
	 * Uses @link sjme_attrSelectAny @endlink or @link sjme_attrWeak @endlink
	 * based on current the platform.
	 */
	#define sjme_attrSelectAnyOrWeak sjme_attrSelectAny
#elif defined(SJME_CONFIG_HAS_ATTR_WEAK)
	/**
	 * Uses @link sjme_attrSelectAny @endlink or @link sjme_attrWeak @endlink
	 * based on current the platform.
	 */
	#define sjme_attrSelectAnyOrWeak sjme_attrWeak
#else
	/**
	 * Uses @link sjme_attrSelectAny @endlink or @link sjme_attrWeak @endlink
	 * based on current the platform.
	 */
	#define sjme_attrSelectAnyOrWeak
#endif
	
#if defined(SJME_CONFIG_HAS_OS_WINDOWS_16)
	/** Calling this function requires more complicated branch logic. */
	#define sjme_attrFarCall FAR
#elif defined(SJME_CONFIG_HAS_GCC_CLONE) && \
	defined(SJME_CONFIG_HAS_ARCH_POWERPC)
	/** Calling this function requires more complicated branch logic. */
	#define sjme_attrFarCall __attribute__((longcall))
#elif defined(SJME_CONFIG_HAS_GCC_CLONE) && \
	defined(SJME_CONFIG_HAS_ARCH_MIPS)
	/** Calling this function requires more complicated branch logic. */
	#define sjme_attrFarCall __attribute__((long_call))
#else
	/** Calling this function requires more complicated branch logic. */
	#define sjme_attrFarCall
#endif
	
/* If building with libwine, we have to match the system calling convention. */
#if defined(SJME_CONFIG_HAS_OS_WINDOWS_16)
	/** SquirrelJME exported calling convention. */
	#define sjme_attrExportCall sjme_attrFarCall PASCAL
#elif defined(SJME_CONFIG_HAS_OS_WINDOWS) || \
	defined(SJME_CONFIG_HAS_OS_WINDOWS_32)
	/** SquirrelJME exported calling convention. */
	#define sjme_attrExportCall __stdcall
#else
	/** SquirrelJME exported calling convention. */
	#define sjme_attrExportCall
#endif
	
#if defined(SJME_CONFIG_HAS_ARCH_IA16)
	/** Full address range pointer. */
	#define sjme_attrHugeP huge
#else
	/** Full address range pointer. */
	#define sjme_attrHugeP
#endif
	
#pragma endregion(visibilityLinkage)
#pragma region(alignPack)

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
	
/* Newer GCC and MSVC allow attributes anywhere. */
#if SJME_CONFIG_GCC_VERSION_LEAST(3, 0) || \
	defined(SJME_CONFIG_HAS_CLANG) || \
	defined(SJME_CONFIG_HAS_MSVC)
	/** Align to 32-bits (left append). */
	#define sjme_align32L sjme_align32
	
	/** Align to 32-bits (right append). */
	#define sjme_align32R
	
	/** Align to 64-bits (left append). */
	#define sjme_align64L sjme_align64
	
	/** Align to 64-bits (right append). */
	#define sjme_align64R
	
	/** Align to pointer (left append). */
	#define sjme_alignPointerL sjme_alignPointer
	
	/** Align to Pointer (right append). */
	#define sjme_alignPointerR
	
	/** Packed structure (left append). */
	#define sjme_packedL sjme_packed
	
	/** Packed structure (right append). */
	#define sjme_packedR
#else
	/** Align to 32-bits (left append). */
	#define sjme_align32L
	
	/** Align to 32-bits (right append). */
	#define sjme_align32R sjme_align32
	
	/** Align to 64-bits (left append). */
	#define sjme_align64L
	
	/** Align to 64-bits (right append). */
	#define sjme_align64R sjme_align64
	
	/** Align to pointer (left append). */
	#define sjme_alignPointerL
	
	/** Align to Pointer (right append). */
	#define sjme_alignPointerR sjme_alignPointer
	
	/** Packed structure (left append). */
	#define sjme_packedL
	
	/** Packed structure (right append). */
	#define sjme_packedR sjme_packed
#endif

/** Align to 32-bits (wrapped). */
#define sjme_align32W(var) \
	sjme_align32L var sjme_align32R

/** Align to 64-bits (wrapped). */
#define sjme_align64W(var) \
	sjme_align64L var sjme_align64R

/** Align to pointer (wrapped). */
#define sjme_alignPointerW(var) \
	sjme_alignPointerL var sjme_alignPointerR

/** Packed structure (wrapped). */
#define sjme_packedW(var) \
	sjme_packedL var sjme_packedR
	
#pragma endregion(alignPack)
#pragma region(other)

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
	
/* Some compilers support typedef volatile struct, others do not. */
#if SJME_CONFIG_GCC_VERSION_LEAST(3, 0) || \
	defined(SJME_CONFIG_HAS_CLANG) || \
	defined(SJME_CONFIG_HAS_MSVC)
	/** Volatile struct type. */
	#define sjme_attrVolatileStruct volatile
#else
	/** Volatile struct type. */
	#define sjme_attrVolatileStruct
#endif
	
#pragma endregion(other)

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
