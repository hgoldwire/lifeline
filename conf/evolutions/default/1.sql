# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table `PULSE` (`datetime` TIMESTAMP NOT NULL,`guid` VARCHAR(254) NOT NULL,`device_name` VARCHAR(254) NOT NULL,`latitude` DOUBLE NOT NULL,`longitude` DOUBLE NOT NULL,`altitude` INTEGER NOT NULL,`horizontal_accuracy` INTEGER NOT NULL,`vertical_accuracy` INTEGER NOT NULL,`battery_state` VARCHAR(254) NOT NULL,`battery_level` INTEGER NOT NULL,`motion_speed` INTEGER NOT NULL,`motion_walking` BOOLEAN NOT NULL,`motion_running` BOOLEAN NOT NULL,`motion_driving` BOOLEAN NOT NULL);
create index `idx_devicename` on `PULSE` (`device_name`);
create index `idx_datetime` on `PULSE` (`guid`);
create index `idx_sudid` on `PULSE` (`guid`);

# --- !Downs

drop table `PULSE`;

