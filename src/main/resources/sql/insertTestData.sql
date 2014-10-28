-- delete from Department_job;

-- IT department
insert into Department_job(id, department_code, department_job, description)
values( gen_id(department_job_gen, 1), 'IT', 'Manager', 'IT Manager' );

insert into Department_job(id, department_code, department_job, description)
values( gen_id(department_job_gen, 1), 'IT', 'Java Leading programmer', 'Java leading programmer (more than 5 years of experience)' );

insert into Department_job(id, department_code, department_job, description)
values( gen_id(department_job_gen, 1), 'IT', 'Java Senior programmer', 'Java senior programmer (2-4 years of experience)' );

insert into Department_job(id, department_code, department_job, description)
values( gen_id(department_job_gen, 1), 'IT', 'Java Junior programmer', 'Java junior programmer (less than 1 year of experience)' );

-- Top management
insert into Department_job(id, department_code, department_job, description)
values( gen_id(department_job_gen, 1), 'Top management', 'DG', 'General director' );

insert into Department_job(id, department_code, department_job, description)
values( gen_id(department_job_gen, 1), 'Top management', 'DD', 'Deputy director' );

-- Outsourcing
insert into Department_job(id, department_code, department_job, description)
values( gen_id(department_job_gen, 1), 'Outsourcing', 'Designer', null );

insert into Department_job(id, department_code, department_job, description)
values( gen_id(department_job_gen, 1), 'Outsourcing', 'Html-coder', 'Занимается версткой html-страниц' );

commit;