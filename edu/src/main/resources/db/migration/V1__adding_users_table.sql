create table tb_user(
    id int8 not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    email varchar(255),
    password varchar (255),
    primary key (id)
);

create table input(
    id int8 not null,
    requisition float8,
    processing float8,
    users int8,
    servers int8,
    no_requisitions float8,
    row_b int8,
    population int8,
    primary key (id)
);

create table output(
    id int8 not null,
    traffic_intensity float8,
    no_user_probability float8,
    probability_of_user float8,
    probability_of_n_user_or_more float8,
    probability_of_user_or_more float8,
    average_response_time float8,
    average_time_of_queue_waiting float8,
    average_users float8,
    average_requisition float8,
    cerlang float8,
    utilization float8,
    probability_of_n_user float8,
    service_request float8,
    average_of_waiting_requests float8,
    receiving_rate float8,
    loss_rate float8,
    average_users_queue_waiting float8,
    primary key (id)
);

create table search(
    id int8 not null,
    user_id int8 not null,
    input_id int8 not null,
    output_id int8 not null,
    primary key (id)
);

 alter table search
       add constraint FKowj7iv4b8ufnctvdig9m0cg0n
       foreign key (user_id)
       references tb_user;

 alter table search
       add constraint FKowj7iv4b8ufnctvdig9m0cg0o
       foreign key (input_id)
       references input;

 alter table search
       add constraint FKowj7iv4b8ufnctvdig9m0cg0p
       foreign key (output_id)
       references output;

