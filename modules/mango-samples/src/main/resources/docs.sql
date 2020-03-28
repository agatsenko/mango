create table if not exists authors (
  id bigint not null,
  name varchar(200) not null,
  description clob,

  constraint authors_pk primary key (id)
);

create sequence if not exists authors_seq;
