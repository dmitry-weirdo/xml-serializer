create table Department_job (
	  id              integer not null
	, department_code varchar(20) not null
	, department_job  varchar(100) not null
	, description     varchar(255)

	, primary key(id)
	, constraint department_job_unique_nk unique (department_code, department_job) using index department_job_natural_key_idx
);

create generator department_job_gen;
set generator department_job_gen to 0;

-- create index department_job_natural_key_idx on Department_job(department_code, department_job);

commit;