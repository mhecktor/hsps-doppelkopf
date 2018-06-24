drop table dbspieler;
drop table dbstatistik;

create table dbstatistik (
  idStatistik integer not null,
  reGewonnen character(1) not null,
  PunktestandRe number not null,
  PunktestandKontra number not null,
  createDateTime date not null,
  constraint idStatistik primary key( idStatistik )
);

create table dbspieler (
  idSpieler integer not null,
  name character(20) default null,
  re character(1) not null,
  idStatistik number not null,
  constraint idSpieler primary key( idSpieler ),
  constraint fkStatistik foreign key (idStatistik) references dbstatistik(idStatistik)
);
