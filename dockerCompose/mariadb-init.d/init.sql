CREATE USER 'service'@'localhost' IDENTIFIED BY 'service';
CREATE USER 'service'@'%' IDENTIFIED BY '1234';

GRANT ALL PRIVILEGES ON *.* TO 'service'@'localhost';
GRANT ALL PRIVILEGES ON *.* TO 'service'@'%';

CREATE DATABASE service DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;