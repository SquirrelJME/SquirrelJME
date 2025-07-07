/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * MLE Bracket instance types.
 * 
 * @since 2025/02/23
 */

#ifndef SQUIRRELJME_MLEBRACKETS_H
#define SQUIRRELJME_MLEBRACKETS_H

#include "sjme/nvm/instance.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_MLEBRACKETS_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Jar package bracket.
 *
 * @since 2025/07/06
 */
typedef struct sjme_jbracketJarPackageBase sjme_jbracketJarPackageBase;
	
/**
 * Jar package bracket.
 *
 * @since 2025/07/06
 */
typedef sjme_jbracketJarPackageBase* sjme_jbracketJarPackage;
	
/** Basic @c sjme_jbracketJarPackage type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jbracketJarPackage \
	SJME_TYPEOF_BASIC_sjme_jobject

/** Java @c sjme_jbracketJarPackage type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jbracketJarPackage \
	SJME_TYPEOF_JAVA_sjme_jobject

/** Is a pointer for @c sjme_jbracketJarPackage ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jbracketJarPackage \
	SJME_TYPEOF_IS_POINTER_sjme_jobject

/**
 * Trace point bracket.
 *
 * @since 2025/06/28
 */
typedef struct sjme_jbracketTraceBase sjme_jbracketTraceBase;
	
/**
 * Trace point bracket.
 *
 * @since 2025/06/28
 */
typedef sjme_jbracketTraceBase* sjme_jbracketTrace;
	
/** Basic @c sjme_jbracketTrace type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jbracketTrace SJME_TYPEOF_BASIC_sjme_jobject

/** Java @c sjme_jbracketTrace type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jbracketTrace SJME_TYPEOF_JAVA_sjme_jobject

/** Is a pointer for @c sjme_jbracketTrace ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jbracketTrace \
	SJME_TYPEOF_IS_POINTER_sjme_jobject
	
/**
 * Pipe bracket.
 *
 * @since 2025/02/23
 */
typedef struct sjme_jbracketPipeBase sjme_jbracketPipeBase;

/**
 * Pipe bracket.
 *
 * @since 2025/02/23
 */
typedef sjme_jbracketPipeBase* sjme_jbracketPipe;
	
/** Basic @c sjme_jbracketPipe type identifier. */
#define SJME_TYPEOF_BASIC_sjme_jbracketPipe SJME_TYPEOF_BASIC_sjme_jobject

/** Java @c sjme_jbracketPipe type identifier. */
#define SJME_TYPEOF_JAVA_sjme_jbracketPipe SJME_TYPEOF_JAVA_sjme_jobject

/** Is a pointer for @c sjme_jbracketPipe ? */
#define SJME_TYPEOF_IS_POINTER_sjme_jbracketPipe \
	SJME_TYPEOF_IS_POINTER_sjme_jobject
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_MLEBRACKETS_H
}
#undef SJME_CXX_MLEBRACKETS_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_MLEBRACKETS_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MLEBRACKETS_H */
