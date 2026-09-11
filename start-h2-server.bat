@echo off
echo 启动 H2 TCP 服务器...
echo.
echo 连接信息：
echo   JDBC URL: jdbc:h2:tcp://localhost:9092/file:./data/learn_java
echo   用户名: sa
echo   密码: (留空)
echo.
echo 按 Ctrl+C 停止服务器
echo.
java -cp "target\classes" org.h2.tools.Server -tcp -tcpPort 9092 -tcpAllowOthers -baseDir .
