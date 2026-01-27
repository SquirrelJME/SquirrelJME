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

# Utilities used to convert icons
find_program(convert_EXECUTABLE
	NAMES "convert")
find_program(uudecode_EXECUTABLE
	NAMES "uudecode")
find_program(xpmtoppm_EXECUTABLE
	NAMES "xpmtoppm")
find_program(pnmtopng_EXECUTABLE
	NAMES "pnmtopng")

# Install4j output directory
set(SQUIRRELJME_INSTALL4J_DIR "${CMAKE_BINARY_DIR}/install4j")
file(MAKE_DIRECTORY "${SQUIRRELJME_INSTALL4J_DIR}")

# Icon output directory
set(SQUIRRELJME_ICONS_DIR "${CMAKE_BINARY_DIR}/icons")
file(MAKE_DIRECTORY "${SQUIRRELJME_ICONS_DIR}")

# Add base target for all icons
add_custom_target(icon)

# Input icons
list(APPEND SQUIRRELJME_ICONS
	"head_8x8"
	"head_16x16"
	"head_24x24"
	"head_32x32"
	"head_48x48"
	"head_64x64"
	"head_128x128")

# Process and decode/convert each icon
foreach(xpmIcon IN LISTS SQUIRRELJME_ICONS)
	# Input (XPM)
	set(inXpm "${CMAKE_SOURCE_DIR}/assets/mascot/${xpmIcon}.xpm")
	file(TO_NATIVE_PATH "${inXpm}" inXpmNative)

	# Input (MIME)
	set(inMime "${CMAKE_SOURCE_DIR}/assets/mascot/${xpmIcon}.png.__mime")
	file(TO_NATIVE_PATH "${inMime}" inMimeNative)

	# Middle (PPM)
	set(midPpm "${CMAKE_BINARY_DIR}/${xpmIcon}.ppm")
	file(TO_NATIVE_PATH "${midPpm}" midPpmNative)

	# Output
	set(outIcon "${SQUIRRELJME_ICONS_DIR}/${xpmIcon}.png")
	file(TO_NATIVE_PATH "${outIcon}" outIconNative)

	# Determine target name
	set(target "icon.${xpmIcon}")

	# Decode PNGs?
	if(uudecode_EXECUTABLE)
		add_custom_target(${target}
			COMMAND "${uudecode_EXECUTABLE}"
				"-o" "${outIconNative}" "${inMimeNative}"
			COMMENT "Decoding ${inMime} -> ${outIcon}"
			SOURCES "${inMime}"
			BYPRODUCTS "${outIcon}")

	# Round trip conversion
	elseif(xpmtoppm_EXECUTABLE AND pnmtopng_EXECUTABLE)
		add_custom_target(${target}
			COMMAND "${xpmtoppm_EXECUTABLE}"
				"--alphaout=${midPpmNative}.alpha"
				"${inXpmNative}" ">" "${midPpmNative}"
			COMMAND "${pnmtopng_EXECUTABLE}"
				"-alpha=${midPpmNative}.alpha"
				"${midPpmNative}" ">" "${outIconNative}"
			COMMENT "Converting ${inXpm} -> ${midPpm}(.alpha) -> ${outIcon}"
			SOURCES "${inIcon}"
			VERBATIM
			BYPRODUCTS "${outIcon}" "${midPpm}" "${midPpm}.alpha")

	# Use imagemagick?
	elseif(convert_EXECUTABLE)
		add_custom_target(${target}
			COMMAND "${convert_EXECUTABLE}"
				"${inXpmNative}" "${outIconNative}"
			COMMENT "Converting ${inXpm} -> ${outIcon}"
			SOURCES "${inIcon}"
			BYPRODUCTS "${outIcon}")
	endif()

	# All icons depend on this
	if(uudecode_EXECUTABLE OR convert_EXECUTABLE OR
		(xpmtoppm_EXECUTABLE AND pnmtopng_EXECUTABLE))
		add_dependencies(icon ${target})
	endif()
endforeach()


