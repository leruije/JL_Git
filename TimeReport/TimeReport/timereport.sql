use timereport;

DROP table if exists timesheet ;
DROP table if exists activity ;
DROP table if exists project ;
DROP table if exists employee ;

CREATE TABLE `employee` (
  `employee_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'identifiant employee',
  `ssn` int(11) NOT NULL COMMENT 'numero securite sociale, à utiliser pour le reporting',
  `employee_name` varchar(255) DEFAULT NULL COMMENT 'nom employee',
  PRIMARY KEY (`employee_id`),
  UNIQUE KEY `ssn` (`ssn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

CREATE TABLE `project` (
  `project_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'identifiant projet',
  `description` varchar(255) DEFAULT NULL COMMENT 'description projet',
  `start_date` date DEFAULT NULL COMMENT 'date prévue de début du projet',
  `end_date` date DEFAULT NULL COMMENT 'date prévue de fin prévue du projet',
  `employee_id` int(11) DEFAULT NULL COMMENT 'fk employee, gestionnaire projet',
  `budget_allowed` float NOT NULL COMMENT 'budget alloué au projet',
  PRIMARY KEY (`project_id`),
  KEY `FKox7xsyl6k8fc7aq1mpe8507hf` (`employee_id`),
  CONSTRAINT `FKox7xsyl6k8fc7aq1mpe8507hf` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

CREATE TABLE `activity` (
  `activity_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'identifiant activité',
  `project_id` int(11) NOT NULL COMMENT 'fk project, identifiant projet parent',
  `parent_activity_id` int(11) NOT NULL COMMENT 'fk activity, identifiant activité parent',
  `activity_code` int(11) NOT NULL COMMENT 'code activité à utiliser pour le reporting',
  `description` varchar(255) NOT NULL COMMENT 'description activité',
  `start_date` date NOT NULL COMMENT 'date prévue début activité',
  `end_date` date NOT NULL COMMENT 'date prévue fin activité',
  PRIMARY KEY (`activity_id`),
  UNIQUE KEY `activity_code` (`activity_code`),
  KEY `project_id` (`project_id`),
  KEY `activity_id` (`activity_id`),
  KEY `parent_activity_id` (`parent_activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;


CREATE TABLE `timesheet` (
  `timesheet_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'identifiant timesheet',
  `employee_id` int(11) NOT NULL COMMENT 'fk employee concerné',
  `activity_id` int(11) NOT NULL COMMENT 'fk activity concernée',
  `start_date` datetime NOT NULL COMMENT 'date début prestation concernée',
  `end_date` datetime DEFAULT NULL COMMENT 'date fin prestation concernée',
  `submitted_date` datetime NOT NULL COMMENT 'date encodage prestation',
  `nbr_heures` smallint(6) NOT NULL COMMENT 'nombre heures prestées',
  PRIMARY KEY (`timesheet_id`),
  KEY `employee_id` (`employee_id`),
  KEY `activity_id` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

