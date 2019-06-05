/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * SquirrelJME RatufaCoat Header.
 *
 * @since 2019/06/02
 */

/** Header guard. */
#ifndef SJME_hGRATUFACOATSJMERCHSJMERCH
#define SJME_hGRATUFACOATSJMERCHSJMERCH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXRATUFACOATSJMERCHSJMERCH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

/**
 * Native functions available for the JVM to use.
 *
 * @since 2019/06/03
 */
typedef struct sjme_nativefuncs
{
} sjme_nativefuncs;

/** Standard C format for arguments. */
#define SJME_JVMARG_FORMAT_STDC 1

/**
 * Java virtual machine arguments.
 *
 * @since 2019/06/03
 */
typedef struct sjme_jvmargs
{
	/** The format of the arguments. */
	int format;
	
	/** Arguments that can be used. */
	union
	{
		/** Standard C. */
		struct
		{
			/** Argument count. */
			int argc;
			
			/** Arguments. */
			char** argv;
		} stdc;
	} args;
} sjme_jvmargs;

/** Instance of the JVM. */
typedef struct sjme_jvm* sjme_jvm;

/**
 * Executes code running within the JVM.
 *
 * @param jvm The JVM to execute.
 * @return Non-zero if the JVM is resuming, otherwise zero on its exit.
 * @since 2019/06/05
 */
int sjme_jvmexec(sjme_jvm* jvm);

/**
 * Creates a new instance of a SquirrelJME JVM.
 *
 * @param args Arguments to the VM.
 * @param nativefuncs Native functions used in the JVM.
 * @return The resulting JVM or {@code NULL} if it could not be created.
 * @since 2019/06/03
 */
sjme_jvm* sjme_jvmnew(sjme_jvmargs* args, sjme_nativefuncs* nativefuncs);

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXRATUFACOATSJMERCHSJMERCH
}
#undef SJME_cXRATUFACOATSJMERCHSJMERCH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXRATUFACOATSJMERCHSJMERCH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGRATUFACOATSJMERCHSJMERCH */