## Builds Install4J executables for every OS on-top of the standalone
#if(Install4JC_EXECUTABLE AND (convert_EXECUTABLE))
#	# Where are the installers placed?
#	set(SQUIRRELJME_INSTALL4J_DIR "${CMAKE_BINARY_DIR}/install4j")
#
#	# Input icons
#	list(APPEND SQUIRRELJME_ICONS
#		"head_8x8.xpm"
#		"head_16x16.xpm"
#		"head_24x24.xpm"
#		"head_32x32.xpm"
#		"head_48x48.xpm"
#		"head_64x64.xpm"
#		"head_128x128.xpm")
#
#	# Convert icons, make sure output exists first
#	add_custom_target(install4jIcons)
#
#	# Add target for each icon version
#	foreach(xpmIcon IN LISTS SQUIRRELJME_ICONS)
#		# Replace XPM
#		string(REPLACE ".xpm" ".png" pngIcon "${xpmIcon}")
#
#		# Notice
#		message(STATUS "Convert icon ${xpmIcon} -> ${pngIcon}")
#
#		# Input
#		file(TO_CMAKE_PATH
#			"${CMAKE_SOURCE_DIR}/assets/mascot/${xpmIcon}"
#			inIcon)
#		file(TO_NATIVE_PATH
#			"${inIcon}"
#			inIconNative)
#
#		# Output
#		file(TO_CMAKE_PATH
#			"${CMAKE_BINARY_DIR}/icons/${pngIcon}"
#			outIcon)
#		file(TO_NATIVE_PATH
#			"${outIcon}"
#			outIconNative)
#
#		# Add command
#		if(convert_EXECUTABLE)
#			add_custom_target(install4jIcons${xpmIcon}
#				COMMAND "${CMAKE_COMMAND}" "-E" "make_directory"
#					"${CMAKE_BINARY_DIR}/icons"
#				COMMAND "${convert_EXECUTABLE}"
#					"${inIconNative}" "${outIconNative}"
#				COMMENT "Converting ${xpmIcon} -> ${pngIcon}"
#				SOURCES "${inIcon}"
#				BYPRODUCTS "${outIcon}")
#		endif()
#
#		# All icons depend on this
#		add_dependencies(install4jIcons install4jIcons${xpmIcon})
#	endforeach()
#
#	# Macro to make adding installers nad installers by ID much easier
#	macro(squirreljme_install4j_register mediaId generatedPath)
#		# Notice
#		message(STATUS "Install4J Media ${mediaId} -> ${generatedPath}")
#
#		# Add to full media ID list
#		list(APPEND SQUIRRELJME_INSTALL4J_IDS "${mediaId}")
#
#		# What is the base path?
#		if(SQUIRRELJME_INSTALL4J_BUNDLE)
#			set(basePath "${SQUIRRELJME_INSTALL4J_DIR}")
#		else()
#			set(basePath "${SQUIRRELJME_INSTALL4J_DIR}/${mediaId}")
#		endif()
#
#		# Set installer files
#		list(APPEND SQUIRRELJME_INSTALL4J_ID${mediaId}_FILES
#			"${basePath}/${generatedPath}")
#		list(APPEND SQUIRRELJME_INSTALL4J_ID${mediaId}_FILES
#			"${basePath}/updates.xml")
#
#		# Add to all/bundle files
#		list(APPEND SQUIRRELJME_INSTALL4J_BUNDLE_FILES
#			"${SQUIRRELJME_INSTALL4J_ID${mediaId}_FILES}")
#
#		# Cleanup lists
#		## IDs
#		list(REMOVE_DUPLICATES SQUIRRELJME_INSTALL4J_IDS)
#		list(SORT SQUIRRELJME_INSTALL4J_IDS)
#		## ID files
#		list(REMOVE_DUPLICATES SQUIRRELJME_INSTALL4J_ID${mediaId}_FILES)
#		list(SORT SQUIRRELJME_INSTALL4J_ID${mediaId}_FILES)
#		## Bundle files
#		list(REMOVE_DUPLICATES SQUIRRELJME_INSTALL4J_BUNDLE_FILES)
#		list(SORT SQUIRRELJME_INSTALL4J_BUNDLE_FILES)
#
#		# Need to force set this into the cache in order to make them
#		# truly global unfortunately
#		set(SQUIRRELJME_INSTALL4J_IDS
#			"${SQUIRRELJME_INSTALL4J_IDS}"
#			CACHE STRING "" FORCE)
#		set(SQUIRRELJME_INSTALL4J_ID${mediaId}_FILES
#			"${SQUIRRELJME_INSTALL4J_ID${mediaId}_FILES}"
#			CACHE STRING "" FORCE)
#		set(SQUIRRELJME_INSTALL4J_BUNDLE_FILES
#			"${SQUIRRELJME_INSTALL4J_BUNDLE_FILES}"
#			CACHE STRING "" FORCE)
#	endmacro()
#
#	# Install4J identifies each media specifically by its ID, there is the
#	# media type however this would build them all for each type and I have
#	# setup multiple ones that share the same media type
#	foreach(nativeMap IN LISTS SQUIRRELJME_JAR_NATIVES_AVAILABLE)
#		# Which System and architecture?
#		squirreljme_unmap(systemNormal 0 "${nativeMap}")
#		squirreljme_unmap(archNormal 1 "${nativeMap}")
#
#		# Windows installer and its portable
#		if("${systemNormal}" STREQUAL "windows" AND
#			("${archNormal}" STREQUAL "ia32" OR
#			"${archNormal}" STREQUAL "amd64"))
#			# Installer
#			squirreljme_install4j_register(28
#				"squirreljme_windows-x64_${SQUIRRELJME_VERSION_UNDER}.exe")
#
#			# Portable
#			squirreljme_install4j_register(128
#				"squirreljme_windows-x64_${SQUIRRELJME_VERSION_UNDER}.zip")
#
#		# Debian Package and Generic Linux RPM
#		elseif("${systemNormal}" STREQUAL "linux" AND
#			("${archNormal}" STREQUAL "ia32" OR
#			"${archNormal}" STREQUAL "amd64"))
#			# Generic RPM, which should have both
#			squirreljme_install4j_register(129
#				"squirreljme_linux_${SQUIRRELJME_VERSION_UNDER}.rpm")
#
#			# Debian packages
#			if("${archNormal}" STREQUAL "ia32")
#				squirreljme_install4j_register(148
#					"squirreljme_linux-i386_${SQUIRRELJME_VERSION_UNDER}.deb")
#			else()
#				squirreljme_install4j_register(130
#					"squirreljme_linux-amd64_${SQUIRRELJME_VERSION_UNDER}.deb")
#			endif()
#
#		# Debian ARM32 Package
#		elseif("${systemNormal}" STREQUAL "linux" AND
#			"${archNormal}" STREQUAL "arm32l")
#			squirreljme_install4j_register(143
#				"squirreljme_linux-armel_${SQUIRRELJME_VERSION_UNDER}.deb")
#
#		# Debian ARM64 Package
#		elseif("${systemNormal}" STREQUAL "linux" AND
#			"${archNormal}" STREQUAL "arm64l")
#			squirreljme_install4j_register(146
#				"squirreljme_linux-aarch64_${SQUIRRELJME_VERSION_UNDER}.deb")
#
#		# Solaris 64-bit Package
#		elseif("${systemNormal}" STREQUAL "solaris" AND
#			"${archNormal}" STREQUAL "amd64")
#			squirreljme_install4j_register(31
#				"squirreljme_solaris-amd64_${SQUIRRELJME_VERSION_UNDER}.sh")
#
#		# macOS Universal (hopefully) folder
#		elseif("${systemNormal}" STREQUAL "macosx" AND
#			("${archNormal}" STREQUAL "powerpc" OR
#			"${archNormal}" STREQUAL "ia32" OR
#			"${archNormal}" STREQUAL "amd64" OR
#			"${archNormal}" STREQUAL "arm64l"))
#			squirreljme_install4j_register(32
#				"squirreljme_macos_${SQUIRRELJME_VERSION_UNDER}.dmg")
#		endif()
#	endforeach()
#
#	# Determine standaloneJar path
#	get_target_property(standalonePath standaloneJar SQUIRRELJME_OUTPUT_PATH)
#	file(TO_NATIVE_PATH "${standalonePath}" standalonePathNative)
#
#	# Determine source path
#	get_target_property(sourcePath sourceZip SQUIRRELJME_OUTPUT_PATH)
#	file(TO_NATIVE_PATH "${sourcePath}" sourcePathNative)
#
#	# Determine icon path
#	file(TO_CMAKE_PATH "${CMAKE_BINARY_DIR}/icons" iconPath)
#	file(TO_NATIVE_PATH "${iconPath}" iconPathNative)
#
#	# Build all installers at once
#	if(SQUIRRELJME_INSTALL4J_BUNDLE)
#		# Setup rule to build all at once
#		string(REPLACE ";" "," mediaIdsComma "${SQUIRRELJME_INSTALL4J_IDS}")
#		add_custom_target(install4j
#			COMMAND "${CMAKE_COMMAND}" "-E"
#				"make_directory" "${SQUIRRELJME_INSTALL4J_DIR}"
#			COMMAND "${Install4JC_EXECUTABLE}"
#				"-D" "squirreljme.standalone.path=${standalonePathNative}"
#				"-D" "squirreljme.source.path=${sourcePathNative}"
#				"-D" "squirreljme.icon.path=${iconPathNative}"
#				"-r" "${SQUIRRELJME_VERSION}"
#				"-d" "${SQUIRRELJME_INSTALL4J_DIR}"
#				"-b" "${mediaIdsComma}"
#				"${CMAKE_SOURCE_DIR}/squirreljme.install4j"
#			DEPENDS standaloneJar sourceZip install4jIcons
#			BYPRODUCTS "${mediaOutDir}"
#			SOURCES "${CMAKE_SOURCE_DIR}/squirreljme.install4j"
#			COMMAND_EXPAND_LISTS)
#
#		# Properties for uploading later
#		set_target_properties(install4j PROPERTIES
#			SQUIRRELJME_OUTPUT_PATH "${SQUIRRELJME_INSTALL4J_BUNDLE_FILES}"
#			SQUIRRELJME_OUTPUT_TYPE "install4j")
#
#		# These get uploaded into Fossil
#		squirreljme_fossil_upload_register(install4j)
#
#	# Individual rules for each system
#	else()
#		# Pseudo all Install4J targets
#		add_custom_target(install4j)
#
#		# Setup rules to build each specific media individually, as it is
#		# easier to see where things go wrong
#		foreach(mediaId IN LISTS SQUIRRELJME_INSTALL4J_IDS)
#			# Place the output installer somewhere
#			set(mediaOutDir "${SQUIRRELJME_INSTALL4J_DIR}/${mediaId}")
#
#			# Setup rule to build the installer
#			add_custom_target(install4j_${mediaId}
#				COMMAND "${CMAKE_COMMAND}" "-E"
#					"make_directory" "${mediaOutDir}"
#				COMMAND "${Install4JC_EXECUTABLE}"
#					"-D" "squirreljme.standalone.path=${standalonePathNative}"
#					"-D" "squirreljme.source.path=${sourcePathNative}"
#					"-D" "squirreljme.icon.path=${iconPathNative}"
#					"-r" "${SQUIRRELJME_VERSION}"
#					"-d" "${mediaOutDir}"
#					"-b" "${mediaId}"
#					"${CMAKE_SOURCE_DIR}/squirreljme.install4j"
#				DEPENDS standaloneJar sourceZip install4jIcons
#				BYPRODUCTS "${mediaOutDir}"
#				SOURCES "${CMAKE_SOURCE_DIR}/squirreljme.install4j"
#				COMMAND_EXPAND_LISTS)
#
#			# Have the all-install4j depend on this
#			add_dependencies(install4j install4j_${mediaId})
#
#			# Properties for uploading later
#			set_target_properties(install4j_${mediaId} PROPERTIES
#				SQUIRRELJME_OUTPUT_PATH
#					"${SQUIRRELJME_INSTALL4J_ID${mediaId}_FILES}"
#				SQUIRRELJME_OUTPUT_TYPE "install4j")
#
#			# These get uploaded into Fossil
#			squirreljme_fossil_upload_register(install4j_${mediaId})
#		endforeach()
#	endif()
#endif()
