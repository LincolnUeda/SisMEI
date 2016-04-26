package com.lincolnueda.sismei.DataBase;

/**
 * Created by Lincoln Ueda on 17/08/2015.
 */
public class ScriptSql {


    public static String getCreateFuncionario() {

        StringBuilder sqlBuilder = new StringBuilder();

        //Tabela Funcionario:
        sqlBuilder.append("create table if not exists Funcionario( ");
        sqlBuilder.append("  _id int primary key, ");
        sqlBuilder.append("cpffuncionario varchar(16), ");
        sqlBuilder.append("rgfuncionario varchar(15), ");
        sqlBuilder.append("nomefuncionario varchar(50), ");
        sqlBuilder.append(" datanascimento date, ");
        sqlBuilder.append("enderecofuncionario varchar(50) );   ");

        return sqlBuilder.toString();
    }

    public static String getCreateEtapaProducao() {

        StringBuilder sqlBuilder = new StringBuilder();
        //Tabela EtapaProdu��o:
        sqlBuilder.append("create table if not exists EtapaProducao( ");
        sqlBuilder.append(" _id int primary key,");
        sqlBuilder.append(" nomeetapa varchar(50) );");

        return sqlBuilder.toString();
    }

    public static String getCreateClienteFornecedor() {

        StringBuilder sqlBuilder = new StringBuilder();

        //Tabela ClienteFornecedor:
        sqlBuilder.append("create table if not exists ClienteFornecedor( ");
        sqlBuilder.append("_id int, ");
        sqlBuilder.append("nome varchar(50), ");
        sqlBuilder.append("cpfcnpj varchar(20), ");
        sqlBuilder.append(" rginsc varchar(20), ");
        sqlBuilder.append(" tipo varchar(3),");
        sqlBuilder.append(" primary key(_id,tipo));   ");

        return sqlBuilder.toString();
    }

    public static String getCreateFinanceiro() {

        StringBuilder sqlBuilder = new StringBuilder();

        //Tabela Financeiro
        sqlBuilder.append("create table if not exists Financeiro( ");
        sqlBuilder.append(" _id int, ");
        sqlBuilder.append("descricao varchar(60) not null, ");
        sqlBuilder.append("numnota int, ");
        sqlBuilder.append(" valor double not null, ");
        sqlBuilder.append("numparcela int, ");
        sqlBuilder.append("totalparcelas int, ");
        sqlBuilder.append(" tipo varchar(5), ");
        sqlBuilder.append(" datavenc date, ");
        sqlBuilder.append(" clientefornecedor int, ");
        sqlBuilder.append(" codpedido int,");
        sqlBuilder.append("primary key (_id,tipo), ");
        sqlBuilder.append("foreign key(clientefornecedor) references ClienteFornecedor(_id) );   ");

        return sqlBuilder.toString();
    }

    public static String getCreateEndereco() {

        StringBuilder sqlBuilder = new StringBuilder();

        //Tabela Endere�o:
        sqlBuilder.append("create table if not exists Endereco( ");
        sqlBuilder.append("_id int, ");
        sqlBuilder.append("endereco varchar(255)not null, ");
        sqlBuilder.append("cep varchar(10) not null, ");
        sqlBuilder.append("cidade varchar(50), ");
        sqlBuilder.append("estado carchar(30), ");
        sqlBuilder.append("clientefornecedor int, ");
        sqlBuilder.append("tipoclifor varchar(5), ");
        sqlBuilder.append("primary key(_id,clientefornecedor,tipoclifor), ");
        sqlBuilder.append("foreign key(clientefornecedor,tipoclifor) references ClienteFornecedor(_id,tipo) );   ");

        return sqlBuilder.toString();
    }

    public static String getCreateTelefone() {

        StringBuilder sqlBuilder = new StringBuilder();

        //Tabela Telefone:
        sqlBuilder.append("create table if not exists Telefone( ");
        sqlBuilder.append("_id int, ");
        sqlBuilder.append("telefone varchar(20)not null, ");
        sqlBuilder.append("tipotelefone varchar(20) not null, ");
        sqlBuilder.append("clientefornecedor int, ");
        sqlBuilder.append("tipoclifor varchar(5), ");
        sqlBuilder.append("primary key(_id,tipoclifor), ");
        sqlBuilder.append("foreign key(clientefornecedor,tipoclifor) references ClienteFornecedor(_id,tipo) );   ");

        return sqlBuilder.toString();
    }

    public static String getCreateEmail() {

        StringBuilder sqlBuilder = new StringBuilder();

        //Tabela Email:
        sqlBuilder.append("create table if not exists Email( ");
        sqlBuilder.append("_id int, ");
        sqlBuilder.append("email varchar(255)not null, ");
        sqlBuilder.append("clientefornecedor int, ");
        sqlBuilder.append("tipoclifor varchar(5), ");
        sqlBuilder.append("primary key(_id,tipoclifor), ");
        sqlBuilder.append("foreign key(clientefornecedor,tipoclifor) references ClienteFornecedor(_id,tipo) );   ");

        return sqlBuilder.toString();
    }


