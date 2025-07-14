@echo off
REM Simple JMeter Test Script - Uses JMeter from PATH

REM Test file path
set TEST_PLAN=d:\Java_Practice\Project_1\TCP_Echo_Server_Test.jmx

echo Running TCP Echo Server Load Test...
echo Test Configuration:
echo - 50 concurrent threads
echo - 100 requests per thread
echo - Total requests: 5000
echo - Ramp-up time: 10 seconds
echo.

REM Clean up previous test results
powershell -Command "Remove-Item report -Recurse -Force -ErrorAction SilentlyContinue"
powershell -Command "Remove-Item results.csv -ErrorAction SilentlyContinue"

REM Run JMeter test in non-GUI mode for better performance
jmeter -n -t TCP_Echo_Server_Test.jmx -l results.csv -e -o report

echo.
echo Test completed!
echo Results saved to: results.csv
echo HTML report generated in: report\

pause
