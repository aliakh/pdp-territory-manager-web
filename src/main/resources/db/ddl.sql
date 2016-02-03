set mode oracle;


drop schema names if exists;
drop user names if exists;

create user names password '';
alter user names admin true;
create schema names authorization names;


create table names.cdd_franchise
(
 franchise_id     number not null,
 dealership_id    number,
 franchise_name   varchar2(300),
 make             varchar2(30),
 street           varchar2(200),
 city             varchar2(100),
 state            varchar2(2),
 zip              varchar2(5),
 phone            varchar2(50),
 url              varchar2(300),
 latitude         number(9,6),
 longitude        number(9,6),
 constraint pk_cf primary key (franchise_id)
);


create table names.dealer_pdp_region
(
region_id         number(100) not null,
region_code       varchar2(100) not null,
region_desc       varchar2(100),
region_make       varchar2(30),
latitude          number(9,6) not null,
longitude         number(9,6) not null,
region_map        clob,
state             varchar2(2),
cdd_franchise_id  number(100),
radius_mi         number(30) default 100,
insert_date       date default sysdate,
update_date       date default sysdate,
constraint pk_dpr primary key (region_id)
);

alter table names.dealer_pdp_region
add constraint fk_franchise_id foreign key (cdd_franchise_id) references names.cdd_franchise (franchise_id);


create table names.zip_code 
(
zpc_zip_code      char(5) not null,
zpc_state         char(2) not null,
zpc_latitude      number(9,6),
zpc_longitude     number(9,6),
zpc_poname        varchar2(100),
zpc_map           clob,
constraint pk_zc primary key (zpc_zip_code)
);


create table names.dealer_pdp_zip_region
(
zip_code          char(5) not null,
region_id         number(10, 0) not null,
pin               varchar2(1),
constraint pk_dpzr primary key (zip_code, region_id)
);

alter table names.dealer_pdp_zip_region
add constraint fk_dpz_region_id foreign key (region_id) references names.dealer_pdp_region (region_id);

alter table names.dealer_pdp_zip_region
add constraint fk_dpz_zip_code foreign key (zip_code) references names.zip_code (zpc_zip_code);

