/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME C Virtual Machine
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------
// This single source code file contains a minitature Java Virtual Machine
// which is able to execute the SquirrelJME class library.
// The primary purpose of this virtual machine is to have an easy to compile
// bootstrap environment that is capable of running the SquirrelJME build
// system.
// --------------------------------------------------------------------------*/

/******************************************************************************
**************************** BASIC SYSTEM DETECTION ***************************
******************************************************************************/

/*----------------------------- OPERATING SYSTEM ----------------------------*/

/** Linux. */
#if defined(__linux__) || defined(__linux) || defined(linux)
	#define SQUIRRELJME_OS_NAME "linux"
	#define SQUIRRELJME_OS_ISPOSIX 1

/** Palm OS. */
#elif defined(__palmos__)
	#define SQUIRRELJME_OS_NAME "palmos"
	#define SQUIRRELJME_OS_ISPALMOS 1

/** Unknown. */
#else
	#error Unknown Operating System
#endif

/*------------------------- FORCE MACRO DEFINITIONS -------------------------*/

/** Is this Palm OS? */
#if !defined(SQUIRRELJME_OS_ISPALMOS)
	#define SQUIRRELJME_OS_ISPALMOS 0
#endif

/** Is this POSIX? */
#if !defined(SQUIRRELJME_OS_ISPOSIX)
	#define SQUIRRELJME_OS_ISPOSIX 0
#endif

/** Standard C? */
#if !defined(SQUIRRELJME_OS_ISSTDC)
	#if SQUIRRELJME_OS_ISPOSIX == 1
		#define SQUIRRELJME_OS_ISSTDC 1
	#else
		#define SQUIRRELJME_OS_ISSTDC 0
	#endif
#endif

/******************************************************************************
*************************** OPERATING SYSTEM DEFINES **************************
******************************************************************************/

/*-------------------------------- STANDARD C -------------------------------*/
#if SQUIRRELJME_OS_ISSTDC == 1

/** C99. */
#include <stdint.h>

/*--------------------------------- UNKNOWN ---------------------------------*/
#else
	#error Unhandled operating system defines.
#endif

/******************************************************************************
*************************** BASE JAVA LEVEL SUPPORT ***************************
******************************************************************************/

/*----------------------------- TYPE DEFINITIONS ----------------------------*/

/** Byte. */
typedef int8_t jbyte;

/** Short. */
typedef int16_t jshort;

/** Integer. */
typedef int32_t jint;

/** Long is virtualized. */
typedef struct jlong
{
	jint bits[2];
} jlong;

/** Float is virtualized. */
typedef struct jfloat
{
	jint bits;
} jfloat;

/** Double is virtualized. */
typedef struct jdouble
{
	jlong bits;
} jdouble;

/** Character. */
typedef uint16_t jchar;

/*------------------------------- TYPE SUPPORT ------------------------------*/

/******************************************************************************
*************************** OPERATING SYSTEM SUPPORT **************************
******************************************************************************/

/**
 * File access structure.
 *
 * @since 2016/10/06
 */
typedef struct squirreljme_file squirreljme_file;

/*-------------------------------- STANDARD C -------------------------------*/
#if SQUIRRELJME_OS_ISSTDC == 1

/** Need access to files. */
#include <stdio.h>

/**
 * {@inheritDoc}
 * @since 2016/10/06
 */
struct squirreljme_file
{
	/** File access pointer. */
	FILE* file;
};

/*--------------------------------- UNKNOWN ---------------------------------*/
#else
	#error Unhandled operating system support.
#endif

/******************************************************************************
****************************** LIBRARY FUNCTIONS ******************************
******************************************************************************/

/*-------------------------- INFLATE DECOMPRESSION --------------------------*/

/*----------------------------- ZIP FILE READER -----------------------------*/

/******************************************************************************
***************************** JAVA VIRTUAL MACHINE ****************************
******************************************************************************/

/*---------------------- BYTE CODE EXECUTION FUNCTIONS ----------------------*/

/******************************************************************************
****************************** MAIN ENTRY POINTS ******************************
******************************************************************************/

/*-------------------------------- STANDARD C -------------------------------*/
#if SQUIRRELJME_OS_ISSTDC == 1

/**
 * Standard C main entry point.
 *
 * @param argc Argument count.
 * @param argv Input arguments.
 * @return The program exit status.
 * @since 2016/10/06
 */
int main(int argc, char** argv)
{
	return 0;
}

/*--------------------------------- PALM OS ---------------------------------*/
#elif SQUIRRELJME_OS_ISPALMOS == 1

/**
 * Palm OS Main entry point.
 *
 * @param cmd The launch command used.
 * @param cmdpbp The parameter block for the command.
 * @param launchflags The flags used to launch this application.
 * @return The program exit status.
 * @since 2016/10/06
 */
uint32_t PilotMain(uint16_t cmd, void* cmdpbp, uint16_t launchflags)
{
	return 0;
}

/*--------------------------------- UNKNOWN ---------------------------------*/
#else
	#error Unhandled main entry point.
#endif

