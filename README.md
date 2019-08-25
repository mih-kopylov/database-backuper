# Description

`mysql-backuper` is a small utility that:

* creates MySQL backups using standard `mysqlbackup`
* names it with `yyyy-MM-dd-HH-mm-ss` template
* archives it using 7z
* uploads it to specified directory in https://disk.yandex.ru

# Preparation

* register a client at https://oauth.yandex.ru/ with
  * web-service platform 
  * developer callback URI
  * all 4 Yandex.Disk REST API options 
* go to https://oauth.yandex.ru/authorize?response_type=token&client_id=<client_id> and get developer token


# Usage

`java -jar mysql-backuper.jar -Dbackuper.diskUser=%YANDEX_LOGIN% -Dbackuper.diskToken=%YANDEX_TOKEN% -Dbackuper.diskPath=%YANDEX_DISK_PATH% -Dbackuper.dbUser=%MYSQL_USER% -Dbackuper.dbPassword=%MYSQL_PASSWORD% -Dbackuper.dbDatabase=%MYSQL_DATABASE%`