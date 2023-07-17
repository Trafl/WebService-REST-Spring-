create table foto_produto(
    produto_id bigint NOT NULL,
    nome_arquivo varchar(150) NOT NULL,
    descricao varchar(150),
    content_type varchar(80) NOT NULL,
    tamanho int NOT NULL,

    primary key (produto_id),
    constraint fk_foto_produto foreign key (produto_id) references produto (id)
) engine=InnoDB default charset=utf8;