
CREATE TABLE `members`
(
  `id`            bigint(20) NOT NULL AUTO_INCREMENT,
  `email`                      varchar(255) DEFAULT NULL,
  `name`                       varchar(255) DEFAULT NULL,
  `pw_hash`                     varchar(255) NOT NULL,
  `salt`                       varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

