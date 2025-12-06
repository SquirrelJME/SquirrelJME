# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Threading and atomics support

# These platforms do not support any kind of threading
if("${SQUIRRELJME_SYSTEM}" STREQUAL "dos" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "3ds" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "playstation2")
	message(STATUS "Threads not supported!")

	# Only a single thread is possible
	add_compile_definitions(SJME_CONFIG_ONLY_THREAD_SINGLE=1)

# On Windows do not use pthreads at all as it has its own threading system
elseif("${SQUIRRELJME_SYSTEM}" STREQUAL "windows")
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
			LINK_LIBRARIES ${CMAKE_THREAD_LIBS_INIT}
			OUTPUT_VARIABLE SQUIRRELJME_PTHREADS_TRY_OUTPUT)
		message("PThread: ${SQUIRRELJME_PTHREADS_TRY_OUTPUT}")

		# Valid?
		if(SQUIRRELJME_PTHREADS_TRY_VALID)
			message("PThread: Valid!")

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
			message("PThread: Not available or misconfigured.")
		endif()
	endif()
endif()
