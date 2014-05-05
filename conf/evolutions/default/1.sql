# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "PULSE" ("guid" VARCHAR NOT NULL PRIMARY KEY,"device_name" VARCHAR NOT NULL,"latitude" DOUBLE NOT NULL,"longitude" DOUBLE NOT NULL);

# --- !Downs

drop table "PULSE";

