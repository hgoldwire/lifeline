# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "PULSE" ("guid" VARCHAR NOT NULL,"device_name" VARCHAR NOT NULL,"latitude" DOUBLE NOT NULL,"longitude" DOUBLE NOT NULL,"altitude" INTEGER NOT NULL,"horizontal_accuracy" INTEGER NOT NULL,"vertical_accuracy" INTEGER NOT NULL,"battery_state" VARCHAR NOT NULL,"battery_level" INTEGER NOT NULL);

# --- !Downs

drop table "PULSE";

