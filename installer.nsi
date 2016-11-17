# define installer name
Name MaxImpact
OutFile "install-maximpact.exe"

# set desktop as install directory
InstallDir $DESKTOP\maximpact
 
# default section start
Section

MessageBox MB_YESNO "Do you want to install MaxImpact?" IDYES continue IDNO abort_installer
abort_installer:
Quit
continue:

 
# define output path
SetOutPath $INSTDIR
 
# specify file to go in output path
File *.dll
File *.exe
File *.jar
File maximpact.html
File /r maximpact_lib
ExecWait "$INSTDIR\vcredist_x64.exe"
 
# define uninstaller name
WriteUninstaller $INSTDIR\uninstaller.exe
 
 
#-------
# default section end
SectionEnd
 
# create a section to define what the uninstaller does.
# the section will always be named "Uninstall"
Section "Uninstall"
 
# Always delete uninstaller first
Delete $INSTDIR\uninstaller.exe
 
# now delete installed files
Delete $INSTDIR\*.dll
Delete $INSTDIR\*.exe
Delete $INSTDIR\*.jar
Delete $INSTDIR\maximpact.html
RMDir /r $INSTDIR\maximpact_lib
Delete "$INSTDIR\vcredist_x64.exe"
RMDir $INSTDIR
 
SectionEnd
