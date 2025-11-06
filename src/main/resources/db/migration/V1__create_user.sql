-- -----------------------------------------------------
-- Table `demo`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `demo`.`users` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255) NOT NULL,
    `display_name` VARCHAR(255) NULL DEFAULT NULL,
    `fullname` VARCHAR(255) NULL DEFAULT NULL,
    `birthday` INT NULL DEFAULT NULL,
    `phone_number` VARCHAR(200) NULL DEFAULT NULL,
    `email` VARCHAR(255) NULL,
    `address` VARCHAR(1000) NULL DEFAULT NULL,
    `auth_key` VARCHAR(32) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `password_reset_token` VARCHAR(255) NULL DEFAULT NULL,
    `group_name` VARCHAR(255) NULL DEFAULT NULL,
    `role` SMALLINT NULL DEFAULT '10',
    `status` SMALLINT NOT NULL DEFAULT '10',
    `type` SMALLINT NOT NULL DEFAULT '1' COMMENT '1 - Admin\\n2 - SP\\n3 - dealer',
    `is_default` INT NULL DEFAULT '0' COMMENT '0: Không phải default\\n1: default',
    `parent_id` INT NULL DEFAULT NULL COMMENT 'ID cua accout me',
    `user_ref_id` INT NULL DEFAULT NULL,
    `access_login_token` VARCHAR(255) NULL DEFAULT NULL,
    `created_at` INT NULL DEFAULT NULL,
    `updated_at` INT NULL DEFAULT NULL,
    `level` INT NULL DEFAULT '1',
    `path` VARCHAR(255) NULL DEFAULT ';',
    `created_by` INT NULL DEFAULT NULL,
    `updated_by` INT NULL DEFAULT NULL,
    `root` INT NULL DEFAULT NULL,
    `permission_group_id` INT NULL,
    `customer_id` INT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_user_user1_idx` (`parent_id` ASC),
    CONSTRAINT `fk_user_user1` FOREIGN KEY (`parent_id`) REFERENCES `demo`.`users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);
-- -----------------------------------------------------
-- Table `demo`.`user_token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `demo`.`user_token` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `access_token` VARCHAR(512) NOT NULL,
    `user_id` INT NOT NULL,
    `client_ip` VARCHAR(45) NULL,
    `type` INT NULL DEFAULT '1' COMMENT '1: Token; 2: refresh token',
    `created_at` INT NULL,
    `updated_at` INT NULL,
    `expired_at` INT NULL,
    `status` INT NULL,
    `user_agent` TEXT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_user_token_user1_idx` (`user_id` ASC),
    CONSTRAINT `fk_user_token_user1` FOREIGN KEY (`user_id`) REFERENCES `demo`.`users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);