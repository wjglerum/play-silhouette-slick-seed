# --- !Ups

create table "users" ("id" UUID NOT NULL PRIMARY KEY,"username" VARCHAR,"name" VARCHAR,"email" VARCHAR);
create table "passwords" ("hash" VARCHAR NOT NULL,"password" VARCHAR NOT NULL,"salt" VARCHAR,"email" VARCHAR NOT NULL PRIMARY KEY);

# --- !Downs

drop table "passwords";
drop table "users";