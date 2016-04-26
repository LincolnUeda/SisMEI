package com.lincolnueda.sismei.Dominio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lincolnueda.sismei.Entidades.Usuario;

/**
 * Created by Lincoln Ueda on 21/08/2015.
 *
 * Classe responsavel pelos metodos referentes a operacoes da classe Usuario com o banco de dados.
 *
 *
 */
public class RepositorioUsuario {
    private SQLiteDatabase conn;
    private Usuario usuario;

    public RepositorioUsuario(SQLiteDatabase conn){this.conn = conn;} //construtor da classe. recebe a conexao feita com o banco

    public Usuario BuscarUsuario(String login, String senha) {
        //metodo para buscar usuario no banco de dados.
        Cursor cursor = conn.query("Usuario", null, null, null, null, null, "_id");
        Usuario usuario = new Usuario();

        //retornar valores do banco
        if (cursor.getCount() > 0) {
            cursor.moveToFirst(); //move para o primeiro registro do banco
            do {
                if (cursor.getString(1).equals(login) && cursor.getString(2).equals(senha)) {
                    usuario.setCodigo(cursor.getInt(0));
                    usuario.setLogin(cursor.getString(1));
                    usuario.setSenha(cursor.getString(2));
                    usuario.setLogado(cursor.getInt(3));
                }
            } while (cursor.moveToNext());
        }
        return usuario; //retorna usuario encontrado. se nao foi encentrado, retorna objeto vazio.
    }

    public Usuario Logout() {
        //metodo para buscar usuario no banco de dados para realizar o logout.
        Cursor cursor = conn.query("Usuario", null, null, null, null, null, "_id");
        Usuario usuario = new Usuario();

        //retornar valores do banco
        if (cursor.getCount() > 0) {
            cursor.moveToFirst(); //move para o primeiro registro do banco
            do {

                    usuario.setCodigo(cursor.getInt(0));
                    usuario.setLogin(cursor.getString(1));
                    usuario.setSenha(cursor.getString(2));
                    usuario.setLogado(cursor.getInt(3));
            } while (cursor.moveToNext());
        }
        return usuario; //retorna usuario encontrado. se nao foi encentrado, retorna objeto vazio.
    }



    public Usuario Usuariologado() {
        //metodo para buscar usuario no banco de dados.
        Cursor cursor = conn.query("Usuario", null, null, null, null, null, null);
        Usuario usuario = new Usuario();
        usuario.setCodigo(0);

        //retornar valores do banco
        if (cursor.getCount() > 0) {
            cursor.moveToFirst(); //move para o primeiro registro do banco
            do {
                int logado = cursor.getInt(3);
                if (logado == 1) { // verifica se usuario esta logado
                    usuario.setCodigo(cursor.getInt(0));
                    usuario.setLogin(cursor.getString(1));
                    usuario.setSenha(cursor.getString(2));
                    usuario.setLogado(cursor.getInt(3));
                }
            } while (cursor.moveToNext());
        }
        return usuario; //retorna usuario encontrado. se nao foi encentrado, retorna objeto vazio.
    }

    public int Varredura() {
        //este metodo apenas conta quantos usuarios existem cadastrados no banco.
        //serve para verificar  a necessidade de criar o usuario padrao.
        int quant = 0;
        Cursor cursor = conn.query("Usuario", null, null, null, null, null, null);
        Usuario usuario = new Usuario();

        //retornar valores do banco
        if (cursor.getCount() > 0) {
            cursor.moveToFirst(); //move para o primeiro registro do banco
            do {
                quant += 1;

            } while (cursor.moveToNext());
        }
        return quant;
    }

    public void Alterar( Usuario usuario){
        //este metodo faz um update no banco para alterar dados do login de usuario
        //nao ha metodo para inserir novo usuario, apenas um usuario pode existir neste sistema(regra de negocio).

        ContentValues values = new ContentValues(); //este objeto eh o que sera mandado para o metodo de update.
        values.put("login",usuario.getLogin());
        values.put("senha",usuario.getSenha());
        values.put("logado",usuario.getLogado());

        conn.update("Usuario",values,"_id = ?",new String[]{String.valueOf(usuario.getCodigo())});
    }

}
