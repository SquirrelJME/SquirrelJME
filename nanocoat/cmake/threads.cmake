# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Threading and atomics support

# For compatibility for Windows, do not use pthreads even if available
find_package(Threads)
if(WIN32)
	add_compile_definitions(SJME_CONFIG_HAS_THREADS=1)
	add_compile_definitions(SJME_CONFIG_HAS_THREADS_WIN32=1)
elseif(Threads_FOUND)
	if(CMAKE_USE_PTHREADS_INIT)
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
