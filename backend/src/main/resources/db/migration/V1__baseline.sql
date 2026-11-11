-- Baseline schema as of 2026-11-11, generated from the local MySQL instance.

CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fullname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','STUDENT','TEACHER') DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_users_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

CREATE TABLE `teachers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `phone` varchar(255) DEFAULT NULL,
  `specialization` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKcd1k6xwg9jqtiwx9ybnxpmoh9` (`user_id`),
  CONSTRAINT `FKb8dct7w2j1vl1r2bpstw5isc0` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

CREATE TABLE `students` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date_of_birth` date DEFAULT NULL,
  `parent_phone` varchar(255) DEFAULT NULL,
  `status` enum('ACTIVE','ALUMNI','INACTIVE') DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `evaluation_sheet_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKg4fwvutq09fjdlb4bb0byp7t` (`user_id`),
  CONSTRAINT `FKdt1cjx5ve5bdabmuuf3ibrwaq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

CREATE TABLE `classes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `curriculum_sheet_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `teacher_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8td8h5k21lq8jax2h6oobm9l0` (`teacher_id`),
  CONSTRAINT `FK8td8h5k21lq8jax2h6oobm9l0` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

CREATE TABLE `class_fee_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `effective_from` date DEFAULT NULL,
  `effective_to` date DEFAULT NULL,
  `monthly_fee` decimal(12,2) DEFAULT NULL,
  `class_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhlc7syxobs2v7aonayjsft2u4` (`class_id`),
  CONSTRAINT `FKhlc7syxobs2v7aonayjsft2u4` FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

CREATE TABLE `class_schedules` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `day_of_week` tinyint DEFAULT NULL,
  `start_time` time DEFAULT NULL,
  `class_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKd4fidccuckfhabn3u91coqo6g` (`class_id`),
  CONSTRAINT `FKd4fidccuckfhabn3u91coqo6g` FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`),
  CONSTRAINT `class_schedules_chk_1` CHECK ((`day_of_week` between 0 and 6))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

CREATE TABLE `class_sessions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `session_date` date DEFAULT NULL,
  `teacher_taught` bit(1) NOT NULL,
  `class_id` bigint DEFAULT NULL,
  `teacher_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1w2pxpii8b2dhn7pcylfpiygh` (`class_id`),
  KEY `FKrcyp1gvuar4dp471lpseo71sj` (`teacher_id`),
  CONSTRAINT `FK1w2pxpii8b2dhn7pcylfpiygh` FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`),
  CONSTRAINT `FKrcyp1gvuar4dp471lpseo71sj` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

CREATE TABLE `enrollments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `enrolled_date` date DEFAULT NULL,
  `status` enum('ACTIVE','COMPLETED','WITHDRAWN') DEFAULT NULL,
  `class_id` bigint DEFAULT NULL,
  `student_id` bigint DEFAULT NULL,
  `active_flag` tinyint GENERATED ALWAYS AS (if((`status` = _utf8mb4'ACTIVE'),1,NULL)) STORED,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_enrollment_active` (`student_id`,`class_id`,`active_flag`),
  KEY `FKloh1q3o1ua6yuw9mwqdvhc54u` (`class_id`),
  CONSTRAINT `FK8kf1u1857xgo56xbfmnif2c51` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`),
  CONSTRAINT `FKloh1q3o1ua6yuw9mwqdvhc54u` FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

CREATE TABLE `attendances` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `status` enum('ABSENT','LATE','PRESENT') DEFAULT NULL,
  `enrollment_id` bigint DEFAULT NULL,
  `session_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_attendance_session_enrollment` (`session_id`,`enrollment_id`),
  KEY `FK7j92ropr8c7lg7jyrwbkffisy` (`enrollment_id`),
  CONSTRAINT `FK3a2tcexm2i91hhlum0bwhymb8` FOREIGN KEY (`session_id`) REFERENCES `class_sessions` (`id`),
  CONSTRAINT `FK7j92ropr8c7lg7jyrwbkffisy` FOREIGN KEY (`enrollment_id`) REFERENCES `enrollments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

CREATE TABLE `invoices` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `adjusted_amount` decimal(12,2) DEFAULT NULL,
  `amount` decimal(12,2) DEFAULT NULL,
  `month` int DEFAULT NULL,
  `status` enum('PAID','PARTIALLY_PAID','UNPAID') DEFAULT NULL,
  `year` int DEFAULT NULL,
  `enrollment_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKev28douxo85baooigoktqfbr1` (`enrollment_id`,`month`,`year`),
  CONSTRAINT `FKi0eiitl2dxmm91eakaqf4x94l` FOREIGN KEY (`enrollment_id`) REFERENCES `enrollments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(12,2) DEFAULT NULL,
  `payment_date` date DEFAULT NULL,
  `payment_method` enum('BANKING','CASH') DEFAULT NULL,
  `invoice_id` bigint DEFAULT NULL,
  `cancelled` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrbqec6be74wab8iifh8g3i50i` (`invoice_id`),
  CONSTRAINT `FKrbqec6be74wab8iifh8g3i50i` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

CREATE TABLE `teacher_rates` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `effective_from` date DEFAULT NULL,
  `effective_to` date DEFAULT NULL,
  `rate_per_session` decimal(12,2) DEFAULT NULL,
  `classroom_id` bigint DEFAULT NULL,
  `teacher_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKljal5lcvl6tww6r5wj8yfor2g` (`classroom_id`),
  KEY `FKs9h4p76k51kwrb2qhfmwhbup2` (`teacher_id`),
  CONSTRAINT `FKljal5lcvl6tww6r5wj8yfor2g` FOREIGN KEY (`classroom_id`) REFERENCES `classes` (`id`),
  CONSTRAINT `FKs9h4p76k51kwrb2qhfmwhbup2` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

CREATE TABLE `teacher_payrolls` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bonus` decimal(12,2) DEFAULT NULL,
  `is_manually_adjusted` bit(1) NOT NULL,
  `month` int DEFAULT NULL,
  `session_pay` decimal(12,2) DEFAULT NULL,
  `status` enum('CONFIRMED','DRAFT','PAID') DEFAULT NULL,
  `total_pay` decimal(12,2) DEFAULT NULL,
  `year` int DEFAULT NULL,
  `teacher_id` bigint DEFAULT NULL,
  `paid_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK4pt4vdo621qhn8hblk9elsc4` (`teacher_id`,`month`,`year`),
  CONSTRAINT `FKqvar01vw8mnqtj3c36u8g7mb4` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

CREATE TABLE `payroll_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rate_applied` decimal(12,2) DEFAULT NULL,
  `sessions_taught` int DEFAULT NULL,
  `subtotal` decimal(12,2) DEFAULT NULL,
  `classroom_id` bigint DEFAULT NULL,
  `teacher_payroll_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmq4yrlbiix1qdp1u58bsgfje6` (`classroom_id`),
  KEY `FKblknjdj44pnyhdvkvchox1olm` (`teacher_payroll_id`),
  CONSTRAINT `FKblknjdj44pnyhdvkvchox1olm` FOREIGN KEY (`teacher_payroll_id`) REFERENCES `teacher_payrolls` (`id`),
  CONSTRAINT `FKmq4yrlbiix1qdp1u58bsgfje6` FOREIGN KEY (`classroom_id`) REFERENCES `classes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

CREATE TABLE `expenses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(12,2) DEFAULT NULL,
  `category` enum('MARKETING','OTHER','RENT','SALARY','SUPPLIES','UTILITIES') DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `expense_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;
