# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Threading and atomics support

# Used to determine if certain symbols exist
include(CheckSymbolExists)

# These platforms do not support any kind of threading
# Also consider unknown platforms as unsupported
if("${SQUIRRELJME_SYSTEM}" STREQUAL "dos" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "3ds" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "playstation2" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "unknown")
	message(STATUS "Threads not supported!")

	# Only a single thread is possible
	add_compile_definitions(SJME_CONFIG_ONLY_THREAD_SINGLE=1)

# On Windows do not use pthreads at all as it has its own threading system
elseif("${SQUIRRELJME_SYSTEM}" STREQUAL "windows" OR
	("${SQUIRRELJME_SYSTEM}" STREQUAL "wine" AND
	SJME_CONFIG_EO_LIBWINE_THREADS))
	# Notice
	message(STATUS "Forcing Win32 Threads")

	# Force this
	add_compile_definitions(SJME_CONFIG_HAS_THREADS=1)
	add_compile_definitions(SJME_CONFIG_HAS_THREADS_WIN32=1)

# Otherwise, try to detect the threading mechanism
else()
	# Locate system threads
	find_package(Threads)

	# Were threads found?
	if(Threads_FOUND AND CMAKE_USE_PTHREADS_INIT)
		# Does pthread actually exist?
		try_compile(SQUIRRELJME_PTHREADS_TRY_VALID
			"${CMAKE_CURRENT_BINARY_DIR}"
			SOURCES "${CMAKE_CURRENT_LIST_DIR}/tryPThread.c"
			CMAKE_FLAGS "-DCMAKE_TRY_COMPILE_TARGET_TYPE=EXECUTABLE"
			LINK_LIBRARIES "${CMAKE_THREAD_LIBS_INIT}"
			OUTPUT_VARIABLE SQUIRRELJME_PTHREADS_TRY_OUTPUT)
		message(STATUS "PThread: ${SQUIRRELJME_PTHREADS_TRY_OUTPUT}")

		# Valid?
		if(SQUIRRELJME_PTHREADS_TRY_VALID)
			message(STATUS "PThread: Valid!")

			add_compile_definitions(
				SJME_CONFIG_HAS_THREADS=1)
			add_compile_definitions(
				SJME_CONFIG_HAS_THREADS_PTHREAD=1)

			# Specific OSes?
			if (LINUX)
				add_compile_definitions(
					SJME_CONFIG_HAS_THREADS_PTHREAD_LINUX=1)
			elseif(APPLE OR BSD)
				if(APPLE)
					add_compile_definitions(
						SJME_CONFIG_HAS_THREADS_PTHREAD_MACOS=1)
				endif()

				add_compile_definitions(
					SJME_CONFIG_HAS_THREADS_PTHREAD_BSD=1)
			endif()
		else()
			message(STATUS "PThread: Not available or misconfigured.")
		endif()
	endif()
endif()

# Older versions of glibc do not have pthread_kill() so determine if a fallback
# can be used specifically for that
if(SQUIRRELJME_PTHREADS_TRY_VALID)
	# Use pthread
	set(CMAKE_REQUIRED_INCLUDES "${CMAKE_THREAD_INCLUDE}")
	set(CMAKE_REQUIRED_LIBRARIES "${CMAKE_THREAD_LIBS_INIT}")

	# Is there pthread_kill()?
	check_symbol_exists(pthread_kill "signal.h"
		SJME_CONFIG_HAS_PTHREAD_KILL)
	if(SJME_CONFIG_HAS_PTHREAD_KILL)
		add_compile_definitions(
			"SJME_CONFIG_HAS_PTHREAD_KILL=1")
	else()
		add_compile_definitions(
			"SJME_CONFIG_HAS_NO_PTHREAD_KILL=1")
	endif()

	# Clear
	unset(CMAKE_REQUIRED_INCLUDES)
	unset(CMAKE_REQUIRED_LIBRARIES)
endif()
