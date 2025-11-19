# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Install4J installers and portables

# Locate the Install4J builder
find_program(Install4JC_EXECUTABLE
	NAMES install4jc)

# Emit locations of Install4J
message(STATUS "install4jc: ${Install4JC_EXECUTABLE}")

# Build all install4j bundles together as a single bundle? This is so they
# share the same update.xml and otherwise
option(SQUIRRELJME_INSTALL4J_BUNDLE
	"Build all Install4J installers as a single bundle." YES)

# Builds Install4J executables for every OS on-top of the standalone
if(Install4JC_EXECUTABLE)
	# Install4J identifies each media specifically by its ID, there is the
	# media type however this would build them all for each type and I have
	# setup multiple ones that share the same media type
	unset(mediaIds)
	foreach(nativeMap IN LISTS SQUIRRELJME_STANDALONE_NATIVES_AVAILABLE)
		# Which System and architecture?
		squirreljme_unmap(systemNormal 0 "${nativeMap}")
		squirreljme_unmap(archNormal 1 "${nativeMap}")

		# Windows installer and its portable
		if("${systemNormal}" STREQUAL "windows" AND
			("${archNormal}" STREQUAL "ia32" OR
			"${archNormal}" STREQUAL "amd64"))
			# Installer
			list(APPEND mediaIds "28")

			# Portable
			list(APPEND mediaIds "128")

		# Debian Package and Generic Linux RPM
		elseif("${systemNormal}" STREQUAL "linux" AND
			("${archNormal}" STREQUAL "ia32" OR
			"${archNormal}" STREQUAL "amd64"))
			# Generic RPM, which should have both
			list(APPEND mediaIds "129")

			# Debian packages
			if("${archNormal}" STREQUAL "ia32")
				list(APPEND mediaIds "148")
			else()
				list(APPEND mediaIds "130")
			endif()

		# Debian ARM32 Package
		elseif("${systemNormal}" STREQUAL "linux" AND
			"${archNormal}" STREQUAL "arm32")
			list(APPEND mediaIds "143")

		# Debian ARM64 Package
		elseif("${systemNormal}" STREQUAL "linux" AND
			"${archNormal}" STREQUAL "arm64")
			list(APPEND mediaIds "146")

		# Solaris 64-bit Package
		elseif("${systemNormal}" STREQUAL "solaris" AND
			"${archNormal}" STREQUAL "amd64")
			list(APPEND mediaIds "31")

		# macOS Universal (hopefully) folder
		elseif("${systemNormal}" STREQUAL "macosx" AND
			("${archNormal}" STREQUAL "powerpc" OR
			"${archNormal}" STREQUAL "ia32" OR
			"${archNormal}" STREQUAL "amd64"))
			list(APPEND mediaIds "32")
		endif()
	endforeach()

	# Remove duplicates, just in case
	list(REMOVE_DUPLICATES mediaIds)
	list(SORT mediaIds)

	# Build all installers at once
	if(SQUIRRELJME_INSTALL4J_BUNDLE)
		# Place the output installers somewhere
		set(mediaOutDir "${CMAKE_BINARY_DIR}/install4j/bundle")

		# Setup rule to build all at once
		string(REPLACE ";" "," mediaIdsComma "${mediaIds}")
		add_custom_target(install4j
			COMMAND "${CMAKE_COMMAND}" "-E"
				"make_directory" "${mediaOutDir}"
			COMMAND "${Install4JC_EXECUTABLE}"
				"-r" "${SQUIRRELJME_VERSION}"
				"-d" "${mediaOutDir}"
				"-b" "${mediaIdsComma}"
				"${CMAKE_SOURCE_DIR}/squirreljme.install4j"
			DEPENDS standaloneJar
			COMMAND_EXPAND_LISTS
			BYPRODUCTS "${mediaOutDir}"
			SOURCES "${CMAKE_SOURCE_DIR}/squirreljme.install4j")

	# Individual rules for each system
	else()
		# Pseudo all Install4J targets
		add_custom_target(install4j)

		# Setup rules to build each specific media individually, as it is
		# easier to see where things go wrong
		foreach(mediaId IN LISTS mediaIds)
			# Place the output installer somewhere
			set(mediaOutDir "${CMAKE_BINARY_DIR}/install4j/${mediaId}")

			# Setup rule to build the installer
			add_custom_target(install4j_${mediaId}
				COMMAND "${CMAKE_COMMAND}" "-E"
					"make_directory" "${mediaOutDir}"
				COMMAND "${Install4JC_EXECUTABLE}"
					"-r" "${SQUIRRELJME_VERSION}"
					"-d" "${mediaOutDir}"
					"-b" "${mediaId}"
					"${CMAKE_SOURCE_DIR}/squirreljme.install4j"
				DEPENDS standaloneJar
				COMMAND_EXPAND_LISTS
				BYPRODUCTS "${mediaOutDir}"
				SOURCES "${CMAKE_SOURCE_DIR}/squirreljme.install4j")

			# Have the all-install4j depend on this
			add_dependencies(install4j install4j_${mediaId})
		endforeach()
	endif()
endif()