    public static String getCreatePedido() {

        StringBuilder sqlBuilder = new StringBuilder();

        //Tabela Pedido:
        sqlBuilder.append("create table if not exists Pedido( ");
        sqlBuilder.append("_id int primary key, ");
        sqlBuilder.append("clientefornecedor int, ");
        sqlBuilder.append("numnota int, ");
        sqlBuilder.append("etapaproducao int, ");
        sqlBuilder.append("data date, ");
        sqlBuilder.append("dataentrega date, ");
        sqlBuilder.append("status varchar(10), ");
        sqlBuilder.append("foreign key(etapaproducao) references EtapaProducao(_id), ");
        sqlBuilder.append("foreign key(clientefornecedor) references ClienteFornecedor(_id) ); ");


        return sqlBuilder.toString();
    }

    public static String getCreateListaPedido() {

        StringBuilder sqlBuilder = new StringBuilder();

        //Tabela ListaPedido:
        sqlBuilder.append("create table if not exists ListaPedido( ");
        sqlBuilder.append("_id integer primary key autoincrement, ");
        sqlBuilder.append("pedido int, ");
        sqlBuilder.append("produto int, ");
        sqlBuilder.append("quantidade double, ");
        sqlBuilder.append("foreign key(pedido) references Pedido(_id), ");
        sqlBuilder.append("foreign key(produto) references Produto(_id) );   ");

        return sqlBuilder.toString();
    }
    public static String getCreateProduto() {

        StringBuilder sqlBuilder = new StringBuilder();

        //Tabela Produto:
        sqlBuilder.append("create table if not exists Produto( ");
        sqlBuilder.append("_id int primary key, ");
        sqlBuilder.append("nomeproduto varchar(50)not null, ");
        sqlBuilder.append("customaterial double not null, ");
        sqlBuilder.append("valorproduto double, ");
        sqlBuilder.append("quantidade int );   ");

        return sqlBuilder.toString();
    }

    public static String getCreateMaterial() {

        StringBuilder sqlBuilder = new StringBuilder();

        //Tabela Material:
        sqlBuilder.append("create table if not exists Material( ");
        sqlBuilder.append(" _id int primary key, ");
        sqlBuilder.append("nomematerial varchar(50), ");
        sqlBuilder.append("cormaterial varchar(50), ");
        sqlBuilder.append("unidmedida varchar(5), ");
        sqlBuilder.append("quantidade double, ");
        sqlBuilder.append("valorunid double, ");
        sqlBuilder.append("clientefornecedor int, ");
        sqlBuilder.append("foreign key(clientefornecedor) references ClienteFornecedor(_id));   ");

        return sqlBuilder.toString();
    }

    public static String getCreateListaMaterial() {

        StringBuilder sqlBuilder = new StringBuilder();

        //Tabela ListaMaterial(Materiais que compõem o produto):
        sqlBuilder.append("create table if not exists ListaMaterial( ");
        sqlBuilder.append("_id integer primary key autoincrement, ");
        sqlBuilder.append("produto int, ");
        sqlBuilder.append("material int, ");
        sqlBuilder.append("quantidadematerial int, ");
        sqlBuilder.append("foreign key(produto) references Produto(_id), ");
        sqlBuilder.append("foreign key(material) references Material(_id)); ");

        return sqlBuilder.toString();
    }

    public static String getCreateOrcamento() {

        StringBuilder sqlBuilder = new StringBuilder();

        //Tabela Or�amento:
        sqlBuilder.append("create table if not exists Orcamento( ");
        sqlBuilder.append("_id int primary key, ");
        sqlBuilder.append("cliente int, ");
        sqlBuilder.append("valortotal double, ");
        sqlBuilder.append("data date, ");
        sqlBuilder.append("precofinal double, ");
        sqlBuilder.append("foreign key(cliente) references ClienteFornecedor(_id) );   ");

        return sqlBuilder.toString();
    }

    public static String getCreateMaterialOrcamento() {

        StringBuilder sqlBuilder = new StringBuilder();

        //Tabela MaterialOrçamento:
        sqlBuilder.append("create table if not exists MaterialOrcamento( ");
        sqlBuilder.append("_id integer primary key autoincrement, ");
        sqlBuilder.append("material int, ");
        sqlBuilder.append("quantidadematerial double, ");
        sqlBuilder.append("orcamento int, ");
        sqlBuilder.append("foreign key(material) references Material(_id), ");
        sqlBuilder.append("foreign key(orcamento) references Orcamento(_id) );   ");

        return sqlBuilder.toString();
    }

    public static String getCreateUsuario() {

        StringBuilder sqlBuilder = new StringBuilder();

        //Tabela Usuario:
        sqlBuilder.append("create table if not exists Usuario( ");
        sqlBuilder.append("_id integer primary key autoincrement, ");
        sqlBuilder.append("login varchar(10) unique, ");
        sqlBuilder.append("senha varchar(10), ");
        sqlBuilder.append("logado int );");

        return sqlBuilder.toString();
    }

    public static String getCreateEmpresa() {

        StringBuilder sqlBuilder = new StringBuilder();

        //Tabela Empresa:
        sqlBuilder.append("create table if not exists Empresa( ");
        sqlBuilder.append("_id integer primary key autoincrement, ");
        sqlBuilder.append("empresa varchar(10) unique, ");
        sqlBuilder.append("cnpj varchar(10)); ");

        return sqlBuilder.toString();
    }


}