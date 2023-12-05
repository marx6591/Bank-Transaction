CREATE TABLE `TRANSACTION`
(
    `ID`          int PRIMARY KEY AUTO_INCREMENT,
    `DESCRIPTION` varchar(50),
    `DATE`        DATETIME       NOT NULL,
    `VALUE`       decimal(15, 2) NOT NULL
);