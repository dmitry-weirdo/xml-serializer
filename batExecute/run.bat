rem
rem ===========================================================================
rem Load test data into the database
"%java_home%\bin\java" -cp ".\*;.\lib\*" ru.pda.xmlSerializer.Main INSERT_TEST_DATA 4 1

rem
rem ===========================================================================
rem Export data from database to xml file
"%java_home%\bin\java" -cp ".\*;.\lib\*" ru.pda.xmlSerializer.Main EXPORT_DATA_TO_XML "departmentJobsFromDB.xml"

rem
rem ===========================================================================
rem Export changed data from xml file to database
"%java_home%\bin\java" -cp ".\*;.\lib\*" ru.pda.xmlSerializer.Main IMPORT_DATA_FROM_XML "departmentJobsChanged.xml"

rem
rem ===========================================================================
rem Try to export data with coinciding natural keys ("departmentCode" and "jobName") fields
"%java_home%\bin\java" -cp ".\*;.\lib\*" ru.pda.xmlSerializer.Main IMPORT_DATA_FROM_XML "departmentJobsCoincidingNaturalKeys.xml"